
Type MenuSecrets
	Field WolfnayaButtonPressed%
	Field MPButtonPressed%
End Type
Type MenuShop
	Field Points#
	Field Sound[2]
	Field ShoundCHN%
End Type

Global IsStartingFromMenu% = False

Global RandomSeed$
Global ClassDNumber

If ClassDNumber = 0 Then
	ClassDNumber = Rand(1000,999999)
EndIf

Global QuickLoadIcon% = LoadImage_Strict("GFX\menu\Quick_Loading.png")
ResizeImage(QuickLoadIcon, ImageWidth(QuickLoadIcon) * MenuScale, ImageHeight(QuickLoadIcon) * MenuScale)
;Global SCPText% = LoadImage_Strict("GFX\menu 2d\scp_text.png")
;ResizeImage(SCPText, ImageWidth(SCPText) * MenuScale, ImageHeight(SCPText) * MenuScale)
;Global RyanIMG% = LoadImage_Strict("GFX\menu 2d\Ryan_back.jpg")
;ResizeImage(RyanIMG, ImageWidth(RyanIMG) * MenuScale, ImageHeight(RyanIMG) * MenuScale)
Global MenuMeterIMG% = LoadImage_Strict("GFX\HUD\menu_meter.jpg")

Global MenuBlinkTimer%[2], MenuBlinkDuration%[2]
MenuBlinkTimer[0] = 1
MenuBlinkTimer[1] = 1

Global MenuStr$, MenuStrX%, MenuStrY%

Global MainMenuTab%
Global PrevMainMenuTab%
Global ShouldDeleteGadgets%

Global ChapterGateA%
Global ChapterGateB%
Global ChapterGateC%
Global ChapterGateD%

Global SelectedInputBox%

LoadSaveGames()

Global CurrLoadGamePage% = 0

; ~ Menu constants
;[Block]
Const MenuTab_Default = 0
Const MenuTab_Singleplayer = 1
Const MenuTab_Serverlist = 2
Const MenuTab_Extras = 3
Const MenuTab_NewGame = 4
Const MenuTab_LoadGame = 5
Const MenuTab_Chapters = 6
Const MenuTab_Options_Graphics = 7
Const MenuTab_Options_Audio = 8
Const MenuTab_Options_Controls = 9
Const MenuTab_Options_Advanced = 10
Const MenuTab_Options_Controller = 11
Const MenuTab_Options_ControlsBinding = 12
Const MenuTab_HostServer = 13
Const MenuTab_GameModes = 14
Const MenuTab_Achievements = 15
Const MenuTab_Continue = 16
Const MenuTab_Lobby = 17
Const MenuTab_SelectMPMap = 18
Const MenuTab_SelectMPGamemode = 19
Const MenuTab_MPGamemodeSettings = 20
Const MenuTab_Credits = 21
Const MenuTab_Websites = 22
Const MenuTab_PointsShop = 23
;[End Block]

Type MenuInstance
	Field MenuLogo.MenuLogo
	Field Cam%
	Field Sprite%
	Field SpriteAlpha#
	Field CurrentSave$
End Type

