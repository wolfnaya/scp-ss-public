; ~ IniControler - A part of BlitzToolBox
; ~ Write & Read ini file.
; ~ v1.06 2022.11.12
; ~ https://github.com/ZiYueCommentary/BlitzToolbox

Function IniWriteBuffer%(File$, ClearPrevious% = True)
	IniWriteBuffer_(File, ClearPrevious)
End Function

Function GetINIString$(file$, section$, parameter$, defaultvalue$="")
	Return IniGetString_(file, section, parameter, defaultvalue, True)
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

Function IniGetBufferString$(File$, Section$, Parameter$, DefaultValue$ = "")
	Return IniGetBufferString_(File, Section, Parameter, DefaultValue)
End Function

Function PutINIValue(file$, INI_sSection$, INI_sKey$, INI_sValue$)
	IniWriteString_(file, INI_sSection, INI_sKey, INI_sValue, True)
End Function

Function IniKeyExist%(file$, INI_sSection$, INI_sKey$)
	Return IniKeyExist_(file, INI_sSection, INI_sKey, True)
End Function

Function FindSCP294Drink$(Drink$)
	Local StrTemp$ = FindSCP294Drink_(I_Loc\LangPath + "Data\SCP-294.ini", Drink)
	
	If StrTemp = "Null" Then StrTemp = FindSCP294Drink_("Data\SCP-294.ini", Drink)
	If StrTemp = "Null" Then Return(StrTemp)
	Return Left(StrTemp, Instr(StrTemp, ",") - 1)
End Function

Function Integer%(String_$)
	Select String_
		Case "True", "true", "1"
			Return 1
		Case "False", "false", "0"
			Return 0
		Default
			Return Int(String_)
	End Select
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