Type INIFile
	Field name$
	Field bank%
	Field bankOffset% = 0
	Field size%
End Type

Function ReadINILine$(file.INIFile)
	Local rdbyte%
	Local firstbyte% = True
	Local offset% = file\bankOffset
	Local bank% = file\bank
	Local retStr$ = ""
	rdbyte = PeekByte(bank,offset)
	While ((firstbyte) Lor ((rdbyte<>13) And (rdbyte<>10))) And (offset<file\size)
		rdbyte = PeekByte(bank,offset)
		If ((rdbyte<>13) And (rdbyte<>10)) Then
			firstbyte = False
			retStr=retStr+Chr(rdbyte)
		EndIf
		offset=offset+1
	Wend
	file\bankOffset = offset
	Return retStr
End Function

Function UpdateINIFile$(filename$)
	Local file.INIFile = Null
	For k.INIFile = Each INIFile
		If k\name = Lower(filename) Then
			file = k
			Exit
		EndIf
	Next
	
	If file=Null Then Return
	
	If file\bank<>0 Then FreeBank file\bank
	Local f% = ReadFile(file\name)
	Local fleSize% = 1
	While fleSize<FileSize(file\name)
		fleSize=fleSize*2
	Wend
	file\bank = CreateBank(fleSize)
	file\size = 0
	While Not Eof(f)
		PokeByte(file\bank,file\size,ReadByte(f))
		file\size=file\size+1
	Wend
	CloseFile(f)
End Function

Function DeleteINIFile(filename$)
	If FileType(filename) <> 0 Then
		Local file.INIFile = Null
		For k.INIFile = Each INIFile
			If k\name = Lower(filename) Then
				file = k
				Exit
			EndIf
		Next
		If file <> Null Then
			FreeBank file\bank
			DebugLog "FREED BANK FOR "+filename
			Delete file
			Return
		EndIf
	EndIf
	DebugLog "COULD NOT FREE BANK FOR "+filename+": INI FILE IS NOT LOADED"
End Function

Function GetINIString$(file$, section$, parameter$, defaultvalue$="")
	Local TemporaryString$ = ""
	
	Local lfile.INIFile = Null
	For k.INIFile = Each INIFile
		If k\name = Lower(file) Then
			lfile = k
			Exit
		EndIf
	Next
	
	If lfile = Null Then
		DebugLog "CREATE BANK FOR "+file
		lfile = New INIFile
		lfile\name = Lower(file)
		lfile\bank = 0
		UpdateINIFile(lfile\name)
	EndIf
	
	lfile\bankOffset = 0
	
	section = Lower(section)
	
	While lfile\bankOffset<lfile\size
		Local strtemp$ = ReadINILine(lfile)
		If Left(strtemp,1) = "[" Then
			strtemp$ = Lower(strtemp)
			If Mid(strtemp, 2, Len(strtemp)-2)=section Then
				Repeat
					TemporaryString = ReadINILine(lfile)
					If Lower(Trim(Left(TemporaryString, Max(Instr(TemporaryString, "=") - 1, 0)))) = Lower(parameter) Then
						Return Trim( Right(TemporaryString,Len(TemporaryString)-Instr(TemporaryString,"=")) )
					EndIf
				Until (Left(TemporaryString, 1) = "[") Lor (lfile\bankOffset>=lfile\size)
				
				Return defaultvalue
			EndIf
		EndIf
	Wend
	
	Return defaultvalue
End Function

Function GetINIInt%(file$, section$, parameter$, defaultvalue% = 0)
	Local txt$ = GetINIString(file$, section$, parameter$, defaultvalue)
	If Lower(txt) = "true" Then
		Return 1
	ElseIf Lower(txt) = "false"
		Return 0
	Else
		Return Int(txt)
	EndIf
End Function

Function GetINIFloat#(file$, section$, parameter$, defaultvalue# = 0.0)
	Return Float(GetINIString(file$, section$, parameter$, defaultvalue))
End Function

Function GetINIFloat2#(File$, Section$, Parameter$, DefaultValue# = 0.0)
	Return(Float(GetINIString2(File, Section, Parameter, DefaultValue)))
End Function

Function GetINIString2$(file$, start%, parameter$, defaultvalue$="")
	Local TemporaryString$ = ""
	Local f% = ReadFile(file)
	
	Local n%=0
	While Not Eof(f)
		Local strtemp$ = ReadLine(f)
		n=n+1
		If n=start Then 
			Repeat
				TemporaryString = ReadLine(f)
				If Lower(Trim(Left(TemporaryString, Max(Instr(TemporaryString, "=") - 1, 0)))) = Lower(parameter) Then
					CloseFile f
					Return Trim( Right(TemporaryString,Len(TemporaryString)-Instr(TemporaryString,"=")) )
				EndIf
			Until Left(TemporaryString, 1) = "[" Lor Eof(f)
			CloseFile f
			Return defaultvalue
		EndIf
	Wend
	
	CloseFile f	
	
	Return defaultvalue
End Function