Function UpdateMainMenu()
	Local x%, y%, z%, width%, height%, temp%, i%, n%, j%
	Local se.Server
	Local mgm.MultiplayerGameMode, mfl.MapForList, sa.Save, r.Rooms
	Local category$
	Local sx%, sy%, swidth%, sheight%
	
	If cpt\Current < 2 Then
		If opt\IntroEnabled Then
			cpt\Current = 0
		Else
			cpt\Current = 1
		EndIf
	EndIf
	
	ShowPointer()
	
	If MainMenuTab = MenuTab_PointsShop Then
		
		UpdatePointsShop()
		
		sx = 59 * MenuScale
		sy = 286 * MenuScale
		
		swidth = 400 * MenuScale
		sheight = 70 * MenuScale
		
		If DrawButton(sx + swidth + 20 * MenuScale, sy, 580 * MenuScale - swidth - 20 * MenuScale, sheight, Upper(GetLocalString("Menu", "exit_shop")), False, False, True, 0, MainMenuTab, co\CurrButtonSub[MainMenuTab]) Then
			MainMenuTab = MenuTab_NewGame
		EndIf
		
	EndIf
	
	If MainMenuOpen Then
		If (Not mns\WolfnayaButtonPressed) Then
			Select opt\MainMenuMusic
				Case 0
					ShouldPlay = Rand(MUS_MENU,MUS_MENU5)
				Case 1
					ShouldPlay = MUS_NTF
				Case 2
					ShouldPlay = MUS_UE
				Case 3
					ShouldPlay = MUS_CB
				Case 4
					ShouldPlay = MUS_BLACK_MESA
			End Select
		Else
			ShouldPlay = MUS_SAVE_ME_FROM
		EndIf
	EndIf
	
	InitLanguage()
	
	If MouseOn(20,opt\GraphicHeight-30,175*MenuScale,20*MenuScale) Then
		If MouseHit1 Then
			mns\WolfnayaButtonPressed = True
		EndIf
	EndIf
	
	If Rand(300) = 1 Then
		MenuBlinkTimer[0] = Rand(4000, 8000)
		MenuBlinkDuration[0] = Rand(200, 500)
	EndIf
	
	MenuBlinkTimer[1]=MenuBlinkTimer[1]-fps\Factor[0]
	If MenuBlinkTimer[1] < MenuBlinkDuration[1]
		If MenuBlinkTimer[1] < 0
			MenuBlinkTimer[1] = Rand(700, 800)
			MenuBlinkDuration[1] = Rand(10, 35)
		EndIf
	EndIf
	
	If (Not MouseDown1)
		OnSliderID = 0
	EndIf
	
	If PrevMainMenuTab<>MainMenuTab
		DeleteMenuGadgets()
	EndIf
	If ShouldDeleteGadgets
		DeleteMenuGadgets()
	EndIf
	PrevMainMenuTab = MainMenuTab
	ShouldDeleteGadgets = False
	
	If MainMenuTab = MenuTab_Default Then
		x = 59 * MenuScale
		y = 286 * MenuScale
		width = 400 * MenuScale
		height = 70 * MenuScale
		If MainMenuOpen Then
			clm\GlobalMode = False
			clm\NTFMode = False
			clm\GuardMode = False
			clm\DMode = False
			;Main Menu Opened
			If opt\Menu3DEnabled Then
				If DrawButtonMenu3D(x, y, width, height, Upper(GetLocalString("Menu", "singleplayer")), True, False, True, i) Then
					MainMenuTab = MenuTab_Singleplayer
					LoadSaveGames()
					clm\GlobalMode = False
					clm\NTFMode = False
					clm\GuardMode = False
					clm\DMode = False
				EndIf
				If DrawButtonMenu3D(x, y + 100 * MenuScale, width, height, Upper(GetLocalString("Menu", "multiplayer")), True, False, True, i) Then
					MainMenuTab = MenuTab_Serverlist
				EndIf
				If DrawButtonMenu3D(x, y + 200 * MenuScale, width, height, Upper(GetLocalString("Menu", "options")), True, False, True, i) Then
					MainMenuTab = MenuTab_Options_Graphics
				EndIf
				If DrawButtonMenu3D(x, y + 300 * MenuScale, width, height, Upper(GetLocalString("Menu", "extra")), True, False, True, i) Then
					MainMenuTab = MenuTab_Extras
				EndIf
				If DrawButtonMenu3D(x, y + 400 * MenuScale, width, height, Upper(GetLocalString("Menu", "quit")), True, False, True, i) Then
					StopStream_Strict(MusicCHN)
					Steam_Shutdown()
					End
				EndIf
			Else
				If DrawButtonMenu3D(x+600 * MenuScale, y, width, height, Upper(GetLocalString("Menu", "singleplayer")), True, False, True, i) Then
					MainMenuTab = MenuTab_Singleplayer
					LoadSaveGames()
					clm\GlobalMode = False
					clm\NTFMode = False
					clm\GuardMode = False
					clm\DMode = False
				EndIf
				If DrawButtonMenu3D(x+610 * MenuScale, y + 100 * MenuScale, width, height, Upper(GetLocalString("Menu", "multiplayer")), True, False, True, i) Then
					MainMenuTab = MenuTab_Serverlist
				EndIf
				If DrawButtonMenu3D(x+640 * MenuScale, y + 200 * MenuScale, width, height, Upper(GetLocalString("Menu", "options")), True, False, True, i) Then
					MainMenuTab = MenuTab_Options_Graphics
				EndIf
				If DrawButtonMenu3D(x+670 * MenuScale, y + 300 * MenuScale, width, height, Upper(GetLocalString("Menu", "extra")), True, False, True, i) Then
					MainMenuTab = MenuTab_Extras
				EndIf
				If DrawButtonMenu3D(x+690 * MenuScale, y + 400 * MenuScale, width, height, Upper(GetLocalString("Menu", "quit")), True, False, True, i) Then
					StopStream_Strict(MusicCHN)
					Steam_Shutdown()
					End
				EndIf
			EndIf
		ElseIf gc\CurrGamemode<>3 Then
			;Singleplayer Pause Menu Opened
			If DrawButtonMenu3D(x, y, width, height, Upper(GetLocalString("Menu", "resume")), True, False, True, i) Then
				MenuOpen = False
				ResumeSounds()
				DeleteMenuGadgets()
				ResetInput()
				Return
			EndIf
			If DrawButtonMenu3D(x, y + 100 * MenuScale, width, height, Upper(GetLocalString("Menu", "loadgame")), True, False, True, i) Then
				If GameSaved And (Not SelectedDifficulty\PermaDeath) Then
					;DrawLoading(0)
					
					DeleteMenuGadgets()
					MenuOpen = False
					LoadGameQuick(SavePath + CurrSave\Name + "\")
					
					MoveMouse Viewport_Center_X,Viewport_Center_Y
					HidePointer()
					
					KillSounds()
					
					Playable=True
					
					UpdateRooms()
					UpdateDoors()
					
					For r.Rooms = Each Rooms
						x = Abs(EntityX(Collider) - EntityX(r\obj))
						z = Abs(EntityZ(Collider) - EntityZ(r\obj))
						
						If x < 12.0 And z < 12.0 Then
							MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)] = Max(MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)], 1)
							If x < 4.0 And z < 4.0 Then
								If Abs(EntityY(Collider) - EntityY(r\obj)) < 1.5 Then PlayerRoom = r
								MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)] = 1
							EndIf
						EndIf
					Next
					
					;DrawLoading(100,True)
					PlaySound_Strict LoadTempSound(("SFX\Horror\Horror8.ogg"))
					
					DropSpeed=0
					
					UpdateWorld 0.0
					
					fps\PrevTime = MilliSecs()
					fps\Factor[0] = 0
					
					ResetInput()
					
					ResumeSounds()
					Return
				EndIf
			EndIf
			If DrawButtonMenu3D(x, y + 200 * MenuScale, width, height, Upper(GetLocalString("Menu", "options")), True, False, True, i) Then
				MainMenuTab = MenuTab_Options_Graphics
			EndIf
			If DrawButtonMenu3D(x, y + 300 * MenuScale, width, height, Upper(GetLocalString("Menu", "achievements")), True, False, True, i) Then; ~ PauseMenu
				MainMenuTab = MenuTab_Achievements
			EndIf
			If DrawButtonMenu3D(x, y + 400 * MenuScale, width, height, Upper(GetLocalString("Menu", "quit_to_menu")), True, False, True, i) Then
				MainMenuOpen = True
				NullGame()
				MenuOpen = False
				MainMenuTab = 0
				CurrSave = Null
				ResetInput()
				LoadChaptersValueFile()
				mns\WolfnayaButtonPressed = False
				
				HasBoughtBeretta = False
				LockedBeretta = False
				HasBoughtUSP = False
				LockedUsp = False
				HasBoughtMP5 = False
				LockedMP5 = False
				HasBoughtP90 = False
				LockedP90 = False
				HasBoughtMedkit = False
				LockedMedkit = False
				HasBoughtCrowbar = False
				LockedCrowbar = False
				HasBoughtKevlar = False
				LockedKevlar = False
				HasBoughtG36 = False
				LockedG36 = False
				HasBoughtSPAS = False
				LockedSPAS = False
				HasBoughtR870 = False
				LockedR870 = False
				
				ResetEventValues()
				
				Return
			EndIf
		Else
			;Multiplayer Pause Menu Opened
			If DrawButtonMenu3D(x, y, width, height, Upper(GetLocalString("Menu", "resume")), True, False, True, i) Then
				MenuOpen = False
				ResumeSounds()
				DeleteMenuGadgets()
				ResetInput()
				Return
			EndIf
			If DrawButtonMenu3D(x, y + 100 * MenuScale, width, height, Upper(GetLocalString("Menu", "options")), True, False, True, i) Then
				MainMenuTab = MenuTab_Options_Graphics
			EndIf
			If DrawButtonMenu3D(x, y + 200 * MenuScale, width, height, Upper(GetLocalString("Menu", "achievements")), True, False, True, i) Then
				MainMenuTab = MenuTab_Achievements
			EndIf
			If DrawButtonMenu3D(x, y + 300 * MenuScale, width, height, Upper(GetLocalString("Menu", "disconnect")), True, False, True, i) Then
				LeaveMPGame(True)
				Return
			EndIf
		EndIf
		
		;UpdateMenuControllerSelection(6,0,0)
	Else
		
		x = 59 * MenuScale
		y = 286 * MenuScale
		
		width = 400 * MenuScale
		height = 70 * MenuScale
		
		Local pressedbutton%=False
		If MainMenuTab = MenuTab_NewGame Then
			If DrawButton(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height, Upper(GetLocalString("Menu", "back")), False, False, True, 0, MainMenuTab, 0) Then
				pressedbutton = True
			EndIf
		ElseIf MainMenuTab = MenuTab_Lobby Then
			If DrawButton(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height, Upper(GetLocalString("Menu", "disconnect")), False, False, True, 0, MainMenuTab, co\CurrButtonSub[MainMenuTab]) Then
				pressedbutton = True
				Disconnect()
			EndIf
		ElseIf MainMenuTab = MenuTab_Serverlist Then
			If mp_I\ServerMSG = SERVER_MSG_NONE Lor mp_I\ServerMSG = SERVER_MSG_OFFLINE Then
				If DrawButton(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height, Upper(GetLocalString("Menu", "back")), False, False, True, 0, MainMenuTab, co\CurrButtonSub[MainMenuTab]) Then
					pressedbutton = True
				EndIf
			EndIf
		Else
			If MainMenuTab <> MenuTab_PointsShop And MainMenuTab <> MenuTab_Chapters Then
				If DrawButton(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height, Upper(GetLocalString("Menu", "back")), False, False, True, 0, MainMenuTab, co\CurrButtonSub[MainMenuTab]) Then
					pressedbutton = True
				EndIf
			EndIf
		EndIf
		If co\Enabled Then
			If JoyHit(CKM_Back) Then
				pressedbutton = True
				PlaySound_Strict ButtonSFX[2]
			EndIf
		EndIf
		
		If pressedbutton Then
			co\CurrButtonSub[MainMenuTab] = 0
			Select MainMenuTab
				Case MenuTab_NewGame
					MainMenuTab = MenuTab_Singleplayer
					LoadSaveGames()
					SaveOptionsINI()
				Case MenuTab_Options_Graphics,MenuTab_Options_Audio,MenuTab_Options_Controls,MenuTab_Options_Advanced;,MenuTab_Options_Multiplayer ;save the options
					SaveOptionsINI()
					UserTrackCheck% = 0
					UserTrackCheck2% = 0
					
					MainMenuTab = MenuTab_Default
				Case MenuTab_Options_ControlsBinding
					SaveOptionsINI()
					MainMenuTab = MenuTab_Options_Controls
				Case MenuTab_PointsShop
					MainMenuTab = MenuTab_PointsShop
				Case MenuTab_Chapters
					MainMenuTab = MenuTab_Chapters
				Case MenuTab_Achievements,MenuTab_Credits,MenuTab_Websites
					If (MainMenuTab <> MenuTab_Achievements And MainMenuTab <> MenuTab_Credits And MainMenuTab <> MenuTab_Websites) Lor MainMenuOpen Then
						MainMenuTab = MenuTab_Extras
					Else
						MainMenuTab = MenuTab_Default
					EndIf
					ScrollMenuHeight = 0
					ScrollBarY = 0
					co\ScrollBarY = 0
					CurrLoadGamePage = 0
				Case MenuTab_Options_Controller
					MainMenuTab = MenuTab_Options_Controls
				Case MenuTab_LoadGame,MenuTab_GameModes,MenuTab_PointsShop,MenuTab_Chapters
					MainMenuTab = MenuTab_Singleplayer
					CurrLoadGamePage = 0
					LoadSaveGames()
				Case MenuTab_Lobby
					If mp_I\PlayState = GAME_SERVER Then
						MainMenuTab = MenuTab_HostServer
						;If mp_O\LocalServer=False Then
						;	RemoveServer(Steam_GetPlayerIDLower(), Steam_GetPlayerIDUpper())
						;EndIf
					Else
						MainMenuTab = MenuTab_Serverlist
					EndIf
					mp_I\PlayState = 0
					mp_I\HasRefreshed = False
				Case MenuTab_HostServer
					mp_I\PasswordVisible = False
					MainMenuTab = MenuTab_Serverlist
				Case MenuTab_Serverlist
					DelSave = Null
					SaveMPOptions()
					mp_I\HasRefreshed = False
					mp_I\PasswordVisible = False
					MainMenuTab = MenuTab_Default
				Case MenuTab_SelectMPMap,MenuTab_SelectMPGamemode,MenuTab_MPGamemodeSettings
					MainMenuTab = MenuTab_HostServer
				Default
					MainMenuTab = MenuTab_Default
			End Select
			co\PressedButton = False
		EndIf
		
		Select MainMenuTab
			Case MenuTab_Continue
				;[Block]
				For sa = Each Save
					If sa\Name = m_I\CurrentSave Then
						CurrSave = sa
						MainMenuOpen = False
						ResetControllerSelections()
						Null3DMenu()
						LoadEntities()
						LoadAllSounds()
						LoadGame(SavePath + CurrSave\Name + "\")
						InitLoadGame()
						Exit
					EndIf
				Next
				;[End Block]
			Case MenuTab_NewGame
				;[Block]
				If SelectedDifficulty\Customizable And co\CurrButton[1]=7 And co\CurrButtonSub[1]=0
					UpdateMenuControllerSelection(9,1,2,2)
				ElseIf SelectedDifficulty\Customizable And co\CurrButtonSub[1]=1
					UpdateMenuControllerSelection(4,1,2,2)
					If co\CurrButton[1]=7 Then co\CurrButton[1]=0
					If co\CurrButtonSub[1]=0 Then co\CurrButton[1]=7
				ElseIf co\CurrButton[1]=8
					UpdateMenuControllerSelection(9,1,2,2)
					If co\CurrButton[1]<>8 Then co\CurrButtonSub[1]=0
				Else
					Local prev = co\CurrButton[1]
					UpdateMenuControllerSelection(9,1,0)
					If co\CurrButton[1]=8 And prev>0
						co\CurrButtonSub[1]=1
					EndIf
				EndIf
				
				clm\GlobalMode = False
				IsStartingFromMenu = True
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 330 * MenuScale
				
				CurrSave\Name = InputBox(x + 150 * MenuScale, y + 15 * MenuScale, 200 * MenuScale, 30 * MenuScale, CurrSave\Name, 1, 15, 1, MainMenuTab)
				If SelectedInputBox = 1 Then
					CurrSave\Name = Replace(CurrSave\Name,":","")
					CurrSave\Name = Replace(CurrSave\Name,".","")
					CurrSave\Name = Replace(CurrSave\Name,"/","")
					CurrSave\Name = Replace(CurrSave\Name,"\","")
					CurrSave\Name = Replace(CurrSave\Name,">","")
					CurrSave\Name = Replace(CurrSave\Name,"<","")
					CurrSave\Name = Replace(CurrSave\Name,"|","")
					CurrSave\Name = Replace(CurrSave\Name,Chr(34),"")
					CurrSave\Name = Replace(CurrSave\Name,"*","")
					CurrSave\Name = Replace(CurrSave\Name,"?","")
					CursorPos = Min(CursorPos, Len(CurrSave\Name))
				EndIf
				
				RandomSeed = InputBox(x+150*MenuScale, y+55*MenuScale, 200*MenuScale, 30*MenuScale, RandomSeed, 3, 15, 2, MainMenuTab)
					
				opt\IntroEnabled = DrawTick(x + 280 * MenuScale, y + 130 * MenuScale, opt\IntroEnabled, False, 3, MainMenuTab)
				;If DrawButton(x + 280 * MenuScale, y + 125 * MenuScale, 120*MenuScale, 30*MenuScale, GetLocalString("Menu", "skip_intro_sp"), False, False, True, 2, MainMenuTab) Then cpt\Current = 1
				
				If opt\IntroEnabled Then
					cpt\Current = 0
				EndIf
				
				If DrawButton(x+280*MenuScale, y+90*MenuScale, 120*MenuScale, 30*MenuScale, GetLocalString("Menu", "classic_mode"), False, False, True, 2, MainMenuTab) Then MainMenuTab = MenuTab_GameModes
				
				For i = SAFE To ESOTERIC
					Local PrevSelectedDifficulty.Difficulty = SelectedDifficulty
					If DrawTick(x + 20 * MenuScale, y + (180+30*i) * MenuScale, (SelectedDifficulty = difficulties[i]),False,(i+4),MainMenuTab) Then SelectedDifficulty = difficulties[i]
					If PrevSelectedDifficulty<>SelectedDifficulty
						If PrevSelectedDifficulty = difficulties[ESOTERIC]
							ShouldDeleteGadgets=True
						EndIf
					EndIf
				Next
				
				If SelectedDifficulty\Customizable
					SelectedDifficulty\PermaDeath =  DrawTick(x + 160 * MenuScale, y + 165 * MenuScale, (SelectedDifficulty\PermaDeath), False, 0, MainMenuTab, 1)
					
					If DrawTick(x + 160 * MenuScale, y + 195 * MenuScale, SelectedDifficulty\SaveType = SAVEANYWHERE And (Not SelectedDifficulty\PermaDeath), SelectedDifficulty\PermaDeath, 1, MainMenuTab, 1) Then 
						SelectedDifficulty\SaveType = SAVEANYWHERE
					Else
						SelectedDifficulty\SaveType = SAVEONSCREENS
					EndIf
					
					SelectedDifficulty\AggressiveNPCs =  DrawTick(x + 160 * MenuScale, y + 225 * MenuScale, SelectedDifficulty\AggressiveNPCs, False, 2, MainMenuTab, 1)
					
					;Other factor's difficulty
					If (Not co\Enabled)
						If MouseHit1
							If ImageRectOverlap(I_MIG\ArrowIMG[1],x + 155 * MenuScale, y+251*MenuScale, ScaledMouseX(),ScaledMouseY(),0,0)
								If SelectedDifficulty\OtherFactors < IMPOSSIBLE
									SelectedDifficulty\OtherFactors = SelectedDifficulty\OtherFactors + 1
								Else
									SelectedDifficulty\OtherFactors = EASY
								EndIf
								PlaySound_Strict(ButtonSFX[2])
							EndIf
						EndIf
					Else
						If co\CurrButton[MainMenuTab]=3 And co\CurrButtonSub[MainMenuTab]=1
							If co\PressedButton
								If SelectedDifficulty\OtherFactors < IMPOSSIBLE
									SelectedDifficulty\OtherFactors = SelectedDifficulty\OtherFactors + 1
								Else
									SelectedDifficulty\OtherFactors = EASY
								EndIf
								PlaySound_Strict(ButtonSFX[2])
							EndIf
						EndIf
					EndIf
				EndIf
				
				If DrawButton(x + 420 * MenuScale, y + height + 45 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "start")), False, False, True, 8, MainMenuTab, 0) Then
					If CurrSave\Name = "" Then CurrSave\Name = "untitled"
					
					IsStartingFromMenu = True
					
					If RandomSeed = "" Then
						RandomSeed = Abs(MilliSecs())
					EndIf
					
					SeedRnd GenerateSeedNumber(RandomSeed)
					
					gc\CurrGamemode = 0
					MainMenuOpen = False
					
					Local SameFound% = 0 ; TODO put this in a func
					Local LowestPossible% = 2
					
					For  I_SAV.Save = Each Save
						If (CurrSave <> I_SAV And CurrSave\Name = I_SAV\Name) Then SameFound = 1 : Exit
					Next
					
					While SameFound = 1
						SameFound = 2
						For I_SAV.Save = Each Save
							If (I_SAV\Name = (CurrSave\Name + " (" + LowestPossible + ")")) Then LowestPossible = LowestPossible + 1 : SameFound = True : Exit
						Next
					Wend
					
					If SameFound = 2 Then CurrSave\Name = CurrSave\Name + " (" + LowestPossible + ")"
					
					ResetControllerSelections()
					Null3DMenu()
					
					If (Not clm\NTFMode) And (Not clm\GuardMode) And (Not clm\DMode) Then
						
						If cpt\Current = 0 Lor cpt\Current = 1 Lor cpt\Current = 7 Then
							gc\CurrZone = LCZ
						ElseIf cpt\Current = 2 Lor cpt\Current = 3 Lor cpt\Current = 4 Lor cpt\Current = 5 Lor cpt\Current = 6 Lor cpt\Current = 8 Then
							gc\CurrZone = HCZ
						ElseIf cpt\Current = 9 Then
							If ChapterGateA Then
								gc\CurrZone = GATE_A_TOPSIDE
							ElseIf ChapterGateB Then
								gc\CurrZone = GATE_B_TOPSIDE
							ElseIf ChapterGateC Then
								gc\CurrZone = GATE_C_TOPSIDE
							Else
								gc\CurrZone = EZ
							EndIf
						EndIf
						
						If cpt\Current = 0 Then
							opt\IntroEnabled = True
						EndIf
							
					ElseIf opt\IntroEnabled And clm\NTFMode Then
						gc\CurrZone = GATE_A_INTRO
					ElseIf (Not opt\IntroEnabled) And clm\NTFMode Then
						gc\CurrZone = EZ
					ElseIf clm\GuardMode Then
						gc\CurrZone = EZ
					Else
						gc\CurrZone = LCZ
					EndIf
					
					LoadEntities()
					LoadAllSounds()
					InitNewGame()
					FlushKeys()
					FlushMouse()
					FlushJoy()
					
					SaveOptionsINI()
				EndIf
				
				If (Not clm\NTFMode) And (Not clm\DMode) And (Not clm\GuardMode) Then
					If DrawButton(x, y + height + 45 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "chapters")), False, False, True, 8, MainMenuTab, 0) Then MainMenuTab = MenuTab_Chapters
				Else
					If DrawButton(x, y + height + 45 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "chapters")), False, False, True, 8, MainMenuTab, 0, False) Then MainMenuTab = MenuTab_NewGame
				EndIf
					
				;[End Block]
			Case MenuTab_LoadGame
				;[Block]
				If co\CurrButton[2]=0
					;co\CurrButtonSub[2]=0
					UpdateMenuControllerSelection(1+SaveGameAmount,2,0)
				Else
					If DelSave = Null
						UpdateMenuControllerSelection(1+SaveGameAmount,2,2,2)
					Else
						UpdateMenuControllerSelection(1+SaveGameAmount,2,3,2)
					EndIf
				EndIf
				
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale
				
				If CurrLoadGamePage < Ceil(Float(SaveGameAmount)/6.0)-1 And DelSave = Null Then 
					If DrawButton(x+530*MenuScale, y + 510*MenuScale, 51*MenuScale, 55*MenuScale, ">", 2) Then
						CurrLoadGamePage = CurrLoadGamePage+1
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				If CurrLoadGamePage > 0 And DelSave = Null Then
					If DrawButton(x, y + 510*MenuScale, 51*MenuScale, 55*MenuScale, "<", 2) Then
						CurrLoadGamePage = CurrLoadGamePage-1
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				
				If CurrLoadGamePage > Ceil(Float(SaveGameAmount)/6.0)-1 Then
					CurrLoadGamePage = CurrLoadGamePage - 1
					ShouldDeleteGadgets = True
				EndIf
				
				If SaveGameAmount = 0 Then
					
				Else
					x = x + 20 * MenuScale
					y = y + 20 * MenuScale
					
					CurrSave = First Save
					
					For i% = 0 To 5+(6*CurrLoadGamePage)
						If i > 0 Then CurrSave = After CurrSave
						If CurrSave = Null Then Exit
						If i >= (6*CurrLoadGamePage) Then
							If DelSave = Null Then
								If CurrSave\Version <> CompatibleNumber Then
									
								Else
									If DrawButton(x + 280 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale, GetLocalString("Menu", "load"), False) Then
										MainMenuOpen = False
										ResetControllerSelections()
										Null3DMenu()
										LoadEntities()
										LoadAllSounds()
										LoadGame(SavePath + CurrSave\Name + "\")
										InitLoadGame()
										Exit
									EndIf
								EndIf
								
								If DrawButton(x + 400 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale, GetLocalString("Menu", "delete"), False) Then
									DelSave = CurrSave
									Exit
								EndIf
							EndIf
							
							If CurrSave = Last Save Then
								Exit
							EndIf
							
							y = y + 80 * MenuScale
						EndIf
					Next
					
					If DelSave <> Null
						x = 640 * MenuScale
						y = 376 * MenuScale
						DrawFrame(x, y, 420 * MenuScale, 200 * MenuScale)
						If DrawButton(x + 50 * MenuScale, y + 150 * MenuScale, 100 * MenuScale, 30 * MenuScale, GetLocalString("Menu", "yes"), False) Then
							
							DeleteGame(DelSave)
							DelSave = Null
							LoadSaveGames()
							ShouldDeleteGadgets = True
						EndIf
						If DrawButton(x + 250 * MenuScale, y + 150 * MenuScale, 100 * MenuScale, 30 * MenuScale, GetLocalString("Menu", "no"), False) Then
							DelSave = Null
							ShouldDeleteGadgets = True
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case MenuTab_Options_Graphics,MenuTab_Options_Audio,MenuTab_Options_Controls,MenuTab_Options_Advanced,MenuTab_Options_Controller,MenuTab_Options_ControlsBinding
				;[Block]
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				If MainMenuTab<>MenuTab_Options_Controller And MainMenuTab<>MenuTab_Options_ControlsBinding Then
					x = 60 * MenuScale
					y = y + height + 20 * MenuScale
					width = 580 * MenuScale
					height = 60 * MenuScale
					Local prevButton = co\CurrButton[MainMenuTab]
					Local prevButtonSub = co\CurrButtonSub[MainMenuTab]
					
					If DrawButton(x+20*MenuScale,y+15*MenuScale,width/5,height/2, Upper(GetLocalString("Options", "graphics")), False,False,True,1,MainMenuTab,0) Then MainMenuTab = MenuTab_Options_Graphics : co\CurrButtonSub[MainMenuTab]=0 : co\CurrButton[MainMenuTab]=1
					If DrawButton(x+160*MenuScale,y+15*MenuScale,width/5,height/2, Upper(GetLocalString("Options", "audio")), False,False,True,1,MainMenuTab,1) Then MainMenuTab = MenuTab_Options_Audio : co\CurrButtonSub[MainMenuTab]=1 : co\CurrButton[MainMenuTab]=1
					If DrawButton(x+300*MenuScale,y+15*MenuScale,width/5,height/2, Upper(GetLocalString("Options", "controls")), False,False,True,1,MainMenuTab,2) Then MainMenuTab = MenuTab_Options_Controls : co\CurrButtonSub[MainMenuTab]=2 : co\CurrButton[MainMenuTab]=1
					If DrawButton(x+440*MenuScale,y+15*MenuScale,width/5,height/2, Upper(GetLocalString("Options", "advanced")), False,False,True,1,MainMenuTab,3) Then MainMenuTab = MenuTab_Options_Advanced : co\CurrButtonSub[MainMenuTab]=3 : co\CurrButton[MainMenuTab]=1
					
					y = y + 70 * MenuScale
				Else
					UpdateMenuControllerSelection(1,3,0)
					
					x = 60 * MenuScale
					y = y + height + 20 * MenuScale
					width = 580 * MenuScale
					height = 330 * MenuScale
					y = y + 30 * MenuScale
				EndIf
				
				If MainMenuTab <> MenuTab_Options_Audio Then
					UserTrackCheck% = 0
					UserTrackCheck2% = 0
				EndIf
				
				Local tx# = x+width
				Local ty# = y
				Local tw# = 400*MenuScale
				Local th# = 150*MenuScale
				
				;DrawOptionsTooltip("")
				
				If MainMenuTab = MenuTab_Options_Graphics Then
					;[Block]
					height = 390 * MenuScale
					
					y=y+25*MenuScale
					ScreenGamma = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, (ScreenGamma-0.5)*100.0,2,MainMenuTab,0,Upper(GetLocalString("Options", "low")),Upper(GetLocalString("Options", "high")))/100.0)+0.5
					
					y=y+35*MenuScale
                    FOV = (SlideBar(x + 310*MenuScale, y,150*MenuScale, (FOV-40)*2.0,3,MainMenuTab,0,40,90)/2.0)+40
					
					y=y+35*MenuScale
					Local hasFrameLimit = (CurrFrameLimit>0.0)
					
					If DrawTick(x + (215+(160*(Not hasFrameLimit))) * MenuScale, y, CurrFrameLimit > 0.0) Then
						CurrFrameLimit = Max((SlideBar(x + 310*MenuScale, y, 150*MenuScale, CurrFrameLimit#*87,4,MainMenuTab,0,30,144)/87), 0.01)
						Framelimit = 29 + (CurrFrameLimit * 100.0)
					Else
						CurrFrameLimit = 0.0
						Framelimit = 0
					EndIf
					
					If hasFrameLimit And hasFrameLimit<>CurrFrameLimit Then
						ShouldDeleteGadgets = True
					EndIf
					
					y=y+35*MenuScale
					Vsync% = DrawTick(x + 375 * MenuScale, y + MenuScale, Vsync%, False, 5, MainMenuTab, 0)
					
					y=y+35*MenuScale
					opt\TextureDetails = DrawDropdown(x+315*MenuScale,y,opt\TextureDetails,6,Upper(GetLocalString("Options", "low")),Upper(GetLocalString("Options", "medium")),Upper(GetLocalString("Options", "high")))
					Select opt\TextureDetails%
						Case 0
							TextureFloat# = 0.8
						Case 1
							TextureFloat# = 0.0
						Case 2
							TextureFloat# = -0.8
					End Select
					TextureLodBias TextureFloat
					
					y=y+35*MenuScale
					opt\TextureFiltering = DrawDropdown(x+315*MenuScale,y,opt\TextureFiltering,7,"2x","4x","8x","16x")
					TextureAnisotropic 2^(opt\TextureFiltering+1)
					
					y=y+35*MenuScale
					ParticleAmount = DrawDropdown(x+315*MenuScale,y,ParticleAmount,8,Upper(GetLocalString("Options", "low")),Upper(GetLocalString("Options", "medium")),Upper(GetLocalString("Options", "high")))
					
					y=y+35*MenuScale
					opt\RenderCubeMapMode = DrawDropdown(x+315*MenuScale,y,opt\RenderCubeMapMode,9,Upper(GetLocalString("Options", "off")),Upper(GetLocalString("Options", "low")),Upper(GetLocalString("Options", "high")))
					
					y=y+35*MenuScale
					If MainMenuOpen Then
						opt\RenderScope = DrawTick(x + 375 * MenuScale, y, opt\RenderScope, False, 11, MainMenuTab, 0)
					Else
						opt\RenderScope = DrawTick(x + 375 * MenuScale, y, opt\RenderScope, True, 11, MainMenuTab, 0)
					EndIf
					
					y=y+35*MenuScale
					opt\EnableRoomLights = DrawTick(x + 375 * MenuScale, y, opt\EnableRoomLights, False, 11, MainMenuTab, 0)
					
					y = y + 35*MenuScale
					
					opt\PlayStartupVideos = DrawTick(x + 375 * MenuScale, y + MenuScale, opt\PlayStartupVideos, False, 6, MainMenuTab, 0)
					
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_Audio Then
					;[Block]
					;height = 330 * MenuScale
					height = 390 * MenuScale
					
					y = y + 25*MenuScale
					
					aud\MasterVol = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, opt\MasterVol*100.0,2,MainMenuTab,0)/100.0)
					opt\MasterVol = aud\MasterVol
					
					y = y + 35*MenuScale
					
					aud\MusicVol = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, opt\MusicVol*100.0,3,MainMenuTab,0)/100.0)
					opt\MusicVol = aud\MusicVol
					
					y = y + 35*MenuScale
					
					aud\EnviromentVol = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, opt\SFXVolume*100.0,4,MainMenuTab,0)/100.0)
					opt\SFXVolume = aud\EnviromentVol
					
					y = y + 35*MenuScale
					
					aud\VoiceVol = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, opt\VoiceVol*100.0,5,MainMenuTab,0)/100.0)
					opt\VoiceVol = aud\VoiceVol
					
					y = y + 35*MenuScale
					
					opt\EnableSFXRelease = DrawTick(x + 375 * MenuScale, y + MenuScale, opt\EnableSFXRelease, False, 6, MainMenuTab, 0)
					If opt\EnableSFXRelease_Prev% <> opt\EnableSFXRelease
						If opt\EnableSFXRelease%
							Local snd.Sound
							For snd.Sound = Each Sound
								For i=0 To 31
									If snd\channels[i]<>0 Then
										If ChannelPlaying(snd\channels[i]) Then
											StopChannel(snd\channels[i])
										EndIf
									EndIf
								Next
								If snd\internalHandle<>0 Then
									FreeSound snd\internalHandle
									snd\internalHandle = 0
								EndIf
								snd\releaseTime = 0
							Next
						Else
							For snd.Sound = Each Sound
								If snd\internalHandle = 0 Then snd\internalHandle = LoadSound(snd\name)
							Next
						EndIf
						opt\EnableSFXRelease_Prev% = opt\EnableSFXRelease
					EndIf
					
					y = y + 35*MenuScale
					
					opt\BMSBossMusic = DrawTick(x + 375 * MenuScale, y + MenuScale, opt\BMSBossMusic, False, 6, MainMenuTab, 0)
					
					y = y + 35*MenuScale
					
					opt\ElevatorMusicEnabled = DrawTick(x + 375 * MenuScale, y + MenuScale, opt\ElevatorMusicEnabled, False, 6, MainMenuTab, 0)
					
					y = y + 35*MenuScale
					
					opt\MainMenuMusic = DrawDropdown(x+315*MenuScale,y,opt\MainMenuMusic,8,Upper("SCP-SS"),Upper("SCP-NTF"),Upper("SCP-UE"),Upper("SCP-CB"),Upper("Black Mesa"))
					
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_Controls Then
					;[Block]
					
					height = 320 * MenuScale
					
					y = y + 25*MenuScale
					MouseSens = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, (MouseSens+0.5)*100.0)/100.0)-0.5
					
					y = y + 35*MenuScale
					opt\MouseSmooth = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, (opt\MouseSmooth)*100.0)/100.0)
					
					y = y + 35*MenuScale
					InvertMouse = DrawTick(x + 375 * MenuScale, y, InvertMouse)
					
					y = y + 35*MenuScale
					opt\HoldToAim = DrawTick(x + 375 * MenuScale, y, opt\HoldToAim)
					
					y = y + 35*MenuScale
					opt\HoldToCrouch = DrawTick(x + 375 * MenuScale, y, opt\HoldToCrouch)
					
					y = y + 50*MenuScale
					
					If DrawButton(x+20*MenuScale,y,220*MenuScale,30*MenuScale,"Control configuration",False) Then
						MainMenuTab = MenuTab_Options_ControlsBinding
					EndIf	
					
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_ControlsBinding Then
					;[Block]
					InputBox(x + 320 * MenuScale, y + 20 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_UP,210)],5)
					InputBox(x + 320 * MenuScale, y + 40 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_DOWN,210)],6)
					InputBox(x + 320 * MenuScale, y + 60 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_LEFT,210)],3)
					InputBox(x + 320 * MenuScale, y + 80 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_RIGHT,210)],4)
					InputBox(x + 320 * MenuScale, y + 100 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_CROUCH,210)],10)
					InputBox(x + 320 * MenuScale, y + 120 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_SPRINT,210)],8)
					
					InputBox(x + 320 * MenuScale, y + 160 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_HOLSTERGUN,210)],17)
					InputBox(x + 320 * MenuScale, y + 180 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_RELOAD,210)],13)
					InputBox(x + 320 * MenuScale, y + 200 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_CHANGEFIREMODE,210)],22)
					InputBox(x + 320 * MenuScale, y + 220 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_SELECTATTACHMENT,210)],23)
					InputBox(x + 320 * MenuScale, y + 240 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_SCRAMBLE,210)],25)
					
					InputBox(x + 320 * MenuScale, y + 280 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_BLINK,210)],7)
					InputBox(x + 320 * MenuScale, y + 300 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_INV,210)],9)
					InputBox(x + 320 * MenuScale, y + 320 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_USE,210)],19)
					
					InputBox(x + 320 * MenuScale, y + 360 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(kb\ChatKey,210)],21)
					InputBox(x + 320 * MenuScale, y + 380 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(kb\CommandWheelKey,210)],14)
					InputBox(x + 320 * MenuScale, y + 400 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(kb\SocialWheelKey,210)],15)
					
					InputBox(x + 320 * MenuScale, y + 440 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_CONSOLE,210)],12)
					InputBox(x + 320 * MenuScale, y + 460 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_SAVE,210)],11)
					InputBox(x + 320 * MenuScale, y + 480 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_LOAD,210)],24)
					
					Local KEY%
					If SelectedInputBox <> 0 Then
						For i = 0 To 227
							If KeyHit(i) Then KEY = i : Exit
						Next
					EndIf	
					If KEY<>0 Then
						Select SelectedInputBox
							Case 3
								KEY_LEFT = KEY
							Case 4
								KEY_RIGHT = KEY
							Case 5
								KEY_UP = KEY
							Case 6
								KEY_DOWN = KEY
							Case 7
								KEY_BLINK = KEY
							Case 8
								KEY_SPRINT = KEY
							Case 9
								KEY_INV = KEY
							Case 10
								KEY_CROUCH = KEY
							Case 11
								KEY_SAVE = KEY
							Case 12
								KEY_CONSOLE = KEY
							Case 13
								KEY_RELOAD = KEY
							Case 14
								kb\CommandWheelKey = KEY
							Case 15
								kb\SocialWheelKey = KEY
							Case 16
								kb\NVToggleKey = KEY
							Case 17
								KEY_HOLSTERGUN = KEY
							Case 18
								KEY_RADIOTOGGLE = KEY
							Case 19
								KEY_USE = KEY
							Case 21
								kb\ChatKey = KEY
							Case 22
								KEY_CHANGEFIREMODE = KEY
							Case 23
								KEY_SELECTATTACHMENT = KEY
							Case 24
								KEY_LOAD = KEY
							Case 25
								KEY_SCRAMBLE = KEY
						End Select
						SelectedInputBox = 0
						KEY = 0
					EndIf
					;[End block]
				ElseIf MainMenuTab = MenuTab_Options_Advanced Then
					;[Block]
					height = 900 * MenuScale
					
					y = y + 25*MenuScale
					HUDenabled = DrawTick(x + 375 * MenuScale, y, HUDenabled,False,2,MainMenuTab,0)
					
					y = y + 35*MenuScale
					opt\ShowFPS% = DrawTick(x + 375 * MenuScale, y, opt\ShowFPS%,False,6,MainMenuTab,0)
					
					y = y + 35*MenuScale
					opt\ConsoleEnabled = DrawTick(x + 375 * MenuScale, y, opt\ConsoleEnabled,False,3,MainMenuTab,0)
					
					y = y + 35*MenuScale
					
					opt\PlayerModelEnabled = DrawTick(x + 375 * MenuScale, y, opt\PlayerModelEnabled,False,3,MainMenuTab,0)
					
					y = y + 35*MenuScale
					;[End Block]
				EndIf
				;[End Block]
			Case MenuTab_PointsShop
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				If DrawButton(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height, Upper(GetLocalString("Menu", "back")), False, False, True, 0, MainMenuTab, co\CurrButtonSub[MainMenuTab]) Then
					MainMenuTab = MenuTab_NewGame
				EndIf
				;[End Block]
			Case MenuTab_Chapters
				;[Block]
				
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				If DrawButton(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height, Upper(GetLocalString("Menu", "back")), False, False, True, 0, MainMenuTab, co\CurrButtonSub[MainMenuTab]) Then
					MainMenuTab = MenuTab_NewGame
				EndIf
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale
				
				If CurrLoadGamePage < 9 Then
					If DrawButton(x+530*MenuScale, y + 490*MenuScale, 51*MenuScale, 55*MenuScale, ">") Then
						CurrLoadGamePage = CurrLoadGamePage+1
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				If CurrLoadGamePage > 0 Then
					If DrawButton(x, y + 490*MenuScale, 51*MenuScale, 55*MenuScale, "<") Then
						CurrLoadGamePage = CurrLoadGamePage-1
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				
				If CurrLoadGamePage > 9 Then
					CurrLoadGamePage = CurrLoadGamePage - 1
					ShouldDeleteGadgets = True
				EndIf
				
				Select CurrLoadGamePage
					Case 0
						If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
							cpt\Current = 0
							opt\IntroEnabled = True
							MainMenuTab = MenuTab_NewGame
						EndIf
					Case 1
						If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
							cpt\Current = 1
							opt\IntroEnabled = False
							MainMenuTab = MenuTab_NewGame
						EndIf
					Case 2
						If cpt\Unlocked > 1 Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								cpt\Current = 2
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_PointsShop
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_1")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 3
						If cpt\Unlocked > 2 Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								cpt\Current = 3
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_PointsShop
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_2")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 4
						If cpt\Unlocked > 3 Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								cpt\Current = 4
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_PointsShop
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_3")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 5
						If cpt\Unlocked > 4 Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								cpt\Current = 5
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_PointsShop
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_4")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 6
						If cpt\Unlocked > 5 Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								cpt\Current = 6
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_PointsShop
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_5")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 7
						If cpt\Unlocked > 6 Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								cpt\Current = 7
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_PointsShop
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_6")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 8
						If cpt\Unlocked > 7 Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								cpt\Current = 8
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_PointsShop
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_7")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 9
						If cpt\Unlocked > 8 Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "chapter_gate_a")), False, False, True, 8, MainMenuTab, 0) Then
								cpt\Current = 9
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_NewGame
								ChapterGateA = True
								ChapterGateB = False
								ChapterGateC = False
								ChapterGateD = False
							EndIf
							If DrawButton(x + 820 * MenuScale, y + 120 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "chapter_gate_b")), False, False, True, 8, MainMenuTab, 0) Then
								cpt\Current = 9
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_NewGame
								ChapterGateA = False
								ChapterGateB = True
								ChapterGateC = False
								ChapterGateD = False
							EndIf
							If DrawButton(x + 820 * MenuScale, y + 220 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "chapter_gate_c")), False, False, True, 8, MainMenuTab, 0) Then
								cpt\Current = 9
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_NewGame
								ChapterGateA = False
								ChapterGateB = False
								ChapterGateC = True
								ChapterGateD = False
							EndIf
							If DrawButton(x + 820 * MenuScale, y + 320 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "chapter_gate_d")), False, False, True, 8, MainMenuTab, 0) Then
								cpt\Current = 9
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_NewGame
								ChapterGateA = False
								ChapterGateB = False
								ChapterGateC = False
								ChapterGateD = True
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_8")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
				End Select
				;[End Block]
			Case MenuTab_Extras
				;[Block]
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 190 * MenuScale
				
				If DrawButton(x+20*MenuScale,y+20*MenuScale,width-40*MenuScale,70*MenuScale,GetLocalString("Menu","achievements"),2) Then MainMenuTab = MenuTab_Achievements
				If DrawButton(x+20*MenuScale,y+100*MenuScale,width-40*MenuScale,70*MenuScale,GetLocalString("Menu","websites"),2) Then MainMenuTab = MenuTab_Websites
				;[End Block]
			Case MenuTab_Achievements
				;[Block]
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale
				
				If CurrLoadGamePage < Ceil(Float(MAXACHIEVEMENTS)/4.0)-1 Then
					If DrawButton(x+530*MenuScale, y + 470*MenuScale, 51*MenuScale, 55*MenuScale, ">") Then
						CurrLoadGamePage = CurrLoadGamePage+1
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				If CurrLoadGamePage > 0 Then
					If DrawButton(x, y + 470*MenuScale, 51*MenuScale, 55*MenuScale, "<") Then
						CurrLoadGamePage = CurrLoadGamePage-1
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				
				If CurrLoadGamePage > Ceil(Float(MAXACHIEVEMENTS)/4.0)-1 Then
					CurrLoadGamePage = CurrLoadGamePage - 1
					ShouldDeleteGadgets = True
				EndIf
				;[End Block]
			Case MenuTab_Websites
				;[Block]
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 465 * MenuScale
				
				If DrawButton(x+20*MenuScale,y+20*MenuScale,width-40*MenuScale,100*MenuScale, "DISCORD", 2) Then
					ExecFile("https://discord.gg/ARgRhhB8ub")
				EndIf
				
				y = y + 110 * MenuScale
				
				If DrawButton(x+20*MenuScale,y+20*MenuScale,width-40*MenuScale,100*MenuScale, "MODDB", 2) Then
					ExecFile("https://www.moddb.com/mods/scp-security-stories")
				EndIf
				
				y = y + 110 * MenuScale
				
				If DrawButton(x+20*MenuScale,y+20*MenuScale,width-40*MenuScale,100*MenuScale, "YOUTUBE", 2) Then
					ExecFile("https://www.youtube.com/channel/UCxH8eXDxIv-QrEYECxcgmKA")
				EndIf
				
				y = y + 110 * MenuScale
				
				If DrawButton(x+20*MenuScale,y+20*MenuScale,width-40*MenuScale,100*MenuScale, "WEBSITE", 2) Then
					ExecFile("https://sites.google.com/view/phantomic-games-studios/main-page")
				EndIf
				;[End Block]
			Case MenuTab_Singleplayer
				;[Block]
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				
				temp = 0
				For sa = Each Save
					If sa\Name = m_I\CurrentSave Then
						If sa\Version = CompatibleNumber Then
							If DrawButton(x+20*MenuScale,y+20*MenuScale,width-40*MenuScale,70*MenuScale, Upper(GetLocalString("Menu", "continue")), 2) Then
								MainMenuTab = MenuTab_Continue
							EndIf
							temp = 1
						EndIf
						Exit
					EndIf
				Next
				If DrawButton(x+20*MenuScale,y+(20 + (80 * temp))*MenuScale,width-40*MenuScale,70*MenuScale, Upper(GetLocalString("Menu", "newgame")), 2) Then
					RandomSeed = ""
					If Rand(16)=1 Then 
						Select Rand(16)
							Case 1 
								RandomSeed = "null"
							Case 2
								RandomSeed = "owo"
							Case 3
								RandomSeed = "benj"
							Case 4
								RandomSeed = ""
							Case 5
								RandomSeed = "asd2"
							Case 6
								RandomSeed = "NWI"
							Case 7
								RandomSeed = "Wolfnaya"
							Case 8
								RandomSeed = "no b*tches?"
							Case 9
								RandomSeed = "noway"
							Case 10
								RandomSeed = "ntf"
							Case 11
								RandomSeed = "JORGE"
							Case 12
								RandomSeed = "devil"
							Case 13
								RandomSeed = "S!X 3!GHT TW0"
							Case 14
								RandomSeed = "ENDSHN"
							Case 15
								RandomSeed = "Ryan"
							Case 16
								RandomSeed = "Vane of a Brain"
						End Select
					Else
						n = Rand(4,8)
						For j = 1 To n
							If Rand(3)=1 Then
								RandomSeed = RandomSeed + Rand(0,9)
							Else
								RandomSeed = RandomSeed + Chr(Rand(97,122))
							EndIf
						Next
					EndIf
					LoadSaveGames()
					CurrSave = New Save
					MainMenuTab = MenuTab_NewGame
				EndIf
				If DrawButton(x+20*MenuScale,y+(100 + (80 * temp))*MenuScale,width-40*MenuScale,70*MenuScale, Upper(GetLocalString("Menu", "loadgame")),2) Then
					LoadSaveGames()
					MainMenuTab = MenuTab_LoadGame
				EndIf
				;[End Block]
			Case MenuTab_GameModes
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				If DrawButton(x + 400 * MenuScale, y + height + 20 * MenuScale, 170 * MenuScale, 170 * MenuScale, Upper(""), False, False, True, 1, MainMenuTab, 0)
					MainMenuTab = MenuTab_NewGame
					
					If cpt\Current > 1 Then
						cpt\Current = 1
					EndIf
					
					clm\NTFMode = True
					clm\GuardMode = False
					clm\DMode = False
				EndIf
				If DrawButton(x + 200 * MenuScale, y + height + 20 * MenuScale, 170 * MenuScale, 170 * MenuScale, Upper(""), False, False, True, 1, MainMenuTab, 0)
					MainMenuTab = MenuTab_NewGame
					
					If cpt\Current > 1 Then
						cpt\Current = 1
					EndIf
					
					clm\NTFMode = False
					clm\GuardMode = True
					clm\DMode = False
				EndIf
				If DrawButton(x, y + height + 20 * MenuScale, 170 * MenuScale, 170 * MenuScale, Upper(""), False, False, True, 1, MainMenuTab, 0)
					MainMenuTab = MenuTab_NewGame
					
					If cpt\Current > 1 Then
						cpt\Current = 1
					EndIf
					
					ClassDNumber = Rand(1000,999999)
					
					clm\NTFMode = False
					clm\GuardMode = False
					clm\DMode = True
				EndIf
				
				If DrawButton(x + 140 * MenuScale, y + height + 370 * MenuScale, 300 * MenuScale, 100 * MenuScale, Upper(GetLocalString("Menu", "classic_deselect")), False, False, True, 1, MainMenuTab, 0)
					MainMenuTab = MenuTab_NewGame
					clm\GlobalMode = False
					clm\NTFMode = False
					clm\GuardMode = False
					clm\DMode = False
				EndIf
				
				;[End Block]
			Case MenuTab_Lobby
				;[Block]
				
				;[End Block]
			Case MenuTab_Serverlist
				;[Block]
				If (Not mp_I\HasRefreshed) Then
					mp_I\ServerListAmount = 1
					ListServers()
					mp_I\ServerListPage = 0
					mp_I\ServerListSort = 0
					mp_I\HasRefreshed = True
				EndIf
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				
				width = 40 * MenuScale
				height = 40 * MenuScale
				
				For i = 0 To (SERVER_LIST_SORT_CATEGORY_MAX-1)
					x = x + width
					Select i
						Case 0
							;Server name
							width = 500 * MenuScale
							category = "servers"
						Case 1
							;Server gamemode
							width = 200 * MenuScale
							category = "gamemode"
						Case 2
							;Server map
							width = 200 * MenuScale
							category = "map"
						Case 3
							;Player amount
							width = 150 * MenuScale
							category = "players"
					End Select
					If DrawButton(x, y, width, height, GetLocalString("Serverlist", category), False) Then
						If Floor(mp_I\ServerListSort / 2.0) = i Then
							mp_I\ServerListSort = mp_I\ServerListSort - ((mp_I\ServerListSort Mod 2) * 2 - 1)
						Else
							mp_I\ServerListSort = i * 2
						EndIf
						ListServers()
					EndIf
				Next
				
				x = 60 * MenuScale
				width = 1130 * MenuScale
				height = 560 * MenuScale
				
				If mp_I\ServerMSG = SERVER_MSG_NONE Then
					If DrawButton(x + 10 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale, GetLocalString("Serverlist", "host_server"), False, False, True) Then
						MainMenuTab = MenuTab_HostServer
						mp_I\SelectedListServer = 0
					EndIf
					
					If DrawButton(x + width*0.25 + 10 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale, GetLocalString("Serverlist", "refresh_list"), False, False, True) Then
						mp_I\SelectedListServer = 0
						mp_I\HasRefreshed = False
						ShouldDeleteGadgets = True
					EndIf
					
					j=1
					For i = (1+(16*mp_I\ServerListPage)) To 16+(16*mp_I\ServerListPage)
						If MouseOn(x,(y+10*MenuScale)+(30*j)*MenuScale,1130*MenuScale,30*MenuScale)
							If MouseHit1 Then
								For se = Each Server
									If se\ID = i Then
										mp_I\SelectedListServer = i
										Exit
									EndIf
								Next
							EndIf
						EndIf
						j=j+1
					Next
					
					If DrawButton(x + width*0.55 + 10 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale, GetLocalString("Serverlist", "join_friend"), False, False, True) Then
						Steam_ActivateOverlay("friends")
					EndIf
					
					If mp_I\SelectedListServer > 0 Then
						If DrawButton(x + width - 210 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale, GetLocalString("Serverlist", "join_server"), False, False, True) Then
							Delete Each MenuButton
							For se = Each Server
								If se\ID = mp_I\SelectedListServer Then
									Connect(se\id_upper, se\id_lower)
									Exit
								EndIf
							Next
							SaveMPOptions()
							If mp_I\PlayState = GAME_CLIENT Then
								Return
							EndIf
						EndIf
					EndIf
					
					x = 59 * MenuScale
					y = 286 * MenuScale
					height = 70 * MenuScale
					
					If mp_I\ServerListAmount > 1 Then
						If mp_I\ServerListPage < Ceil(Float(mp_I\ServerListAmount-1)/16.0)-1 Then
							If DrawButton(x+width-height,y, height, height, ">", 2, False, True, 0, MainMenuTab, 0)
								mp_I\ServerListPage = mp_I\ServerListPage+1
								ShouldDeleteGadgets = True
								mp_I\SelectedListServer = 0
							EndIf
						EndIf
						If mp_I\ServerListPage > 0 Then
							If DrawButton(x+width-height-280*MenuScale,y, height, height, "<", 2, False, True, 0, MainMenuTab, 0)
								mp_I\ServerListPage = mp_I\ServerListPage-1
								ShouldDeleteGadgets = True
								mp_I\SelectedListServer = 0
							EndIf
						EndIf
					EndIf
				ElseIf mp_I\ServerMSG = SERVER_MSG_OFFLINE Then
					If DrawButton(x + width * 0.3 + 20 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale, GetLocalString("Menu", "cancel"), False, False, True) Then
						mp_I\ServerMSG = SERVER_MSG_NONE
						ShouldDeleteGadgets=True
					EndIf
					
					If DrawButton(x + width * 0.3 + 280 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale, GetLocalString("Menu", "retry"), False, False, True) Then
						mp_I\SelectedListServer = 0
						mp_I\HasRefreshed = False
						ShouldDeleteGadgets = True
						mp_I\ServerMSG = SERVER_MSG_NONE
					EndIf
				ElseIf mp_I\ServerMSG = SERVER_MSG_PASSWORD Then
					mp_I\ConnectPassword = InputBox(x + width * 0.3 + 230*MenuScale, y+190*MenuScale, 200*MenuScale, 30*MenuScale, mp_I\ConnectPassword, 1, 30, 2, MainMenuTab, 0, (Not mp_I\PasswordVisible))
					
					If DrawButton(x + width * 0.3 + 20 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale, GetLocalString("Menu", "cancel"), False, False, True) Then
						mp_I\ServerMSG = SERVER_MSG_NONE
						ShouldDeleteGadgets=True
						Steam_CloseConnection(Players[0]\SteamIDUpper, Players[0]\SteamIDLower)
						mp_I\PlayerCount = 0
						mp_I\ChatMSGID = 0
					EndIf
					
					If mp_I\ConnectPassword <> "" Then
						If DrawButton(x + width * 0.3 + 280 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale, GetLocalString("Menu", "continue"), False, False, True) Then
							ConnectViaPassword()
							mp_I\ServerMSG = SERVER_MSG_PWAIT
							ShouldDeleteGadgets = True
							mp_I\PasswordVisible = False
						EndIf
					Else
						ShouldDeleteGadgets = True
					EndIf
				ElseIf mp_I\ServerMSG = SERVER_MSG_PWAIT Then
					Local getconn = Steam_LoadPacket()
					While getconn
						ConnectFinal()
						If mp_I\PlayState = GAME_CLIENT Then
							mp_I\ServerMSG = SERVER_MSG_NONE
							SaveMPOptions()
							Return
						EndIf
						getconn = Steam_LoadPacket()
					Wend
				ElseIf mp_I\ServerMSG = SERVER_MSG_CONNECT Then
					getconn = Steam_LoadPacket()
					If getconn Then
						CreateHostPlayerAsClient(Steam_GetSenderIDUpper(), Steam_GetSenderIDLower())
						
						mp_I\ServerMSG = SERVER_MSG_NONE
						ConnectFinal()
						
						If m3d = Null And MainMenuOpen Then
							MainMenuTab = MenuTab_Serverlist
							Delete Each Menu3DInstance
							InitConsole(2)
							Load3DMenu()
						EndIf
						Steam_LeaveLobby()
						Steam_FlushLobbyID()
						Return
					Else
						mp_I\ConnectionTime = mp_I\ConnectionTime + fps\Factor[0]
						
						If mp_I\ConnectionTime > 70*10 Then
							ConnectWithNoPassword(Steam_GetSenderIDUpper(), Steam_GetSenderIDLower())
							mp_I\ConnectionTime = 0.0
							mp_I\ConnectionRetries = mp_I\ConnectionRetries + 1
						EndIf
						
						If DrawButton(x + width * 0.3 + 280 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale, GetLocalString("Menu", "cancel"), False, False, True) Lor mp_I\ConnectionRetries >= MAX_RETRIES Then
							DeleteMenuGadgets()
							Steam_LeaveLobby()
							Steam_FlushLobbyID()
							Steam_CloseConnection(Steam_GetSenderIDUpper(), Steam_GetSenderIDLower())
							If (Not MainMenuOpen) Then
								Delete Each Menu3DInstance
								MainMenuOpen = True
								InitConsole(2)
								Load3DMenu()
							EndIf
							mp_I\ServerMSG = SERVER_MSG_NONE
							If mp_I\ConnectionRetries >= MAX_RETRIES Then
								mp_I\ServerMSG = SERVER_MSG_RETRIES
							EndIf
							Return
						EndIf
					EndIf
				Else
					If DrawButton(x + width * 0.3 + 280 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale, GetLocalString("Menu", "close"), False, False, True) Then
						SaveMPOptions()
						mp_I\ServerMSG = SERVER_MSG_NONE
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				;[End Block]
			Case MenuTab_HostServer
				;[Block]
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				prevButton = co\CurrButton[MainMenuTab]
				prevButtonSub = co\CurrButtonSub[MainMenuTab]
				
				If co\CurrButton[MainMenuTab]=1
					UpdateMenuControllerSelection(7,MainMenuTab,2,2)
				Else
					UpdateMenuControllerSelection(7,MainMenuTab,0,2)
				EndIf
				If prevButton > 1
					co\CurrButtonSub[MainMenuTab]=0
				EndIf
				If co\CurrButton[MainMenuTab]>1
					co\CurrButtonSub[MainMenuTab]=0
				EndIf
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 300 * MenuScale
				
				mp_O\ServerName = InputBox(x+215*MenuScale, y+15*MenuScale, 340*MenuScale, 30*MenuScale, mp_O\ServerName, 6, 32, 6, MainMenuTab)
				mp_O\ServerName = Replace(mp_O\ServerName,"|","")
				
				mp_O\Password = InputBox(x+300*MenuScale, y+55*MenuScale, 170*MenuScale, 30*MenuScale, mp_O\Password, 7, 16, 7, MainMenuTab, 0, (Not mp_I\PasswordVisible))
				
				If MouseHit1 Then
					If MouseOn(x + 470 * MenuScale, y + 55 * MenuScale, 30 * MenuScale, 30 * MenuScale) Then
						PlaySound_Strict ButtonSFX[2]
						mp_I\PasswordVisible = Not mp_I\PasswordVisible
					EndIf
				EndIf
				
				If DrawButton(x+300*MenuScale, y+95*MenuScale, 170*MenuScale, 30*MenuScale, mp_O\Gamemode\name, False) Then
					MainMenuTab = MenuTab_SelectMPGamemode
				EndIf
				
				If mp_O\Gamemode\ID = Gamemode_Waves Then
					If DrawButton(x + 470 * MenuScale, y + 95 * MenuScale, 30*MenuScale, 30*MenuScale, "...",False,False,True,3,MainMenuTab,1) Then
						MainMenuTab = MenuTab_MPGamemodeSettings
					EndIf
				EndIf
				
				If DrawButton(x+300*MenuScale, y+135*MenuScale, 170*MenuScale, 30*MenuScale, mp_O\MapInList\Name, False) Then
					MainMenuTab = MenuTab_SelectMPMap
				EndIf
				
				Local TempInputString$ = mp_O\MaxPlayers
				If mp_O\MaxPlayers = 0 Then
					TempInputString = ""
				EndIf
				mp_O\MaxPlayers = InputBox(x+335*MenuScale, y+175*MenuScale, 100*MenuScale, 30*MenuScale, TempInputString, 4, 2, MainMenuTab)
				If SelectedInputBox <> 4 Then
					If mp_O\MaxPlayers < 2 Then mp_O\MaxPlayers = 2
					If mp_O\MaxPlayers > mp_O\Gamemode\MaxPlayersAllowed Then mp_O\MaxPlayers = mp_O\Gamemode\MaxPlayersAllowed
				EndIf
				
				TempInputString$ = mp_O\TimeOut
				If mp_O\TimeOut = 0 Then
					TempInputString = ""
				EndIf
				mp_O\TimeOut = InputBox(x+335*MenuScale, y+215*MenuScale, 100*MenuScale, 30*MenuScale, TempInputString, 5, 5, MainMenuTab)
				If SelectedInputBox <> 5 Then
					If mp_O\TimeOut < 1 Then mp_O\TimeOut = 1
					If mp_O\TimeOut > 30 Then mp_O\TimeOut = 30
				EndIf
				
				mp_O\LocalServer = DrawTick(x + 375 * MenuScale, y + 260 * MenuScale, mp_O\LocalServer, False, 2, MainMenuTab, 1)
				
				If DrawButton(x + 420 * MenuScale, y + height + 20 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Serverlist","create_server")),False,False,True,6,MainMenuTab,1) Then
					If mp_O\MaxPlayers < 2 Then mp_O\MaxPlayers = 2
					If mp_O\MaxPlayers > mp_O\Gamemode\MaxPlayersAllowed Then mp_O\MaxPlayers = mp_O\Gamemode\MaxPlayersAllowed
					If mp_O\TimeOut < 1 Then mp_O\TimeOut = 1
					If mp_O\TimeOut > 30 Then mp_O\TimeOut = 30
					CreateServer()
					gc\CurrGamemode = 2
					SaveMPOptions()
					
					mp_I\IsReady = False
					mp_I\PasswordVisible = False
					ResetControllerSelections()
					Null3DMenu()
					MainMenuOpen = False
					DebugLog "Starting Multiplayer Match!"
					LoadingServer()
					Return
				EndIf
				;[End block]
			Case MenuTab_SelectMPMap
				;[Block]
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 350 * MenuScale
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				
				x = x + 10 * MenuScale
				y = y + 10 * MenuScale
				
				i = 0
				Local mgmAmount = 0
				For mgm = Each MultiplayerGameMode
					mgmAmount = mgmAmount + 1
				Next
				
				For mfl = Each MapForList
					Local mapHasGM = False
					For j = 1 To mgmAmount
						If mp_O\Gamemode\name = Piece(mfl\Gamemodes,j,"|") Then
							mapHasGM = True
							Exit
						EndIf
					Next
					
					If mapHasGM Then
						If DrawButton(x, y, 170*MenuScale, 25*MenuScale, mfl\Name, False, False, True, i+1, MainMenuTab) Then
							mp_O\MapInList = mfl
						EndIf
						y=y+30*MenuScale
						i=i+1
					EndIf
				Next
				;[End Block]
			Case MenuTab_SelectMPGamemode
				;[Block]
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 350 * MenuScale
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				
				x = x + 10 * MenuScale
				y = y + 10 * MenuScale
				
				mgmAmount = 0
				For mgm = Each MultiplayerGameMode
					mgmAmount = mgmAmount + 1
				Next
				
				i = 0
				For mgm = Each MultiplayerGameMode
					mapHasGM = False
					For mfl = Each MapForList
						For j = 1 To mgmAmount
							If mgm\name = Piece(mfl\Gamemodes,j,"|") Then
								mapHasGM = True
								Exit
							EndIf
						Next
						If mapHasGM Then
							Exit
						EndIf
					Next
					If mapHasGM Then
						If DrawButton(x, y, 170*MenuScale, 25*MenuScale, mgm\name, False, False, True, i+1, MainMenuTab) Then
							mp_O\Gamemode = mgm
							mapHasGM = False
							For j = 1 To mgmAmount
								If mgm\name = Piece(mp_O\MapInList\Gamemodes,j,"|") Then
									mapHasGM = True
									Exit
								EndIf
							Next
							If (Not mapHasGM) Then
								For mfl = Each MapForList
									For j = 1 To mgmAmount
										If mgm\name = Piece(mfl\Gamemodes,j,"|") Then
											mp_O\MapInList = mfl
											mapHasGM = True
											Exit
										EndIf
									Next
									If mapHasGM Then
										Exit
									EndIf
								Next
							EndIf
						EndIf
						y=y+30*MenuScale
						i=i+1
					EndIf
				Next
				;[End Block]
			Case MenuTab_MPGamemodeSettings
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				
				Select mp_O\Gamemode\ID
					Case Gamemode_Waves
						;[Block]
						y=y+25*MenuScale
						
						mp_O\Gamemode\Difficulty = Slider3(x+310*MenuScale,y+6*MenuScale,150*MenuScale,mp_O\Gamemode\Difficulty,1,GetLocalString("Multiplayer","safe"),GetLocalString("Multiplayer","euclid"),GetLocalString("Multiplayer","keter"),1,MainMenuTab,0)
						
						y=y+50*MenuScale
						
						mp_O\Gamemode\MaxPhase = Waves_Short+(3*Slider3(x+310*MenuScale,y+6*MenuScale,150*MenuScale,Int(mp_O\Gamemode\MaxPhase/4-1),2,Waves_Short+" (short)",Waves_Medium+" (medium)",Waves_Long+" (long)",2,MainMenuTab,0))
						;[End Block]
				End Select
				;[End Block]
		End Select
	EndIf
	
	Select MainMenuTab
		Case MenuTab_Default, MenuTab_Options_Graphics
			m_I\SpriteAlpha = Max(0.0,m_I\SpriteAlpha-0.01*fps\Factor[1])
		Case MenuTab_NewGame,MenuTab_LoadGame,MenuTab_Options_Controller,MenuTab_HostServer,MenuTab_GameModes,MenuTab_Achievements,MenuTab_Credits,MenuTab_Websites,MenuTab_Chapters
			If m_I\SpriteAlpha > 0.7
				m_I\SpriteAlpha = Max(0.7,m_I\SpriteAlpha-0.01*fps\Factor[1])
			Else
				m_I\SpriteAlpha = Min(0.7,m_I\SpriteAlpha+0.01*fps\Factor[1])
			EndIf
		Default
			If m_I\SpriteAlpha > 0.5
				m_I\SpriteAlpha = Max(0.5,m_I\SpriteAlpha-0.01*fps\Factor[1])
			Else
				m_I\SpriteAlpha = Min(0.5,m_I\SpriteAlpha+0.01*fps\Factor[1])
			EndIf
	End Select
	
	EntityAlpha m_I\Sprite,m_I\SpriteAlpha
	
	If gc\CurrGamemode = -1 Then
		If Steam_SeekLobbyUpper() <> 0 Then
			If mp_O\PlayerName = "" Then mp_O\PlayerName = "Player"
			Delete Each MenuButton
			Connect(Steam_SeekLobbyUpper(),Steam_SeekLobbyLower())
			Steam_FlushLobbyID()
			SaveMPOptions()
			MainMenuTab = MenuTab_Serverlist
		EndIf
	EndIf
	
End Function

Function RenderMainMenu()
	Local x%, y%, width%, height%, temp%, i%, x2%
	Local se.Server
	Local mgm.MultiplayerGameMode, mfl.MapForList, sa.Save
	
	ShowPointer()
			
	Color 0,0,0
	
	SetFont fo\Font[Font_Default]
	
	If (Not opt\Menu3DEnabled) Then
		If MainMenuOpen Then
			DrawBlock(SCPText, Viewport_Center_X - ImageWidth(SCPText) / 2, opt\GraphicHeight - (20 * MenuScale) - ImageHeight(SCPText))
			DrawBlock(RyanIMG, opt\GraphicWidth - ImageWidth(RyanIMG), opt\GraphicHeight - ImageHeight(RyanIMG))
		EndIf
	EndIf
	
	If MenuBlinkTimer[1] < MenuBlinkDuration[1] Then
		Color(50, 50, 50)
		Text(MenuStrX + Rand(-5, 5), MenuStrY + Rand(-5, 5), MenuStr, True)
		If MenuBlinkTimer[1] < 0
			MenuStrX = Rand(700, 1000) * MenuScale
			MenuStrY = Rand(100, 600) * MenuScale
			
			Select Rand(0, 23)
				Case 0, 2, 3
					MenuStr = "DON'T BLINK"
				Case 4, 5
					MenuStr = "Secure. Contain. Protect."
				Case 6, 7, 8
					MenuStr = "You want happy endings? Fuck you."
				Case 9, 10, 11
					MenuStr = "Sometimes we would have had time to scream."
				Case 12, 19
					MenuStr = "NIL"
				Case 13
					MenuStr = "NO"
				Case 14
					MenuStr = "black white black white black white gray"
				Case 15
					MenuStr = "Stone does not care"
				Case 16
					MenuStr = "9341"
				Case 17
					MenuStr = "It controls the doors"
				Case 18
					MenuStr = "e8m106]af173o+079m895w914"
				Case 20
					MenuStr = "It has taken over everything"
				Case 21
					MenuStr = "The spiral is growing"
				Case 22
					MenuStr = Chr(34)+"Some kind of gestalt effect due to massive reality damage."+Chr(34)
				Case 23
					MenuStr = "Nein!"
			End Select
		EndIf
	EndIf
	
	SetFont fo\Font[Font_Menu]
	
	If MainMenuTab<>MenuTab_Default Then
		x = 59 * MenuScale
		y = 286 * MenuScale
		
		width = 400 * MenuScale
		height = 70 * MenuScale
		
		DrawFrame(x, y, width, height)
		
		If MainMenuTab = MenuTab_Serverlist And mp_I\ServerMSG <> SERVER_MSG_NONE And mp_I\ServerMSG <> SERVER_MSG_OFFLINE Then
			DrawFrame(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height)
			Color(100, 100, 100)
			SetFont fo\Font[Font_Default]
			Text((x + width + 20 * MenuScale) + (580 * MenuScale - width - 20 * MenuScale) / 2, y + height / 2, Upper(GetLocalString("Menu", "back")), True, True)
		EndIf
		
		Select MainMenuTab
			Case MenuTab_NewGame
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				
				SetFont fo\Font[Font_Menu]
					
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Menu", "newgame")), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 370 * MenuScale
				
				DrawFrame(x, y, width, height)				
				
				SetFont fo\Font[Font_Default]
				
				Text (x + 20 * MenuScale, y + 20 * MenuScale, GetLocalString("Menu","name")+":")
				
				Color 255,255,255
				
				Text (x + 20 * MenuScale, y + 60 * MenuScale, GetLocalString("Menu","map_seed")+":")
					
				Color 255,255,255
				
				Text(x + 20 * MenuScale, y + 125 * MenuScale, GetLocalString("Menu","enable_intro")+":")
				Text(x + 20 * MenuScale, y + 100 * MenuScale, GetLocalString("Menu","classic_mode")+":")
				If MouseOn(x + 280 * MenuScale, y + 100 * MenuScale, 120*MenuScale, 30*MenuScale)
					DrawOptionsTooltip("classic_mode_txt")
				EndIf
				
				Text (x + 20 * MenuScale, y + 150 * MenuScale, GetLocalString("Menu","difficulty")+":")
				For i = SAFE To ESOTERIC
					Color(difficulties[i]\r,difficulties[i]\g,difficulties[i]\b)
					Text(x + 60 * MenuScale, y + (180+30*i) * MenuScale, difficulties[i]\Name)
				Next
				
				Color(255, 255, 255)
				DrawFrame(x + 150 * MenuScale,y + 155 * MenuScale, 410*MenuScale, 150*MenuScale)
				
				If SelectedDifficulty\Customizable
					SelectedDifficulty\PermaDeath =  DrawTick(x + 160 * MenuScale, y + 165 * MenuScale, (SelectedDifficulty\PermaDeath), False, 0, MainMenuTab, 1)
					Text(x + 200 * MenuScale, y + 165 * MenuScale, GetLocalString("Menu","permadeath"))
					
					Text(x + 200 * MenuScale, y + 195 * MenuScale, GetLocalString("Menu","save_anywhere"))
					
					Text(x + 200 * MenuScale, y + 225 * MenuScale, GetLocalString("Menu","aggressive_npcs"))
					
				;Other factor's difficulty
					Color 255,255,255
					If co\Enabled
						If co\CurrButton[MainMenuTab]=3 And co\CurrButtonSub[MainMenuTab]=1
							Color 255,255,255
							Rect x+160*MenuScale,y+251*MenuScale,ImageWidth(I_MIG\ArrowIMG[1])-5*MenuScale,ImageHeight(I_MIG\ArrowIMG[1])
						EndIf
					EndIf
					DrawImage I_MIG\ArrowIMG[1],x + 155 * MenuScale, y+251*MenuScale
					Color 255,255,255
					Select SelectedDifficulty\OtherFactors
						Case EASY
							Text(x + 200 * MenuScale, y + 255 * MenuScale, GetLocalString("Menu","odf")+": "+GetLocalString("Menu","easy"))
						Case NORMAL
							Text(x + 200 * MenuScale, y + 255 * MenuScale, GetLocalString("Menu","odf")+": "+GetLocalString("Menu","normal"))
						Case HARD
							Text(x + 200 * MenuScale, y + 255 * MenuScale, GetLocalString("Menu","odf")+": "+GetLocalString("Menu","hard"))
						Case HARDER
							Text(x + 200 * MenuScale, y + 255 * MenuScale, GetLocalString("Menu","odf")+": "+GetLocalString("Menu","harder"))
						Case IMPOSSIBLE
							Text(x + 200 * MenuScale, y + 255 * MenuScale, GetLocalString("Menu","odf")+": "+GetLocalString("Menu","impossible"))
					End Select
				Else
					RowText(SelectedDifficulty\Description, x+160*MenuScale, y+160*MenuScale, (410-20)*MenuScale, 200)					
				EndIf
				
				SetFont fo\Font[Font_Menu]
					
				;[End Block]
			Case MenuTab_LoadGame
				;[Block]
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 510 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				
				SetFont fo\Font[Font_Menu]
					
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Menu","loadgame")), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale	
				
				DrawFrame(x+50*MenuScale,y+510*MenuScale,width-100*MenuScale,55*MenuScale)
				
				SetFont fo\Font[Font_Default_Large]
				If CurrLoadGamePage < Ceil(Float(SaveGameAmount)/6.0)-1 And DelSave = Null Then 
					
				Else
					DrawFrame(x+530*MenuScale, y + 510*MenuScale, 51*MenuScale, 55*MenuScale)
					Color(100, 100, 100)
					Text(x+555*MenuScale, y + 537*MenuScale, ">", True, True)
				EndIf
				If CurrLoadGamePage > 0 And DelSave = Null Then
					
				Else
					DrawFrame(x, y + 510*MenuScale, 51*MenuScale, 55*MenuScale)
					Color(100, 100, 100)
					Text(x+25*MenuScale, y + 537*MenuScale, "<", True, True)
				EndIf
				
				SetFont fo\Font[Font_Menu]
				
				Color 255,255,255
				Text(x+(width/2.0),y+536*MenuScale,GetLocalString("Menu","page")+" "+Int(Max((CurrLoadGamePage+1),1))+"/"+Int(Max((Int(Ceil(Float(SaveGameAmount)/6.0))),1)),True,True)
				
				SetFont fo\Font[Font_Default]
				
				If SaveGameAmount = 0 Then
					Text (x + 20 * MenuScale, y + 20 * MenuScale, GetLocalString("Menu","no_saved"))
				Else
					x = x + 20 * MenuScale
					y = y + 20 * MenuScale
					
					CurrSave = First Save
					
					For i% = 0 To 5+(6*CurrLoadGamePage)
						If i > 0 Then CurrSave = After CurrSave
						If CurrSave = Null Then Exit
						If i >= (0+(6*CurrLoadGamePage))
							DrawFrame(x,y,540* MenuScale, 70* MenuScale)
							
							If CurrSave\Version <> CompatibleNumber Then
								Color 255,0,0
							Else
								Color 255,255,255
							EndIf
							
							Text(x + 20 * MenuScale, y + 10 * MenuScale, CurrSave\Name)
							Text(x + 20 * MenuScale, y + (10+18) * MenuScale, CurrSave\Time)
							Text(x + 120 * MenuScale, y + (10+18) * MenuScale, CurrSave\Date)
							Text(x + 20 * MenuScale, y + (10+36) * MenuScale, CurrSave\Version)
							
							If DelSave = Null Then
								If CurrSave\Version <> CompatibleNumber Then
									DrawFrame(x + 280 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale)
									Color(255, 0, 0)
									Text(x + 330 * MenuScale, y + 34 * MenuScale, GetLocalString("Menu","load"), True, True)
								EndIf
							Else
								DrawFrame(x + 280 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale)
								If CurrSave\Version <> CompatibleNumber Then
									Color(255, 0, 0)
								Else
									Color(100, 100, 100)
								EndIf
								Text(x + 330 * MenuScale, y + 34 * MenuScale, GetLocalString("Menu","load"), True, True)
								
								DrawFrame(x + 400 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale)
								Color(100, 100, 100)
								Text(x + 450 * MenuScale, y + 34 * MenuScale, GetLocalString("Menu","delete"), True, True)
							EndIf
							
							If CurrSave = Last Save Then
								Exit
							EndIf
							
							y = y + 80 * MenuScale
						EndIf
					Next
					
					If DelSave <> Null
						x = 640 * MenuScale
						y = 376 * MenuScale
						DrawFrame(x, y, 420 * MenuScale, 200 * MenuScale)
						RowText(GetLocalString("Menu","ask_save_delete"), x + 20 * MenuScale, y + 15 * MenuScale, 400 * MenuScale, 200 * MenuScale)
					EndIf
				EndIf
				;[End Block]	
			Case MenuTab_Options_Graphics,MenuTab_Options_Audio,MenuTab_Options_Controls,MenuTab_Options_Advanced,MenuTab_Options_Controller,MenuTab_Options_ControlsBinding
				;[Block]
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				
				SetFont fo\Font[Font_Menu]
					
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Menu", "options")), True, True)
				
				If MainMenuTab<>MenuTab_Options_Controller And MainMenuTab<>MenuTab_Options_ControlsBinding Then
					x = 60 * MenuScale
					y = y + height + 20 * MenuScale
					width = 580 * MenuScale
					height = 60 * MenuScale
					DrawFrame(x, y, width, height)
					Local prevButton = co\CurrButton[MainMenuTab]
					Local prevButtonSub = co\CurrButtonSub[MainMenuTab]
					
					Color 255,255,255
					If MainMenuTab = MenuTab_Options_Graphics Then
						Rect(x+18*MenuScale,y+13*MenuScale,(width/5.3)+10*MenuScale,(height/2.6)+10*MenuScale,False)
					ElseIf MainMenuTab = MenuTab_Options_Audio Then
						Rect(x+158*MenuScale,y+13*MenuScale,(width/5.3)+10*MenuScale,(height/2.6)+10*MenuScale,False)
					ElseIf MainMenuTab = MenuTab_Options_Controls Then
						Rect(x+298*MenuScale,y+13*MenuScale,(width/5.3)+10*MenuScale,(height/2.6)+10*MenuScale,False)
					ElseIf MainMenuTab = MenuTab_Options_Advanced Then
						Rect(x+438*MenuScale,y+13*MenuScale,(width/5.3)+10*MenuScale,(height/2.6)+10*MenuScale,False)
					EndIf
					
					Color 255,255,255
					
					SetFont fo\Font[Font_Default]
					y = y + 70 * MenuScale
				Else
					x = 60 * MenuScale
					y = y + height + 20 * MenuScale
					width = 580 * MenuScale
					If MainMenuTab<>MenuTab_Options_ControlsBinding Then
						height = 480 * MenuScale
					Else
						height = 550 * MenuScale
					EndIf
					DrawFrame(x, y, width, height)
					SetFont fo\Font[Font_Default]
					y = y + 30 * MenuScale
				EndIf
				
				If MainMenuTab = MenuTab_Options_Graphics Then
					;[Block]
					height = 420 * MenuScale
					DrawFrame(x, y, width, height)
					
					y=y+30*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","gamma")+":")
					If MouseAndControllerSelectBox(x+300*MenuScale,y-6*MenuScale,170*MenuScale+14,20,6,MainMenuTab) And OnSliderID=0
						DrawOptionsTooltip("gamma",ScreenGamma)
					EndIf
					
					y=y+35*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","fov")+":")
					If MouseOn(x+300*MenuScale,y-6*MenuScale,170*MenuScale+14,20)
						DrawOptionsTooltip("fov",FOV)
					EndIf
					
					y=y+35*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","framelimit")+":")
					If CurrFrameLimit>0.0
						If MouseAndControllerSelectBox(x+300*MenuScale,y-6*MenuScale,170*MenuScale+14,20*MenuScale,7,MainMenuTab)
							DrawOptionsTooltip("framelimit",Framelimit)
						EndIf
						If MouseAndControllerSelectBox(x+215*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,8,MainMenuTab)
							DrawOptionsTooltip("framelimit",Framelimit)
						EndIf
					EndIf
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,8,MainMenuTab)
						DrawOptionsTooltip("framelimit",Framelimit)
					EndIf
					
					y=y+35*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","vsync")+":")
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,3,MainMenuTab) And OnSliderID=0
						DrawOptionsTooltip("vsync")
					EndIf
					
					y=y+37*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","texquality")+":")
					If (MouseAndControllerSelectBox(x + 315 * MenuScale, y-6*MenuScale, 150*MenuScale+14, 20, 8, MainMenuTab) And OnSliderID=0) Lor OnSliderID=3
						DrawOptionsTooltip("texquality")
					EndIf
					
					y=y+37*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","texfiltering")+":")
					If (MouseAndControllerSelectBox(x + 315 * MenuScale, y-6*MenuScale, 150*MenuScale+14, 20, 8, MainMenuTab) And OnSliderID=0) Lor OnSliderID=3
						DrawOptionsTooltip("texfiltering")
					EndIf
					
					y=y+37*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","particleamount")+":")
					If (MouseAndControllerSelectBox(x + 315 * MenuScale, y-6*MenuScale, 150*MenuScale+14, 20, 7, MainMenuTab) And OnSliderID=0) Lor OnSliderID=2
						DrawOptionsTooltip("particleamount",ParticleAmount)
					EndIf
					
					y=y+37*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","cubemap")+":")
					If (MouseAndControllerSelectBox(x + 315 * MenuScale, y-6*MenuScale, 150*MenuScale+14, 20, 10, MainMenuTab) And OnSliderID=0) Lor OnSliderID=4
						DrawOptionsTooltip("cubemap",opt\RenderCubeMapMode)
					EndIf
					
					y=y+35*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","renderscope")+":")
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,5,MainMenuTab) And OnSliderID=0
						DrawOptionsTooltip("renderscope")
					EndIf
					
					y=y+35*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","roomlights")+":")
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,5,MainMenuTab) And OnSliderID=0
						DrawOptionsTooltip("roomlights")
					EndIf
					
					y = y + 35*MenuScale
					
					Text x + 20 * MenuScale, y, GetLocalString("Options","startupvideos")+":"
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,4,MainMenuTab)
						DrawOptionsTooltip("startupvideos")
					EndIf
					
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_Audio Then
					;[Block]
					height = 320 * MenuScale
					DrawFrame(x, y, width, height)	
					
					y = y + 30*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","masterv")+":")
					If MouseAndControllerSelectBox(x+310*MenuScale,y-6*MenuScale,150*MenuScale+14,20,2,MainMenuTab)
						DrawOptionsTooltip("mastervol",aud\MasterVol)
					EndIf
					
					y = y + 35*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","musicv")+":")
					If MouseAndControllerSelectBox(x+310*MenuScale,y-6*MenuScale,150*MenuScale+14,20,3,MainMenuTab)
						DrawOptionsTooltip("musicvol",aud\MusicVol)
					EndIf
					
					y = y + 35*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","soundv")+":")
					If MouseAndControllerSelectBox(x+310*MenuScale,y-6*MenuScale,150*MenuScale+14,20,3,MainMenuTab)
						DrawOptionsTooltip("soundvol",aud\EnviromentVol)
					EndIf
					
					y = y + 35*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","voicev")+":")
					If MouseAndControllerSelectBox(x+310*MenuScale,y-6*MenuScale,150*MenuScale+14,20,3,MainMenuTab)
						DrawOptionsTooltip("voicevol",aud\VoiceVol)
					EndIf
					
					y = y + 35*MenuScale
					
					Color 255,255,255
					Text x + 20 * MenuScale, y, GetLocalString("Options","sfxautorelease")+":"
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,4,MainMenuTab)
						DrawOptionsTooltip("sfxautorelease")
					EndIf
					
					y = y + 35*MenuScale
					
					Text x + 20 * MenuScale, y, GetLocalString("Options","bmsbossmus")+":"
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,4,MainMenuTab)
						DrawOptionsTooltip("bmsbossmus")
					EndIf
					
					y = y + 35*MenuScale
					
					Text x + 20 * MenuScale, y, GetLocalString("Options","elevmus")+":"
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,4,MainMenuTab)
						DrawOptionsTooltip("elevmus")
					EndIf
					
					y = y + 35*MenuScale
					
					Text x + 20 * MenuScale, y, GetLocalString("Options","mainmenumus")+":"
					If (MouseAndControllerSelectBox(x + 315 * MenuScale, y-6*MenuScale, 150*MenuScale+14, 20, 7, MainMenuTab) And OnSliderID=0) Lor OnSliderID=2
						DrawOptionsTooltip("mainmenumus",opt\MainMenuMusic)
					EndIf
					
					y = y + 30*MenuScale
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_Controls Then
					;[Block]
					
					height = 270 * MenuScale
					DrawFrame(x, y, width, height)	
					
					y = y + 30*MenuScale
					
					Color(255, 255, 255)
					Text(x + 20 * MenuScale, y, GetLocalString("Options","sensitivity")+":")
					If MouseOn(x+310*MenuScale,y-6*MenuScale,150*MenuScale+14,20)
						DrawOptionsTooltip("mousesensitivity",MouseSens)
					EndIf
					
					y = y + 35*MenuScale
					
					Color(255, 255, 255)
					Text(x + 20 * MenuScale, y, GetLocalString("Options","smoothing")+":")
					If MouseOn(x+310*MenuScale,y-6*MenuScale,150*MenuScale+14,20)
						DrawOptionsTooltip("mousesmoothing",opt\MouseSmooth)
					EndIf
					
					y = y + 35*MenuScale
					
					Color(255, 255, 255)
					Text(x + 20 * MenuScale, y, GetLocalString("Options","invert")+":")
					If MouseOn(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale)
						DrawOptionsTooltip("mouseinvert")
					EndIf
					
					y = y + 35*MenuScale
					
					Color(255, 255, 255)
					Text(x + 20 * MenuScale, y, GetLocalString("Options","holdtoaim")+":")
					If MouseOn(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale)
						DrawOptionsTooltip("holdtoaim")
					EndIf
					
					y = y + 35*MenuScale
					
					Color(255, 255, 255)
					Text(x + 20 * MenuScale, y, GetLocalString("Options","holdtocrouch")+":")
					If MouseOn(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale)
						DrawOptionsTooltip("holdtocrouch")
					EndIf
					
					y = y + 35*MenuScale
					
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_ControlsBinding Then
					;[Block]
					Text(x + 20 * MenuScale, y - 10 * MenuScale, GetLocalString("Menu","control_cfg"))
					y = y + 10 * MenuScale
					
					Text(x + 20 * MenuScale, y + 15 * MenuScale, GetLocalString("Options","cont_forward"))
					Text(x + 20 * MenuScale, y + 35 * MenuScale, GetLocalString("Options","cont_back"))
					Text(x + 20 * MenuScale, y + 55 * MenuScale, GetLocalString("Options","cont_left"))
					Text(x + 20 * MenuScale, y + 75 * MenuScale, GetLocalString("Options","cont_right"))
					Text(x + 20 * MenuScale, y + 95 * MenuScale, GetLocalString("Options","cont_crouch"))
					Text(x + 20 * MenuScale, y + 115 * MenuScale, GetLocalString("Options","cont_sprint"))
					
					Text(x + 20 * MenuScale, y + 155 * MenuScale, GetLocalString("Options","cont_holster"))
					Text(x + 20 * MenuScale, y + 175 * MenuScale, GetLocalString("Options","cont_reload"))
					Text(x + 20 * MenuScale, y + 195 * MenuScale, GetLocalString("Options","cont_firemode"))
					Text(x + 20 * MenuScale, y + 215 * MenuScale, GetLocalString("Options","cont_attachment"))
					Text(x + 20 * MenuScale, y + 235 * MenuScale, GetLocalString("Options","cont_scramble"))
					
					Text(x + 20 * MenuScale, y + 275 * MenuScale, GetLocalString("Options","cont_blink"))
					Text(x + 20 * MenuScale, y + 295 * MenuScale, GetLocalString("Options","cont_inventory"))
					Text(x + 20 * MenuScale, y + 315 * MenuScale, GetLocalString("Options","cont_interact"))
					
					Text(x + 20 * MenuScale, y + 355 * MenuScale, GetLocalString("Options","cont_chat"))
					Text(x + 20 * MenuScale, y + 375 * MenuScale, GetLocalString("Options","cont_commandwheel"))
					Text(x + 20 * MenuScale, y + 395 * MenuScale, GetLocalString("Options","cont_socialwheel"))
					
					Text(x + 20 * MenuScale, y + 435 * MenuScale, GetLocalString("Options","cont_console"))
					Text(x + 20 * MenuScale, y + 455 * MenuScale, GetLocalString("Options","cont_save"))
					Text(x + 20 * MenuScale, y + 475 * MenuScale, GetLocalString("Options","cont_load"))
					
					If MouseOn(x+20*MenuScale,y,width-40*MenuScale,420*MenuScale)
						DrawOptionsTooltip("controls")
					EndIf
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_Advanced Then
					;[Block]
					height = 180 * MenuScale
					DrawFrame(x, y, width, height)	
					
					y = y + 30*MenuScale
					
					Color 255,255,255				
					Text(x + 20 * MenuScale, y, GetLocalString("Options","hud")+":")
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,2,MainMenuTab)
						DrawOptionsTooltip("hud")
					EndIf
					
					y = y + 35*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","showfps")+":")
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,6,MainMenuTab)
						DrawOptionsTooltip("showfps")
					EndIf
					
					y = y + 35*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","consoleenable")+":")
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,3,MainMenuTab)
						DrawOptionsTooltip("consoleenable")
					EndIf
					
					y = y + 35*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","playermodel")+":")
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,3,MainMenuTab)
						DrawOptionsTooltip("playermodel")
					EndIf
				EndIf
				;[End Block]
			Case MenuTab_PointsShop
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				CreatePointsShop()
				
				DrawFrame(x, y, width, height)
				
				Color(255, 255, 255)
				
				SetFont fo\Font[Font_Menu]
					
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Menu", "points_shop")), True, True)
				;[End Block]
			Case MenuTab_Chapters
				;[Block]
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 470 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, GetLocalString("Menu","chapter")+" "+Int(CurrLoadGamePage), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale	
				
				SetFont fo\Font[Font_Menu]
				
				DrawFrame(x+50*MenuScale,y+490*MenuScale,width-100*MenuScale,55*MenuScale)
				
				If CurrLoadGamePage < 9 Then 
					
				Else
					DrawFrame(x+530*MenuScale, y + 490*MenuScale, 51*MenuScale, 55*MenuScale)
					Color(100, 100, 100)
					Text(x+555*MenuScale, y + 517*MenuScale, ">", True, True)
				EndIf
				If CurrLoadGamePage > 0 Then
					
				Else
					DrawFrame(x, y + 490*MenuScale, 51*MenuScale, 55*MenuScale)
					Color(100, 100, 100)
					Text(x+25*MenuScale, y + 517*MenuScale, "<", True, True)
				EndIf
				
				Color 255,255,255
				Text(x+(width/2.0),y+516*MenuScale, GetLocalString("Menu","page")+" "+Int(CurrLoadGamePage+1)+"/"+10,True,True)
				
				SetFont fo\Font[Font_Default]
				
				x = x + 20 * MenuScale
				y = y + 20 * MenuScale
				
				SetFont fo\Font[Font_Default]
				
				Select CurrLoadGamePage
					Case 0
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						DrawImage(I_MIG\ChapterIMG[0], x , y)
					Case 1
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						DrawImage(I_MIG\ChapterIMG[1], x , y)
					Case 2
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						DrawImage(I_MIG\ChapterIMG[2], x , y)
					Case 3
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						DrawImage(I_MIG\ChapterIMG[3], x , y)
					Case 4
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						DrawImage(I_MIG\ChapterIMG[4], x , y)
					Case 5
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						DrawImage(I_MIG\ChapterIMG[5], x , y)
					Case 6
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						DrawImage(I_MIG\ChapterIMG[6], x , y)
					Case 7
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						DrawImage(I_MIG\ChapterIMG[7], x , y)
					Case 8
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						DrawImage(I_MIG\ChapterIMG[8], x , y)
					Case 9
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						DrawImage(I_MIG\ChapterIMG[9], x , y)
				End Select
				;[End Block]	
			Case MenuTab_Extras
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, GetLocalString("Menu","extra"), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 190 * MenuScale
				
				DrawFrame(x, y, width, height)
				;[End Block]	
			Case MenuTab_Achievements
				;[Block]
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 470 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, GetLocalString("Menu","achievements"), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale	
				
				SetFont fo\Font[Font_Menu]
				
				DrawFrame(x+50*MenuScale,y+470*MenuScale,width-100*MenuScale,55*MenuScale)
				
				If CurrLoadGamePage < Ceil(Float(MAXACHIEVEMENTS)/4.0)-1 Then 
					
				Else
					DrawFrame(x+530*MenuScale, y + 470*MenuScale, 51*MenuScale, 55*MenuScale)
					Color(100, 100, 100)
					Text(x+555*MenuScale, y + 497*MenuScale, ">", True, True)
				EndIf
				If CurrLoadGamePage > 0 Then
					
				Else
					DrawFrame(x, y + 470*MenuScale, 51*MenuScale, 55*MenuScale)
					Color(100, 100, 100)
					Text(x+25*MenuScale, y + 497*MenuScale, "<", True, True)
				EndIf
				
				Color 255,255,255
				Text(x+(width/2.0),y+496*MenuScale, GetLocalString("Menu","page")+Int(Max((CurrLoadGamePage+1),1))+"/"+Int(Max((Int(Ceil(Float(MAXACHIEVEMENTS)/4.0))),1)),True,True)
				
				SetFont fo\Font[Font_Default]
				
				x = x + 20 * MenuScale
				y = y + 20 * MenuScale
				
				SetFont fo\Font[Font_Default]
				For i% = (1+(4*CurrLoadGamePage)) To 4+(4*CurrLoadGamePage)
					If i <= MAXACHIEVEMENTS And i>0 Then
						DrawFrame(x,y,540 * MenuScale, 100 * MenuScale)
						
						Color 0,0,0
						Rect x + 3 * MenuScale, y + 3 * MenuScale, ImageWidth(achv\AchvIMG[i - 1]),ImageHeight(achv\AchvIMG[i - 1]) + 7 * MenuScale,True
						
						If achv\Achievement[i - 1] = False Then
							DrawImage achv\AchvLocked,x + 3 * MenuScale, y + 7.5 * MenuScale
							Color 255,0,0
							Text(x + 100 * MenuScale, y + 10 * MenuScale,  GetLocalString("Menu","locked"))
						Else
							DrawImage achv\AchvIMG[i - 1],x + 3 * MenuScale, y + 7.5 * MenuScale
							Color 255,255,255
							Text(x + 100 * MenuScale, y + 10 * MenuScale, achv\AchievementStrings[i - 1])
							Text(x + 100 * MenuScale, y + (10+22) * MenuScale, achv\AchievementDescs[i - 1])
						EndIf
						
						y = y + 110 * MenuScale
					Else
						Exit
					EndIf
				Next
				;[End Block]	
			Case MenuTab_Websites
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2,  GetLocalString("Menu","websites"), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 465 * MenuScale
				
				DrawFrame(x, y, width, height)
				;[End Block]
			Case MenuTab_Singleplayer
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Menu", "singleplayer")), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				temp = 0
				For sa = Each Save
					If sa\Name = m_I\CurrentSave Then
						If sa\Version = CompatibleNumber Then
							temp = 1
						EndIf
						Exit
					EndIf
				Next
				height = 200 + (80 * temp) * MenuScale
				
				DrawFrame(x, y, width, height)
				;[End Block]
			Case MenuTab_GameModes
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Menu", "classic_mode")), True, True)
				;[End Block]
			Case MenuTab_Lobby
				;[Block]
				
				;[End Block]	
			Case MenuTab_Serverlist
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Serverlist", "join_server")), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				height = 560 * MenuScale
				width = 1130 * MenuScale
				
				x2 = x
				
				DrawFrame(x, y, width, height)
				
				SetFont fo\Font[Font_Default_Medium]
				If mp_I\ServerListAmount = 1 Then
					Text x + 20 * MenuScale, y + 50 * MenuScale, GetLocalString("Serverlist", "no_servers")
				EndIf
				
				;Lock icon for servers (when the server is set to private)
				width = 40 * MenuScale
				height = 40 * MenuScale
				DrawFrame(x, y, width, height)
				
				SetFont fo\Font[Font_Default]
				
				For i = 0 To (SERVER_LIST_SORT_CATEGORY_MAX-1)
					x = x + width
					Select i
						Case 0
							;Server name
							width = 500 * MenuScale
						Case 1, 2
							;Server gamemode
							;Server map
							width = 200 * MenuScale
						Case 3
							;Player amount
							width = 150 * MenuScale
					End Select
				Next
				
				;Server ping
				x = x + width
				width = 40 * MenuScale
				DrawFrame(x, y, width, height)
				
				y = y + height
				
				;Server list itself
				For i = (1+(16*mp_I\ServerListPage)) To 16+(16*mp_I\ServerListPage)
					If i <= mp_I\ServerListAmount
						For se = Each Server
							If se\ID = i Then
								x = x2
								width = 40 * MenuScale
								height = 30 * MenuScale
								DrawFrame(x+3*MenuScale, y, width-3*MenuScale, height, 0, 0, 1024, 1024, 1)
								Local isSelected% = (se\ID = mp_I\SelectedListServer)
								If (MouseOn(x2,y,1130*MenuScale,height) Lor isSelected) And mp_I\ServerMSG = SERVER_MSG_NONE Then
									Color 30+(30*isSelected),30+(30*isSelected),30+(30*isSelected)
									Rect x+4*MenuScale,y+1*MenuScale,width-6*MenuScale,height-2*MenuScale
								EndIf
								If se\password Then
									DrawImage mp_I\ServerIcon, x+4*MenuScale+(((width-6*MenuScale)/2)-(ImageWidth(mp_I\ServerIcon)/2)), y+2*MenuScale, se\password-1
								EndIf	
								x = x + width
								width = 500*MenuScale
								DrawFrame(x, y, width, height, 0, 0, 1024, 1024, 1)
								If (MouseOn(x2,y,1130*MenuScale,height) Lor isSelected) And mp_I\ServerMSG = SERVER_MSG_NONE Then
									Color 30+(30*isSelected),30+(30*isSelected),30+(30*isSelected)
									Rect x+1*MenuScale,y+1*MenuScale,width-2*MenuScale,height-2*MenuScale
								EndIf
								Color 255,255,255
								Text(x + width / 2, y + height / 2, "[" + se\region + "] " + se\name, True, True)
								x = x + width
								width = 200 * MenuScale
								DrawFrame(x, y, width, height, 0, 0, 1024, 1024, 1)
								If (MouseOn(x2,y,1130*MenuScale,height) Lor isSelected) And mp_I\ServerMSG = SERVER_MSG_NONE Then
									Color 30+(30*isSelected),30+(30*isSelected),30+(30*isSelected)
									Rect x+1*MenuScale,y+1*MenuScale,width-2*MenuScale,height-2*MenuScale
								EndIf
								Color 255,255,255
								Text(x + width / 2, y + height / 2,  se\gamemode, True, True)
								x = x + width
								width = 200 * MenuScale
								DrawFrame(x, y, width, height, 0, 0, 1024, 1024, 1)
								If (MouseOn(x2,y,1130*MenuScale,height) Lor isSelected) And mp_I\ServerMSG = SERVER_MSG_NONE Then
									Color 30+(30*isSelected),30+(30*isSelected),30+(30*isSelected)
									Rect x+1*MenuScale,y+1*MenuScale,width-2*MenuScale,height-2*MenuScale
								EndIf
								Color 255,255,255
								Text(x + width / 2, y + height / 2, se\map, True, True)
								x = x + width
								width = 150 * MenuScale
								DrawFrame(x, y, width, height, 0, 0, 1024, 1024, 1)
								If (MouseOn(x2,y,1130*MenuScale,height) Lor isSelected) And mp_I\ServerMSG = SERVER_MSG_NONE Then
									Color 30+(30*isSelected),30+(30*isSelected),30+(30*isSelected)
									Rect x+1*MenuScale,y+1*MenuScale,width-2*MenuScale,height-2*MenuScale
								EndIf
								Color 255,255,255
								Text(x + width / 2, y + height / 2, se\pl_on+"/"+se\pl_max, True, True)
								x = x + width
								width = 40 * MenuScale
								DrawFrame(x, y, width-3*MenuScale, height, 0, 0, 1024, 1024, 1)
								If (MouseOn(x2,y,1130*MenuScale,height) Lor isSelected) And mp_I\ServerMSG = SERVER_MSG_NONE Then
									Color 30+(30*isSelected),30+(30*isSelected),30+(30*isSelected)
									Rect x+1*MenuScale,y+1*MenuScale,width-6*MenuScale,height-2*MenuScale
								EndIf
								Color 255,255,255
								DrawImage mp_I\ConnectionIcons, x+2*MenuScale+(((width-6*MenuScale)/2)-(ImageWidth(mp_I\ServerIcon)/2)), y+2*MenuScale, Steam_GetConnectionQuality(se\region)
								y=y+height
							EndIf
						Next
					Else
						Exit
					EndIf
				Next
				
				;Buttons and stuff at bottom
				y = 286 * MenuScale
				height = 70 * MenuScale
				y = y + height + 20 * MenuScale
				y = y + 520 * MenuScale
				height = 40 * MenuScale
				x = 60 * MenuScale
				width = 1130 * MenuScale
				DrawFrame(x, y, width, height)
				
				If mp_I\ServerMSG = SERVER_MSG_NONE Then
					If mp_I\SelectedListServer = 0 Then
						DrawFrame(x + width - 210 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale)
						Color(100, 100, 100)
						Text(x + width - 110 * MenuScale, y + height - 21 * MenuScale, GetLocalString("Serverlist", "join_server"), True, True)
					EndIf
					
					x = 59 * MenuScale
					y = 286 * MenuScale
					height = 70 * MenuScale
					SetFont fo\Font[Font_Default_Large]
					If mp_I\ServerListPage >= Ceil(Float(mp_I\ServerListPage)/16.0)-1 Then 
						DrawFrame(x+width-height,y, height, height)
						Color(100, 100, 100)
						Text(x+(1130*MenuScale)-(height/2), y + height/2, "▶", True, True)
					EndIf
					If mp_I\ServerListPage = 0 Then 
						DrawFrame(x+width-height-280*MenuScale,y, height, height)
						Color(100, 100, 100)
						Text(x+(1130*MenuScale)-(height/2)-280*MenuScale, y + height/2, "◀", True, True)
					EndIf
					SetFont fo\Font[Font_Menu]
				Else
					SetFont fo\Font[Font_Default]
					
					DrawFrame(x + 10 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale)
					Color(100, 100, 100)
					Text(x + 110 * MenuScale, y + height - 21 * MenuScale, GetLocalString("Serverlist", "host_server"), True, True)
					
					DrawFrame(x + width*0.25 + 10 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale)
					Color(100, 100, 100)
					Text(x + width*0.25 + 110 * MenuScale, y + height - 21 * MenuScale, GetLocalString("Serverlist", "refresh_list"), True, True)
					
					DrawFrame(x + width*0.55 + 10 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale)
					Color(100, 100, 100)
					Text(x + width*0.55 + 110 * MenuScale, y + height - 21 * MenuScale, GetLocalString("Serverlist", "join_friend"), True, True)
					
					DrawFrame(x + width - 210 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale)
					Color(100, 100, 100)
					Text(x + width - 110 * MenuScale, y + height - 21 * MenuScale, GetLocalString("Serverlist", "join_server"), True, True)
					
					DrawFrame(x + width * 0.3 - 20 * MenuScale, y - 350 * MenuScale, 500 * MenuScale, 200 * MenuScale)
					
					x = 59 * MenuScale
					y = 286 * MenuScale
					height = 70 * MenuScale
					SetFont fo\Font[Font_Default_Large]
					DrawFrame(x+width-height,y, height, height)
					Color(100, 100, 100)
					Text(x+(1130*MenuScale)-(height/2), y + height/2, "▶", True, True)
					DrawFrame(x+width-height-280*MenuScale,y, height, height)
					Color(100, 100, 100)
					Text(x+(1130*MenuScale)-(height/2)-280*MenuScale, y + height/2, "◀", True, True)
					
					y = 286 * MenuScale
					height = 70 * MenuScale
					y = y + height + 20 * MenuScale
					y = y + 520 * MenuScale
					height = 40 * MenuScale
					x = 60 * MenuScale
					
					SetFont fo\Font[Font_Default]
					If mp_I\ServerMSG = SERVER_MSG_CONNECT Then
						Color 255,255,255
						Text (x + width * 0.3 + 20 * MenuScale, y - 330 * MenuScale, GetLocalStringR("Serverlist", "waiting_seconds", Str(Int(mp_I\ConnectionTime / 70.0))))
						If mp_I\ConnectionRetries > 0 Then
							Text (x + width * 0.3 + 20 * MenuScale, y - 280 * MenuScale, GetLocalStringR("Serverlist", "no_connection_retries", Str(mp_I\ConnectionRetries)))
						EndIf
					ElseIf mp_I\ServerMSG = SERVER_MSG_OFFLINE Then
						Color(255, 255, 255)
						Text (x + width * 0.3 + 20 * MenuScale, y - 330 * MenuScale, GetLocalString("Serverlist", "no_connect"))
						Text (x + width * 0.3 + 20 * MenuScale, y - 280 * MenuScale, GetLocalString("Serverlist", "no_connect2"))
					ElseIf mp_I\ServerMSG = SERVER_MSG_RETRIES Then
						Color(255, 255, 255)
						Text (x + width * 0.3 + 20 * MenuScale, y - 330 * MenuScale, GetLocalString("Serverlist", "retry_failed"))
						Text (x + width * 0.3 + 20 * MenuScale, y - 280 * MenuScale, GetLocalStringR("Serverlist", "retry_failed2", Str(MAX_RETRIES)))
					ElseIf mp_I\ServerMSG = SERVER_MSG_PASSWORD Then
						Color(255, 255, 255)
						Text (x + width * 0.3 + 20 * MenuScale, y - 330 * MenuScale, GetLocalString("Serverlist", "password") + ":")
						
						If (mp_I\ConnectPassword = "" And mp_I\ServerMSG = SERVER_MSG_PASSWORD) Then
							y = 286 * MenuScale
							height = 70 * MenuScale
							x = 60 * MenuScale
							y = y + height + 20 * MenuScale
							height = 560 * MenuScale
							width = 1130 * MenuScale
							DrawFrame(x + width * 0.3 + 280 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale)
							SetFont fo\Font[Font_Default]
							Color(100, 100, 100)
							Text(x + width * 0.3 + 355 * MenuScale, y + 320 * MenuScale, GetLocalString("Menu", "continue"), True, True)
						EndIf
					ElseIf mp_I\ServerMSG = SERVER_MSG_PWAIT Then
						Color(255, 255, 255)
						Text (x + width * 0.3 + 20 * MenuScale, y - 330 * MenuScale, GetLocalString("Serverlist", "password_check"))
					Else
						Color(255, 255, 255)
						Local message$ = ""
						Select mp_I\ServerMSG
							Case SERVER_MSG_QUIT
								message = "has_quit"
							Case SERVER_MSG_TIMEOUT
								message = "has_timeout"
							Case SERVER_MSG_KICK_MANYPLAYERS
								message = "has_kicked_full"
							Case SERVER_MSG_KICK_PASSWORD
								message = "has_kicked_password"
							Case SERVER_MSG_KICK_ENCRYPTION
								message = "has_kicked_encryption"
							Case SERVER_MSG_KICK_VERSION
								message = "has_kicked_version"
							Case SERVER_MSG_KICK_KICKED
								message = "has_kicked_kicked"
							Case SERVER_MSG_KICK_BANNED
								message = "has_kicked_banned"
						End Select
						Text (x + width * 0.3 + 20 * MenuScale, y - 330 * MenuScale, GetLocalString("Serverlist", message))
						If mp_I\KickReason <> "" Then
							Text (x + width * 0.3 + 20 * MenuScale, y - 280 * MenuScale, mp_I\KickReason)
						EndIf
					EndIf
				EndIf
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				height = 70 * MenuScale
				SetFont fo\Font[Font_Default_Medium]
				DrawFrame(x+width-height-210*MenuScale,y,height+140*MenuScale,height)
				Color 255,255,255
				Text(x+width-height-210*MenuScale+((height+140*MenuScale)/2),y+(height/2),GetLocalString("Menu", "page")+" "+Int(Max((mp_I\ServerListPage+1),1))+"/"+Int(Max((Int(Ceil(Float(mp_I\ServerListAmount-1)/16.0))),1)),True,True)
				
				;[End block]
			Case MenuTab_HostServer
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, GetLocalString("Serverlist", "host_server"), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 300 * MenuScale
				DrawFrame(x, y, width, height)
				
				SetFont fo\Font[Font_Default]
				
				Text (x + 20 * MenuScale, y + 25 * MenuScale, GetLocalString("Serverlist", "server_name"))
				
				Text (x + 20 * MenuScale, y + 65 * MenuScale, GetLocalString("Serverlist", "password"))
				
				DrawImage mp_I\PasswordIcon, x + 470 * MenuScale, y + 55 * MenuScale, (mp_I\PasswordVisible)
				If MouseOn(x + 470 * MenuScale, y + 55 * MenuScale, 30 * MenuScale, 30 * MenuScale) Then
					Color 150,150,150
				Else
					Color 255,255,255
				EndIf
				Rect x + 470 * MenuScale, y + 55 * MenuScale, 30 * MenuScale, 30 * MenuScale, False
				
				Color 255,255,255
				
				Text (x + 20 * MenuScale, y + 105 * MenuScale, GetLocalString("Serverlist", "gamemode"))
				
				Text (x + 20 * MenuScale, y + 145 * MenuScale, GetLocalString("Serverlist", "map"))
				
				Text (x + 20 * MenuScale, y + 185 * MenuScale, GetLocalString("Serverlist", "max_players"))
				
				Text (x + 20 * MenuScale, y + 225 * MenuScale, GetLocalString("Serverlist", "server_timeout"))
				
				Text (x + 20 * MenuScale, y + 265 * MenuScale, GetLocalString("Serverlist", "private_server"))
				If MouseOn(x+375*MenuScale,y+260*MenuScale,20*MenuScale,20*MenuScale)
					DrawOptionsTooltip("private")
				EndIf
				;[End Block]
			Case MenuTab_SelectMPMap
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, GetLocalString("Serverlist", "select_map"), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 45 * MenuScale + ImageHeight(mp_O\MapInList\Image) + FRAME_THICK * MenuScale
				
				DrawFrame(x, y, width, height)
				
				width = width - 190 * MenuScale
				x = x + 190 * MenuScale
				height = 45 * MenuScale
				
				DrawFrame(x, y, width, height)
				SetFont fo\Font[Font_Default_Medium]
				Text x + width / 2, y + height / 2, mp_O\MapInList\Name, True, True
				
				y = y + height - FRAME_THICK * MenuScale
				height = ImageHeight(mp_O\MapInList\Image) + 2 * FRAME_THICK * MenuScale
				
				DrawFrame x, y, width, height
				DrawBlock mp_O\MapInList\Image, x + FRAME_THICK * MenuScale, y + FRAME_THICK * MenuScale
				;[End Block]
			Case MenuTab_SelectMPGamemode
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, GetLocalString("Serverlist", "select_gamemode"), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 45 * MenuScale + ImageHeight(mp_O\Gamemode\Image) + FRAME_THICK * MenuScale + 250 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				width = width - 190 * MenuScale
				x = x + 190 * MenuScale
				height = 45 * MenuScale
				
				DrawFrame(x, y, width, height)
				SetFont fo\Font[Font_Default_Medium]
				Text x + width / 2, y + height / 2, mp_O\Gamemode\name, True, True
				
				y = y + height - FRAME_THICK * MenuScale
				height = ImageHeight(mp_O\Gamemode\Image) + 2 * FRAME_THICK * MenuScale
				
				DrawFrame x, y, width, height
				DrawImage mp_O\Gamemode\Image, x + FRAME_THICK * MenuScale, y + FRAME_THICK * MenuScale
				
				y = y + height - FRAME_THICK * MenuScale
				height = 250 * MenuScale + FRAME_THICK * MenuScale
				DrawFrame(x, y, width, height)
				
				SetFont fo\Font[Font_Default]
				x = x + 2 * FRAME_THICK * MenuScale
				y = y + 2 * FRAME_THICK * MenuScale
				width = width - 4 * FRAME_THICK * MenuScale
				height = height - 4 * FRAME_THICK * MenuScale
				RowText(GetLocalString("Gamemode", "description_" + Lower(mp_O\Gamemode\name)), x, y, width, height)
				y = y + height - 10 * MenuScale
				Text(x, y, GetLocalStringR("Gamemode", "max_player_amount", mp_O\Gamemode\MaxPlayersAllowed))
				;[End Block]
			Case MenuTab_MPGamemodeSettings
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, GetLocalString("Serverlist", "gamemode_options"), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				
				SetFont fo\Font[Font_Default]
				
				Select mp_O\Gamemode\ID
					Case Gamemode_Waves
						;[Block]
						height = 120 * MenuScale
						DrawFrame(x, y, width, height)
						
						Text (x + 20 * MenuScale, y + 30 * MenuScale, GetLocalString("Menu", "difficulty"))
						
						y=y+50*MenuScale
						
						Text (x + 20 * MenuScale, y + 30 * MenuScale, GetLocalString("Multiplayer", "wave_amount"))
						;[End Block]
				End Select
				;[End Block]
		End Select
	EndIf
	
	DrawAllMenuButtons()
	DrawAllMenuTicks()
	DrawAllMenuInputBoxes()
	DrawAllMenuSlideBars()
	DrawAllMenuSliders()
	DrawAllMenuDropdowns()
	
	;If opt\DisplayMode = 0 And (Not mm\OnPalette) Then DrawImage(CursorIMG, ScaledMouseX(), ScaledMouseY())
	
	;Put anything in here that's supposed to be drawn after all the buttons, inputs, etc...
	If MainMenuTab<>MenuTab_Default Then
		Select MainMenuTab
			Case MenuTab_Websites
				;[Block]
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 465 * MenuScale
				
				DrawFrame(x + 30 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale)
				DrawImage(I_MIG\WebsiteIMG[WEBSITE_DISCORD], x + 35 * MenuScale, y + 35 * MenuScale)
				
				y = y + 110 * MenuScale
				
				DrawFrame(x + 30 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale)
				DrawImage(I_MIG\WebsiteIMG[WEBSITE_STEAM], x + 35 * MenuScale, y + 35 * MenuScale)
				
				y = y + 110 * MenuScale
				
				DrawFrame(x + 30 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale)
				DrawImage(I_MIG\WebsiteIMG[WEBSITE_YOUTUBE], x + 35 * MenuScale, y + 35 * MenuScale)
				
				y = y + 110 * MenuScale
				
				DrawFrame(x + 30 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale)
				DrawImage(I_MIG\WebsiteIMG[WEBSITE_REDDIT], x + 35 * MenuScale, y + 35 * MenuScale)
				;[End Block]
			Case MenuTab_GameModes
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				DrawImage(I_MIG\ClassicIMG[CLASSIC_IMG_NTF], x + 405 * MenuScale, y +height+ 25 * MenuScale)
				
				DrawImage(I_MIG\ClassicIMG[CLASSIC_IMG_GUARD], x + 205 * MenuScale, y +height+ 25 * MenuScale)
				
				DrawImage(I_MIG\ClassicIMG[CLASSIC_IMG_D], x + 5 * MenuScale, y +height+ 25 * MenuScale)
				;[End Block]
			Case MenuTab_Serverlist
				;[Block]
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 40 * MenuScale
				height = 40 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Default]
				
				For i = 0 To (SERVER_LIST_SORT_CATEGORY_MAX-1)
					x = x + width
					Select i
						Case 0
							;Server name
							width = 500 * MenuScale
						Case 1, 2
							;Server gamemode
							;Server map
							width = 200 * MenuScale
						Case 3
							;Player amount
							width = 150 * MenuScale
					End Select
					If mp_I\ServerListSort = i * 2 Then
						Text x + width - 20 * MenuScale, y + height / 2, "▲", False, True
					ElseIf mp_I\ServerListSort = i * 2 + 1 Then
						Text x + width - 20 * MenuScale, y + height / 2, "▼", False, True
					EndIf
				Next
				;[End Block]
		End Select
	EndIf
	
	SetFont fo\Font[Font_Default]
	
End Function

Type ChangeLogLines
	Field txt$
End Type

Type Resolution
	Field width%
	Field height%
	Field index%
End Type

Function LoadResolutions()
	Local res.Resolution, res2.Resolution, res3.Resolution
	Local isResolutionSelected%, resolutionAmount%, swapWidth%, swapHeight%
	Local i%, n%
	
	For i% = 1 To opt\TotalGFXModes
		Local samefound% = False
		If IsResolutionWidthValid(GfxModeWidth(i)) And IsResolutionHeightValid(GfxModeHeight(i)) Then
			For res = Each Resolution
				If res\width = GfxModeWidth(i) And res\height = GfxModeHeight(i) Then
					samefound = True
					Exit
				EndIf
			Next
			If (Not samefound) Then
				res = New Resolution
				res\width = GfxModeWidth(i)
				res\height = GfxModeHeight(i)
				res\index = resolutionAmount
				resolutionAmount = resolutionAmount + 1
			EndIf
		EndIf
	Next
	
	isResolutionSelected = False
	For res = Each Resolution
		If res\width = DesktopWidth() And res\height = DesktopHeight() Then
			isResolutionSelected% = True
			Exit
		EndIf
	Next
	If (Not isResolutionSelected) Then
		res = New Resolution
		res\width = DesktopWidth()
		res\height = DesktopHeight()
		res\index = resolutionAmount
		resolutionAmount = resolutionAmount + 1
	EndIf
	
	i = 0
	n = 0
	opt\TotalGFXModes = resolutionAmount
	For res = Each Resolution
		swapWidth = res\width
		swapHeight = res\height
		If i > 0 Then
			n = i
			res2 = Before(res)
			res3 = res
			While (n > 0) And (res2\width > swapWidth Lor (res2\width = swapWidth And res2\height > swapHeight))
				res3\width = res2\width
				res3\height = res2\height
				res3 = res2
				res2 = Before(res2)
				n = n - 1
			Wend
			res3\width = swapWidth
			res3\height = swapHeight
		EndIf
		i = i + 1
	Next
	
	isResolutionSelected = False
	For res = Each Resolution
		If opt\GraphicWidth = res\width And opt\GraphicHeight = res\height Then
			isResolutionSelected = True
			Exit
		EndIf
	Next
	If (Not isResolutionSelected) Then
		opt\GraphicWidth = DesktopWidth()
		opt\GraphicHeight = DesktopHeight()
		RealGraphicWidth = opt\GraphicWidth
		RealGraphicHeight = opt\GraphicHeight
	EndIf
	If (Not IsResolutionWidthValid(opt\GraphicWidth)) Lor (Not IsResolutionHeightValid(opt\GraphicHeight)) Then
		res = Last Resolution
		opt\GraphicWidth = res\width
		opt\GraphicHeight = res\height
	EndIf
	