Function GetINIInt2%(file$, start%, parameter$, defaultvalue$="")
	Local txt$ = GetINIString2(file$, start%, parameter$, defaultvalue$)
	
	If Lower(txt) = "true" Then
		Return 1
	ElseIf Lower(txt) = "false"
		Return 0
	Else
		Return Int(txt)
	EndIf
	
End Function

Function GetINISectionLocation%(file$, section$)
	Local Temp%
	Local f% = ReadFile(file)
	
	section = Lower(section)
	
	Local n%=0
	While Not Eof(f)
		Local strtemp$ = ReadLine(f)
		n=n+1
		If Left(strtemp,1) = "[" Then
			strtemp$ = Lower(strtemp)
			Temp = Instr(strtemp, section)
			If Temp>0 Then
				If (Mid(strtemp, Temp-1, 1)="[" Lor Mid(strtemp, Temp-1, 1)="|") And (Mid(strtemp, Temp+Len(section), 1)="]" Lor Mid(strtemp, Temp+Len(section), 1)="|") Then
					CloseFile f
					Return n
				EndIf
			EndIf
		EndIf
	Wend
	
	CloseFile f
End Function

Function PutINIValue%(file$, INI_sSection$, INI_sKey$, INI_sValue$)
	
	INI_sSection = "[" + Trim$(INI_sSection) + "]"
	Local INI_sUpperSection$ = Upper$(INI_sSection)
	INI_sKey = Trim$(INI_sKey)
	INI_sValue = Trim$(INI_sValue)
	Local INI_sFilename$ = file$
	
	Local INI_sContents$ = INI_FileToString(INI_sFilename)
	
	Local INI_bWrittenKey% = False
	Local INI_bSectionFound% = False
	Local INI_sCurrentSection$ = ""
	
	Local INI_lFileHandle% = WriteFile(INI_sFilename)
	If INI_lFileHandle = 0 Then Return False
	
	Local INI_lOldPos% = 1
	Local INI_lPos% = Instr(INI_sContents, Chr$(0))
	
	While (INI_lPos <> 0)
		
		Local INI_sTemp$ = Mid$(INI_sContents, INI_lOldPos, (INI_lPos - INI_lOldPos))
		
		If (INI_sTemp <> "") Then
			
			If Left$(INI_sTemp, 1) = "[" And Right$(INI_sTemp, 1) = "]" Then
				
				If (INI_sCurrentSection = INI_sUpperSection) And (INI_bWrittenKey = False) Then
					INI_bWrittenKey = INI_CreateKey(INI_lFileHandle, INI_sKey, INI_sValue)
				End If
				INI_sCurrentSection = Upper$(INI_CreateSection(INI_lFileHandle, INI_sTemp))
				If (INI_sCurrentSection = INI_sUpperSection) Then INI_bSectionFound = True
				
			Else
				If Left(INI_sTemp, 1) = ":" Then
					WriteLine INI_lFileHandle, INI_sTemp
				Else
					Local lEqualsPos% = Instr(INI_sTemp, "=")
					If (lEqualsPos <> 0) Then
						If (INI_sCurrentSection = INI_sUpperSection) And (Upper$(Trim$(Left$(INI_sTemp, (lEqualsPos - 1)))) = Upper$(INI_sKey)) Then
							If (INI_sValue <> "") Then INI_CreateKey INI_lFileHandle, INI_sKey, INI_sValue
							INI_bWrittenKey = True
						Else
							WriteLine INI_lFileHandle, INI_sTemp
						End If
					End If
				EndIf
			EndIf
		EndIf
		
		INI_lOldPos = INI_lPos + 1
		INI_lPos% = Instr(INI_sContents, Chr$(0), INI_lOldPos)
		
	Wend
	
	If (INI_bWrittenKey = False) Then
		If (INI_bSectionFound = False) Then INI_CreateSection INI_lFileHandle, INI_sSection
		INI_CreateKey INI_lFileHandle, INI_sKey, INI_sValue
	End If
	
	CloseFile INI_lFileHandle
	
	Return True
	
End Function

Function INI_FileToString$(INI_sFilename$)
	
	Local INI_sString$ = ""
	Local INI_lFileHandle%= ReadFile(INI_sFilename)
	If INI_lFileHandle <> 0 Then
		While Not(Eof(INI_lFileHandle))
			INI_sString = INI_sString + ReadLine$(INI_lFileHandle) + Chr$(0)
		Wend
		CloseFile INI_lFileHandle
	End If
	Return INI_sString
	
End Function

Function INI_CreateSection$(INI_lFileHandle%, INI_sNewSection$)
	
	If FilePos(INI_lFileHandle) <> 0 Then WriteLine INI_lFileHandle, ""
	WriteLine INI_lFileHandle, INI_sNewSection
	Return INI_sNewSection
	
End Function

Function INI_CreateKey%(INI_lFileHandle%, INI_sKey$, INI_sValue$)
	
	WriteLine INI_lFileHandle, INI_sKey + " = " + INI_sValue
	Return True
	
End Function