End Function

Function UpdateChangelog()
	Local ChangeLogFile
	Local ChangeLogLineAmount% = 0
	Local l$ = ""
	Local chl.ChangeLogLines
	Local canWriteLines% = False
	
	Delete Each ChangeLogLines
	
	InitLanguage()
	UpdateLang(Language$)
	
	ChangeLogFile = OpenFile(I_Loc\LangPath + "changelog.txt")
	
	While Not Eof(ChangeLogFile)
		l$ = ReadLine(ChangeLogFile)
		If Instr(l,"v"+VersionNumber)=1 Then
			canWriteLines = True
		EndIf
		If canWriteLines Then
			If Left(l,5)<>"-----" Then
				chl.ChangeLogLines = New ChangeLogLines
				If Instr(l,"v"+VersionNumber)>0 Then
					chl\txt = GetLocalString("Menu","new_update")+l
				Else
					chl\txt = l
				EndIf
				ChangeLogLineAmount = ChangeLogLineAmount + 1
			Else
				Exit
			EndIf
		EndIf
	Wend
	CloseFile(ChangeLogFile)
	
End Function

Function UpdateLauncher()
	Local res.Resolution
	Local LinesAmount%, isResolutionSelected%
	Local currentWidth%, currentHeight%
	Local i%, n%,chl.ChangeLogLines
	
	MenuScale = 1
	
	Graphics(640, 480, 0, 4)
	
	SetBuffer BackBuffer()
	
	RealGraphicWidth = opt\GraphicWidth
	RealGraphicHeight = opt\GraphicHeight
	
	currentWidth = opt\GraphicWidth
	currentHeight = opt\GraphicHeight
	
	fo\Font[Font_Default] = LoadFont_Strict("GFX\font\Courier New.ttf", 16)
	SetFont fo\Font[Font_Default]	
	LauncherIMG = LoadImage_Strict("GFX\menu\launcher.jpg")
	
	LoadMenuImages(False)
	
	UpdateChangelog()
	
	Local UpdaterIMG% = CreateImage(400,445)
	
	AppTitle GetLocalString("Launcher", "launcher_name")
	
	Repeat
		Local y = 0
		
		Cls
		
		MouseHit1 = MouseHit(1)
		MouseDown1 = MouseDown(1)
		
		Color 255, 255, 255
		DrawImage(LauncherIMG, 0, 0)
		
		If DrawButton(640 - 32, 0, 32, 32, "X", False, False, False) Then
			PutINIValue(gv\OptionFile, "options", "width", currentWidth)
			PutINIValue(gv\OptionFile, "options", "height", currentHeight)
			PutINIValue(gv\OptionFile, "options", "display mode", opt\DisplayMode)
			PutINIValue(gv\OptionFile, "options", "language", opt\LanguageVal)
			PutINIValue(gv\OptionFile, "options", "enabled 3d menu", opt\Menu3DEnabled)
			Steam_Shutdown()
			End
		EndIf
		If DrawButton(640 - 64, 0, 32, 32, "-", False, False, False) Then
			api_ShowWindow(SystemProperty("AppHWND"),6)
		EndIf
		