Function SaveOptionsINI()
	
	PutINIValue(gv\OptionFile, "options", "mouse sensitivity", MouseSens)
	PutINIValue(gv\OptionFile, "options", "mouse smoothing", opt\MouseSmooth)
	PutINIValue(gv\OptionFile, "options", "invert mouse y", InvertMouse)
	PutINIValue(gv\OptionFile, "options", "hold to aim", opt\HoldToAim)
	PutINIValue(gv\OptionFile, "options", "hold to crouch", opt\HoldToCrouch)
	;PutINIValue(gv\OptionFile, "options", "bump mapping enabled", BumpEnabled)	
	PutINIValue(gv\OptionFile, "options", "play startup videos", opt\PlayStartupVideos)
	PutINIValue(gv\OptionFile, "options", "HUD enabled", HUDenabled)
	PutINIValue(gv\OptionFile, "options", "screengamma", ScreenGamma)
	PutINIValue(gv\OptionFile, "options", "vsync", Vsync)
	PutINIValue(gv\OptionFile, "options", "show FPS", opt\ShowFPS)
	PutINIValue(gv\OptionFile, "options", "framelimit", Framelimit%) 
	PutINIValue(gv\OptionFile, "options", "achievement popup enabled", AchvMSGenabled%)
	PutINIValue(gv\OptionFile, "options", "room lights enabled", opt\EnableRoomLights%)
	PutINIValue(gv\OptionFile, "options", "texture details", opt\TextureDetails%)
	PutINIValue(gv\OptionFile, "options", "texture filtering", opt\TextureFiltering%)
	PutINIValue(gv\OptionFile, "options", "particle amount", ParticleAmount)
	PutINIValue(gv\OptionFile, "options", "enable vram", SaveTexturesInVRam)
	PutINIValue(gv\OptionFile, "options", "cubemaps", opt\RenderCubeMapMode)
	PutINIValue(gv\OptionFile, "options", "enable player model", opt\PlayerModelEnabled)
	PutINIValue(gv\OptionFile, "options", "fov", FOV)
	PutINIValue(gv\OptionFile, "options", "render scope", opt\RenderScope)
	
	PutINIValue(gv\OptionFile, "options", "intro enabled", opt\IntroEnabled)
	PutINIValue(gv\OptionFile, "options", "language", opt\LanguageVal)
	
	PutINIValue(gv\OptionFile, "console", "enabled", opt\ConsoleEnabled%)
	PutINIValue(gv\OptionFile, "console", "auto opening", opt\ConsoleOpening%)
	
	PutINIValue(gv\OptionFile, "audio", "master volume", opt\MasterVol)
	PutINIValue(gv\OptionFile, "audio", "music volume", opt\MusicVol)
	PutINIValue(gv\OptionFile, "audio", "sound volume", opt\SFXVolume)
	PutINIValue(gv\OptionFile, "audio", "voice volume", opt\VoiceVol)
	PutINIValue(gv\OptionFile, "audio", "sfx release", opt\EnableSFXRelease)
	PutINIValue(gv\OptionFile, "audio", "enable user tracks", EnableUserTracks%)
	PutINIValue(gv\OptionFile, "audio", "user track setting", UserTrackMode%)
	PutINIValue(gv\OptionFile, "audio", "bms boss music", opt\BMSBossMusic)
	PutINIValue(gv\OptionFile, "audio", "elevator music", opt\ElevatorMusicEnabled)
	PutINIValue(gv\OptionFile, "audio", "mainmenu music", opt\MainMenuMusic)
	
	PutINIValue(gv\OptionFile, "binds", "Right key", KEY_RIGHT)
	PutINIValue(gv\OptionFile, "binds", "Left key", KEY_LEFT)
	PutINIValue(gv\OptionFile, "binds", "Up key", KEY_UP)
	PutINIValue(gv\OptionFile, "binds", "Down key", KEY_DOWN)
	PutINIValue(gv\OptionFile, "binds", "Blink key", KEY_BLINK)
	PutINIValue(gv\OptionFile, "binds", "Sprint key", KEY_SPRINT)
	PutINIValue(gv\OptionFile, "binds", "Inventory key", KEY_INV)
	PutINIValue(gv\OptionFile, "binds", "Crouch key", KEY_CROUCH)
	PutINIValue(gv\OptionFile, "binds", "Save key", KEY_SAVE)
	PutINIValue(gv\OptionFile, "binds", "Console key", KEY_CONSOLE)
	PutINIValue(gv\OptionFile, "binds", "Reload key", KEY_RELOAD)
	PutINIValue(gv\OptionFile, "binds", "Holstergun key", KEY_HOLSTERGUN)
	PutINIValue(gv\OptionFile, "binds", "Firemode key", KEY_CHANGEFIREMODE)
	PutINIValue(gv\OptionFile, "binds", "Attachment key", KEY_SELECTATTACHMENT)
	PutINIValue(gv\OptionFile, "binds", "Scramble key", KEY_SCRAMBLE)
	PutINIValue(gv\OptionFile, "binds", "Radiotoggle key", KEY_RADIOTOGGLE)
	PutINIValue(gv\OptionFile, "binds", "Use key", KEY_USE)
	PutINIValue(gv\OptionFile, "binds", "Load key", KEY_LOAD)
	
	SaveController()
	SaveKeyBinds()
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D