;		If DrawButton(5, 340, 145, 32, GetLocalString("Menu", "enable_3d_menu"), False, False, False) Then
;			opt\Menu3DEnabled = True
;		EndIf
;		If DrawButton(5, 370, 145, 32, GetLocalString("Menu", "enable_2d_menu"), False, False, False) Then
;			opt\Menu3DEnabled = False
;		EndIf
		
		If LinesAmount > 21
			y= 200-(20*ScrollMenuHeight2*ScrollBarY2)
			SetBuffer(ImageBuffer(UpdaterIMG))
			Cls
			LinesAmount%=0
			For chl.ChangeLogLines = Each ChangeLogLines
				Color 255,255,255
				If Instr(chl\txt,"v"+VersionNumber)>0 Then
					Color 200,0,0
				EndIf
				RowText(chl\txt$,5,y-195,390,440)
				y = y+(20*GetLineAmount(chl\txt$,390,440))
				LinesAmount = LinesAmount + (GetLineAmount(chl\txt$,390,440))
			Next
			SetBuffer BackBuffer()
			DrawImage UpdaterIMG,200,45
			ScrollMenuHeight2# = LinesAmount-21
			ScrollBarY2 = DrawScrollBar(620,40,20,440,620,40+(440-Max(440-8*ScrollMenuHeight2,50))*ScrollBarY2,20,Max(440-(8*ScrollMenuHeight2),50),ScrollBarY2,1,SelectedInputBox<>0)
		Else
			y = 200
			LinesAmount%=0
			Color 0,0,0
			For chl.ChangeLogLines = Each ChangeLogLines
				Color 255,255,255
				If Instr(chl\txt,"v"+VersionNumber)>0 Then
					Color 200,0,0
				EndIf
				RowText(chl\txt$,205,y-150,390,440)
				y = y+(20*GetLineAmount(chl\txt$,390,440))
				LinesAmount = LinesAmount + (GetLineAmount(chl\txt$,390,440))
			Next
			ScrollMenuHeight2# = LinesAmount
		EndIf
		
		Color 255,255,255
		Text(20, 130, GetLocalString("Launcher", "resolution") + ":")
		DrawFrame(5, 150, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
		Text(65, 165, currentWidth + "x" + currentHeight, True, True)
		Local frameheight = 0
		y = 0
		Select SelectedInputBox
			Case 0 ;Every tab hidden
				If DrawButton(120, 150, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 1
				EndIf
				
				Local txt$
				
				Text(15, 270, GetLocalString("Language", "lang") + ":")
				DrawFrame(5, 290, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\LanguageVal
					Case 0
						txt = GetLocalString("Language", "eng")
					Case 1
						txt = GetLocalString("Language", "rus")
				End Select
				Text(65, 305, txt, True, True)
				
				If DrawButton(120, 290, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 2
				EndIf
				
				Text(15, 200, GetLocalString("Launcher", "mode") + ":")
				DrawFrame(5, 220, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\DisplayMode
					Case 0
						txt = GetLocalString("Launcher", "windowed")
					Case 1
						txt = GetLocalString("Launcher", "fullscreen")
				End Select
				Text(65, 235, txt, True, True)
				
				If DrawButton(120, 220, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = -1
				EndIf
			Case -1 ;Display mode selection
				If DrawButton(120, 150, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 1
				EndIf
				
				Text(15, 270, GetLocalString("Language", "lang")+":")
				DrawFrame(5, 290, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				txt$ = ""
				Select opt\LanguageVal
					Case 0
						txt = GetLocalString("Language", "eng")
					Case 1
						txt = GetLocalString("Language", "rus")
				End Select
				Text(65, 305, txt,True,True)
				
				If DrawButton(120, 290, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 2
				EndIf
				
				DrawFrame(5, 237, 145, 70, 0, 0, 1024, 1024, FRAME_THICK, 512)
				For i = 0 To 1
					Select i
						Case 0
							Text(5+(145/2),267+(20*i),GetLocalString("Launcher", "windowed"),True,True)
						Case 1
							Text(5+(145/2),267+(20*i),GetLocalString("Launcher", "fullscreen"),True,True)
					End Select
					If MouseOn(20,257+(20*i),113,20) Then
						Color 100,100,100
						Rect(20,257+(20*i),113,20,False)
						Color 255,255,255
						If MouseHit1 Then
							opt\DisplayMode = i
							SelectedInputBox = 0
							PlaySound_Strict ButtonSFX[2]
							Exit
						EndIf
					EndIf
				Next
				
				Text(15, 200, GetLocalString("Launcher", "mode")+":")
				DrawFrame(5, 220, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				txt$ = ""
				Select opt\DisplayMode
					Case 0
						txt = GetLocalString("Launcher", "windowed")
					Case 1
						txt = GetLocalString("Launcher", "fullscreen")
				End Select
				Text(65, 235, txt,True,True)
				
				If DrawButton(120, 220, 30, 30, "▲", False, False, False) Then
					SelectedInputBox = 0
				EndIf
			Case 2 ;Language
				If DrawButton(120, 150, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 2
				EndIf
				
				Text(15, 200, GetLocalString("Launcher", "mode") + ":")
				DrawFrame(5, 220, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\DisplayMode
					Case 0
						txt = GetLocalString("Launcher", "windowed")
					Case 1
						txt = GetLocalString("Launcher", "fullscreen")
				End Select
				Text(65, 235, txt, True, True)
				
				If DrawButton(120, 220, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = -1
				EndIf
				
				DrawFrame(5, 307, 145, 70, 0, 0, 1024, 1024, FRAME_THICK, 512)
				For i = 0 To 1
					Select i
						Case 0
							Text(5+(145/2),337+(20*i),GetLocalString("Language", "eng"),True,True)
						Case 1
							Text(5+(145/2),337+(20*i),GetLocalString("Language", "rus"),True,True)
					End Select
					If MouseOn(20,327+(20*i),113,20) Then
						Color 100,100,100
						Rect(20,327+(20*i),113,20,False)
						Color 255,255,255
						If MouseHit1 Then
							opt\LanguageVal = i
							SelectedInputBox = 0
							UpdateChangelog()
							PlaySound_Strict ButtonSFX[2]
							Exit
						EndIf
					EndIf
				Next
				
				Text(15, 270, GetLocalString("Language", "lang")+":")
				DrawFrame(5, 290, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				txt$ = ""
				Select opt\LanguageVal
					Case 0
						txt = GetLocalString("Language", "eng")
					Case 1
						txt = GetLocalString("Language", "rus")
				End Select
				Text(65, 305, txt,True,True)
				
				If DrawButton(120, 290, 30, 30, "▲", False, False, False) Then
					SelectedInputBox = 0
				EndIf
			Default ;Resolution tab shown
				For i = 0 To Min(opt\TotalGFXModes, 9)
					y = y + 1
				Next
				DrawFrame(5, 177, 145, 20*y)
				frameheight = 20*y-4
				y = 0
				For i = (-1+SelectedInputBox) To (7+SelectedInputBox)
					If i < opt\TotalGFXModes Then
						If (opt\TotalGFXModes - 1) > 8 Then
							ScrollMenuHeight = opt\TotalGFXModes - 9
							SelectedInputBox = 1 + (ScrollBarY * (opt\TotalGFXModes - 9))
						EndIf
						y = y + 1
						If (177+(20*y)) < 177+frameheight Then
							isResolutionSelected = False
							For res = Each Resolution
								If res\index = i Then
									Text(145 / 2, 177 + (20 * y), res\width + "x" + res\height, True, True)
									If MouseOn(15,167+(20*y),113,20) Then
										Color 100,100,100
										Rect(15,167+(20*y),113,20,False)
										Color 255,255,255
										If MouseHit1 Then
											currentWidth = res\width
											currentHeight = res\height
											SelectedInputBox = 0
											PlaySound_Strict ButtonSFX[2]
											isResolutionSelected = True
										EndIf
									EndIf
									Exit
								EndIf
							Next
							If isResolutionSelected Then
								Exit
							EndIf
						EndIf
					Else
						Exit
					EndIf
				Next
				
				ScrollBarY = DrawScrollBar(127,179,20,frameheight,127,179+(frameheight-Max(frameheight-8*ScrollMenuHeight,50))*ScrollBarY,20,Max(frameheight-(8*ScrollMenuHeight),50),ScrollBarY,1)
				
				If DrawButton(120, 150, 30, 30, "▲", False, False, False) Then
					SelectedInputBox = 0
				EndIf
		End Select
		
		If DrawButton(5, 480 - 45, 145, 40, Upper(GetLocalString("Launcher", "launch")), False, False, False) Then
			opt\GraphicWidth = currentWidth
			opt\GraphicHeight = currentHeight
			RealGraphicWidth = opt\GraphicWidth
			RealGraphicHeight = opt\GraphicHeight
			Language$ = opt\LanguageVal
			Exit
		EndIf
		Flip
	Forever
	
	InitLanguage()
	UpdateLang(Language$)
	
	PutINIValue(gv\OptionFile, "options", "width", opt\GraphicWidth)
	PutINIValue(gv\OptionFile, "options", "height", opt\GraphicHeight)
	PutINIValue(gv\OptionFile, "options", "display mode", opt\DisplayMode)
	PutINIValue(gv\OptionFile, "options", "language", opt\LanguageVal)
	PutINIValue(gv\OptionFile, "options", "enabled 3d menu", opt\Menu3DEnabled)
	
	DeleteMenuImages()
	
	FreeImage UpdaterIMG
	FreeImage LauncherIMG
	
	SelectedInputBox = 0
	ScrollBarY = 0
	ScrollBarY2 = 0
	ScrollMenuHeight = 0
	ScrollMenuHeight2 = 0
	
End Function

Function IsResolutionWidthValid%(width%)
	
	If width >= 800 And width <= DesktopWidth() And width <= 4096 Then
		Return True
	EndIf
	Return False
	
End Function

Function IsResolutionHeightValid%(height%)
	
	If height >= 600 And height <= DesktopHeight() And height <= 4096 Then
		Return True
	EndIf
	Return False
	
End Function

Function DrawTiledImageRect(img%, srcX%, srcY%, srcwidth#, srcheight#, x%, y%, width%, height%)
	Local srcwidth_orig = srcwidth
	Local srcheight_orig = srcheight
	
	Local x2% = x
	While x2 < x+width
		Local y2% = y
		While y2 < y+height
			If x2 + srcwidth > x + width Then srcwidth = srcwidth - Max((x2 + srcwidth) - (x + width), 1)
			If y2 + srcheight > y + height Then srcheight = srcheight - Max((y2 + srcheight) - (y + height), 1)
			DrawBlockRect(img, x2, y2, srcX, srcY, srcwidth, srcheight)
			y2 = y2 + srcheight_orig
		Wend
		x2 = x2 + srcwidth_orig
	Wend
	
End Function

Global CursorPos% = -1

Function rInput$(aString$, MaxChr%)
	Local value% = GetKey()
	Local length% = Len(aString$)
	
	If CursorPos = -1 Then CursorPos = length
	
	If KeyDown(29) Then
		If value = 30 Then CursorPos = length
		If value = 31 Then CursorPos = 0
		If value = 127 Then aString = "" : CursorPos = 0
		If value = 22 Then
			aString = Left(aString, CursorPos) + GetClipboardContents() + Right(aString, Max(length - CursorPos,0))
			CursorPos = CursorPos + Len(aString) - length
			If MaxChr > 0 And MaxChr < Len(aString) Then aString = Left(aString, MaxChr) : CursorPos = MaxChr
		EndIf
		Return TextInput(aString)
	EndIf
	
	If value = 8 Then
		If CursorPos > 0 Then
			aString = TextInput(Left(aString, CursorPos)) + Right(aString, Max(length - CursorPos,0))
			CursorPos = CursorPos - 1
		EndIf
	ElseIf value = 30
		CursorPos = Min(CursorPos + 1, length)
	ElseIf value = 31
		CursorPos = Max(CursorPos - 1, 0)
	ElseIf value <> 127 And value >= 32 And value <= 254
		aString = TextInput(Left(aString, CursorPos)) + Right(aString, Max(length - CursorPos,0))
		CursorPos = CursorPos + Len(aString) - length
		If MaxChr > 0 And MaxChr < Len(aString) Then 
			aString = Left(aString, MaxChr)
			CursorPos = MaxChr
		EndIf	
	EndIf
	
	Return aString
End Function

Function InputBox$(x%, y%, width%, height%, Txt$, ID% = 0, MaxChr% = 0, currButton%=-1, currButtonTab%=0, currButtonSub%=0, drawAsPassword%=False, font%=Font_Default)
	Local currInputBox.MenuInputBox
	Local mib.MenuInputBox
	Local buttonexists%=False
	For mib = Each MenuInputBox
		If mib\x=x And mib\y=y And mib\width=width And mib\height=height
			buttonexists=True
			Exit
		EndIf
	Next
	If (Not buttonexists)
		mib = New MenuInputBox
		mib\x = x
		mib\y = y
		mib\width = width
		mib\height = height
		mib\txt = Txt
		mib\id = ID
		mib\currButton = currButton
		mib\currButtonTab = currButtonTab
		mib\currButtonSub = currButtonSub
		mib\drawAsPassword = drawAsPassword
		mib\font = font
	Else
		currInputBox = mib
		currInputBox\txt = Txt
		currInputBox\drawAsPassword = drawAsPassword
	EndIf
	
	Local MouseOnBox% = False
	If (Not co\Enabled)
		If MouseOn(x, y, width, height) Then
			MouseOnBox = True
			If MouseHit1 And SelectedInputBox <> ID Then SelectedInputBox = ID : FlushKeys : CursorPos = -1
		EndIf
	Else
		If co\CurrButton[currButtonTab] = currButton
			If co\CurrButtonSub[currButtonTab] = currButtonSub
				MouseOnBox = True
				If co\PressedButton And SelectedInputBox <> ID Then SelectedInputBox = ID : FlushKeys() : FlushJoy() : CursorPos = -1
			EndIf
		EndIf
	EndIf
	
	If (Not co\Enabled)
		If (Not MouseOnBox) And MouseHit1 And SelectedInputBox = ID Then SelectedInputBox = 0 : CursorPos = -1
	Else
		If (Not MouseOnBox) And co\PressedButton And SelectedInputBox = ID Then SelectedInputBox = 0 : CursorPos = -1
	EndIf
	
	If SelectedInputBox = ID Then
		Txt = rInput(Txt, MaxChr)
	EndIf
	
	Return Txt
End Function

Function InputBoxConsole$(x%, y%, width%, height%, Txt$, ID%, MaxChr%, font%=Font_Default)
	Color (255, 255, 255)
	;DrawTiledImageRect(MenuWhite, (x Mod 256), (y Mod 256), 512, 512, x, y, width, height)
	Rect(x, y, width, height)
	
	Color (0, 0, 0)
	
	Local MouseOnBox% = False
	If MouseOn(x, y, width, height) Then
		Color(50, 50, 50)
		MouseOnBox = True
		If MouseHit1 And SelectedInputBox <> ID Then SelectedInputBox = ID : FlushKeys : CursorPos = -1
	EndIf
	
	Rect(x + 2, y + 2, width - 4, height - 4)
	Color (255, 255, 255)	
	
	If (Not MouseOnBox) And MouseHit1 And SelectedInputBox = ID Then SelectedInputBox = 0 : CursorPos = -1
	
	If SelectedInputBox = ID Then
		Txt = rInput(Txt, MaxChr)
		If (MilliSecs() Mod 800) < 400 Then Rect (x + width / 2 - (StringWidth(Txt)) / 2 + StringWidth(Left(Txt, CursorPos)), y + height / 2 - 5, 2, 12)
	EndIf	
	SetFont fo\Font[font]
	Text(x + width / 2, y + height / 2, Txt, True, True)
	
	Return Txt
End Function

Const FRAME_THICK = 3

Function DrawFrame(x%, y%, width%, height%, xoffset%=0, yoffset%=0, srcwidth% = 1024, srcheight% = 1024, frameoffset%=FRAME_THICK, tile%=256, Locked% = False)
	
	If Locked Then
		Color 250, 0, 0
	Else
		Color 50,50,50
	EndIf
	Rect(x, y, width, height)
	Color 10, 10, 10
	Rect(x+frameoffset*MenuScale, y+frameoffset*MenuScale, width-(frameoffset*2)*MenuScale, height-(frameoffset*2)*MenuScale)
	Color 255, 255, 255
	
End Function

Function DrawButton%(x%, y%, width%, height%, txt$, bigfont% = True, waitForMouseUp%=False, usingAA%=True, currButton%=-1, currButtonTab%=0, currButtonSub%=0, locked% = False)
	Local clicked% = False
	
	If usingAA
		Local currMButton.MenuButton
		Local mb.MenuButton
		Local buttonexists%=False
		For mb = Each MenuButton
			If mb\x=x And mb\y=y And mb\width=width And mb\height=height
				buttonexists=True
				Exit
			EndIf
		Next
		If (Not buttonexists)
			mb = New MenuButton
			mb\x = x
			mb\y = y
			mb\width = width
			mb\height = height
			mb\txt = txt
			mb\bigfont = bigfont
			mb\currButton = currButton
			mb\currButtonTab = currButtonTab
			mb\currButtonSub = currButtonSub
			mb\locked = locked
		Else
			currMButton = mb
			currMButton\txt = txt
		EndIf
	EndIf
	
	If (Not co\Enabled)
		If MouseOn(x, y, width, height) Then
			If (Not locked) Then
				If (MouseHit1 And (Not waitForMouseUp)) Lor (MouseUp1 And waitForMouseUp) Then 
					clicked = True
					PlaySound_Strict(ButtonSFX[2])
					If waitForMouseUp Then
						MouseUp1 = False
					Else
						MouseHit1 = False
					EndIf
				EndIf
			Else
				If (MouseHit1 And (Not waitForMouseUp)) Lor (MouseUp1 And waitForMouseUp) Then 
					PlaySound_Strict(ButtonSFX[1])
					If waitForMouseUp Then
						MouseUp1 = False
					Else
						MouseHit1 = False
					EndIf
					clicked = False
				EndIf
			EndIf
		EndIf
	Else
		If co\CurrButton[currButtonTab] = currButton
			If co\CurrButtonSub[currButtonTab] = currButtonSub
				If (co\PressedButton)
					PlaySound_Strict(ButtonSFX[2])
					clicked = True
					FlushJoy()
				EndIf
			EndIf
		EndIf
	EndIf
	
	;Button for launcher
	If (Not usingAA)
		DrawFrame(x,y,width,height)
		
		If (Not co\Enabled)
			If MouseOn(x, y, width, height)
				Color(30, 30, 30)
				Rect(x + 4, y + 4, width - 8, height - 8)	
			Else
				Color(0, 0, 0)
			EndIf
		Else
			If co\CurrButton[currButtonTab] = currButton
				If co\CurrButtonSub[currButtonTab] = currButtonSub
					Color(30, 30, 30)
					Rect(x + 4, y + 4, width - 8, height - 8)
				EndIf
			Else
				Color(0, 0, 0)
			EndIf
		EndIf
		Color (255, 255, 255)
		If bigfont Then 
			SetFont fo\Font[Font_Menu]
		Else 
			SetFont fo\Font[Font_Default]
		EndIf
		Text(x + width / 2, y + height / 2, txt, True, True)
	EndIf
	
	Return clicked
End Function

Function DrawTick%(x%, y%, selected%, locked% = False, currButton%=-1, currButtonTab%=0, currButtonSub%=0)
	Local width% = 20 * MenuScale, height% = 20 * MenuScale
	
	Local currTick.MenuTick
	Local mt.MenuTick
	Local buttonexists%=False
	For mt = Each MenuTick
		If mt\x=x And mt\y=y
			buttonexists=True
			Exit
		EndIf
	Next
	If (Not buttonexists)
		mt = New MenuTick
		mt\x = x
		mt\y = y
		mt\selected = selected
		mt\locked = locked
		mt\currButton = currButton
		mt\currButtonTab = currButtonTab
		mt\currButtonSub = currButtonSub
	Else
		currTick = mt
		mt\selected = selected
		mt\locked = locked
	EndIf
	
	If (Not co\Enabled)
		Local Highlight% = MouseOn(x, y, width, height) And (Not locked)
	Else
		If co\CurrButton[currButtonTab]=currButton And co\CurrButtonSub[currButtonTab]=currButtonSub
			Highlight = True
		Else
			Highlight = False
		EndIf
	EndIf
	
	If Highlight Then
		If (Not locked) Then
			If (Not co\Enabled)
				If MouseHit1 Then selected = (Not selected) : PlaySound_Strict (ButtonSFX[2])
			Else
				If co\PressedButton Then selected = (Not selected) : PlaySound_Strict(ButtonSFX[2])
			EndIf
		Else
			If (Not co\Enabled)
				If MouseHit1 Then selected = (Not selected) : PlaySound_Strict (ButtonSFX[1])
			Else
				If co\PressedButton Then selected = (Not selected) : PlaySound_Strict(ButtonSFX[1])
			EndIf
		EndIf
	EndIf
	
	If locked Then
		Color 250, 0, 0
	Else
		Color 50, 50, 50
	EndIf
	
	Return selected
End Function

Function DrawDropdown%(x%, y%, value%, id%, txt1$, txt2$, txt3$, txt4$="", txt5$="")
	Local currDrop.MenuDropdown
	Local md.MenuDropdown
	Local buttonexists%=False
	Local i%
	
	For md = Each MenuDropdown
		If md\x=x And md\y=y
			buttonexists=True
			Exit
		EndIf
	Next
	If (Not buttonexists)
		md = New MenuDropdown
		md\x = x
		md\y = y
		md\value = value
		md\ID = id
		md\txt1 = txt1
		md\txt2 = txt2
		md\txt3 = txt3
		md\txt4 = txt4
		md\txt5 = txt5
		If txt5 = "" Then
			If txt4 = "" Then
				md\size = 3
			Else
				md\size = 4
			EndIf
		Else
			md\size = 5
		EndIf
	Else
		currDrop = md
		currDrop\value = value
	EndIf
	If SelectedInputBox = md\ID
		If DrawButton(md\x + (120 - FRAME_THICK) * MenuScale, md\y, 30 * MenuScale, 30 * MenuScale, "▲", False) Then
			SelectedInputBox = 0
		EndIf
		For i = 0 To md\size-1
			If MouseOn(md\x + 5.5 * MenuScale, md\y + (20 - FRAME_THICK + 20 + 20 * i) * MenuScale, 137 * MenuScale, 20 * MenuScale) Then
				If MouseHit1 Then
					value = i
					MouseHit1 = False
					SelectedInputBox = 0
					PlaySound_Strict(ButtonSFX[2])
				EndIf
			EndIf	
		Next
	Else
		If DrawButton(md\x + (120 - FRAME_THICK) * MenuScale, md\y, 30 * MenuScale, 30 * MenuScale, "▼", False) Then
			SelectedInputBox = md\ID
		EndIf
	EndIf
	
	Return value
End Function

Function SlideBar#(x%, y%, width%, value#, currButton%=-1, currButtonTab%=0, currButtonSub%=0, txtlow$="LOW", txthigh$="HIGH")
	Local currSlideBar.MenuSlideBar
	Local msb.MenuSlideBar
	Local buttonexists%=False
	For msb = Each MenuSlideBar
		If msb\x=x And msb\y=y And msb\width=width
			buttonexists=True
			Exit
		EndIf
	Next
	If (Not buttonexists)
		msb = New MenuSlideBar
		msb\x = x
		msb\y = y
		msb\width = width
		msb\value = value
		msb\currButton = currButton
		msb\currButtonTab = currButtonTab
		msb\currButtonSub = currButtonSub
		msb\txtlow = txtlow
		msb\txthigh = txthigh
	Else
		currSlideBar = msb
		currSlideBar\value = value
	EndIf
	
	If (Not co\Enabled)
		If MouseDown1 And OnSliderID=0 Then
			If ScaledMouseX() >= x And ScaledMouseX() <= x + width + 14 And ScaledMouseY() >= y And ScaledMouseY() <= y + 20 Then
				value = Min(Max((ScaledMouseX() - x) * 100 / width, 0), 100)
			EndIf
		EndIf
	Else
		If co\CurrButton[currButtonTab]=currButton
			If co\CurrButtonSub[currButtonTab]=currButtonSub
				value = UpdateControllerSideSelection(value,0,100)
			EndIf
		EndIf
	EndIf
	
	Return value
	
End Function

Function RowText(A$, X, Y, W, H, align% = 0, Leading#=1)
	;Display A$ starting at X,Y - no wider than W And no taller than H (all in pixels).
	;Leading is optional extra vertical spacing in pixels
	
	If H<1 Then H=2048
	
	Local LinesShown = 0
	Local Height = StringHeight(A$) + Leading
	Local b$
	
	While Len(A) > 0
		Local space = Instr(A$, " ")
		If space = 0 Then space = Len(A$)
		Local temp$ = Left(A$, space)
		Local trimmed$ = Trim(temp) ;we might ignore a final space 
		Local extra = 0 ;we haven't ignored it yet
		;ignore final space If doing so would make a word fit at End of Line:
		If (StringWidth (b$ + temp$) > W) And (StringWidth (b$ + trimmed$) <= W) Then
			temp = trimmed
			extra = 1
		EndIf
		
		If StringWidth (b$ + temp$) > W Then ;too big, so Print what will fit
			If align Then
				Text(X + W / 2 - (StringWidth(b) / 2), LinesShown * Height + Y, b)
			Else
				Text(X, LinesShown * Height + Y, b)
			EndIf			
			
			LinesShown = LinesShown + 1
			b$=""
		Else ;append it To b$ (which will eventually be printed) And remove it from A$
			b$ = b$ + temp$
			A$ = Right(A$, Len(A$) - (Len(temp$) + extra))
		EndIf
		
		If ((LinesShown + 1) * Height) > H Then Exit ;the Next Line would be too tall, so leave
	Wend
	
	If (b$ <> "") And((LinesShown + 1) <= H) Then
		If align Then
			Text(X + W / 2 - (StringWidth(b) / 2), LinesShown * Height + Y, b) ;Print any remaining Text If it'll fit vertically
		Else
			Text(X, LinesShown * Height + Y, b) ;Print any remaining Text If it'll fit vertically
		EndIf
	EndIf
	
End Function

Function GetLineAmount(A$, W, H, Leading#=1)
	;Display A$ starting at X,Y - no wider than W And no taller than H (all in pixels).
	;Leading is optional extra vertical spacing in pixels
	
	If H<1 Then H=2048
	
	Local LinesShown = 0
	Local Height = StringHeight(A$) + Leading
	Local b$
	
	While Len(A) > 0
		Local space = Instr(A$, " ")
		If space = 0 Then space = Len(A$)
		Local temp$ = Left(A$, space)
		Local trimmed$ = Trim(temp) ;we might ignore a final space 
		Local extra = 0 ;we haven't ignored it yet
		;ignore final space If doing so would make a word fit at End of Line:
		If (StringWidth (b$ + temp$) > W) And (StringWidth (b$ + trimmed$) <= W) Then
			temp = trimmed
			extra = 1
		EndIf
		
		If StringWidth (b$ + temp$) > W Then ;too big, so Print what will fit
			
			LinesShown = LinesShown + 1
			b$=""
		Else ;append it To b$ (which will eventually be printed) And remove it from A$
			b$ = b$ + temp$
			A$ = Right(A$, Len(A$) - (Len(temp$) + extra))
		EndIf
		
		If ((LinesShown + 1) * Height) > H Then Exit ;the Next Line would be too tall, so leave
	Wend
	
	Return LinesShown+1
	
End Function

Function LimitText$(txt$,width%)
	Local TextLength%, UnFitting%, LetterWidth%
	If txt = "" Lor width = 0 Then Return ""
	TextLength = StringWidth(txt)
	UnFitting = TextLength - width*FontWidth()
	If UnFitting <= 0 Then
		Return txt
	Else
		LetterWidth = TextLength / Len(txt)
		Return (Left(txt, Max(Len(txt) - UnFitting / (LetterWidth - 4), 1)) + "...")
	EndIf
End Function

Function DrawTooltip(message$)
	Local scale# = opt\GraphicHeight/768.0
	
	Local width = (StringWidth(message$))+20*MenuScale
	
	Color 25,25,25
	Rect(ScaledMouseX()+20,ScaledMouseY(),width,19*scale,True)
	Color 150,150,150
	Rect(ScaledMouseX()+20,ScaledMouseY(),width,19*scale,False)
	SetFont fo\Font[Font_Default]
	Text(ScaledMouseX()+(20*MenuScale)+(width/2),ScaledMouseY()+(12*MenuScale), message$, True, True)
End Function

Global QuickLoadPercent% = -1
Global QuickLoadPercent_DisplayTimer# = 0
Global QuickLoad_CurrEvent.Events

Function DrawQuickLoading()
	
	If QuickLoadPercent > -1
		MidHandle QuickLoadIcon
		;DrawImage QuickLoadIcon,opt\GraphicWidth-90,opt\GraphicHeight-150
		DrawImage QuickLoadIcon,opt\GraphicWidth-90,opt\GraphicHeight-190
		Color 255,255,255
		SetFont fo\Font[Font_Default]
		;Text opt\GraphicWidth-100,opt\GraphicHeight-90,"LOADING: "+QuickLoadPercent+"%",1
		Text opt\GraphicWidth-100,opt\GraphicHeight-130,Upper(GetLocalString("Menu","loading"))+": "+QuickLoadPercent+"%",1
		If QuickLoadPercent > 99
			If QuickLoadPercent_DisplayTimer < 70
				QuickLoadPercent_DisplayTimer# = Min(QuickLoadPercent_DisplayTimer+fps\Factor[0],70)
			Else
				QuickLoadPercent = -1
			EndIf
		EndIf
		QuickLoadEvents()
	Else
		QuickLoadPercent = -1
		QuickLoadPercent_DisplayTimer# = 0
		QuickLoad_CurrEvent = Null
	EndIf
	
End Function

Function DrawOptionsTooltip(option$,value#=0,ingame%=False)
	; TODO make these fucking constant
	Local x = (60 + 580) * MenuScale
	Local fx# = x + 6 * MenuScale
	Local y = (286 + 70 + 20 + 70) * MenuScale
	Local fy# = y+6*MenuScale
	Local width = 400 * MenuScale
	Local fw# = width - 12 * MenuScale
	Local fh# = (150-12) * MenuScale
	Local lines% = 0, lines2% = 0
	Local txt$ = ""
	Local txt2$ = "", R% = 0, G% = 0, B% = 0
	Local usetestimg% = False, extraspace% = 0
	
	SetFont fo\Font[Font_Default]
	Color 255,255,255
	Select Lower(option$)
		;Graphic options
			;[Block]
		;Case "bump"
		;	txt = GetLocalString("Options", "bumptxt")
		;	txt2 = GetLocalString("Options", "cantingame")
		;	R = 255
		Case "startupvideos"
			txt = GetLocalString("Options", "startupvideos_txt")
		Case "3dmenu"
			txt = GetLocalString("Options", "3dmenu_txt")
		Case "vsync"
			txt = GetLocalString("Options", "vsynctxt")
		Case "roomlights"
			txt = GetLocalString("Options", "roomlightstxt")
		Case "gamma"
			txt = GetLocalString("Options", "gammatxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value*100))+"% "+GetLocalStringR("Options", "defaultval",Int(100))
		Case "framelimit"
			txt = GetLocalString("Options", "framelimittxt")
			If value > 0 Then
				R = 255
				G = 255
				B = 255
				txt2 = GetLocalStringR("Options", "currentval", Int(value))+" "+GetLocalString("Options", "fps")
			EndIf
		Case "texquality"
			txt = GetLocalString("Options", "texqualitytxt")
		Case "texfiltering"
			txt = GetLocalString("Options", "texfilteringtxt")
		Case "particleamount"
			txt = GetLocalString("Options", "particleamounttxt")
			Select value
				Case 0
					R = 255
					txt2 = GetLocalString("Options", "particleamounttxt_low")
				Case 1
					R = 255
					G = 255
					txt2 = GetLocalString("Options", "particleamounttxt_med")
				Case 2
					G = 255
					txt2 = GetLocalString("Options", "particleamounttxt_high")
			End Select
		Case "vram"
			txt = GetLocalString("Options", "vramtxt") 
			txt2 = GetLocalString("Options", "cantingame")
			R = 255
		Case "fov"
			txt = GetLocalString("Options", "fovtxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value))+" "+GetLocalString("Options", "fov2")
		Case "cubemap"
			txt = GetLocalString("Options", "cubemaptxt")
			;[End Block]
		Case "renderscope"
			txt = GetLocalString("Options", "renderscopetxt")
			;[End Block]
		;Sound options
			;[Block]
		Case "musicvol"
			txt = GetLocalString("Options", "musicvoltxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value*100))+"% "+GetLocalStringR("Options", "defaultval",Int(50))
		Case "soundvol"
			txt = GetLocalString("Options", "soundvoltxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value*100))+"% "+GetLocalStringR("Options", "defaultval",Int(100))
		Case "sfxautorelease"
			txt = GetLocalString("Options", "sfxautoreleasetxt")
		Case "mastervol"
			txt = GetLocalString("Options", "mastervoltxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value*100))+"% "+GetLocalStringR("Options", "defaultval",Int(50))
		Case "voicevol"
			txt = GetLocalString("Options", "voicevoltxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value*100))+"% "+GetLocalStringR("Options", "defaultval",Int(100))
		Case "bmsbossmus"
			txt = GetLocalString("Options", "bmsbossmus_txt")
		Case "elevmus"
			txt = GetLocalString("Options", "elevmus_txt")
		Case "mainmenumus"
			txt = GetLocalString("Options", "mainmenumus_txt")
			Select value
				Case 0
					R = 17
					G = 38
					B = 124
					txt2 = GetLocalString("Options", "ss_mus")
				Case 1
					R = 122
					G = 61
					B = 20
					txt2 = GetLocalString("Options", "ntf_mus")
				Case 2
					R = 255
					G = 71
					B = 242
					txt2 = GetLocalString("Options", "ue_mus")
				Case 3
					R = 19
					G = 76
					B = 0
					txt2 = GetLocalString("Options", "cb_mus")
				Case 4
					R = 188
					G = 75
					B = 0
					txt2 = GetLocalString("Options", "bms_mus")
			End Select
			;[End Block]
		;Control options	
			;[Block]
		Case "mousesensitivity"
			txt = GetLocalString("Options", "sensitivitytxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int((0.5+value)*100))+"% "+GetLocalStringR("Options", "defaultval",Int(50))
		Case "mouseinvert"
			txt = GetLocalString("Options", "inverttxt")
		Case "mousesmoothing"
			txt = GetLocalString("Options", "smoothingtxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value*100))+"% "+GetLocalStringR("Options", "defaultval",Int(100))
		Case "controls"
			txt = "Configure the in-game control scheme."
		Case "controller"
			txt = "Enables/Disables controller support."
			If value=1
				txt2 = "No controller found. Please plug in a controller or test if your current controller is supported by Windows. "
				txt2 = txt2 + "You may need to restart the game in order for it to initialize the controller."
				R = 255
			EndIf
		Case "controllersettings"
			txt = "Configures the controller control scheme."
		Case "holdtoaim"
			txt = GetLocalString("Options", "holdtoaimtxt")
		Case "holdtocrouch"
			txt = GetLocalString("Options", "holdtocrouchtxt")
			;[End Block]
		;Advanced options	
			;[Block]
		Case "hud"
			txt = GetLocalString("Options", "hudtxt")
		Case "consoleenable"
			txt = GetLocalStringR("Options", "consoleenabletxt", KeyName[KEY_CONSOLE])
		Case "showfps"
			txt = GetLocalString("Options", "showfpstxt")
		Case "playermodel"
			txt = GetLocalString("Options", "playermodel_txt")
			;[End Block]
		;Singleplayer options
			;[Block]
		Case "classic_mode_txt"
			txt = GetLocalString("Menu", "classic_mode_txt")
			;[End Block]
		;Multiplayer options
			;[Block]
		Case "private"
			txt = GetLocalString("Options", "opt_private")
			;[End Block]
	End Select
	
	lines% = GetLineAmount(txt,fw,fh)
	If usetestimg
		extraspace = 210*MenuScale
	EndIf
	If txt2$ = ""
		DrawFrame(x,y,width,((StringHeight(txt)*lines)+(10+lines)*MenuScale)+extraspace)
	Else
		lines2% = GetLineAmount(txt2,fw,fh)
		DrawFrame(x,y,width,(((StringHeight(txt)*lines)+(10+lines)*MenuScale)+(StringHeight(txt2)*lines2)+(10+lines2)*MenuScale)+extraspace)
	EndIf
	RowText(txt,fx,fy,fw,fh)
	If txt2$ <> ""
		Color R,G,B
		RowText(txt2,fx,(fy+(StringHeight(txt)*lines)+(5+lines)*MenuScale),fw,fh)
	EndIf
	If usetestimg
		MidHandle Menu_TestIMG
		If txt2$ = ""
			DrawImage Menu_TestIMG,x+(width/2),y+100*MenuScale+((StringHeight(txt)*lines)+(10+lines)*MenuScale)
		Else
			DrawImage Menu_TestIMG,x+(width/2),y+100*MenuScale+(((StringHeight(txt)*lines)+(10+lines)*MenuScale)+(StringHeight(txt2)*lines2)+(10+lines2)*MenuScale)
		EndIf
	EndIf
	
End Function

Global OnSliderID% = 0

Function Slider3(x%,y%,width%,value%,ID%,val1$,val2$,val3$,currButton%=-1,currButtonTab%=0,currButtonSub%=0)
	Local currSlider.MenuSlider
	Local ms.MenuSlider
	Local buttonexists%=False
	For ms = Each MenuSlider
		If ms\x=x And ms\y=y And ms\width=width And ms\amount=3
			buttonexists=True
			Exit
		EndIf
	Next
	If (Not buttonexists)
		ms = New MenuSlider
		ms\x = x
		ms\y = y
		ms\width = width
		ms\ID = ID
		ms\value = value
		ms\val1 = val1
		ms\val2 = val2
		ms\val3 = val3
		ms\currButton = currButton
		ms\currButtonTab = currButtonTab
		ms\currButtonSub = currButtonSub
		ms\amount = 3
	Else
		currSlider = ms
		currSlider\value = value
	EndIf
	
	If (Not co\Enabled)
		If MouseDown1 Then
			If (ScaledMouseX() >= x) And (ScaledMouseX() <= x+width+14) And (ScaledMouseY() >= y-8) And (ScaledMouseY() <= y+10)
				OnSliderID = ID
			EndIf
		EndIf
	Else
		If co\CurrButton[currButtonTab]=currButton
			If co\CurrButtonSub[currButtonTab]=currButtonSub
				OnSliderID = ID
			EndIf
		EndIf
	EndIf
	
	If (Not co\Enabled)
		If ID = OnSliderID
			If (ScaledMouseX() <= x+8)
				value = 0
			ElseIf (ScaledMouseX() >= x+width/2) And (ScaledMouseX() <= x+(width/2)+8)
				value = 1
			ElseIf (ScaledMouseX() >= x+width)
				value = 2
			EndIf
		EndIf
	Else
		If ID = OnSliderID
			value = UpdateControllerSideSelection(value,0,2,1)
		EndIf
	EndIf
	
	Return value
	
End Function

Function Slider5(x%,y%,width%,value%,ID%,val1$,val2$,val3$,val4$,val5$,currButton%=-1,currButtonTab%=0,currButtonSub%=0)
	Local currSlider.MenuSlider
	Local ms.MenuSlider
	Local buttonexists%=False
	For ms = Each MenuSlider
		If ms\x=x And ms\y=y And ms\width=width And ms\amount=5
			buttonexists=True
			Exit
		EndIf
	Next
	If (Not buttonexists)
		ms = New MenuSlider
		ms\x = x
		ms\y = y
		ms\width = width
		ms\ID = ID
		ms\value = value
		ms\val1 = val1
		ms\val2 = val2
		ms\val3 = val3
		ms\val4 = val4
		ms\val5 = val5
		ms\currButton = currButton
		ms\currButtonTab = currButtonTab
		ms\currButtonSub = currButtonSub
		ms\amount = 5
	Else
		currSlider = ms
		currSlider\value = value
	EndIf
	
	If (Not co\Enabled)
		If MouseDown1 Then
			If (ScaledMouseX() >= x) And (ScaledMouseX() <= x+width+14) And (ScaledMouseY() >= y-8) And (ScaledMouseY() <= y+10)
				OnSliderID = ID
			EndIf
		EndIf
	Else
		If co\CurrButton[currButtonTab]=currButton
			If co\CurrButtonSub[currButtonTab]=currButtonSub
				OnSliderID = ID
			EndIf
		EndIf
	EndIf
	
	If (Not co\Enabled)
		If ID = OnSliderID
			If (ScaledMouseX() <= x+8)
				value = 0
			ElseIf (ScaledMouseX() >= x+width/4) And (ScaledMouseX() <= x+(width/4)+8)
				value = 1
			ElseIf (ScaledMouseX() >= x+width/2) And (ScaledMouseX() <= x+(width/2)+8)
				value = 2
			ElseIf (ScaledMouseX() >= x+width*0.75) And (ScaledMouseX() <= x+(width*0.75)+8)
				value = 3
			ElseIf (ScaledMouseX() >= x+width)
				value = 4
			EndIf
		EndIf
	Else
		If ID = OnSliderID
			value = UpdateControllerSideSelection(value,0,4,1)
		EndIf
	EndIf
	
	Return value
	
End Function

Global OnBar%
Global ScrollBarY# = 0.0
Global ScrollMenuHeight# = 0.0
Global ScrollBarY2# = 0.0
Global ScrollMenuHeight2# = 0.0

Function DrawScrollBar#(x, y, width, height, barx, bary, barwidth, barheight, bar#, dir = 0, locked = False, speed = 2)
	
	If (Not co\Enabled)
		
		Color(0, 0, 0)
		Button(barx, bary, barwidth, barheight, "", locked)
		
		If dir = 0 Then
			If height > 10 Then
				If (Not locked) Then
					Color 250,250,250
				Else
					Color 150,150,150
				EndIf
				Rect(barx + barwidth / 2, bary + 5*MenuScale, 2*MenuScale, barheight - 10)
				Rect(barx + barwidth / 2 - 3*MenuScale, bary + 5*MenuScale, 2*MenuScale, barheight - 10)
				Rect(barx + barwidth / 2 + 3*MenuScale, bary + 5*MenuScale, 2*MenuScale, barheight - 10)
			EndIf
		Else
			If width > 10 Then
				If (Not locked) Then
					Color 250,250,250
				Else
					Color 150,150,150
				EndIf
				Rect(barx + 4*MenuScale, bary + barheight / 2, barwidth - 10*MenuScale, 2*MenuScale)
				Rect(barx + 4*MenuScale, bary + barheight / 2 - 3*MenuScale, barwidth - 10*MenuScale, 2*MenuScale)
				Rect(barx + 4*MenuScale, bary + barheight / 2 + 3*MenuScale, barwidth - 10*MenuScale, 2*MenuScale)
			EndIf
		EndIf
		
		If (Not locked) Then
			If MouseX()>barx And MouseX()<barx+barwidth
				If MouseY()>bary And MouseY()<bary+barheight
					OnBar = True
				Else
					If (Not MouseDown1)
						OnBar = False
					EndIf
				EndIf
			Else
				If (Not MouseDown1)
					OnBar = False
				EndIf
			EndIf
			
			If MouseDown1
				If OnBar
					If dir = 0
						Return Min(Max(bar + MouseXSpeed() / Float(width - barwidth), 0), 1)
					Else
						Return Min(Max(bar + MouseYSpeed() / Float(height - barheight), 0), 1)
					EndIf
				EndIf
			EndIf
			
			Local MouseSpeedZ = MouseZSpeed()
			
			If MouseSpeedZ<>0 Then ;Only for vertical scroll bars
                Return Min(Max(bar - (MouseSpeedZ*speed) / Float(height - barheight), 0), 1)
            EndIf
		EndIf
	Else
		Color(0, 0, 0)
		Button(barx, bary, barwidth, barheight, "", locked)
		
		If dir = 0 Then 
			If height > 10 Then
				Color 250,250,250
				Rect(barx + barwidth / 2, bary + 5*MenuScale, 2*MenuScale, barheight - 10)
				Rect(barx + barwidth / 2 - 3*MenuScale, bary + 5*MenuScale, 2*MenuScale, barheight - 10)
				Rect(barx + barwidth / 2 + 3*MenuScale, bary + 5*MenuScale, 2*MenuScale, barheight - 10)
			EndIf
		Else
			If width > 10 Then
				Color 250,250,250
				Rect(barx + 4*MenuScale, bary + barheight / 2, barwidth - 10*MenuScale, 2*MenuScale)
				Rect(barx + 4*MenuScale, bary + barheight / 2 - 3*MenuScale, barwidth - 10*MenuScale, 2*MenuScale)
				Rect(barx + 4*MenuScale, bary + barheight / 2 + 3*MenuScale, barwidth - 10*MenuScale, 2*MenuScale)
			EndIf
		EndIf
		
		OnBar = True
		
		If dir = 0
			Return Min(Max(bar / Float(width - barwidth), 0), 1)
		Else
			Return Min(Max(bar / Float(height - barheight), 0), 1)
		EndIf
	EndIf
	
	Return bar
	
End Function

Function Button%(x,y,width,height,txt$, disabled%=False)
	Local Pushed = False
	
	Color 50, 50, 50
	If Not disabled Then
		If (Not co\Enabled)
			If ScaledMouseX() > x And ScaledMouseX() < x+width Then
				If ScaledMouseY() > y And ScaledMouseY() < y+height Then
					If MouseDown1 Then
						Pushed = True
						Color 50*0.6, 50*0.6, 50*0.6
					Else
						Color Min(50*1.2,255),Min(50*1.2,255),Min(50*1.2,255)
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
	If Pushed Then 
		Rect x,y,width,height
		Color 133,130,125
		Rect x+1*MenuScale,y+1*MenuScale,width-1*MenuScale,height-1*MenuScale,False	
		Color 10,10,10
		Rect x,y,width,height,False
		Color 250,250,250
		Line x,y+height-1*MenuScale,x+width-1*MenuScale,y+height-1*MenuScale
		Line x+width-1*MenuScale,y,x+width-1*MenuScale,y+height-1*MenuScale
	Else
		Rect x,y,width,height
		Color 133,130,125
		Rect x,y,width-1*MenuScale,height-1*MenuScale,False	
		Color 250,250,250
		Rect x,y,width,height,False
		Color 10,10,10
		Line x,y+height-1,x+width-1,y+height-1
		Line x+width-1,y,x+width-1,y+height-1		
	EndIf
	
	Color 255,255,255
	If disabled Then Color 70,70,70
	Text x+width/2, y+height/2-1*MenuScale, txt, True, True
	
	Color 0,0,0
	
	If Pushed And MouseHit1 Then Return True
End Function

Type MenuButton
	Field x%,y%,width%,height%
	Field txt$
	Field bigfont%
	Field currButton%=-1
	Field currButtonTab%=0
	Field currButtonSub%=0
	Field Menu3D%
	Field locked%
End Type

Function DrawAllMenuButtons()
	Local mb.MenuButton
	
	For mb = Each MenuButton
		If mb\Menu3D Then
			Color (255, 255, 255)
			If mb\bigfont = True Then
				SetFont fo\Font[Font_Menu]
			ElseIf mb\bigfont = 2 Then
				SetFont fo\Font[Font_Default_Large]
			ElseIf mb\bigfont = 3 Then
				SetFont fo\Font[Font_Default_Medium]
			Else
				SetFont fo\Font[Font_Default]
			EndIf
			Text(mb\x, mb\y + mb\height / 2, mb\txt, False, True)
			If (Not ConsoleOpen) Then
				If (Not co\Enabled) Then
					If MouseOn(mb\x, mb\y, mb\width, mb\height) Then
						If mb\bigfont = True Then
							SetFont fo\Font[Font_Menu]
						ElseIf mb\bigfont = 2 Then
							SetFont fo\Font[Font_Default_Large]
						ElseIf mb\bigfont = 3 Then
							SetFont fo\Font[Font_Default_Medium]
						Else
							SetFont fo\Font[Font_Default]
						EndIf
						If Rand(20)=1 Then
							Color 40,40,40
							Text(mb\x+Rand(-20,20), (mb\y + mb\height / 2)+Rand(-20,20), mb\txt, False, True)
						EndIf
						Color 100,100,100
						Text(mb\x, mb\y + mb\height / 2, mb\txt, False, True)
					EndIf
				Else
					If co\CurrButton[0] = mb\currButton Then
						If mb\bigfont = True Then
							SetFont fo\Font[Font_Menu]
						ElseIf mb\bigfont = 2 Then
							SetFont fo\Font[Font_Default_Large]
						ElseIf mb\bigfont = 3 Then
							SetFont fo\Font[Font_Default_Medium]
						Else
							SetFont fo\Font[Font_Default]
						EndIf
						If Rand(20)=1 Then
							Color 40,40,40
							Text(mb\x+Rand(-20,20), (mb\y + mb\height / 2)+Rand(-20,20), mb\txt, False, True)
						EndIf
						Color 100,100,100
						Text(mb\x, mb\y + mb\height / 2, mb\txt, False, True)
					EndIf
				EndIf
			EndIf
		Else
			DrawFrame (mb\x, mb\y, mb\width, mb\height)
			If (Not co\Enabled) Then
				If MouseOn(mb\x, mb\y, mb\width, mb\height) Then
					Color(30, 30, 30)
					Rect(mb\x + 4, mb\y + 4, mb\width - 8, mb\height - 8)	
				Else
					Color(0, 0, 0)
				EndIf
			Else
				If co\CurrButton[mb\currButtonTab] = mb\currButton Then
					If co\CurrButtonSub[mb\currButtonTab] = mb\currButtonSub Then
						Color(30, 30, 30)
						Rect(mb\x + 4, mb\y + 4, mb\width - 8, mb\height - 8)
					EndIf
				Else
					Color(0, 0, 0)
				EndIf
			EndIf
			
			Color (255, 255, 255)
			If mb\bigfont = True Then
				SetFont fo\Font[Font_Menu]
			ElseIf mb\bigfont = 2 Then
				SetFont fo\Font[Font_Default_Large]
			ElseIf mb\bigfont = 3 Then
				SetFont fo\Font[Font_Default_Medium]
			Else
				SetFont fo\Font[Font_Default]
			EndIf
			
			Text(mb\x + mb\width / 2, mb\y + mb\height / 2, mb\txt, True, True)
		EndIf
	Next
	
End Function

Type MenuTick
	Field x%,y%
	Field selected%
	Field locked%
	Field currButton%=-1
	Field currButtonTab%=0
	Field currButtonSub%=0
End Type

Function DrawAllMenuTicks()
	Local mt.MenuTick
	Local width%, height%
	
	
	For mt = Each MenuTick
		width%=20*MenuScale
		height%=20*MenuScale
		
		If mt\locked Then
			Color 250,0,0
		Else
			Color 50,50,50
		EndIf
		
		;Color (255, 255, 255)
		Rect(mt\x, mt\y, width, height)
		Color (255, 255, 255)
		If (Not co\Enabled) Then
			Local Highlight% = MouseOn(mt\x, mt\y, width, height) And (Not mt\locked)
		Else
			If co\CurrButton[mt\currButtonTab]=mt\currButton And co\CurrButtonSub[mt\currButtonTab]=mt\currButtonSub Then
				Highlight = True
			Else
				Highlight = False
			EndIf
		EndIf
		If Highlight Then
			Color(50, 50, 50)
		Else
			Color(0, 0, 0)		
		EndIf
		If Highlight And co\Enabled And mt\selected Then
			Color 200,200,200
			Rect(mt\x, mt\y, width, height)
			;Color (255, 255, 255)
			If mt\locked Then
				Color 250,0,0
			Else
				Color 50,50,50
			EndIf
			Rect(mt\x, mt\y, width, height)
			Color (255, 255, 255)
		EndIf
		Rect(mt\x + 2, mt\y + 2, width - 4, height - 4)
		If mt\selected Then
			If Highlight Then
				Color 255,255,255
			Else
				Color 200,200,200
			EndIf
			Rect(mt\x + 4, mt\y + 4, width - 8, height - 8)
		EndIf
		Color 255, 255, 255
	Next
	
End Function

Type MenuInputBox
	Field x%,y%,width%,height%
	Field txt$
	Field id%
	Field currButton%=-1
	Field currButtonTab%=0
	Field currButtonSub%=0
	Field drawAsPassword%=False
	Field font%=Font_Default
End Type

Function DrawAllMenuInputBoxes()
	Local mib.MenuInputBox
	Local i%
	
	For mib = Each MenuInputBox
		Color (255, 255, 255)
		;DrawTiledImageRect(MenuWhite, (mib\x Mod 256), (mib\y Mod 256), 512, 512, mib\x, mib\y, mib\width, mib\height)
		Rect(mib\x, mib\y, mib\width, mib\height)
		Color (0, 0, 0)
		SetFont fo\Font[mib\font]
		If (Not co\Enabled) Then
			If MouseOn(mib\x, mib\y, mib\width, mib\height) Then
				Color(50, 50, 50)
			EndIf
		Else
			If co\CurrButton[mib\currButtonTab] = mib\currButton Then
				If co\CurrButtonSub[mib\currButtonTab] = mib\currButtonSub Then
					Color(50, 50, 50)
				EndIf
			EndIf
		EndIf
		Rect(mib\x + 2, mib\y + 2, mib\width - 4, mib\height - 4)
		Color (255, 255, 255)
		If SelectedInputBox = mib\id Then
			If (MilliSecs() Mod 800) < 400 Then
				Rect (mib\x + mib\width / 2 - StringWidth(mib\txt) / 2 + StringWidth(Left(mib\txt, CursorPos)), mib\y + mib\height / 2 - 5, 2, 12)
			EndIf
		EndIf
		If (Not mib\drawAsPassword) Then
			Text(mib\x + mib\width / 2, mib\y + mib\height / 2, mib\txt, True, True)
		Else
			Local PWString$ = ""
			For i = 0 To Len(mib\txt)-1
				PWString = PWString + "*"
			Next
			Text(mib\x + mib\width / 2, mib\y + mib\height / 2, PWString, True, True)
		EndIf
	Next
	
End Function

Type MenuSlideBar
	Field x%,y%,width%
	Field value#
	Field currButton%=-1
	Field currButtonTab%=0
	Field currButtonSub%=0
	Field txtlow$,txthigh$
End Type

Function DrawAllMenuSlideBars()
	Local msb.MenuSlideBar
	
	For msb = Each MenuSlideBar
		If co\Enabled Then
			If co\CurrButton[msb\currButtonTab]=msb\currButton Then
				If co\CurrButtonSub[msb\currButtonTab]=msb\currButtonSub Then
					Color 30,30,30
					Rect(msb\x, msb\y, msb\width + 14, 20,True)
				EndIf
			EndIf
		EndIf
		Color 255,255,255
		Rect(msb\x, msb\y, msb\width + 14, 20,False)
		DrawImage(MenuMeterIMG, msb\x + msb\width * msb\value / 100.0 +3, msb\y+3)
		Color 170,170,170 
		Text (msb\x - 50 * MenuScale, msb\y + 6*MenuScale, msb\txtlow)					
		Text (msb\x + msb\width + 38 * MenuScale, msb\y+6*MenuScale, msb\txthigh)
	Next
	
End Function

Type MenuSlider
	Field x%,y%,width%
	Field value%
	Field ID%
	Field val1$,val2$,val3$,val4$,val5$
	Field currButton%=-1
	Field currButtonTab%=0
	Field currButtonSub%=0
	Field amount%
End Type

Function DrawAllMenuSliders()
	Local ms.MenuSlider
	
	For ms = Each MenuSlider
		If ms\amount=3
			Color 200,200,200
			Rect(ms\x,ms\y,ms\width+14,10,True)
			Rect(ms\x,ms\y-8,4,14,True)
			Rect(ms\x+(ms\width/2)+5,ms\y-8,4,14,True)
			Rect(ms\x+ms\width+10,ms\y-8,4,14,True)
			If (Not co\Enabled)
				If ms\ID = OnSliderID
					Color 0,255,0
					Rect(ms\x,ms\y,ms\width+14,10,True)
				Else
					If (ScaledMouseX() >= ms\x) And (ScaledMouseX() <= ms\x+ms\width+14) And (ScaledMouseY() >= ms\y-8) And (ScaledMouseY() <= ms\y+10)
						Color 0,200,0
						Rect(ms\x,ms\y,ms\width+14,10,False)
					EndIf
				EndIf
			Else
				If ms\ID = OnSliderID
					Color 0,255,0
					Rect(ms\x,ms\y,ms\width+14,10,True)
				EndIf
			EndIf
			If ms\value = 0
				DrawImage(MenuMeterIMG,ms\x,ms\y-8)
			ElseIf ms\value = 1
				DrawImage(MenuMeterIMG,ms\x+(ms\width/2)+3,ms\y-8)
			Else
				DrawImage(MenuMeterIMG,ms\x+ms\width+6,ms\y-8)
			EndIf
			Color 170,170,170
			If ms\value = 0
				Text(ms\x+2,ms\y+10+MenuScale,ms\val1,True)
			ElseIf ms\value = 1
				Text(ms\x+(ms\width/2)+7,ms\y+10+MenuScale,ms\val2,True)
			Else
				Text(ms\x+ms\width+12,ms\y+10+MenuScale,ms\val3,True)
			EndIf
		ElseIf ms\amount=5
			Color 200,200,200
			Rect(ms\x,ms\y,ms\width+14,10,True)
			Rect(ms\x,ms\y-8,4,14,True) ;1
			Rect(ms\x+(ms\width/4)+2.5,ms\y-8,4,14,True) ;2
			Rect(ms\x+(ms\width/2)+5,ms\y-8,4,14,True) ;3
			Rect(ms\x+(ms\width*0.75)+7.5,ms\y-8,4,14,True) ;4
			Rect(ms\x+ms\width+10,ms\y-8,4,14,True) ;5
			If (Not co\Enabled)
				If ms\ID = OnSliderID
					Color 0,255,0
					Rect(ms\x,ms\y,ms\width+14,10,True)
				Else
					If (ScaledMouseX() >= ms\x) And (ScaledMouseX() <= ms\x+ms\width+14) And (ScaledMouseY() >= ms\y-8) And (ScaledMouseY() <= ms\y+10)
						Color 0,255,0
						Rect(ms\x,ms\y,ms\width+14,10,False)
					EndIf
				EndIf
			Else
				If ms\ID = OnSliderID
					Color 0,255,0
					Rect(ms\x,ms\y,ms\width+14,10,True)
				EndIf
			EndIf
			If ms\value = 0
				DrawImage(MenuMeterIMG,ms\x,ms\y-8)
			ElseIf ms\value = 1
				DrawImage(MenuMeterIMG,ms\x+(ms\width/4)+1.5,ms\y-8)
			ElseIf ms\value = 2
				DrawImage(MenuMeterIMG,ms\x+(ms\width/2)+3,ms\y-8)
			ElseIf ms\value = 3
				DrawImage(MenuMeterIMG,ms\x+(ms\width*0.75)+4.5,ms\y-8)
			Else
				DrawImage(MenuMeterIMG,ms\x+ms\width+6,ms\y-8)
			EndIf
			Color 170,170,170
			If ms\value = 0
				Text(ms\x+2,ms\y+10+MenuScale,ms\val1,True)
			ElseIf ms\value = 1
				Text(ms\x+(ms\width/4)+4.5,ms\y+10+MenuScale,ms\val2,True)
			ElseIf ms\value = 2
				Text(ms\x+(ms\width/2)+7,ms\y+10+MenuScale,ms\val3,True)
			ElseIf ms\value = 3
				Text(ms\x+(ms\width*0.75)+9.5,ms\y+10+MenuScale,ms\val4,True)
			Else
				Text(ms\x+ms\width+12,ms\y+10+MenuScale,ms\val5,True)
			EndIf
		EndIf
	Next
	
End Function

Type MenuDropdown
	Field x%,y%
	Field value%
	Field ID%
	Field txt1$,txt2$,txt3$,txt4$,txt5$
	Field size%
End Type	

Function DrawAllMenuDropdowns()
	Local md.MenuDropdown
	Local txt$
	Local i%
	For md = Each MenuDropdown
		DrawFrame(md\x, md\y, 120 * MenuScale, 30 * MenuScale)
		Select md\value
			Case 0
				txt = md\txt1
			Case 1
				txt = md\txt2
			Case 2
				txt = md\txt3
			Case 3
				txt = md\txt4
			Case 4
				txt = md\txt5
		End Select
		Text(md\x + (120/2) * MenuScale, md\y + 15 * MenuScale, txt, True, True)
		Color(255, 255, 255)
	Next
	
	;Drawing the selection box when selected
	For md = Each MenuDropdown
		If SelectedInputBox = md\ID Then
			;Why 30.5? I have no idea!
			DrawFrame(md\x, md\y + (30.5 - FRAME_THICK) * MenuScale, (120 + 30 - FRAME_THICK) * MenuScale, (20 + (20 * md\size)) * MenuScale)
			For i = 0 To md\size-1
				Color(255, 255, 255)
				Select i
					Case 0
						txt = md\txt1
					Case 1
						txt = md\txt2
					Case 2
						txt = md\txt3
					Case 3
						txt = md\txt4
					Case 4
						txt = md\txt5
				End Select
				Text(md\x + ((120 + 30 - FRAME_THICK)/2) * MenuScale, md\y + (30.5 - FRAME_THICK + 20 + 20 * i) * MenuScale, txt, True, True)
				If MouseOn(md\x + 5.5 * MenuScale, md\y + (20 - FRAME_THICK + 20 + 20 * i) * MenuScale, 137 * MenuScale, 20 * MenuScale) Then
					Color(100, 100, 100)
					Rect(md\x + 5.5 * MenuScale, md\y + (20 - FRAME_THICK + 20 + 20 * i) * MenuScale, 137 * MenuScale, 20 * MenuScale, False)
					DrawOptionsTooltip(txt, i)
				EndIf
			Next
			Exit
		EndIf
	Next
	
End Function	

Function DeleteMenuGadgets()
	
	Delete Each MenuButton
	Delete Each MenuTick
	;Delete Each MenuPalette
	Delete Each MenuInputBox
	Delete Each MenuSlideBar
	Delete Each MenuSlider
	Delete Each MenuDropdown
	
End Function

Const WEBSITE_DISCORD = 0
Const WEBSITE_STEAM = 1
Const WEBSITE_YOUTUBE = 2
Const WEBSITE_REDDIT = 3

Const CLASSIC_IMG_NTF = 0
Const CLASSIC_IMG_GUARD = 1
Const CLASSIC_IMG_D = 2

Type MenuImages
	Field ArrowIMG[4]
	Field WebsiteIMG[4]
	Field ChapterIMG[10]
	Field ShopIMG[10]
	Field ClassicIMG[3]
End Type

Function LoadMenuImages(loadWebsites%=True)
	If I_MIG <> Null Then
		Return
	EndIf
	
	I_MIG = New MenuImages
	
	For i = 0 To 3
		I_MIG\ArrowIMG[i] = LoadImage_Strict("GFX\menu\arrow.png")
		RotateImage I_MIG\ArrowIMG[i], 90 * i
		HandleImage I_MIG\ArrowIMG[i], 0, 0
	Next
	
	If loadWebsites Then
		I_MIG\WebsiteIMG[WEBSITE_DISCORD] = LoadImage_Strict("GFX\menu\websites\discord.png")
		I_MIG\WebsiteIMG[WEBSITE_STEAM] = LoadImage_Strict("GFX\menu\websites\moddb.png")
		I_MIG\WebsiteIMG[WEBSITE_YOUTUBE] = LoadImage_Strict("GFX\menu\websites\youtube.png")
		I_MIG\WebsiteIMG[WEBSITE_REDDIT] = LoadImage_Strict("GFX\menu\websites\website.png")
		
		For i = 0 To 3
			I_MIG\WebsiteIMG[i] = ResizeImage2(I_MIG\WebsiteIMG[i], 70*MenuScale, 70*MenuScale)
		Next
		
		I_MIG\ChapterIMG[0] = LoadImage_Strict("GFX\menu\chapters\chapter_0.png")
		I_MIG\ChapterIMG[1] = LoadImage_Strict("GFX\menu\chapters\chapter_1.png")
		I_MIG\ChapterIMG[2] = LoadImage_Strict("GFX\menu\chapters\chapter_2.png")
		I_MIG\ChapterIMG[3] = LoadImage_Strict("GFX\menu\chapters\chapter_3.png")
		I_MIG\ChapterIMG[4] = LoadImage_Strict("GFX\menu\chapters\chapter_4.png")
		I_MIG\ChapterIMG[5] = LoadImage_Strict("GFX\menu\chapters\chapter_5.png")
		I_MIG\ChapterIMG[6] = LoadImage_Strict("GFX\menu\chapters\chapter_6.png")
		I_MIG\ChapterIMG[7] = LoadImage_Strict("GFX\menu\chapters\chapter_7.png")
		I_MIG\ChapterIMG[8] = LoadImage_Strict("GFX\menu\chapters\chapter_8.png")
		I_MIG\ChapterIMG[9] = LoadImage_Strict("GFX\menu\chapters\chapter_9.png")
		For i = 0 To 9
			ResizeImage(I_MIG\ChapterIMG[i], ImageWidth(I_MIG\ChapterIMG[i]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[i]) * MenuScale)
		Next
		
		I_MIG\ShopIMG[0] = LoadImage_Strict("GFX\menu\shop\Shop_USP.jpg")
		I_MIG\ShopIMG[1] = LoadImage_Strict("GFX\menu\shop\Shop_Card.jpg")
		I_MIG\ShopIMG[2] = LoadImage_Strict("GFX\menu\shop\Shop_MP5K.jpg")
		I_MIG\ShopIMG[3] = LoadImage_Strict("GFX\menu\shop\Shop_P90.jpg")
		I_MIG\ShopIMG[4] = LoadImage_Strict("GFX\menu\shop\Shop_Medkit.jpg")
		I_MIG\ShopIMG[5] = LoadImage_Strict("GFX\menu\shop\Shop_Crowbar.jpg")
		I_MIG\ShopIMG[6] = LoadImage_Strict("GFX\menu\shop\Shop_Kevlar.jpg")
		I_MIG\ShopIMG[7] = LoadImage_Strict("GFX\menu\shop\Shop_HK416.jpg")
		I_MIG\ShopIMG[8] = LoadImage_Strict("GFX\menu\shop\Shop_SPAS.jpg")
		I_MIG\ShopIMG[9] = LoadImage_Strict("GFX\menu\shop\Shop_SCP500.jpg")
		For i = 0 To 9
			I_MIG\ShopIMG[i] = ResizeImage2(I_MIG\ShopIMG[i], 70*MenuScale, 70*MenuScale)
		Next
		
		I_MIG\ClassicIMG[CLASSIC_IMG_NTF] = LoadImage_Strict("GFX\menu\ntf_gamemode.png")
		I_MIG\ClassicIMG[CLASSIC_IMG_GUARD] = LoadImage_Strict("GFX\menu\guard_gamemode.png")
		I_MIG\ClassicIMG[CLASSIC_IMG_D] = LoadImage_Strict("GFX\menu\d_gamemode.png")
		For i = 0 To 2
			I_MIG\ClassicIMG[i] = ResizeImage2(I_MIG\ClassicIMG[i], 160*MenuScale, 160*MenuScale)
		Next
		
		Cls()
		Flip()
	EndIf
	
End Function

Function DeleteMenuImages()
	
	For i = 0 To 3
		FreeImage I_MIG\ArrowIMG[i]
	Next
	For i = 0 To 3
		FreeImage I_MIG\WebsiteIMG[i]
	Next
	For i = 0 To 9
		FreeImage I_MIG\ChapterIMG[i]
	Next
	For i = 0 To 9
		FreeImage I_MIG\ShopIMG[i]
	Next
	For i = 0 To 2
		FreeImage I_MIG\ClassicIMG[i]
	Next
	
	Delete I_MIG
End Function

Function CreateMenuLogo.MenuLogo(parent%)
	Local ml.MenuLogo = New MenuLogo,tex
	
	ml\logo = CreateSprite(parent)
	If (Left(CurrentDate(), 7) = "31 Oct ") Then
		tex = LoadTexture_Strict("GFX\menu\Logos\Menu_3d_logo_haloween.png",1+2,0)
	ElseIf (Left(CurrentDate(), 7) = "31 Dec ") Then
		tex = LoadTexture_Strict("GFX\menu\Logos\Menu_3d_logo_new_year.png",1+2,0)
	ElseIf (Left(CurrentDate(), 7) = "31 Aug ") Then
		tex = LoadTexture_Strict("GFX\menu\Logos\Menu_3d_logo_birthday.png",1+2,0)
	Else
		tex = LoadTexture_Strict("GFX\menu\Logos\Menu_3d_logo.png",1+2,0)
	EndIf
	Local gr_scale# = ((Float(opt\GraphicWidth)/Float(opt\GraphicHeight))/(16.0/9.0))
	ScaleSprite ml\logo,0.29/gr_scale,(0.4/650.0*228.0)/gr_scale
	EntityTexture ml\logo,tex
	EntityFX ml\logo,1
	EntityOrder ml\logo,-3000
	If opt\Menu3DEnabled Lor (Not MainMenuOpen) Then
		MoveEntity ml\logo,-0.65+((0.35/gr_scale)-0.35),0.4/gr_scale,1
	Else
		MoveEntity ml\logo,0.06,0.4/gr_scale,1.0
	EndIf
	
	ml\gradient = CreateSprite(parent)
	Local tex2
	If opt\Menu3DEnabled Then
		tex2 = LoadTexture_Strict("GFX\menu\Menu_3d.png",1+2+16+32,0)
	Else
		tex2 = LoadTexture_Strict("GFX\menu 2d\Menu_2d.jpg",1+2+16+32,0)
	EndIf
	ScaleSprite ml\gradient,0.57/gr_scale,0.57/gr_scale
	EntityTexture ml\gradient,tex2
	EntityFX ml\gradient,1
	EntityOrder ml\gradient,-2999
	MoveEntity ml\gradient,-0.43+((0.57/gr_scale)-0.57),0,1
	
	HideEntity ml\logo
	HideEntity ml\gradient
	
	Return ml
End Function

Global HasBoughtUSP%
Global LockedUsp% = False

Global HasBoughtBeretta%
Global LockedBeretta% = False

Global HasBoughtMP5%
Global LockedMP5% = False

Global HasBoughtP90%
Global LockedP90% = False

Global HasBoughtMedkit%
Global LockedMedkit% = False

Global HasBoughtCrowbar%
Global LockedCrowbar% = False

Global HasBoughtKevlar%
Global LockedKevlar% = False

Global HasBoughtG36%
Global LockedG36% = False

Global HasBoughtSPAS%
Global LockedSPAS% = False

Global HasBoughtR870%
Global LockedR870% = False

Function CreatePointsShop()
	Local y = 286 * MenuScale
	Local height = 70 * MenuScale
	
	Local x = 60 * MenuScale
	y = y + height + 20 * MenuScale
	Local width = 580 * MenuScale
	height = 465 * MenuScale
	
; ~ First Row
	
	
	DrawFrame(x + 30 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale)
	DrawImage(I_MIG\ShopIMG[5], x + 35 * MenuScale, y + 35 * MenuScale) ; ~ c-bar
	
	DrawFrame(x + 120 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale)
	DrawImage(I_MIG\ShopIMG[1], x + 125 * MenuScale, y + 35 * MenuScale) ; ~ card
	
	DrawFrame(x + 210 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale)
	DrawImage(I_MIG\ShopIMG[0], x + 215 * MenuScale, y + 35 * MenuScale) ; ~ usp
	
	DrawFrame(x + 300 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale)
	DrawImage(I_MIG\ShopIMG[2], x + 305 * MenuScale, y + 35 * MenuScale) ; ~ mp5
	
	DrawFrame(x + 390 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale)
	DrawImage(I_MIG\ShopIMG[3], x + 395 * MenuScale, y + 35 * MenuScale) ; ~ p90
	
; ~ Second Row
	
	DrawFrame(x + 30 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale)
	DrawImage(I_MIG\ShopIMG[4], x + 35 * MenuScale, y + 125 * MenuScale) ; ~ medkit
	
	DrawFrame(x + 120 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale)
	DrawImage(I_MIG\ShopIMG[7], x + 125 * MenuScale, y + 125 * MenuScale) ; ~ hk416
	
	DrawFrame(x + 210 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale)
	DrawImage(I_MIG\ShopIMG[6], x + 215 * MenuScale, y + 125 * MenuScale) ; ~ armor
	
	DrawFrame(x + 300 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale)
	DrawImage(I_MIG\ShopIMG[8], x + 305 * MenuScale, y + 125 * MenuScale) ; ~ spas - 12
	
	DrawFrame(x + 390 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale)
	DrawImage(I_MIG\ShopIMG[9], x + 395 * MenuScale, y + 125 * MenuScale) ; ~ scp-500
	
End Function

Function UpdatePointsShop()
	Local y = 286 * MenuScale
	Local height = 70 * MenuScale
	
	Local x = 60 * MenuScale
	y = y + height + 20 * MenuScale
	Local width = 580 * MenuScale
	height = 465 * MenuScale
	
	If MainMenuOpen Then
		If cpt\Current > 1 Then
			If mnsh\Points >= 10 Then
				; ~ [Crowbar]
				If (Not LockedCrowbar) Then
					If MouseOn(x + 30 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							mnsh\Points = mnsh\Points - 10
							HasBoughtCrowbar = True
							LockedCrowbar = True
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_successfull.ogg"))
						EndIf
					EndIf
				Else
					If MouseOn(x + 30 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_denied.ogg"))
						EndIf
					EndIf
				EndIf
			EndIf
			If mnsh\Points >= 40 Then
				; ~ [Card]
				If (Not LockedBeretta) Then
					If MouseOn(x + 120 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							mnsh\Points = mnsh\Points - 40
							HasBoughtBeretta = True
							LockedBeretta = True
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_successfull.ogg"))
						EndIf
					EndIf
				Else
					If MouseOn(x + 120 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_denied.ogg"))
						EndIf
					EndIf
				EndIf
			EndIf
			If mnsh\Points >= 50 Then
				; ~ [USP]
				If (Not LockedUsp) Then
					If MouseOn(x + 210 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							mnsh\Points = mnsh\Points - 50
							HasBoughtUSP = True
							LockedUsp= True
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_successfull.ogg"))
						EndIf
					EndIf
				Else
					If MouseOn(x + 210 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_denied.ogg"))
						EndIf
					EndIf
				EndIf
			EndIf
			If mnsh\Points >= 120 Then
				; ~ [mp5k]
				If (Not LockedMP5) Then
					If MouseOn(x + 300 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							mnsh\Points = mnsh\Points - 120
							HasBoughtMP5 = True
							LockedMP5 = True
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_successfull.ogg"))
						EndIf
					EndIf
				Else
					If MouseOn(x + 300 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_denied.ogg"))
						EndIf
					EndIf
				EndIf
			EndIf
			If mnsh\Points >= 150 Then
				; ~ [p90]
				If (Not LockedP90) Then
					If MouseOn(x + 390 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							mnsh\Points = mnsh\Points - 150
							HasBoughtP90 = True
							LockedP90 = True
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_successfull.ogg"))
						EndIf
					EndIf
				Else
					If MouseOn(x + 390 * MenuScale, y + 30 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_denied.ogg"))
						EndIf
					EndIf
				EndIf
			EndIf
			If mnsh\Points >= 200 Then
				; ~ [medkit]
				If (Not LockedMedkit) Then
					If MouseOn(x + 30 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							mnsh\Points = mnsh\Points - 200
							HasBoughtMedkit = True
							LockedMedkit = True
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_successfull.ogg"))
						EndIf
					EndIf
				Else
					If MouseOn(x + 30 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_denied.ogg"))
						EndIf
					EndIf
				EndIf
			EndIf
			If mnsh\Points >= 250 Then
				; ~ [HK416]
				If (Not LockedG36) Then
					If MouseOn(x + 120 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							mnsh\Points = mnsh\Points - 250
							HasBoughtG36 = True
							LockedG36 = True
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_successfull.ogg"))
						EndIf
					EndIf
				Else
					If MouseOn(x + 120 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_denied.ogg"))
						EndIf
					EndIf
				EndIf
			EndIf
			If mnsh\Points >= 300 Then
				; ~ [ARMOR]
				If (Not LockedKevlar) Then
					If MouseOn(x + 210 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							mnsh\Points = mnsh\Points - 300
							HasBoughtKevlar = True
							LockedKevlar = True
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_successfull.ogg"))
						EndIf
					EndIf
				Else
					If MouseOn(x + 210 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_denied.ogg"))
						EndIf
					EndIf
				EndIf
			EndIf
			If mnsh\Points >= 400 Then
				; ~ [SPAS-12]
				If (Not LockedSPAS) Then
					If MouseOn(x + 300 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							mnsh\Points = mnsh\Points - 400
							HasBoughtSPAS = True
							LockedSPAS = True
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_successfull.ogg"))
						EndIf
					EndIf
				Else
					If MouseOn(x + 300 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_denied.ogg"))
						EndIf
					EndIf
				EndIf
			EndIf
			If mnsh\Points >= 500 Then
				; ~ [SCP-500]
				If (Not LockedR870) Then
					If MouseOn(x + 390 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							mnsh\Points = mnsh\Points - 500
							HasBoughtR870 = True
							LockedR870 = True
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_successfull.ogg"))
						EndIf
					EndIf
				Else
					If MouseOn(x + 390 * MenuScale, y + 120 * MenuScale, 80 * MenuScale, 80 * MenuScale) Then
						If MouseHit1 Then
							PlaySound_Strict(LoadTempSound("SFX\Interact\Purchase_denied.ogg"))
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D