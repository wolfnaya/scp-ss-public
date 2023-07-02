
; ~ [MATHEMATICS]

Include "SourceCode\Math_Core.bb"

Type FixedTimesteps
	Field tickDuration#
	Field accumulator#
	Field PrevTime%
	Field currTime%
	Field fps%
	Field tempfps%
	Field fpsgoal%
	Field DeltaTime%
End Type

Function SetTickrate(tickrate%)
	ft\tickDuration = 70.0/Float(tickrate)
End Function

Function AddToTimingAccumulator(milliseconds%)
	If (milliseconds < 1 Lor milliseconds > 500) Then
		Return
	EndIf
	ft\accumulator = ft\accumulator+Max(0,Float(milliseconds)*70.0/1000.0)
End Function

Function ResetTimingAccumulator()
	ft\accumulator = 0.0
End Function

Function SetCurrTime(time%)
	ft\currTime = time%
End Function

Function SetPrevTime(time%)
	ft\PrevTime = time%
End Function

Function GetCurrTime%()
	Return ft\currTime
End Function

Function GetPrevTime%()
	Return ft\PrevTime
End Function

Function GetTickDuration#()
	Return ft\tickDuration
End Function

SetTickrate(60)

Type Loc
	Field Lang$
	Field LangPath$
	Field Localized%
End Type

Global I_Loc.Loc = New Loc

Global Language$ = "English"

Function InitLanguage()
	
	Select opt\LanguageVal
		Case 0
			Language$ = "English"
		Case 1
			Language$ = "Russian"
	End Select
	
End Function

Function UpdateLang(Lang$)

	If Lang = "English" Then
		I_Loc\Lang = ""
		I_Loc\LangPath = ""
		I_Loc\Localized = False
	Else
		I_Loc\Lang = Lang
		I_Loc\LangPath = "Localization\" + Lang + "\"
		I_Loc\Localized = True
	EndIf

	IniWriteBuffer(I_Loc\LangPath + "Data\Local.ini")
	IniWriteBuffer(I_Loc\LangPath + "Data\Achievements.ini")
	
	InitFonts()
	
End Function

IniWriteBuffer("Data\1499chunks.ini")
IniWriteBuffer("Data\Achievements.ini")
IniWriteBuffer("Data\Events.ini")
IniWriteBuffer("Data\Loadingscreens.ini")
IniWriteBuffer("Data\Local.ini")
IniWriteBuffer("Data\Materials.ini")
IniWriteBuffer("Data\NPCAnims.ini")
IniWriteBuffer("Data\NPCBones.ini")
IniWriteBuffer("Data\NPCs.ini")
IniWriteBuffer("Data\PlayerBones.ini")
IniWriteBuffer("Data\Rooms.ini")
IniWriteBuffer("Data\SCP-294.ini")
IniWriteBuffer("Data\Weapons.ini")
InitLanguage()
UpdateLang(Language$)

Function GetLocalString$(Section$, Parameter$)
	
	Return GetFileLocalString("Data\Local.ini", Section, Parameter, Section + "." + Parameter)
	
End Function

Function GetFileLocalString$(File$, Section$, Parameter$, DefaultValue$ = "")
	
	If IniBufferKeyExist(I_Loc\LangPath + File, Section, Parameter) Then
		Return IniGetBufferString(I_Loc\LangPath + File, Section, Parameter)
	ElseIf IniBufferKeyExist(File, Section, Parameter)
		Return IniGetBufferString(File, Section, Parameter)
	Else
		Return DefaultValue
	EndIf
	
End Function

Function GetLocalStringR$(Section$, Parameter$, Replace$)
	
	Return Replace(GetLocalString(Section, Parameter), "%s", Replace)
	
End Function

Include "SourceCode\Strict_Loads.bb"
Include "SourceCode\Key_Names.bb"
Include "SourceCode\Key_Binds.bb"

LoadKeyBinds()

; ~ [FONT]

;[Block]

Const MaxFontAmount = 10
Const Font_Default = 0
Const Font_Menu = 1
Const Font_Digital_Small = 2
Const Font_Digital_Large = 3
Const Font_Journal = 4
Const Font_Default_Large = 5
Const Font_Menu_Medium = 6
Const Font_Menu_Small = 7
Const Font_Digital_Medium = 8
Const Font_Default_Medium = 9

Type Fonts
	Field UpdaterFont
	Field ConsoleFont
	Field Font[MaxFontAmount]
End Type

InitController()

Const VersionNumber$ = "0.0.5_DEV_Patch#1"
Const CompatibleNumber$ = "0.0.5"

Global MenuWhite%, MenuBlack%

Global ButtonSFX[4]

ButtonSFX[0] = LoadSound_Strict("SFX\Interact\Button.ogg")
ButtonSFX[1] = LoadSound_Strict("SFX\Interact\Button2.ogg")
ButtonSFX[2] = LoadSound_Strict("SFX\Interact\Button3.ogg")
ButtonSFX[3] = LoadSound_Strict("SFX\Interact\Button4.ogg")

;[End Block]

;[Block]

Global LauncherIMG%
Global Fresize_Image%, Fresize_Texture%, Fresize_Texture2%
Global Fresize_Cam%
Global WireframeState
Global HalloweenTex
Global RealGraphicWidth%,RealGraphicHeight%
Global AspectRatioRatio#
Global TextureFloat#

Select opt\TextureDetails%
	Case 0
		TextureFloat# = 0.8
	Case 1
		TextureFloat# = 0.0
	Case 2
		TextureFloat# = -0.8
End Select

Global AchvIni$

AchvIni$ = I_Loc\LangPath + "Data\Achievements.ini"

Global Data294$ = I_Loc\LangPath + "Data\SCP-294.ini"

AspectRatioRatio = 1.0

UpdateLauncher()

Delete Each Resolution

If opt\DisplayMode=1 Then
	Graphics3DExt(DesktopWidth(), DesktopHeight(), 0, 4)
	RealGraphicWidth = DesktopWidth()
	RealGraphicHeight = DesktopHeight()
	AspectRatioRatio = (Float(opt\GraphicWidth)/Float(opt\GraphicHeight))/(Float(RealGraphicWidth)/Float(RealGraphicHeight))
Else
	Graphics3DExt(opt\GraphicWidth, opt\GraphicHeight, 0, 2)
	RealGraphicWidth = opt\GraphicWidth
	RealGraphicHeight = opt\GraphicHeight
EndIf

Global MenuScale# = (opt\GraphicHeight / 1024.0)

LoadMenuImages()

SetBuffer(BackBuffer())

Type FPSSettings
	Field CurTime%
	Field PrevTime%
	Field LoopDelay%
	Field Factor#[2]
End Type

Global Framelimit% = GetINIInt(gv\OptionFile, "options", "framelimit", 120)
Global Vsync% = GetINIInt(gv\OptionFile, "options", "vsync")
Global CurrFrameLimit# = (Framelimit%-29)/100.0
Global ScreenGamma# = GetINIFloat(gv\OptionFile, "options", "screengamma", 1.0)

TextureAnisotropic 2^(opt\TextureFiltering+1)
TextureLodBias TextureFloat#

SeedRnd MilliSecs()

;[End Block]

Global GameSaved%

Global CanSave% = True

;[Block]

;Const Title_1$ = "He's watching..."
;Const Title_2$ = "Norther Wolf What???"
;Const Title_3$ = "Are you sure you're in safe? Well, no."
;Const Title_4$ = "Does numbers 9,6,3 and 2 says sth for ya??? If no,then you'll hear about it eventually..."
;Const Title_5$ = "Also try SCP: NTF!"
;Const Title_6$ = "The cild of inevitable"
;Const Title_7$ = "Do you like boss fights? I don't care"
;Const Title_8$ = "Well, I guess you will have a bad time..."
;Const Title_9$ = "Howling at the moon"
;Const Title_10$ = "Oh and by the way..."
;Const Title_11$ = "Wait, I'm from the science team!"
;Const Title_12$ = "Whata f?, What the fuck?, OH JESUS!"
;Const Title_13$ = "Waiting for AFAO..."
;Const Title_14$ = "H@!F-L14E"
;Const Title_15$ = "Developers cult"
;Const Title_16$ = "Press Alt + F4"
;Const Title_17$ = "SCP - Security Stories: SCP - Security Stories:"
;Const Title_18$ = "Is anyone reaing this?"
;Const Title_19$ = "The answer is - 42"
;Const Title_20$ = "Connection Terminated..."
;
;Local StrTemp$ = ""
;
;Select Rand(20)
;	Case 1
;		StrTemp = Title_1
;	Case 2
;		StrTemp = Title_2
;	Case 3
;		StrTemp = Title_3
;	Case 4
;		StrTemp = Title_4
;	Case 5
;		StrTemp = Title_5
;	Case 6
;		StrTemp = Title_6
;	Case 7
;		StrTemp = Title_7
;	Case 8
;		StrTemp = Title_8
;	Case 9
;		StrTemp = Title_9
;	Case 10
;		StrTemp = Title_10
;	Case 11
;		StrTemp = Title_11
;	Case 12
;		StrTemp = Title_12
;	Case 13
;		StrTemp = Title_13
;	Case 14
;		StrTemp = Title_14
;	Case 15
;		StrTemp = Title_15
;	Case 16
;		StrTemp = Title_16
;	Case 17
;		StrTemp = Title_17
;	Case 18
;		StrTemp = Title_18
;	Case 19
;		StrTemp = Title_19
;	Case 20
;		StrTemp = Title_20
;End Select

AppTitle "SCP - Security Stories: "+ VersionNumber;StrTemp

Delay 100

;[End Block]

Global CursorIMG% = LoadImage_Strict("GFX\Menu\Cursor.png")

Include "SourceCode\Loading_Screens.bb"

InitFonts()

DrawLoading(0, True)

;[Block]

Global CreditsFont%,CreditsFont2%

Global Viewport_Center_X% = opt\GraphicWidth / 2, Viewport_Center_Y% = opt\GraphicHeight / 2

Global MouseLook_X_Inc# = 0.3
Global MouseLook_Y_Inc# = 0.3
Global Mouse_Left_Limit% = 250 * MenuScale, Mouse_Right_Limit% = opt\GraphicWidth - Mouse_Left_Limit
Global Mouse_Top_Limit% = 150 * MenuScale, Mouse_Bottom_Limit% = opt\GraphicHeight - Mouse_Top_Limit

Global Mouse_X_Speed_1#, Mouse_Y_Speed_1#

Global Mesh_MinX#, Mesh_MinY#, Mesh_MinZ#
Global Mesh_MaxX#, Mesh_MaxY#, Mesh_MaxZ#
Global Mesh_MagX#, Mesh_MagY#, Mesh_MagZ#

Global KEY_RIGHT = GetINIInt(gv\OptionFile, "binds", "Right key", 32)
Global KEY_LEFT = GetINIInt(gv\OptionFile, "binds", "Left key", 30)
Global KEY_UP = GetINIInt(gv\OptionFile, "binds", "Up key", 17)
Global KEY_DOWN = GetINIInt(gv\OptionFile, "binds", "Down key", 31)
Global KEY_BLINK = GetINIInt(gv\OptionFile, "binds", "Blink key", 57)
Global KEY_SPRINT = GetINIInt(gv\OptionFile, "binds", "Sprint key", 42)
Global KEY_INV = GetINIInt(gv\OptionFile, "binds", "Inventory key", 15)
Global KEY_CROUCH = GetINIInt(gv\OptionFile, "binds", "Crouch key", 29)
Global KEY_SAVE = GetINIInt(gv\OptionFile, "binds", "Save key", 63)
Global KEY_LOAD = GetINIInt(gv\OptionFile, "binds", "Load key", 65)
Global KEY_CONSOLE = GetINIInt(gv\OptionFile, "binds", "Console key", 61)
Global KEY_USE = GetINIInt(gv\OptionFile, "binds", "Use key", 18)
Global KEY_RADIOTOGGLE = GetINIInt(gv\OptionFile, "binds", "Radiotoggle key", 20)
Global KEY_RELOAD = GetINIInt(gv\OptionFile, "binds", "Reload key", 19)
Global KEY_HOLSTERGUN = GetINIInt(gv\OptionFile, "binds", "Holstergun key", 16)
Global KEY_CHANGEFIREMODE = GetINIInt(gv\OptionFile, "binds", "Firemode key", 48)
Global KEY_SELECTATTACHMENT = GetINIInt(gv\OptionFile, "binds", "Attachment key", 45)
Global KEY_SCRAMBLE = GetINIInt(gv\OptionFile, "binds", "Scramble key", 35)

;[End Block]

; ~ [PLAYER CONTROLS]

;[Block]

Global KillTimer#, KillAnim%, FallTimer#, DeathTimer#
Global Sanity#, ForceMove#, ForceAngle#
Global RestoreSanity%
Global Playable% = True
Global BLINKFREQ#
Global BlinkTimer#, EyeIrritation#, EyeStuck#, BlinkEffect# = 1.0, BlinkEffectTimer#
Global Stamina#, StaminaEffect#=1.0, StaminaEffectTimer#
Global CameraShakeTimer#, Vomit%, VomitTimer#, Regurgitate%
Global HeartBeatRate#, HeartBeatTimer#, HeartBeatVolume#
Global SuperMan%, SuperManTimer#
Global HealTimer#
Global RefinedItems%
Global CanPlayerUseGuns%=True

;[End Block]

; ~ [GAME]

;[Block]

gc\CurrGamemode = -1

;[End Block]

; ~ [PLAYER]

;[Block]

Global DropSpeed#, HeadDropSpeed#, CurrSpeed#
Global User_Camera_Pitch#, Side#
Global Crouch%, CrouchState#
Global PlayerZone%, PlayerRoom.Rooms
Global GrabbedEntity%
Global InvertMouse% = GetINIInt(gv\OptionFile, "options", "invert mouse y")
Global MouseHit1%, MouseDown1%, MouseHit2%, MouseDown2%, DoubleClick%, LastMouseHit1%, MouseUp1%, MouseHit3%
Global KeyHitUse,KeyDownUse
Global GodMode%, NoClip%, NoClipSpeed# = 2.0
Global CoffinDistance#
Global PlayerSoundVolume#

;[End Block]

; ~ [CAMERA EFFECTS]

;[Block]

Global Shake#
Global ExplosionTimer#
Global LightsOn% = True
Global SoundTransmission%

;[End Block]

; ~ [MENUs&GUI]

;[Block]

Global MainMenuOpen%, MenuOpen%, StopHidingTimer#, InvOpen%
Global OtherOpen.Items = Null
Global SelectedEnding$, EndingScreen%, EndingTimer#
Global KeypadInput$, KeypadTimer#, KeypadMSG$
Global DrawHandIcon%
Global DrawArrowIcon%[4]
Global AttachmentOpen%
Global AccessCode%[4]

;[End Block]

; ~ [MISC]

;[Block]

Const ClrR = 50, ClrG = 50, ClrB = 50

Global MTFtimer#, MTFrooms.Rooms[10], MTFroomState%[10]
Global CITimer#
Global RadioState#[9]
Global RadioState3%[7]
Global RadioState4%[9]
Global RadioCHN%[8]

Global PlayTime%
Global InfiniteStamina% = False
Global IsPlayerSprinting% = False
Global NVBlink%
Global IsNVGBlinking% = False
Global Contained106%, Contained173%, Contained457%
Global Contain173State% = 0
Global Contain173Timer# = 0.0

Global FOV% = GetINIInt(gv\OptionFile, "options", "fov", 60)
Global SaveTexturesInVRam = GetINIInt(gv\OptionFile,"options","enable vram",1)

Global SelectedElevatorFloor% = 0
Global CurrElevatorButtonTex%[3]
Global SelectedElevatorEvent
Global UpdateAlarmLight% = False
Global EntityMapLoading% = 0

Include "SourceCode\Achievements_Core.bb"
Include "SourceCode\Difficulty_Core.bb"

;[End Block]

; ~ [CONSOLE]

;[Block]

Global ConsoleOpen%, ConsoleInput$
Global ConsoleScroll#,ConsoleScrollDragging%
Global ConsoleMouseMem%
Global ConsoleReissue.ConsoleMsg = Null
Global ConsoleR% = 255,ConsoleG% = 255,ConsoleB% = 255
Global IsUsingAimCross% = False
Global ConsoleFlush%
Global ConsoleFlushSnd% = 0, ConsoleMusFlush% = 0, ConsoleMusPlay% = 0

;[End Block]

; ~ [CHEATS]

;[Block]

Type Cheats
	Field CDScream%
	Field Mini173%
	Field OwO%
End Type

Global Cheat.Cheats = New Cheats

;[End Block]

; ~ [MENU]

;[Block]

Global DebugHUD%
Global BlurVolume#, BlurTimer#
Global LightBlink#, LightFlash#
Global HUDenabled% = GetINIInt(gv\OptionFile, "options", "HUD enabled", 1)
Global Camera%, CameraShake#,BigCameraShake#, CurrCameraZoom#
Global Brightness% = GetINIFloat(gv\OptionFile, "options", "brightness", 20)
Global CameraFogNear# = GetINIFloat(gv\OptionFile, "options", "camera fog near", 0.5)
Global CameraFogFar# = GetINIFloat(gv\OptionFile, "options", "camera fog far", 6.0)
Global StoredCameraFogFar# = CameraFogFar
Global MouseSens# = GetINIFloat(gv\OptionFile, "options", "mouse sensitivity")

Include "SourceCode\Dream_Filter.bb"

;[End Block]

; ~ [SOUNDS]

Include "SourceCode\Sounds_Core.bb"

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

DrawLoading(10, True)

Global PrevMusicVolume# = opt\MusicVol
Global PrevSFXVolume# = opt\SFXVolume#

DrawLoading(20, True)

AmbientSFXAmount[0]=8 : AmbientSFXAmount[1]=11 : AmbientSFXAmount[2]=12
AmbientSFXAmount[3]=15 : AmbientSFXAmount[4]=5
AmbientSFXAmount[5]=10

DrawLoading(25, True)

DrawLoading(30, True)

;[Block]

Global IntercomStreamCHN%
Global ForestNPC,ForestNPCTex,ForestNPCData#[3]
Global ParticleEffect[10]
Global PlayCustomMusic% = False, CustomMusic% = 0
Global Monitor2, Monitor3, MonitorTexture2, MonitorTexture3, MonitorTexture4, MonitorTextureOff
Global MonitorTimer# = 0.0, MonitorTimer2# = 0.0, UpdateCheckpoint1%, UpdateCheckpoint2%
Global PlayerDetected%
Global NVGImages = LoadAnimImage("GFX\HUD\Battery.png",64,64,0,2)
MaskImage NVGImages,255,0,255
Global AmbientLightRoomTex%, AmbientLightRoomVal%
Global EnableUserTracks% = GetINIInt(gv\OptionFile, "audio", "enable user tracks")
Global UserTrackMode% = GetINIInt(gv\OptionFile, "audio", "user track setting")
Global UserTrackCheck% = 0, UserTrackCheck2% = 0
Global UserTrackMusicAmount% = 0, CurrUserTrack%, UserTrackFlag% = False
Global UserTrackName$[64]
Global OptionsMenu% = 0
Global QuitMSG% = 0
Global room2gw_brokendoor% = False
Global room2gw_x# = 0.0
Global room2gw_z# = 0.0
Global Menu_TestIMG
Global menuroomscale# = 8.0 / 2048.0
Global CurrMenu_TestIMG$ = ""
Global ParticleAmount% = GetINIInt(gv\OptionFile,"options","particle amount", 2)
Global LightConeModel

Global InFacility% = True
Global DeafPlayer% = False
Global DeafTimer# = 0.0
Global IsZombie% = False
Global UnableToMove% = False
Global ShouldEntitiesFall% = True
Global PlayerFallingPickDistance# = 10.0
Global BatMsgTimer#

;[End Block]

; ~ [IMAGES]

;[Block]

Global O5_Screen[3]
Global SCP_963_2_Screen[2]
Global Surveil_Room_Textures[2]
Global Checkpoint_Screen[3]

Global PauseMenuIMG%[2]
Global FineKevlarIcon%
Global Panel294, Using294%, Input294$
Global NavBG

;[End Block]

; ~ [ICONS]

;[Block]

Global PlayerIcons[9]
Global SCPIcons[4]

Global AttachmentIMG[16]
Global GunAttachmentPreviewIMG[14]

;[End Block]

; ~ [OVERLAYS]

;[Block]

Global Overlay[12]
Global OverlayTexture[12]

;[End Block]

DrawLoading(35, True)

; ~ [ITEMS]

Include "SourceCode\Items_Core.bb"

DrawLoading(40, True)

; ~ [PARTICLES]

Include "SourceCode\Particles_Core.bb"

Include "SourceCode\Rain.bb"

DrawLoading(45, True)

; ~ [DOOR]

Include "SourceCode\Doors_Core.bb"

DrawLoading(50, True)

; ~ [MATERIALS]

Include "SourceCode\Materials.bb"

Global GorePics%[6]

; ~ [MAP]

Include "SourceCode\Map_Core.bb"

DrawLoading(55, True)

LoadRoomTemplates("Data\Rooms.ini")

; ~ [MOD]

Include "SourceCode\Stuff.bb"

DrawLoading(60, True)

Include "SourceCode\Weapons_Core.bb"

DrawLoading(65, True)

Include "SourceCode\Texture_Cache.bb"

DrawLoading(70, True)

Include "SourceCode\Mission.bb"

DrawLoading(75, True)

; ~ [NPCs]

Include "SourceCode\NPCs_Core.bb"

DrawLoading(80, True)

; ~ [EVENTS]

Include "SourceCode\Events_Core.bb"

; ~ [COLLISIONS]

;[Block]

Const HIT_MAP% = 1
Const HIT_PLAYER% = 2
Const HIT_ITEM% = 3
Const HIT_APACHE% = 4
Const HIT_178% = 5
Const HIT_DEAD% = 6
Const HIT_PLAYER_MP% = 7
Const HIT_NPC_MP% = 8
Const HIT_GRENADE% = 9
Const HIT_RAIN% = 10

Collisions HIT_PLAYER, HIT_MAP, 2, 2
Collisions HIT_PLAYER, HIT_PLAYER, 1, 2
Collisions HIT_ITEM, HIT_MAP, 2, 2
Collisions HIT_GRENADE, HIT_MAP, 2, 2
Collisions HIT_APACHE, HIT_APACHE, 1, 2
Collisions HIT_178, HIT_MAP, 2, 2
Collisions HIT_178, HIT_178, 1, 3
Collisions HIT_DEAD, HIT_MAP, 2, 2
Collisions HIT_RAIN, HIT_MAP, 2, 2
; ~ [MULTIPLAYER]
Collisions HIT_PLAYER_MP, HIT_MAP, 2, 2
Collisions HIT_PLAYER_MP, HIT_NPC_MP, 1, 2
Collisions HIT_NPC_MP, HIT_MAP, 2, 2

;[End Block]

Type AudioControl
	Field MasterVol#
	Field MusicVol#
	Field VoiceVol#
	Field EnviromentVol#
End Type

DrawLoading(90, True)

; ~ [MESHES&TEXTURES]

;[Block]

Global Collider%, Head%

Global TeslaTexture%
Global LightSpriteTex%[4]
Global LeverOBJ%, LeverBaseOBJ%
Global Monitor%
Global CamBaseOBJ%, CamOBJ%
Global LiquidObj%
Global ApacheObj%,ApacheRotorObj%

;[End Block]

; ~ [MOD]

Include "SourceCode\Multiplayer\Multiplayer_Base.bb"

Include "SourceCode\Player_Core.bb"

Include "SourceCode\Menu_Core.bb"

Include "SourceCode\Menu_3D.bb"

Include "SourceCode\Console_Core.bb"

DrawLoading(95, True)

MainMenuOpen = True

InitConsole(2)

Load3DMenu()

FlushKeys()
FlushMouse()
FlushJoy()

DrawLoading(100, True)

If opt\PlayStartupVideos Then PlayStartupVideos()

CurrMusicVolume# = 0.01
fps\LoopDelay = MilliSecs()

Global CurrTrisAmount%

Global Input_ResetTime# = 0.0

;! ~ [TYPE INSTANCES]

;[Block]

; ~ [PLAYER]

Type WearibleItems
	Field GasMask%
	Field Hazmat%
	Field Vest%
	Field NightVision%
	Field NightVisionTimer#
	Field SCRAMBLE%
	Field Helmet%
End Type

Type HazardousDefenceSuit
	Field Wearing%
	Field BootUpTimer#
	Field Timer#
	Field ExplodeTimer#
	Field isBroken%
	Field Health#
	
	Field Sound[25]
	Field SoundCHN%
End Type

Type Messages
	Field Txt$
	Field HintTxt$
	Field HintTxt_Y#
	Field HintTimer#
	Field DeathTxt$
	Field Timer#
	Field isSplash%
	Field R#,G#,B#
End Type

Type SplashMsg
	Field Timer#
	Field Speed#
	Field ShowTime#
	Field CurrentLength%
	Field DisplayAmount#
	Field X#, Y#
	Field Txt$
	Field Centered%
	Field R#,G#,B#
End Type

; ~ [EVENTS&GAME]

Type ClassicMode
	Field GlobalMode%
	Field GuardMode%
	Field NTFMode%
	Field DMode%
End Type

Type Chapters
	Field Unlocked#
	Field Current#
End Type

Type EventConstants
	Field SuccessRocketLaunch%
	Field EzDoorOpened%
	Field WasInHCZ%
	Field NewCavesEvent%
	Field CIArrived%
	Field WasInRoom2_SL%
	Field WasInLCZCore%
	Field UnlockedGateDoors%
	Field KilledGuard%
	Field NTFArrived%
	Field WasInO5%
	Field WasIn076%
	Field After076Timer#
	Field WasInCaves%
	Field WasInO5Again%
	Field WasInReactor%
	Field WasInBCZ%
	Field Contained008%
	Field Contained409%
	
	Field UnlockedAirlock%
	
	Field UnlockedWolfnaya%
	Field ChanceToSpawnWolfNote%
	
	Field FusesAmount#
	
	Field UnlockedEMRP%
	Field UnlockedHDS%
	
	Field WasInLWS%
	Field WasInWS%
	Field WasInEWS%
	Field WasInHWS%
	Field WasInSWS%
	Field WasInAllSupplies%
	
	Field IntercomEnabled%
	Field IntercomIsReady%
	Field IntercomTimer#
	
	Field OmegaWarheadActivated%
	Field OmegaWarheadDetonate%
	Field OmegaWarheadTimer#
End Type

; ~ [SCPs]

Type SCP005
	Field ChanceToSpawn%
End Type

Type SCP008
	Field Timer#
End Type

Type SCP016
	Field Timer#
End Type

Type SCP035
	Field Possessed%
End Type

Type SCP059
	Field Timer#
	Field Using%
End Type

Type SCP109
    Field Timer#
    Field Used%
    Field Sound%[1]
	Field Vomit%
	Field VomitTimer#
End Type

Type SCP127
	Field RestoreTimer#
End Type

Type SCP198
	Field Timer#
	Field DeathTimer#
	Field Injuries#
	Field Vomit%
	Field VomitTimer#
End Type

Type SCP207
    Field Timer#
	Field DeathTimer#
	Field Factor%
	Field Limit%
End Type

Type SCP268
    Field Using%
    Field Timer#
    Field Sound%[2]
    Field SoundCHN%[1]
End Type

Type SCP330
	Field Taken#
	Field Timer#
End Type

Type SCP357
	Field Timer#
	Field Using%
End Type

Type SCP402
    Field Using%
    Field Timer#
End Type

Type SCP409
	Field Timer#
	Field Revert%
End Type 

Type SCP427
	Field Using%
	Field Timer#
	Field Sound[2]
	Field SoundCHN[2]
End Type

Type SCP500
    Field Limit%
End Type

Type SCP714
	Field Using%
End Type

Type SCP1025
	Field State#[6]
End Type

Type SCP1033RU
    Field HP%
    Field DHP%
    Field Using%
    Field Sound%[3]
    Field Sound2%[1]
End Type

Type SCP1079
    Field Foam#
    Field Trigger%
    Field Take%
    Field Limit%
End Type

Type SCP1102RU
	Field IsInside%
	Field State#
	Field Sound[1]
End Type

;[End Block]

InitErrorMsgs(9)
SetErrorMsg(0, GetLocalString("Errors","error_occured")+VersionNumber+Chr(10)+GetLocalString("Errors","save_compatible")+CompatibleNumber+GetLocalString("Errors","engine_version")+SystemProperty("blitzversion"))
SetErrorMsg(1, "OS: "+SystemProperty("os")+" "+gv\OSBit+" bit (Build: "+SystemProperty("osbuild")+")")
SetErrorMsg(2, "CPU: "+Trim(SystemProperty("cpuname"))+" (Arch: "+SystemProperty("cpuarch")+", "+GetEnv("NUMBER_OF_PROCESSORS")+" Threads)")
SetErrorMsg(8, Chr(10)+GetLocalString("Errors","take_screenshot"))

;!-----------------------------------------------------------------------------------------------------------------------------------------------------------------!
;------------------------------------------------------------   			[MAIN LOOP]                 ------------------------------------------------------------
;!-----------------------------------------------------------------------------------------------------------------------------------------------------------------!

GlobalGameLoop()
Steam_Shutdown()

Function GlobalGameLoop()
	Local ev.Events
	
	Repeat
		Local CurrDelta% = MilliSecs()
		
		SetErrorMsg(3, "GPU: "+GfxDriverName(CountGfxDrivers())+" ("+((TotalVidMem()/1024)-(AvailVidMem()/1024))+" MB/"+(TotalVidMem()/1024)+" MB)")
		SetErrorMsg(4, "Triangles rendered: "+CurrTrisAmount+", Active textures: "+ActiveTextures()+Chr(10))
		If gc\CurrGamemode=0 Then
			If PlayerRoom <> Null Then
				SetErrorMsg(5, "Map seed: "+RandomSeed + ", Room: " + PlayerRoom\RoomTemplate\Name+" (" + Floor(EntityX(PlayerRoom\obj) / 8.0 + 0.5) + ", " + Floor(EntityZ(PlayerRoom\obj) / 8.0 + 0.5) + ", angle: "+PlayerRoom\angle + ")")
				
				For ev.Events = Each Events
					If ev\room = PlayerRoom Then
						SetErrorMsg(6, "Room event: "+ev\EventName+" (" +ev\EventState[0]+", "+ev\EventState[1]+", "+ev\EventState[2]+")"+Chr(10))
						Exit
					EndIf
				Next
			EndIf
		ElseIf gc\CurrGamemode=3 Then
			SetErrorMsg(5, "Map: "+mp_I\MapInList\Name)
			SetErrorMsg(6, "Gamemode: "+mp_I\Gamemode\name+Chr(10))
		EndIf
		
		CatchErrors("Global main loop")
		Cls
		
		Local elapsedMilliseconds%
		SetCurrTime(MilliSecs())
		elapsedMilliseconds = GetCurrTime()-GetPrevTime()
		AddToTimingAccumulator(elapsedMilliseconds)
		SetPrevTime(GetCurrTime())
		
		If Framelimit > 0 Then
			Local WaitingTime% = (1000.0 / Framelimit) - (MilliSecs() - fps\LoopDelay)
			Delay WaitingTime%
			fps\LoopDelay = MilliSecs()
		EndIf
		
		fps\Factor[0] = GetTickDuration()
		fps\Factor[1] = fps\Factor[0]
		
		If MainMenuOpen Then
			MainLoopMenu()
		Else
			If gc\CurrGamemode<>3 Then
				MainLoop()
			Else
				If mp_I\PlayState=GAME_SERVER
					MPMainLoop()
				Else
					MPMainLoopClient()
				EndIf
			EndIf
		EndIf
		
		GammaUpdate()
		
		If opt\ShowFPS Then
			If ft\fpsgoal < MilliSecs() Then
				ft\fps = ft\tempfps
				ft\tempfps = 0
				ft\fpsgoal = MilliSecs() + 1000
			Else
				ft\tempfps = ft\tempfps + 1
			EndIf
		EndIf
		
		Steam_Update()
		UpdateRichPresence()
		
		Flip Vsync
		
		ft\DeltaTime = MilliSecs() - CurrDelta
		
		CatchErrors("Global main loop / uncaught")
	Forever
	
End Function

Function MainLoop()
	Local r.Rooms,e.Events
	Local i%,g.Guns
	
	While (ft\accumulator>0.0)
		ft\accumulator = ft\accumulator-GetTickDuration()
		If (ft\accumulator<=0.0) Then CaptureWorld()
		
		;If MenuOpen Lor OtherOpen <> Null Lor ConsoleOpen Lor SelectedScreen <> Null Lor Using294 Then fps\Factor[0] = 0
		If MenuOpen Lor ConsoleOpen Lor SelectedScreen <> Null Lor Using294 Then fps\Factor[0] = 0
		
		If d_I <> Null Then 
			If d_I\SelectedDoor <> Null Then
				fps\Factor[0] = 0
			EndIf
		EndIf	
		If mi_I\IsEnding Then fps\Factor[0] = 0
		
		If Input_ResetTime > 0 And fps\Factor[0] > 0.0 Then
			Input_ResetTime = Max(Input_ResetTime-fps\Factor[0],0.0)
		Else
			DoubleClick = False
			If (Not co\Enabled)
				MouseHit1 = MouseHit(1)
				If MouseHit1
					If MilliSecs() - LastMouseHit1 < 800 Then DoubleClick = True
					LastMouseHit1 = MilliSecs()
				EndIf
				Local prevmousedown1 = MouseDown1
				MouseDown1 = MouseDown(1)
				If prevmousedown1 = True And MouseDown1=False Then MouseUp1 = True Else MouseUp1 = False
				
				MouseHit2 = MouseHit(2)
				MouseDown2 = MouseDown(2)
				
				MouseHit3 = MouseHit(3)
				
				KeyHitUse = KeyHit(KEY_USE)
				KeyDownUse = KeyDown(KEY_USE)
			Else
				; ~ [CONTROLLER]
				MouseHit1 = JoyHit(CK_LMouse)
				If MouseHit1 Then
					If MilliSecs() - LastMouseHit1 < 800 Then DoubleClick = True
					LastMouseHit1 = MilliSecs()
				EndIf
				prevmousedown1 = MouseDown1
				MouseDown1 = JoyDown(CK_LMouse)
				If prevmousedown1 = True And MouseDown1=False Then MouseUp1 = True Else MouseUp1 = False
				MouseHit2 = JoyHit(CK_RMouse)
				MouseDown2 = JoyDown(CK_RMouse)
				MouseHit3 = JoyHit(CK_MMouse)
				KeyHitUse = JoyHit(CK_Use)
				KeyDownUse = JoyDown(CK_Use)
			EndIf
		EndIf
		
		If (Not KeyDownUse) And (Not KeyHitUse) Then GrabbedEntity = 0
		
		UpdateMusic()
		
		If opt\EnableSFXRelease Then AutoReleaseSounds()
		
		If mi_I\EndSFX <> 0.0
			If mi_I\EndingTimer = 0.0
				If mi_I\EndSFX_Vol > 0.0
					mi_I\EndSFX_Vol = Max(mi_I\EndSFX_Vol-0.01*fps\Factor[1],0)
					SetStreamVolume_Strict(mi_I\EndSFX,(opt\MusicVol)*mi_I\EndSFX_Vol)
				Else
					StopStream_Strict(mi_I\EndSFX)
					mi_I\EndSFX = 0
				EndIf
			EndIf
		EndIf
		
		UpdateStreamSounds()
		
		DrawHandIcon = False
		For i = 0 To 3
			DrawArrowIcon[i] = False
		Next
		
		RestoreSanity = True
		ShouldEntitiesFall = True
		
		ShouldUpdateWater = ""
		WaterRender_IgnoreObject = 0
		
		If fps\Factor[0] > 0 Then UpdateSecurityCams()
		
		If (Not MenuOpen) And (Not InvOpen) And (Not AttachmentOpen) And (OtherOpen=Null) And (d_I\SelectedDoor = Null) And (ConsoleOpen = False) And (Using294 = False) And (SelectedScreen = Null) And EndingTimer=>0 Then 
			If (Not PlayerInNewElevator) Then
				Select gc\CurrZone
					Case EZ
						ShouldPlay = MUS_EZ
					Case BCZ
						ShouldPlay = MUS_BCZ
					Case LCZ
						ShouldPlay = MUS_LCZ
					Case HCZ
						ShouldPlay = MUS_HCZ
					Case RCZ
						ShouldPlay = MUS_RCZ
				End Select
			EndIf
		EndIf
		
		If fps\Factor[0] < 0 Then
			co\PressedButton = JoyHit(CKM_Press)
			co\PressedNext = JoyDown(CKM_Next)
			co\PressedPrev = JoyDown(CKM_Prev)
			If co\PressedNext And co\PressedPrev
				co\PressedNext = False
				co\PressedPrev = False
			EndIf
		EndIf
		
		If PlayerRoom\RoomTemplate\Name <> "facility_reactor" And PlayerRoom\RoomTemplate\Name <> "pocketdimension" And PlayerRoom\RoomTemplate\Name <> "gate_a_topside" And PlayerRoom\RoomTemplate\Name <> "gate_a_road" And PlayerRoom\RoomTemplate\Name <> "gate_b_topside" And PlayerRoom\RoomTemplate\Name <> "gate_c_topside" And (Not MenuOpen) And (Not ConsoleOpen) And (Not AttachmentOpen) And (Not InvOpen) Then 
			
			If mi_I\EndingTimer = 0.0
				If PlayerRoom\RoomTemplate\Name <> "gate_a_intro" And PlayerRoom\RoomTemplate\Name <> "room1_intro" And PlayerRoom\RoomTemplate\Name <> "area_076" And PlayerRoom\RoomTemplate\Name <> "area_1102_ru"
					If Rand(1500) = 1 Then
						If NTF_AmbienceSFX=0
							For i = 0 To 5
								If AmbientSFX[i * 15 + CurrAmbientSFX]<>0 Then
									If ChannelPlaying(AmbientSFXCHN)=0 Then FreeSound_Strict AmbientSFX[i * 15 + CurrAmbientSFX] : AmbientSFX[i * 15 + CurrAmbientSFX] = 0
								EndIf			
							Next
						EndIf
						If ChannelPlaying(NTF_AmbienceCHN)=0 Then FreeSound_Strict NTF_AmbienceSFX : NTF_AmbienceSFX = 0
						
						PositionEntity (SoundEmitter, EntityX(Camera) + Rnd(-1.0, 1.0), EntityY(Camera) + Rnd(-1.0, 1.0), EntityZ(Camera) + Rnd(-1.0, 1.0))
						
						PlayerZone = gc\CurrZone - 1
						
						Select Rand(0,3)
							Case 0
								PlayerZone = 3
							Case 1
								PlayerZone = 6
							Case 2
								PlayerZone = 7
							Default
								PlayerZone = gc\CurrZone - 1
						End Select
						
						If PlayerRoom\RoomTemplate\Name = "gate_a_intro" Then 
							PlayerZone = 4
						ElseIf PlayerRoom\RoomTemplate\Name = "testroom_860"
							For e.Events = Each Events
								If e\EventName = "testroom_860"
									If e\EventState[0] = 1.0
										PlayerZone = 5
										PositionEntity (SoundEmitter, EntityX(SoundEmitter), 30.0, EntityZ(SoundEmitter))
									EndIf
									
									Exit
								EndIf
							Next
						ElseIf PlayerRoom\RoomTemplate\Name = "room2_maintenance"
							If EntityY(Collider)<-3500.0*RoomScale
								PlayerZone = 8
							EndIf
						EndIf
						
						If PlayerZone < 6
							CurrAmbientSFX = Rand(0,AmbientSFXAmount[PlayerZone]-1)
						ElseIf PlayerZone=6
							CurrAmbientSFX = Rand(0,NTF_MaxAmbienceSFX-1)
						ElseIf PlayerZone = 7
							Select gc\CurrZone
								Case LCZ
									If PlayerRoom\RoomTemplate\Name <> "room1_intro" Then
										CurrAmbientSFX = Rand(0,NTF_MaxLCZAmbience-1)
									EndIf
								Case HCZ
									CurrAmbientSFX = Rand(0,NTF_MaxHCZAmbience-1)
								Case EZ
									CurrAmbientSFX = Rand(0,NTF_MaxEZAmbience-1)
								Case RCZ
									CurrAmbientSFX = Rand(0,NTF_MaxHCZAmbience-1)
								Case BCZ
									CurrAmbientSFX = Rand(0,NTF_MaxLCZAmbience-1)
							End Select
						Else
							CurrAmbientSFX = Rand(0,NTF_MaxSewerAmbienceSFX-1)
						EndIf
						
						Select PlayerZone
							Case 0,1,2
								;If AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=0 Then AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=LoadSound_Strict("SFX\Ambient\Zone"+(PlayerZone+1)+"\ambient"+(CurrAmbientSFX+1)+".ogg")
							Case 3
								;If AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=0 Then AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=LoadSound_Strict("SFX\Ambient\General\ambient"+(CurrAmbientSFX+1)+".ogg")
							Case 4
								;If AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=0 Then AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=LoadSound_Strict("SFX\Ambient\Pre-breach\ambient"+(CurrAmbientSFX+1)+".ogg")
							Case 5
								;If AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=0 Then AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=LoadSound_Strict("SFX\Ambient\Forest\ambient"+(CurrAmbientSFX+1)+".ogg")
							Case 6
								If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\"+NTF_AmbienceStrings[CurrAmbientSFX]+".ogg")
							Case 7
								Select gc\CurrZone
									Case LCZ
										If PlayerRoom\RoomTemplate\Name <> "room1_intro" Then
											If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\LCZ\"+NTF_LCZAmbienceStrings[CurrAmbientSFX]+".ogg")
										EndIf
									Case HCZ
										If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\HCZ\"+NTF_HCZAmbienceStrings[CurrAmbientSFX]+".ogg")
									Case EZ
										If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\EZ\"+NTF_EZAmbienceStrings[CurrAmbientSFX]+".ogg")
									Case RCZ
										If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\HCZ\"+NTF_HCZAmbienceStrings[CurrAmbientSFX]+".ogg")
									Case BCZ
										If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\LCZ\"+NTF_LCZAmbienceStrings[CurrAmbientSFX]+".ogg")
								End Select
							Case 8
								;If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\rooms\sewers\"+NTF_SewerAmbienceStrings[CurrAmbientSFX]+".ogg")
						End Select
						
						If PlayerZone < 6
							AmbientSFXCHN = PlaySound2(AmbientSFX[PlayerZone * 15 + CurrAmbientSFX], Camera, SoundEmitter)
						Else
							NTF_AmbienceCHN = PlaySound2(NTF_AmbienceSFX, Camera, SoundEmitter)
						EndIf
						UpdateSoundOrigin(AmbientSFXCHN,Camera, SoundEmitter)
						UpdateSoundOrigin(NTF_AmbienceCHN,Camera, SoundEmitter)
					EndIf
					
					If Rand(50000) = 3 Then
						Local RN$ = PlayerRoom\RoomTemplate\Name$
						If (Not IsPlayerOutside()) Then
							If RN$ <> "testroom_860" And RN$ <> "room1_intro" Then
								If fps\Factor[0] > 0 Then LightBlink = Rnd(1.0,2.0)
								PlaySound_Strict  LoadTempSound("SFX\SCP\079\Broadcast"+Rand(1,7)+".ogg")
							EndIf
						EndIf
					EndIf
					
				ElseIf PlayerRoom\RoomTemplate\Name = "gate_a_intro" Then
					If Rand(1500) = 1 Then
						If ChannelPlaying(NTF_AmbienceCHN)=0 Then FreeSound_Strict NTF_AmbienceSFX : NTF_AmbienceSFX = 0
						
						PositionEntity (SoundEmitter, EntityX(Camera) + Rnd(-1.0, 1.0), 0.0, EntityZ(Camera) + Rnd(-1.0, 1.0))
						
						CurrAmbientSFX = Rand(0,NTF_MaxIntroAmbienceSFX-1)
						
						If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\Intro\"+NTF_IntroAmbienceStrings[CurrAmbientSFX]+".ogg")
						
						NTF_AmbienceCHN = PlaySound2(NTF_AmbienceSFX, Camera, SoundEmitter)
					EndIf
					UpdateSoundOrigin(NTF_AmbienceCHN,Camera, SoundEmitter)
				ElseIf PlayerRoom\RoomTemplate\Name = "gate_c_topside" Then
					If Rand(1500) = 1 Then
						If ChannelPlaying(NTF_AmbienceCHN)=0 Then FreeSound_Strict NTF_AmbienceSFX : NTF_AmbienceSFX = 0
						
						PositionEntity (SoundEmitter, EntityX(Camera) + Rnd(-1.0, 1.0), 0.0, EntityZ(Camera) + Rnd(-1.0, 1.0))
						
						CurrAmbientSFX = Rand(0,NTF_MaxSurfaceAmbienceSFX-1)
						
						If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\Surface\"+NTF_SurfaceAmbienceStrings[CurrAmbientSFX]+".ogg")
						
						NTF_AmbienceCHN = PlaySound2(NTF_AmbienceSFX, Camera, SoundEmitter)
					EndIf
					UpdateSoundOrigin(NTF_AmbienceCHN,Camera, SoundEmitter)
				EndIf
			EndIf
		EndIf
		
		UpdateCheckpoint1 = False
		UpdateCheckpoint2 = False
		
		If (Not MenuOpen) And (Not AttachmentOpen) And (Not InvOpen) And (OtherOpen=Null) And (d_I\SelectedDoor = Null) And (ConsoleOpen = False) And (Using294 = False) And (SelectedScreen = Null) And EndingTimer=>0 And (Not mi_I\IsEnding) Then
			LightVolume = CurveValue(TempLightVolume, LightVolume, 50.0)
			CameraFogRange(Camera, CameraFogNear*LightVolume,CameraFogFar*LightVolume)
			CameraFogMode Camera,1
			CameraRange(Camera, 0.01, Min(CameraFogFar*LightVolume*1.5,28))
			For r.Rooms = Each Rooms
				For i = 0 To r\MaxLights%
					If r\Lights%[i]<>0
						EntityAutoFade r\LightSprites%[i],CameraFogNear*LightVolume,CameraFogFar*LightVolume
					EndIf
				Next
			Next
			
			AmbientLight Brightness, Brightness, Brightness	
			PlayerSoundVolume = CurveValue(0.0, PlayerSoundVolume, 5.0)
			
			If mpl\HasNTFGasmask Then
				ShowEntity Overlay[2]
			Else
				HideEntity Overlay[2]
			EndIf
			If wbi\Helmet Then
				ShowEntity Overlay[3]
			ElseIf (Not wbi\Helmet) Then
				HideEntity Overlay[3]
			EndIf
			If hds\Wearing Then
				ShowEntity Overlay[4]
			Else
				HideEntity Overlay[4]
			EndIf
			
			CanSave% = True
			For g = Each Guns
				If opt\RenderScope Then
					If IsSPPlayerAlive() And g\HasAcog Then
						UpdateScope()
					EndIf
				EndIf
			Next
			If psp\Health = 0 And KillTimer >= 0 Then
				Kill()
			EndIf
			UpdateDeafPlayer()
			UpdateEmitters()
			UpdateNewElevators()
			MouseLook()
			MovePlayer()
			UpdatePlayerModel()
			UpdateNightVision()
			InFacility = CheckForPlayerInFacility()
			UpdateDoors()
			UpdateScreens()
			Update109()
			Update127()
			Update198()
			Update207()
			Update268()
			Update294()
			Update357()
			Update402()
			If QuickLoadPercent = -1 Lor QuickLoadPercent = 100
				UpdateEvents()
			EndIf
			UpdateRoomLights(Camera)
			UpdateFluLights()
			UpdateFuseBoxes()
			UpdateFuseBoxes2()
			If ShouldUpdateWater<>"" Then
				UpdateWater(ShouldUpdateWater)
			EndIf
			If KillTimer >= 0 Then
				If CanPlayerUseGuns Then
					UpdateGuns()
					AnimateGuns()
					UpdateIronSight()
					For g = Each Guns
						If g\HasLaserSight Then
							UpdateLaserSight()
						EndIf
					Next
				EndIf
				UpdateGrenades()
				UpdateGrenadeLauncherGrenades()
			Else
				HideEntity g_I\GunPivot
			EndIf
			UpdateDecals()
			UpdatePlayerDialogue()
			UpdateMTF()
			UpdateCI()
			UpdateEventValues()
			UpdateNPCs()
			UpdateItems()
			UpdateParticles()
			UpdateDust()
			UpdateDamageOverlay()
			UpdateIntercomSystem()
			UpdateOmegaWarhead()
		Else
			KeyHitUse = False
			KeyDownUse = False
		EndIf
		If AttachmentOpen Then
			UpdateAttachments()
		EndIf
		If hds\Wearing Then
			UpdateHazardousDefenceSuit()
		EndIf
		
		Local CurrFogColor$ = ""
		
		If PlayerRoom <> Null Then
			If PlayerRoom\RoomTemplate\Name = "room2_maintenance" And EntityY(Collider)<-3500.0*RoomScale Then
				CurrFogColor = FogColor_Sewers
			ElseIf PlayerRoom\RoomTemplate\Name = "gate_a_topside" Lor PlayerRoom\RoomTemplate\Name = "gate_a_intro" Then
				CurrFogColor = FogColor_Outside
			ElseIf PlayerRoom\RoomTemplate\Name = "pocketdimension" Then
				CurrFogColor = FogColor_PD
			ElseIf PlayerRoom\RoomTemplate\Name = "testroom_860" Then
				If IsInsideForest Then
					If Is860_Chasing Then
						CurrFogColor = FogColor_Forest
					Else
						CurrFogColor = FogColor_Forest_2
					EndIf
				Else
					CurrFogColor = FogColor_LCZ
				EndIf
			EndIf
		EndIf
		
		If CurrFogColor = "" Then
			Select gc\CurrZone
				Case LCZ
					CurrFogColor = FogColor_LCZ
				Case HCZ
					CurrFogColor = FogColor_HCZ
				Case EZ
					CurrFogColor = FogColor_EZ
				Case RCZ
					CurrFogColor = FogColor_RCZ
				Case BCZ
					CurrFogColor = FogColor_BCZ
				Case AREA_076
					CurrFogColor = FogColor_Area_076
				Case AREA_106_ESCAPE
					CurrFogColor = FogColor_Area_106_Escape
			End Select
		EndIf
		
		Local FogColorR% = Left(CurrFogColor,3)
		Local FogColorG% = Mid(CurrFogColor,4,3)
		Local FogColorB% = Right(CurrFogColor,3)
		CameraFogColor Camera,FogColorR,FogColorG,FogColorB
		CameraClsColor Camera,FogColorR,FogColorG,FogColorB
		
		If InfiniteStamina% Then Stamina = Min(100, Stamina + (100.0-Stamina)*0.01*fps\Factor[0])
		
		If gc\CurrGamemode=1
			UpdateMissionEvents()
			UpdateMissionEnding()
		EndIf
		If fps\Factor[0]=0
			UpdateWorld(0)
		Else
			UpdateWorld()
			ManipulateNPCBones()
		EndIf
		
		BlurVolume = Min(CurveValue(0.0, BlurVolume, 20.0),0.95)
		If BlurTimer > 0.0 Then
			BlurVolume = Max(Min(0.95, BlurTimer / 1000.0), BlurVolume)
			BlurTimer = Max(BlurTimer - fps\Factor[0], 0.0)
		EndIf
		
		;[Block]
		Local darkA# = 0.0
		If (Not MenuOpen)  Then
			If Sanity < 0 Then
				If RestoreSanity Then Sanity = Min(Sanity + fps\Factor[0], 0.0)
				If Sanity < (-200) Then 
					darkA = Max(Min((-Sanity - 200) / 700.0, 0.6), darkA)
					If KillTimer => 0 Then 
						HeartBeatVolume = Min(Abs(Sanity+200)/500.0,1.0)
						HeartBeatRate = Max(70 + Abs(Sanity+200)/6.0,HeartBeatRate)
					EndIf
				EndIf
			End If
			
			If EyeStuck > 0 Then 
				BlinkTimer = BLINKFREQ
				EyeStuck = Max(EyeStuck-fps\Factor[0],0)
				
				If EyeStuck < 9000 Then BlurTimer = Max(BlurTimer, (9000-EyeStuck)*0.5)
				If EyeStuck < 6000 Then darkA = Min(Max(darkA, (6000-EyeStuck)/5000.0),1.0)
				If EyeStuck < 9000 And EyeStuck+fps\Factor[0] =>9000 Then 
					CreateMsg(GetLocalString("Items","eyedrops_tear"))
				EndIf
			EndIf
			
			If BlinkTimer < 0 Then
				If BlinkTimer > - 5 Then
					darkA = Max(darkA, Sin(Abs(BlinkTimer * 18.0)))
				ElseIf BlinkTimer > - 15
					darkA = 1.0
				Else
					darkA = Max(darkA, Abs(Sin(BlinkTimer * 18.0)))
				EndIf
				
				If BlinkTimer <= - 20 Then
					Select SelectedDifficulty\OtherFactors
						Case EASY
							BLINKFREQ = Rnd(490,700)
						Case NORMAL
							BLINKFREQ = Rnd(455,665)
						Case HARD
							BLINKFREQ = Rnd(420,630)
						Case HARDER
							BLINKFREQ = Rnd(385,595)
						Case IMPOSSIBLE
							BLINKFREQ = Rnd(350,555)
					End Select 
					BlinkTimer = BLINKFREQ
				EndIf
				
				BlinkTimer = BlinkTimer - fps\Factor[0]
			Else
				BlinkTimer = BlinkTimer - (fps\Factor[0] * 0.6 * BlinkEffect)
				If wbi\NightVision = 0 And wbi\SCRAMBLE = 0 Then
					If EyeIrritation > 0.0 Then BlinkTimer = BlinkTimer - Min((EyeIrritation / 100.0) + 1.0, 4.0) * fps\Factor[0]
				EndIf
			EndIf
			
			If BlinkEffectTimer > 0 Then
				BlinkEffectTimer = BlinkEffectTimer - (fps\Factor[0]/70)
			Else
				If BlinkEffect <> 1.0 Then BlinkEffect = 1.0
			EndIf
			
			LightBlink = Max(LightBlink - (fps\Factor[0] / 35.0), 0)
			If LightBlink > 0 Then darkA = Min(Max(darkA, LightBlink * Rnd(0.3, 0.8)), 1.0)
			
			If Using294 Then darkA=1.0
			
			If (Not mpl\NightVisionEnabled) And (Not wbi\NightVision) And (Not wbi\SCRAMBLE) Then
				darkA = Max((1.0-SecondaryLightOn)*0.9, darkA)
			EndIf
			
			If KillTimer < 0 Then
				InvOpen = False
				AttachmentOpen = False
				SelectedItem = Null
				SelectedScreen = Null
				SelectedMonitor = Null
				BlurTimer = Abs(KillTimer*5)
				KillTimer=KillTimer-(fps\Factor[0]*0.8)
				If KillTimer < - 360 And (gc\CurrGamemode<>1) Then 
					MenuOpen = True
				EndIf
				darkA = Max(darkA, Min(Abs(KillTimer / 400.0), 1.0))
			EndIf
			
			If SelectedEnding <> "" Then
				EndingTimer = EndingTimer - (fps\Factor[0]*0.8)
			EndIf
			
			If FallTimer < 0 Then
				InvOpen = False
				AttachmentOpen = False
				SelectedItem = Null
				SelectedScreen = Null
				SelectedMonitor = Null
				BlurTimer = Abs(FallTimer*10)
				FallTimer = FallTimer-fps\Factor[0]
				darkA = Max(darkA, Min(Abs(FallTimer / 400.0), 1.0))				
			EndIf
			
			If SelectedScreen <> Null Then darkA = Max(darkA, 0.5)
			
			EntityAlpha(Overlay[10], darkA)	
		EndIf
		
		If LightFlash > 0 Then
			ShowEntity Overlay[11]
			EntityAlpha(Overlay[11], Max(Min(LightFlash + Rnd(-0.2, 0.2), 1.0), 0.0))
			LightFlash = Max(LightFlash - (fps\Factor[0] / 70.0), 0)
		Else
			HideEntity Overlay[11]
		EndIf
		;[End block]
		
		; ~ [CONTROLLER]
		
		If InteractHit(KEY_INV,CK_Inv) And I_330\Taken < 3 And VomitTimer >= 0 And (Not MenuOpen) Then 
			If InvOpen Then
				MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
			EndIf
			InvOpen = Not InvOpen
			If OtherOpen<>Null Then OtherOpen=Null
			SelectedItem = Null 
		EndIf
		If KeyHit(KEY_SELECTATTACHMENT) And (Not ConsoleOpen) And (Not InvOpen) Then
			If g_I\HoldingGun > 0 Then
				If AttachmentOpen Then
					ResumeSounds()
				Else
					PauseSounds()
				EndIf
				AttachmentOpen = Not AttachmentOpen
				FlushKeys()
			EndIf
		EndIf
		
		UpdateWorld2()
		
		UpdateGUI()
		
		If gc\CurrGamemode<>1
			If InteractHit(KEY_SAVE,CK_Save) Then
				If SelectedDifficulty\SaveType = SAVEANYWHERE Then
					RN$ = PlayerRoom\RoomTemplate\Name$
					If RN$ = "173" Lor (RN$ = "exit1" And EntityY(Collider)>1040.0*RoomScale) Lor RN$ = "gatea"
						CreateHintMsg(GetLocalString("Singleplayer","cant_save_here"))
					ElseIf (Not CanSave) Lor QuickLoadPercent > -1
						CreateHintMsg(GetLocalString("Singleplayer","cant_save_now"))
						If QuickLoadPercent > -1
							m_msg\HintTxt = m_msg\HintTxt + " (game is loading)"
						EndIf
					Else
						SaveGame(SavePath + CurrSave\Name + "\")
					EndIf
				ElseIf SelectedDifficulty\SaveType = SAVEONSCREENS
					If SelectedScreen=Null And SelectedMonitor=Null Then
						CreateHintMsg(GetLocalString("Singleplayer","cant_save_here"))
					Else
						RN$ = PlayerRoom\RoomTemplate\Name$
						If RN$ = "173" Lor (RN$ = "exit1" And EntityY(Collider)>1040.0*RoomScale) Lor RN$ = "gatea"
							CreateHintMsg(GetLocalString("Singleplayer","cant_save_here"))
						ElseIf (Not CanSave) Lor QuickLoadPercent > -1
							CreateHintMsg(GetLocalString("Singleplayer","cant_save_now"))
							If QuickLoadPercent > -1
								m_msg\HintTxt = m_msg\HintTxt + GetLocalString("Singleplayer","save_loading")
							EndIf
						Else
							If SelectedScreen<>Null
								GameSaved = False
								Playable = True
								DropSpeed = 0
							EndIf
							SaveGame(SavePath + CurrSave\Name + "\")
						EndIf
					EndIf
				Else
					CreateHintMsg(GetLocalString("Singleplayer","cant_save"))
				EndIf
			Else If SelectedDifficulty\SaveType = SAVEONSCREENS And (SelectedScreen<>Null Lor SelectedMonitor<>Null)
				If (m_msg\HintTxt<>GetLocalString("Menu","progress_saved") And m_msg\HintTxt<>GetLocalString("Singleplayer","cant_save_here") And m_msg\HintTxt<>GetLocalString("Singleplayer","cant_save_now")) Lor m_msg\HintTimer<=0
					CreateHintMsg(GetLocalStringR("Singleplayer","press_to_save",KeyName[KEY_SAVE]))
				EndIf
				
				If MouseHit2 Then SelectedMonitor = Null
			EndIf
			
			If KeyHit(KEY_LOAD) Then
				If GameSaved And (Not SelectedDifficulty\PermaDeath) Then
					
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
						Local x = Abs(EntityX(Collider) - EntityX(r\obj))
						Local z = Abs(EntityZ(Collider) - EntityZ(r\obj))
						
						If x < 12.0 And z < 12.0 Then
							MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)] = Max(MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)], 1)
							If x < 4.0 And z < 4.0 Then
								If Abs(EntityY(Collider) - EntityY(r\obj)) < 1.5 Then PlayerRoom = r
								MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)] = 1
							EndIf
						EndIf
					Next
					
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
			
			If KeyHit(KEY_CONSOLE) Then
				If opt\ConsoleEnabled
					If ConsoleOpen Then
						UsedConsole = True
						ResumeSounds()
						MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
					Else
						PauseSounds()
					EndIf
					ConsoleOpen = (Not ConsoleOpen)
					FlushKeys()
				EndIf
			EndIf
		EndIf
		
		If EndingTimer < 0
			If SelectedEnding <> "" Then UpdateEnding()
		Else
			If mi_I\EndingTimer=0.0 Then UpdateMenu()
		EndIf
		If mi_I\EndingTimer > 0.0
			InvOpen = False
			AttachmentOpen = False
			SelectedItem = Null
			SelectedScreen = Null
			SelectedMonitor = Null
			MenuOpen = False
			ShouldPlay = MUS_NULL
		EndIf
		
		If m_msg\Timer > 0
			m_msg\Timer = m_msg\Timer - fps\Factor[1]
		EndIf
		
		UpdateAchievementMsg()
		UpdateHintMsg()
	Wend
	
	If MainMenuOpen Then Return
	
	If ShouldUpdateWater<>"" Then
		RenderWater(ShouldUpdateWater)
	EndIf
	
	RenderMainLoop()
	
	UpdateConsole(1)
	
	UpdateMsg()
	
	Color 255, 255, 255
	SetFont fo\ConsoleFont
	If opt\ShowFPS Then
		Text 20, 20, "FPS: " + ft\fps : SetFont fo\Font[Font_Default]
	EndIf
	If InvOpen Then
		If clm\NTFMode Then
			Color 0,19,200
			TextWithAlign 1650, -10, GetLocalString("Menu","selected_gamemode")+GetLocalString("Menu","gm_ntf"),2
		ElseIf clm\GuardMode Then
			Color 170,170,170
			TextWithAlign 1650, -10, GetLocalString("Menu","selected_gamemode")+GetLocalString("Menu","gm_guard"),2
		ElseIf clm\DMode Then
			Color 255,106,0
			TextWithAlign 1650, -10, GetLocalString("Menu","selected_gamemode")+GetLocalStringR("Menu","gm_d",ClassDNumber),2
		EndIf
	EndIf
	
End Function

Function RenderMainLoop%()
	Local g.Guns
	
	If fps\Factor[0] > 0 Then RenderSecurityCams()
	
	For g = Each Guns
		If opt\RenderScope Then
			If IsSPPlayerAlive() And g\HasAcog Then
				RenderScope()
			EndIf
		EndIf
	Next
	
	RenderWorld2(Max(0.0,1.0+(ft\accumulator/ft\tickDuration)))
	
	UpdateBlur(BlurVolume)
	
	DrawGUI()
	
	If EndingTimer < 0 Then
		If SelectedEnding <> "" Then DrawEnding()
	Else
		If mi_I\EndingTimer=0.0 Then DrawMenu()
	EndIf
	DrawMissionEnding()
	
	DrawQuickLoading()
	
	RenderAchievementMsg()
	DrawHintMSG()
	
End Function

;----------------------------------------------------------------------------------------------------------------------------------------------------
;----------------------------------------------------------------------------------------------------------------------------------------------------
;----------------------------------------------------------------------------------------------------------------------------------------------------

Function Kill()
	If GodMode Then Return
	
	psp\Health = 0
	
	If BreathCHN <> 0 Then
		If ChannelPlaying(BreathCHN) Then StopChannel(BreathCHN)
	EndIf
	
	If KillTimer >= 0 Then
		KillAnim = Rand(0,1)
		PlaySound_Strict(DamageSFX[0])
		If SelectedDifficulty\PermaDeath Then
			DeleteGame(CurrSave)
			LoadSaveGames()
		EndIf
		
		KillTimer = Min(-1, KillTimer)
		ShowEntity Head
		PositionEntity(Head, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True), True)
		ResetEntity (Head)
		RotateEntity(Head, 0, EntityYaw(Camera), 0)		
	EndIf
	
End Function

Function DrawEnding()
	Local i
	
	ShowPointer()
	
	Local x,y,width,height, temp
	Local itt.ItemTemplates, r.Rooms
	
	ClsColor 0,0,0
	
	Cls
	
	If EndingTimer<-200 Then
		If EndingTimer > -700 Then
			If Rand(1,150)<Min((Abs(EndingTimer)-200),155) Then
				DrawImage EndingScreen, opt\GraphicWidth/2-400, opt\GraphicHeight/2-400
			Else
				Color 0,0,0
				Rect 100,100,opt\GraphicWidth-200,opt\GraphicHeight-200
				Color 255,255,255
			EndIf
			
		Else
			If EndingTimer > -2000 Then
				DrawImage EndingScreen, opt\GraphicWidth/2-400, opt\GraphicHeight/2-400
			EndIf
			
			If EndingTimer < -1000 And EndingTimer > -2000 Then
				width = ImageWidth(PauseMenuIMG[1])
				height = ImageHeight(PauseMenuIMG[1])
				x = opt\GraphicWidth / 2 - width / 2
				y = opt\GraphicHeight / 2 - height / 2
				
				DrawImage PauseMenuIMG[1], x, y
				
				Color(255, 255, 255)
				
				SetFont fo\Font[Font_Menu]
				
				Text(x + width / 2, y + 20*MenuScale, GetLocalString("Menu","the_end"), True)
				SetFont fo\Font[Font_Default]
				
				If AchievementsMenu=0 Then 
					x = x+42*MenuScale
					y = y+122*MenuScale
					
					Local roomamount = 0, roomsfound = 0
					For r.Rooms = Each Rooms
						roomamount = roomamount + 1
						roomsfound = roomsfound + r\found
					Next
					
					Local achievementsUnlocked =0
					For i = 0 To MAXACHIEVEMENTS-1
						achievementsUnlocked = achievementsUnlocked + achv\Achievement[i]
					Next
					
					Local secretsfound = 0
					
					If IsVaneCoinDropped Then secretsfound = secretsfound + 1
					
					Text x+100*MenuScale, y+20*MenuScale, GetLocalString("Menu","achievements_unlocked") + achievementsUnlocked+"/"+MAXACHIEVEMENTS
					Text x+100*MenuScale, y+50*MenuScale, GetLocalString("Menu","rooms_found") + roomsfound
					Text x+100*MenuScale, y+80*MenuScale, GetLocalString("Menu","secrets_found") + secretsfound
					
					x = opt\GraphicWidth / 2 - width / 2
					y = opt\GraphicHeight / 2 - height / 2
					x = x+width/2
					y = y+height-100*MenuScale
					
				Else
					DrawMenu()
				EndIf
				
			; ~ Credits
				
			ElseIf EndingTimer<=-2000 Then
				DrawCredits()
			EndIf
			
		EndIf
		
	EndIf
	
	SetFont fo\Font[Font_Default]
	
	DrawAllMenuButtons()
	
End Function

Function UpdateEnding()
	Local i
	
	fps\Factor[0] = 0
	If EndingTimer>-2000
		EndingTimer=Max(EndingTimer-fps\Factor[1],-1111)
	Else
		EndingTimer=EndingTimer-fps\Factor[1]*(opt\GraphicHeight/800.0)
	EndIf
	
	GiveAchievement(Achv055)
	If (Not UsedConsole) Then GiveAchievement(AchvConsole)
	If SelectedDifficulty\Name = "Keter" Then GiveAchievement(AchvKeter)
	If SelectedDifficulty\Name = "Thaumiel" Then 
		GiveAchievement(AchvThaumiel)
		GiveAchievement(AchvKeter)
	EndIf
	If SelectedDifficulty\Name = "Appolyon" Then
		GiveAchievement(AchvAppolyon)
		GiveAchievement(AchvThaumiel)
		GiveAchievement(AchvKeter)
	EndIf
	Local x,y,width,height, temp
	Local itt.ItemTemplates, r.Rooms
	
	ShouldPlay = MUS_NULL
	
	If EndingTimer<-200 Then
		
		If BreathCHN <> 0 Then
			If ChannelPlaying(BreathCHN) Then StopChannel BreathCHN : Stamina = 100
		EndIf
		If EndingScreen = 0 Then
			EndingScreen = LoadImage_Strict("GFX\HUD\ending_screen.png")
			PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Bells_far.ogg"))
			PlaySound_Strict LightSFX
		EndIf
		
		If EndingTimer > -700 Then
;			If EndingTimer+fps\Factor[1] > -450 And EndingTimer <= -450 Then
;				Select Lower(SelectedEnding)
;					Case "a1", "a2"
;						PlaySound_Strict LoadTempSound("SFX\Ending\GateA\Ending"+SelectedEnding+".ogg")
;					Case "b1", "b2", "b3"
;						PlaySound_Strict LoadTempSound("SFX\Ending\GateB\Ending"+SelectedEnding+".ogg")
;					Case "c1", "c2"
;						PlaySound_Strict LoadTempSound("SFX\Ending\GateC\Ending"+SelectedEnding+".ogg")
;					Case "d1"
;						PlaySound_Strict LoadTempSound("SFX\Ending\GateD\Ending"+SelectedEnding+".ogg")
;				End Select
;			EndIf			
		Else
			
			If EndingTimer < -1000 And EndingTimer > -2000
				
				width = ImageWidth(PauseMenuIMG[1])
				height = ImageHeight(PauseMenuIMG[1])
				x = opt\GraphicWidth / 2 - width / 2
				y = opt\GraphicHeight / 2 - height / 2
				
				If AchievementsMenu=0 Then 
					x = x+132*MenuScale
					y = y+122*MenuScale
					
					x = opt\GraphicWidth / 2 - width / 2
					y = opt\GraphicHeight / 2 - height / 2
					x = x+width/2
					y = y+height-100*MenuScale
					
					Local achievementsUnlocked =0
					For i = 0 To MAXACHIEVEMENTS-1
						achievementsUnlocked = achievementsUnlocked + achv\Achievement[i]
					Next
					
					If DrawButton(x-175*MenuScale,y-150*MenuScale,390*MenuScale,60*MenuScale,GetLocalString("Menu","main_menu"), True)
						LoadCredits()
					EndIf
				Else
					UpdateMenu()
				EndIf
			ElseIf EndingTimer<=-2000
				UpdateCredits()
			EndIf
		EndIf
	EndIf
	
End Function

Type CreditsLine
	Field txt$
	Field id%
	Field stay%
End Type

Const MaxCreditScreens% = 3

Global CreditsTimer# = 0.0
Global CreditsScreen[MaxCreditScreens]
Global CreditsScreenCurrent% = 0
Global CreditsScreenNext% = 0

Function LoadCredits()
	Local i%
	
	If SelectedEnding = "scp-035" Then
		ShouldPlay = MUS_TURN_AROUND
	ElseIf SelectedEnding = "scp-2935" Then
		ShouldPlay = MUS_SCP_2935
	ElseIf SelectedEnding = "c1" Then
		ShouldPlay = MUS_GOOD_KARMA
	ElseIf SelectedEnding = "d1" Then
		ShouldPlay = MUS_CYBER_CHASE_3
	Else
		ShouldPlay = MUS_CREDITS
	EndIf
	
	NowPlaying = ShouldPlay
	For i = 0 To 9
		If TempSounds[i] <> 0 Then FreeSound_Strict TempSounds[i] : TempSounds[i]=0
	Next
	StopStream_Strict(MusicCHN)
	MusicCHN = StreamSound_Strict("SFX\Music\"+Music[NowPlaying]+".ogg",0.0,Mode)
	SetStreamVolume_Strict(MusicCHN,1.0*aud\MusicVol)
	FlushKeys()
	EndingTimer=-2000
	
	DeleteMenuGadgets()
	InitCredits()
	
End Function

Function InitCredits()
	Local cl.CreditsLine, d.Doors
	Local file%
	Local l$, i%, scr
	
	file% = OpenFile(I_Loc\LangPath+"Credits.txt")
		
	CreditsFont% = LoadFont_Strict("GFX\font\Courier New.ttf", Int(21 * (opt\GraphicHeight / 1024.0)))
	CreditsFont2% = LoadFont_Strict("GFX\font\Courier New BD.ttf", Int(35 * (opt\GraphicHeight / 1024.0)))
	
	CreditsScreenCurrent = Rand(0, (MaxCreditScreens-1))
	CreditsScreenNext = CreditsScreenCurrent
	While CreditsScreenCurrent = CreditsScreenNext
		CreditsScreenNext = Rand(0, (MaxCreditScreens-1))
	Wend
	ShowEntity CreditsScreen[CreditsScreenCurrent]
	ShowEntity CreditsScreen[CreditsScreenNext]
	PositionEntity CreditsScreen[CreditsScreenNext], 0, 100 - 0.75, 4096.0 * RoomScale
	FreeEntity Camera
	Camera = CreateCamera()
	CameraFogMode Camera, 1
	CameraRange Camera, 0.01, 5
	CameraFogRange Camera, 0.5, 2.5
	CameraFogColor Camera,0,0,0
	CameraClsColor Camera,0,0,0
	PositionEntity Camera, 0, 100, 1024.0 * RoomScale
	
	d = CreateDoor(0, 0.0, 100 - 0.75, 3072.0 * RoomScale, 0, Null, False, DOOR_LCZ)
	If EntityDistanceSquared(Camera,d\obj)<PowTwo(2.0) Then
		d\open = True
	Else
		d\open = False
	EndIf
	MoveEntity d\obj, 0, -10.0, 0
	
	Repeat
		l = ReadLine(file)
		cl = New CreditsLine
		cl\txt = l
	Until Eof(file)
	
	Delete First CreditsLine
	CreditsTimer = 0
	
End Function

Function DrawCredits()
    Local credits_Y# = (EndingTimer+2000)/2+(opt\GraphicHeight+10)
    Local cl.CreditsLine
    Local id%
    Local endlinesamount%
	Local LastCreditLine.CreditsLine
	
	CameraProjMode Camera, 1
	RenderWorld(Max(0.0,1.0+(ft\accumulator/ft\tickDuration)))
	
	id = 0
	endlinesamount = 0
	LastCreditLine = Null
	Color 255,255,255
	For cl = Each CreditsLine
		cl\id = id
		If Left(cl\txt,1) = "*" Then
			SetFont CreditsFont2
			If (Not cl\stay) Then
				Text opt\GraphicWidth/2,credits_Y+(24*cl\id*MenuScale),Right(cl\txt,Len(cl\txt)-1),True
			EndIf
		ElseIf Left(cl\txt,1) = "/" Then
			LastCreditLine = Before(cl)
		Else
			SetFont CreditsFont
			If cl\stay = False Then
				Text opt\GraphicWidth/2,credits_Y+(24*cl\id*MenuScale),cl\txt,True
			EndIf
		EndIf
		If LastCreditLine<>Null Then
			If cl\id>LastCreditLine\id Then
				cl\stay = True
			EndIf
		EndIf
		If cl\stay Then
			endlinesamount = endlinesamount + 1
		EndIf
		id = id + 1
	Next
	If (credits_Y+(24*LastCreditLine\id*MenuScale)) < -StringHeight(LastCreditLine\txt) Then
		If CreditsTimer >= 0.0 And CreditsTimer < 255.0 Then
			Color Max(Min(CreditsTimer,255),0),Max(Min(CreditsTimer,255),0),Max(Min(CreditsTimer,255),0)
		ElseIf CreditsTimer >= 255.0 Then
			Color 255,255,255
		Else
			Color Max(Min(-CreditsTimer,255),0),Max(Min(-CreditsTimer,255),0),Max(Min(-CreditsTimer,255),0)
		EndIf
	Else
		Color 0,0,0
	EndIf
	If CreditsTimer <> 0.0 Then
		For cl = Each CreditsLine
			If cl\stay Then
				SetFont CreditsFont
				If Left(cl\txt,1) = "/" Then
					Text opt\GraphicWidth/2,(opt\GraphicHeight/2)+(endlinesamount/2)+(24*cl\id*MenuScale),Right(cl\txt,Len(cl\txt)-1),True
				Else
					Text opt\GraphicWidth/2,(opt\GraphicHeight/2)+(24*(cl\id-LastCreditLine\id)*MenuScale)-((endlinesamount/2)*24*MenuScale),cl\txt,True
				EndIf
			EndIf
		Next
	EndIf
    
End Function

Function UpdateCredits()
	Local credits_Y# = (EndingTimer+2000)/2+(opt\GraphicHeight+10)
    Local cl.CreditsLine
    Local id%, i%
    Local endlinesamount%
	Local LastCreditLine.CreditsLine
	
	If SelectedEnding = "scp-035" Then
		ShouldPlay = MUS_TURN_AROUND
	ElseIf SelectedEnding = "scp-2935" Then
		ShouldPlay = MUS_SCP_2935
	ElseIf SelectedEnding = "c1" Then
		ShouldPlay = MUS_GOOD_KARMA
	ElseIf SelectedEnding = "d1" Then
		ShouldPlay = MUS_CYBER_CHASE_3
	Else
		ShouldPlay = MUS_CREDITS
	EndIf
	
	MoveEntity Camera, 0, 0, 0.008*fps\Factor[1]
	If EntityZ(Camera) > 3072.0 * RoomScale Then
		CreditsScreenCurrent = CreditsScreenNext
		While CreditsScreenCurrent = CreditsScreenNext
			CreditsScreenNext = Rand(0, (MaxCreditScreens-1))
		Wend
		For i = 0 To (MaxCreditScreens-1)
			HideEntity CreditsScreen[i]
		Next
		ShowEntity CreditsScreen[CreditsScreenCurrent]
		ShowEntity CreditsScreen[CreditsScreenNext]
		PositionEntity CreditsScreen[CreditsScreenCurrent], 0, 100 - 0.75, 2048.0 * RoomScale
		PositionEntity CreditsScreen[CreditsScreenNext], 0, 100 - 0.75, 4096.0 * RoomScale
		PositionEntity Camera, EntityX(Camera), EntityY(Camera), EntityZ(Camera) - 2048.0 * RoomScale
		CaptureWorld()
	EndIf
	
	id = 0
	endlinesamount = 0
	LastCreditLine = Null
	
	For cl = Each CreditsLine
		cl\id = id
		If Left(cl\txt,1) = "/" Then
			LastCreditLine = Before(cl)
		EndIf
		If LastCreditLine <> Null Then
			If cl\id>LastCreditLine\id Then
				cl\stay = True
			EndIf
		EndIf
		If cl\stay Then
			endlinesamount = endlinesamount + 1
		EndIf
		id = id + 1
	Next
	If (credits_Y+(24*LastCreditLine\id*MenuScale)) < -StringHeight(LastCreditLine\txt) Then
		CreditsTimer=CreditsTimer+(0.5*fps\Factor[1])
		If CreditsTimer>=255.0 Then
			If CreditsTimer > 500.0 Then
				CreditsTimer = -255.0
			EndIf
		ElseIf CreditsTimer < 0.0 Then
			If CreditsTimer >= -1.0 Then
				CreditsTimer = -1.0
			EndIf
		EndIf
	EndIf
	
	If GetKey() Then CreditsTimer = -1
	
	If CreditsTimer = -1 Then
		FreeFont CreditsFont
		FreeFont CreditsFont2
		For i = 0 To (MaxCreditScreens-1)
			CreditsScreen[i] = 0
		Next
		CreditsScreenCurrent = 0
		CreditsScreenNext = 0
		FreeImage EndingScreen
		EndingScreen = 0
		Delete Each CreditsLine
		Local prevMainMenuOpen = MainMenuOpen
		If (Not prevMainMenuOpen) Then
			MainMenuOpen = True
			NullGame(False, False)
		Else
			EndingTimer = 0
			Reload()
		EndIf
		If (Not prevMainMenuOpen) Then
			StopStream_Strict(MusicCHN)
			PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Bells.ogg"))
		EndIf
		MenuOpen = False
        MainMenuTab = 0
        CurrSave = Null
        FlushKeys()
	EndIf
	
End Function

; ~ [PLAYER CONTROLS]

Function MovePlayer()
	CatchErrors("Uncaught (MovePlayer)")
	Local Sprint# = 1.0, Speed# = 0.018, i%, angle#
	
	If SuperMan Then
		
		Speed = Speed * 3
		
		SuperManTimer=SuperManTimer+fps\Factor[0]
		
		CameraShake = Sin(SuperManTimer / 5.0) * (SuperManTimer / 1500.0)
		
		If SuperManTimer > 70 * 50 Then
			m_msg\DeathTxt = GetLocalString("Singleplayer","superman_death_1")
			m_msg\DeathTxt = m_msg\DeathTxt + GetLocalString("Singleplayer","superman_death_2")
			Kill()
			ShowEntity Overlay[0]
		Else
			BlurTimer = 500		
			HideEntity Overlay[0]
		EndIf
		
	EndIf
	
	If DeathTimer > 0 Then
		DeathTimer=DeathTimer-fps\Factor[0]
		If DeathTimer < 1 Then DeathTimer = -1.0
	ElseIf DeathTimer < 0 
		Kill()
	EndIf
	
	If CurrSpeed > 0 Then
        Stamina = Min(Stamina + 0.15 * fps\Factor[0]/1.25, 100.0)
    Else
        Stamina = Min(Stamina + 0.15 * fps\Factor[0]*1.25, 100.0)
    EndIf
	
	If StaminaEffectTimer > 0 Then
		StaminaEffectTimer = StaminaEffectTimer - (fps\Factor[0]/70)
	Else
		If StaminaEffect <> 1.0 Then StaminaEffect = 1.0
	EndIf
	
	Local temp#
	
	If PlayerRoom\RoomTemplate\Name <> "pocketdimension" And (Not InvOpen) And OtherOpen = Null Then
		If KeyDown(KEY_SPRINT) Then
			If Stamina < 5 Then
				temp = 0
				If wbi\GasMask>0 Then temp=1
				If ChannelPlaying(BreathCHN)=False Then BreathCHN = PlaySound_Strict(BreathSFX[temp * 5])
			ElseIf Stamina < 50
				If BreathCHN=0 Then
					temp = 0
					If wbi\GasMask>0 Then temp=1
					BreathCHN = PlaySound_Strict(BreathSFX[temp * 5 + Rand(1,3)])
					ChannelVolume BreathCHN, Min((70.0-Stamina)/70.0,1.0)*opt\SFXVolume*opt\MasterVol
				Else
					If ChannelPlaying(BreathCHN)=False Then
						temp = 0
						If wbi\GasMask>0 Then temp=1
						BreathCHN = PlaySound_Strict(BreathSFX[temp * 5 + Rand(1,3)])
						ChannelVolume BreathCHN, Min((70.0-Stamina)/70.0,1.0)*opt\SFXVolume*opt\MasterVol		
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
	If I_714\Using > 0 Then 
		Stamina = Min(Stamina, 10)
		Sanity = Max(-850, Sanity)
	EndIf
	
	If IsZombie Then Crouch = False
	
	If (Not psp\NoMove) Then
		If Abs(CrouchState-Crouch)<0.001 Then 
			CrouchState = Crouch
		Else
			CrouchState = CurveValue(Crouch, CrouchState, 10.0)
		EndIf
		If (Not NoClip) Then
			
			; ~ [CONTROLLER]
			
			If co\Enabled
				Local case1% = 0
				Local case2% = ((GetLeftAnalogStickPitch()<>0 Lor GetLeftAnalogStickYaw()<>0) And Playable)
			Else
				case1% = ((KeyDown(KEY_DOWN) Xor KeyDown(KEY_UP)) Lor (KeyDown(KEY_RIGHT) Xor KeyDown(KEY_LEFT)) And Playable)
				case2% = 0
			EndIf
			If case1% Lor case2% Lor ForceMove>0
				
				; ~ [CONTROLLER]
				
				Local SprintKeyAssigned = False
				If (Not co\Enabled)
					If KeyDown(KEY_SPRINT) Then SprintKeyAssigned = True
				Else
					If IsPlayerSprinting
						If JoyHit(CK_Sprint)
							SprintKeyAssigned = 0
						Else
							SprintKeyAssigned = 1
						EndIf
					Else
						If JoyHit(CK_Sprint)
							SprintKeyAssigned = 1
						Else
							SprintKeyAssigned = 0
						EndIf
					EndIf
				EndIf
				
				IsPlayerSprinting% = False
				
				If (Not Crouch) And (SprintKeyAssigned) And Stamina > 0.0 And (Not IsZombie) And g_I\IronSight = 0 And (Not InvOpen) And OtherOpen = Null Then
					Sprint = 2.5
					IsPlayerSprinting% = True
					Stamina = Stamina - fps\Factor[0] * 0.25 * StaminaEffect
					If Stamina <= 0 Then Stamina = -20.0
				EndIf
				If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then 
					If EntityY(Collider)<2000*RoomScale Lor EntityY(Collider)>2608*RoomScale Then
						Stamina = 0
						IsPlayerSprinting% = False
						Speed = 0.015
						Sprint = 1.0					
					EndIf
				EndIf
				
				If InvOpen Lor OtherOpen <> Null Then Speed = 0.009
				
				If ForceMove > 0 Then Speed = Speed * ForceMove
				
				If SelectedItem<>Null Then
					If SelectedItem\itemtemplate\tempname = "firstaid" Lor SelectedItem\itemtemplate\tempname = "finefirstaid" Lor SelectedItem\itemtemplate\tempname = "firstaid2" Then 
						Sprint = 0
						IsPlayerSprinting% = False
					EndIf
				EndIf
				
				Sprint = (Sprint / (1.0+Crouch))
				
				temp# = (Shake Mod 360)
				Local tempchn%
				Local auxchn%
				If (Not UnableToMove%) Then Shake# = (Shake + fps\Factor[0] * Min(Sprint, 1.7) * 10) Mod 720
				If temp < 180 And (Shake Mod 360) >= 180 And KillTimer>=0 Then
					If CurrStepSFX=0 Then
						temp = GetStepSound(Collider)
						If Sprint = 1.0 Then
							PlayerSoundVolume = Max(4.0,PlayerSoundVolume)
							tempchn% = PlaySound_Strict(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
							ChannelVolume tempchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							If wbi\Vest Then
								auxchn% = PlaySound_Strict(EquipmentSFX[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							ElseIf hds\Wearing Then
								auxchn% = PlaySound_Strict(HDSWalkSFX[Rand(0,3)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							EndIf
						Else
							PlayerSoundVolume = Max(2.5-(Crouch*0.6),PlayerSoundVolume)
							tempchn% = PlaySound_Strict(mpl\StepSoundRun[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
							ChannelVolume tempchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							If wbi\Vest Then
								auxchn% = PlaySound_Strict(EquipmentSFX[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							ElseIf hds\Wearing Then
								auxchn% = PlaySound_Strict(HDSWalkSFX[Rand(0,3)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							EndIf
						EndIf
					ElseIf CurrStepSFX=1 Then
						tempchn% = PlaySound_Strict(Step2SFX[Rand(0, 2)])
						ChannelVolume tempchn, (1.0-(Crouch*0.4))*(opt\SFXVolume*opt\MasterVol)
						If wbi\Vest Then
							auxchn% = PlaySound_Strict(EquipmentSFX[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
							ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
						ElseIf hds\Wearing Then
							auxchn% = PlaySound_Strict(HDSWalkSFX[Rand(0,3)])
							ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
						EndIf
					ElseIf CurrStepSFX=2 Then
						tempchn% = PlaySound_Strict(Step2SFX[Rand(3,5)])
						ChannelVolume tempchn, (1.0-(Crouch*0.4))*(opt\SFXVolume*opt\MasterVol)
						If wbi\Vest Then
							auxchn% = PlaySound_Strict(EquipmentSFX[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
							ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
						ElseIf hds\Wearing Then
							auxchn% = PlaySound_Strict(HDSWalkSFX[Rand(0,3)])
							ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
						EndIf
					ElseIf CurrStepSFX=3 Then
						If Sprint = 1.0 Then
							PlayerSoundVolume = Max(4.0,PlayerSoundVolume)
							tempchn% = PlaySound_Strict(StepSFX(0, 0, Rand(0, 7)))
							ChannelVolume tempchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							If wbi\Vest Then
								auxchn% = PlaySound_Strict(EquipmentSFX[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							ElseIf hds\Wearing Then
								auxchn% = PlaySound_Strict(HDSWalkSFX[Rand(0,3)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							EndIf
						Else
							PlayerSoundVolume = Max(2.5-(Crouch*0.6),PlayerSoundVolume)
							tempchn% = PlaySound_Strict(StepSFX(0, 1, Rand(0, 7)))
							ChannelVolume tempchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							If wbi\Vest Then
								auxchn% = PlaySound_Strict(EquipmentSFX[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							ElseIf hds\Wearing Then
								auxchn% = PlaySound_Strict(HDSWalkSFX[Rand(0,3)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							EndIf
						EndIf
					EndIf
				EndIf
				
				Sprint = (Sprint * (1.0+Crouch))
			Else
				IsPlayerSprinting% = False
			EndIf
		Else ; ~ Noclip on
			
			; ~ [CONTROLLER]
			
			If (Not co\Enabled)
				If (KeyDown(KEY_SPRINT) And (Not InvOpen) And OtherOpen = Null) Then 
					Sprint = 2.5
				ElseIf KeyDown(KEY_CROUCH)
					Sprint = 0.5
				EndIf
			Else
				If IsPlayerSprinting
					If JoyDown(CK_Sprint And (Not InvOpen) And OtherOpen = Null)
						Sprint = 2.5
					ElseIf JoyDown(CK_Crouch)
						Sprint = 0.5
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
	; ~ [CONTROLLER]
	
	If opt\HoldToCrouch Then
		If Playable Then
			If (Not co\Enabled) Then
				Crouch = KeyDown(KEY_CROUCH)
			Else
				Crouch = JoyDown(CK_Crouch)
			EndIf
		EndIf
	Else
		If (Not co\Enabled) Then
			If KeyHit(KEY_CROUCH) And Playable Then Crouch = (Not Crouch)
		Else
			If JoyHit(CK_Crouch) And Playable Then Crouch = (Not Crouch)
		EndIf
	EndIf
	
	Local temp2# = (Speed * Sprint) / (1.0+CrouchState)
	
	If NoClip Then 
		Shake = 0
		CurrSpeed = 0
		CrouchState = 0
		Crouch = 0
		
		RotateEntity Collider, WrapAngle(EntityPitch(Camera)), WrapAngle(EntityYaw(Camera)), 0
		
		temp2 = temp2 * NoClipSpeed
		
		; ~ [CONTROLLER]
		
		If (Not co\Enabled)
			If KeyDown(KEY_DOWN) Then MoveEntity Collider, 0, 0, -temp2*fps\Factor[0]
			If KeyDown(KEY_UP) Then MoveEntity Collider, 0, 0, temp2*fps\Factor[0]
			
			If KeyDown(KEY_LEFT) Then MoveEntity Collider, -temp2*fps\Factor[0], 0, 0
			If KeyDown(KEY_RIGHT) Then MoveEntity Collider, temp2*fps\Factor[0], 0, 0
		Else
			If GetLeftAnalogStickPitch()<0.0
				MoveEntity Collider, 0, 0, -temp2*fps\Factor[0]
			EndIf
			If GetLeftAnalogStickPitch()>0.0
				MoveEntity Collider, 0, 0, temp2*fps\Factor[0]
			EndIf
			If GetLeftAnalogStickYaw(True)<0.0
				MoveEntity Collider, -temp2*fps\Factor[0], 0, 0
			EndIf
			If GetLeftAnalogStickYaw(True)>0.0
				MoveEntity Collider, temp2*fps\Factor[0], 0, 0
			EndIf
		EndIf
		
		ResetEntity Collider
	Else
		If (Not psp\NoMove) Then
			temp = False
			If (Not IsZombie%)
				If (Not co\Enabled)
					If KeyDown(KEY_DOWN) And (Not KeyDown(KEY_UP)) And Playable Then
						temp = True 
						angle = 180
						If KeyDown(KEY_LEFT) And (Not KeyDown(KEY_RIGHT)) Then angle = 135 
						If KeyDown(KEY_RIGHT) And (Not KeyDown(KEY_LEFT)) Then angle = -135
					ElseIf KeyDown(KEY_UP) And (Not KeyDown(KEY_DOWN)) And Playable Then
						temp = True
						angle = 0
						If KeyDown(KEY_LEFT) And (Not KeyDown(KEY_RIGHT)) Then angle = 45
						If KeyDown(KEY_RIGHT) And (Not KeyDown(KEY_LEFT)) Then angle = -45
					ElseIf ForceMove>0 Then
						temp=True
						angle = ForceAngle
					Else If Playable Then
						If KeyDown(KEY_LEFT) And (Not KeyDown(KEY_RIGHT)) Then angle = 90 : temp = True
						If KeyDown(KEY_RIGHT) And (Not KeyDown(KEY_LEFT)) Then angle = -90 : temp = True 
					EndIf
				Else
					
					; ~ [CONTROLLER]
					
					If GetLeftAnalogStickPitch()<0.0 And Playable
						temp = True
						angle = 180
						If GetLeftAnalogStickYaw(True)<>0.0
							angle = GetLeftAnalogStickYaw(True,True)*(180.0-(45.0*Abs(GetLeftAnalogStickYaw())))
						EndIf
					ElseIf GetLeftAnalogStickPitch()>0.0 And Playable
						temp = True
						angle = 0
						If GetLeftAnalogStickYaw(True)<>0.0
							angle = GetLeftAnalogStickYaw(True,True)*(45.0*Abs(GetLeftAnalogStickYaw()))
						EndIf
					ElseIf ForceMove>0
						temp = True
						angle = ForceAngle
					ElseIf Playable
						If GetLeftAnalogStickYaw(True)<>0.0
							angle = GetLeftAnalogStickYaw(True,True)*90.0
							temp = True
						EndIf
					EndIf
				EndIf
			Else
				temp=True
				angle = ForceAngle
			EndIf
			
			angle = WrapAngle(EntityYaw(Collider,True)+angle+90.0)
			
			If temp Then 
				CurrSpeed = CurveValue(temp2, CurrSpeed, 20.0)
			Else
				CurrSpeed = Max(CurveValue(0.0, CurrSpeed-0.1, 1.0),0.0)
			EndIf
			
			If (Not UnableToMove%) Then TranslateEntity Collider, Cos(angle)*CurrSpeed * fps\Factor[0], 0, Sin(angle)*CurrSpeed * fps\Factor[0], True
		EndIf
		
		Local CollidedFloor% = False
		For i = 1 To CountCollisions(Collider)
			If CollisionY(Collider, i) < EntityY(Collider) - 0.25 Then CollidedFloor = True
		Next
		
		If CollidedFloor = True Then
			If DropSpeed# < - 0.07 Then
				If CurrStepSFX=0 Then
					PlaySound_Strict(StepSFX(GetStepSound(Collider), 0, Rand(0, 7)))
				ElseIf CurrStepSFX=1
					PlaySound_Strict(Step2SFX[Rand(0, 2)])
				ElseIf CurrStepSFX=2
					PlaySound_Strict(Step2SFX[Rand(3, 5)])
				ElseIf CurrStepSFX=3
					PlaySound_Strict(StepSFX(0, 0, Rand(0, 7)))
				EndIf
				PlayerSoundVolume = Max(3.0,PlayerSoundVolume)
			EndIf
			DropSpeed# = 0
		Else
			If PlayerFallingPickDistance#<>0.0
				Local pick = LinePick(EntityX(Collider),EntityY(Collider),EntityZ(Collider),0,-PlayerFallingPickDistance,0)
				If pick
					DropSpeed# = Min(Max(DropSpeed - 0.006 * fps\Factor[0], -2.0), 0.0)
				Else
					DropSpeed# = 0
				EndIf
			Else
				;DropSpeed# = Min(Max(DropSpeed - 0.006 * fps\Factor[0], -2.0), 0.0)
				DropSpeed# = Min(Max(DropSpeed - 0.003 * fps\Factor[0], -2.0), 0.0)
			EndIf
		EndIf
		PlayerFallingPickDistance# = 10.0
		
		If (Not UnableToMove%) And ShouldEntitiesFall Then TranslateEntity Collider, 0, DropSpeed * fps\Factor[0], 0
	EndIf
	
	ForceMove = False
	
	Update008()
	Update016()
	Update059()
	Update409()
	
	If HealTimer > 0 Then
		DebugLog HealTimer
		HealTimer = HealTimer - (fps\Factor[0] / 70)
		HealSPPlayer(0.01 * fps\Factor[0])
	EndIf
	If Playable Then
		If (Not co\Enabled)
			If KeyHit(KEY_BLINK) Then BlinkTimer = 0 : BlurTimer = BlurTimer - 5
			If KeyDown(KEY_BLINK) And BlinkTimer < - 10 Then BlinkTimer = -10
		Else
			
			; ~ [CONTROLLER]
			
			If JoyHit(CK_Blink) Then BlinkTimer = 0 : BlurTimer = BlurTimer - 5
			If JoyDown(CK_Blink) And BlinkTimer < - 10 Then BlinkTimer = -10
		EndIf
	EndIf
	If HeartBeatVolume > 0 Then
		If HeartBeatTimer <= 0 Then
			tempchn = PlaySound_Strict (HeartBeatSFX)
			ChannelVolume tempchn, HeartBeatVolume*(opt\SFXVolume*opt\MasterVol)
			HeartBeatTimer = 70.0*(60.0/Max(HeartBeatRate,1.0))
		Else
			HeartBeatTimer = HeartBeatTimer - fps\Factor[0]
		EndIf
		HeartBeatVolume = Max(HeartBeatVolume - fps\Factor[0]*0.001, 0)
	EndIf
	
	CatchErrors("MovePlayer")
	
End Function

Function MouseLook()
	Local i%, g.Guns, currGun.Guns
	
	CameraShake = Max(CameraShake - (fps\Factor[0] / 10), 0)
	BigCameraShake = Max(BigCameraShake - (fps\Factor[0] / 10), 0)
	
	For g.Guns = Each Guns
		If g\ID = g_I\HoldingGun Then
			If g\GunType<>GUNTYPE_MELEE And g\GunType<>GUNTYPE_GRENADE Then
				currGun = g
			EndIf
			Exit
		EndIf
	Next
	
	Local IronSight_AddFOV# = 0.0
	If currGun <> Null Then
		IronSight_AddFOV = Abs(EntityX(IronSightPivot2%)/currGun\IronSightCoords\x)*0.5
	EndIf
	
	CameraZoom(Camera, (Min(1.0+(CurrCameraZoom/400.0),1.1) + IronSight_AddFOV) / (Tan((2*ATan(Tan(Float(FOV)/2)*(Float(RealGraphicWidth)/Float(RealGraphicHeight))))/2.0)))
	CurrCameraZoom = Max(CurrCameraZoom - fps\Factor[0], 0)
	
	If KillTimer >= 0 And FallTimer >=0 Then
		
		HeadDropSpeed = 0
		
		Local up# = (Sin(Shake) / (20.0+CrouchState*20.0))*0.6
		Local roll# = Max(Min(Sin(Shake/2)*0.625,8.0),-8.0)
		
		PositionEntity Camera, EntityX(Collider), EntityY(Collider), EntityZ(Collider)
		
		If (Not psp\NoRotation) Then
			RotateEntity Camera, 0, EntityYaw(Collider), (roll*0.5)*0.5
			MoveEntity Camera, Side, (up*0.5) + 0.6 + CrouchState * -0.3, 0
			If mpl\WheelOpened=WHEEL_CLOSED Then
				If (Not co\Enabled)
					Mouse_X_Speed_1# = CurveValue(MouseXSpeed() * (MouseSens + 0.6) , Mouse_X_Speed_1, (6.0 / (MouseSens + 1.0))*opt\MouseSmooth)
				Else
					
					; ~ [CONTROLLER]
					
					If GetRightAnalogStickYaw(True)<>0.0
						Mouse_X_Speed_1# = CurveValue(GetRightAnalogStickYaw() * ((co\Sensitivity+0.6)*10*fps\Factor[0]), Mouse_X_Speed_1, 6.0 / ((co\Sensitivity+1.0)*10*fps\Factor[0]))
					Else
						Mouse_X_Speed_1# = CurveValue(0.0, Mouse_X_Speed_1, 6.0 / ((co\Sensitivity+1.0)*10*fps\Factor[0]))
					EndIf
				EndIf
				If IsNaN(Mouse_X_Speed_1) Then Mouse_X_Speed_1 = 0
				
				If (Not co\Enabled)
					If InvertMouse Then
						Mouse_Y_Speed_1# = CurveValue(-MouseYSpeed() * (MouseSens + 0.6), Mouse_Y_Speed_1, (6.0/(MouseSens+1.0))*opt\MouseSmooth)
					Else
						Mouse_Y_Speed_1# = CurveValue(MouseYSpeed () * (MouseSens + 0.6), Mouse_Y_Speed_1, (6.0/(MouseSens+1.0))*opt\MouseSmooth) 
					EndIf
				Else
					
					; ~ [CONTROLLER]
					
					If Int(GetRightAnalogStickPitch(True))<>0
						Mouse_Y_Speed_1# = CurveValue(GetRightAnalogStickPitch(False,InvertMouse) * ((co\Sensitivity+0.6)*10*fps\Factor[0]), Mouse_Y_Speed_1, 6.0/((co\Sensitivity+1.0)*10*fps\Factor[0]))
					Else
						Mouse_Y_Speed_1# = CurveValue(0.0, Mouse_Y_Speed_1, 6.0/((co\Sensitivity+1.0)*10*fps\Factor[0]))
					EndIf
				EndIf
				If IsNaN(Mouse_Y_Speed_1) Then Mouse_Y_Speed_1 = 0
				
				Local the_yaw# = ((Mouse_X_Speed_1#)) * MouseLook_X_Inc# / (1.0+wbi\Vest)
				Local the_pitch# = ((Mouse_Y_Speed_1#)) * MouseLook_Y_Inc# / (1.0+wbi\Vest)
				
				TurnEntity Collider, 0.0, -the_yaw#, 0.0
				If UnableToMove = 2
					RotateEntity Collider,0.0,Max(Min(EntityYaw(Collider),70),-70),0.0
				EndIf
				User_Camera_Pitch# = User_Camera_Pitch# + the_pitch#
				If User_Camera_Pitch# > 70.0 Then User_Camera_Pitch# = 70.0
				If User_Camera_Pitch# < - 70.0 Then User_Camera_Pitch# = -70.0
			EndIf
		Else
			RotateEntity Camera, 0, EntityYaw(Collider), 0
			Shake = 0
			MoveEntity Camera, 0, 0.6 + CrouchState * -0.3, 0
		EndIf
		
		If (Not NoClip)
			
		Else
			
		EndIf
		
		Local ShakeTimer# = CameraShake + BigCameraShake
		
		RotateEntity Camera, WrapAngle(User_Camera_Pitch + Rnd(-ShakeTimer, ShakeTimer)), WrapAngle(EntityYaw(Collider) + Rnd(-ShakeTimer, ShakeTimer)), roll
		
		If gc\CurrGamemode<>3
			If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then
				If EntityY(Collider)<2000*RoomScale Lor EntityY(Collider)>2608*RoomScale Then
					RotateEntity Camera, WrapAngle(EntityPitch(Camera)),WrapAngle(EntityYaw(Camera)), roll+WrapAngle(Sin(MilliSecs()/150.0)*30.0)
				EndIf
			EndIf
		EndIf
	Else
		HideEntity Collider
		PositionEntity Camera, EntityX(Head), EntityY(Head), EntityZ(Head)
		
		Local CollidedFloor% = False
		
		For i = 1 To CountCollisions(Head)
			If CollisionY(Head, i) < EntityY(Head) - 0.01 Then CollidedFloor = True
		Next
		
		If CollidedFloor = True Then
			HeadDropSpeed# = 0
		Else
			If KillAnim = 0 Then 
				MoveEntity Head, 0, 0, HeadDropSpeed
				RotateEntity(Head, CurveAngle(-90.0, EntityPitch(Head), 20.0), EntityYaw(Head), EntityRoll(Head))
				RotateEntity(Camera, CurveAngle(EntityPitch(Head) - 40.0, EntityPitch(Camera), 40.0), EntityYaw(Camera), EntityRoll(Camera))
			Else
				MoveEntity Head, 0, 0, -HeadDropSpeed
				RotateEntity(Head, CurveAngle(90.0, EntityPitch(Head), 20.0), EntityYaw(Head), EntityRoll(Head))
				RotateEntity(Camera, CurveAngle(EntityPitch(Head) + 40.0, EntityPitch(Camera), 40.0), EntityYaw(Camera), EntityRoll(Camera))
			EndIf
			
			HeadDropSpeed# = HeadDropSpeed - 0.002 * fps\Factor[0]
		EndIf
		
		If (Not co\Enabled)
			If InvertMouse Then
				TurnEntity (Camera, -MouseYSpeed() * 0.05 * fps\Factor[0], -MouseXSpeed() * 0.15 * fps\Factor[0], 0)
			Else
				TurnEntity (Camera, MouseYSpeed() * 0.05 * fps\Factor[0], -MouseXSpeed() * 0.15 * fps\Factor[0], 0)
			EndIf
		Else
			
			; ~ [CONTROLLER]
			
			TurnEntity (Camera, GetRightAnalogStickPitch(False,InvertMouse) * 0.05 * fps\Factor[0], GetRightAnalogStickYaw(False,True) * 0.15 * fps\Factor[0], 0)
		EndIf
		
	EndIf
	If ParticleAmount=2
		If Rand(35) = 1 Then
			Local pvt% = CreatePivot()
			PositionEntity(pvt, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True))
			RotateEntity(pvt, 0, Rnd(360), 0)
			If Rand(2) = 1 Then
				MoveEntity(pvt, 0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
			Else
				MoveEntity(pvt, 0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
			End If
			
			Local p.Particles = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 2, 0.002, 0, 300)
			p\speed = 0.001
			RotateEntity(p\pvt, Rnd(-20, 20), Rnd(360), 0)
			
			p\SizeChange = -0.00001
			
			FreeEntity pvt
		End If
	EndIf
	If mpl\WheelOpened=WHEEL_CLOSED Then
		If (ScaledMouseX() > Mouse_Right_Limit) Lor (ScaledMouseX() < Mouse_Left_Limit) Lor (ScaledMouseY() > Mouse_Bottom_Limit) Lor (ScaledMouseY() < Mouse_Top_Limit)
			MoveMouse Viewport_Center_X, Viewport_Center_Y
		EndIf
	EndIf
	If wbi\GasMask Lor wbi\Hazmat Then
		If I_714\Using = False Then
			If wbi\GasMask = 2 Lor wbi\Hazmat = 2 Then
				Stamina = Min(100, Stamina + (100.0-Stamina)*0.01*fps\Factor[0])
			EndIf
		EndIf
		If wbi\Hazmat = 1 Then
			Stamina = Min(60, Stamina)
		EndIf
		
		ShowEntity(Overlay[1])
	Else
		HideEntity(Overlay[1])
	EndIf
	
	If I_109\Used > 0 Then 
        I_109\Timer = Max(I_109\Timer - fps\Factor[0] / 1.5, 0)
		
        If I_109\Timer = 0 Then 
            I_109\Timer = 70 * 10
            I_109\Used = I_109\Used - 1
        EndIf
    EndIf
	
	If I_268\Using > 0 Then
		ShowEntity(Overlay[7])
		ShouldPlay = MUS_NULL
	Else
		HideEntity(Overlay[7])
	EndIf
	If I_268\Timer < 1 Then
		HideEntity(Overlay[7])
	EndIf
	
	If I_1033RU\Using = 1 Then
        I_1033RU\HP = 100 - I_1033RU\DHP
    ElseIf I_1033RU\Using = 2
        I_1033RU\HP = 200 - I_1033RU\DHP
    Else
        I_1033RU\HP = 0
    EndIf
	
    If I_1033RU\Using > 0 And I_1033RU\HP > 0 Then
        ShouldPlay = MUS_SCP_1033RU
    EndIf
	
	If wbi\NightVision > 0 Lor wbi\SCRAMBLE > 0 Then
		ShowEntity(Overlay[6])
		If wbi\NightVision = 2 Then
			EntityColor(Overlay[6], 0.0, 100.0, 255.0)
			AmbientLightRooms(15)
		ElseIf wbi\NightVision = 3
			EntityColor(Overlay[6], 255.0, 0.0, 0.0)
			AmbientLightRooms(15)
		ElseIf wbi\NightVision = 1
			EntityColor(Overlay[6], 0.0, 255.0, 0.0)
			AmbientLightRooms(15)
		Else
			EntityColor(Overlay[6], 128.0, 128.0, 128.0)
			AmbientLightRooms(0)
		EndIf
		EntityTexture(Overlay[0], OverlayTexture[9])
	Else
		AmbientLightRooms(0)
		HideEntity(Overlay[6])
		EntityTexture(Overlay[0], OverlayTexture[0])
	EndIf
	If gc\CurrGamemode<>3 Then
		For i = 0 To 5
			If I_1025\State[i]>0 Then
				Select i
					Case 0 ; ~ Common cold
						If fps\Factor[0]>0 Then 
							If Rand(1000)=1 Then
								If CoughCHN = 0 Then
									CoughCHN = PlaySound_Strict(CoughSFX[Rand(0, 2)])
								Else
									If Not ChannelPlaying(CoughCHN) Then CoughCHN = PlaySound_Strict(CoughSFX[Rand(0, 2)])
								End If
							EndIf
						EndIf
						Stamina = Stamina - fps\Factor[0] * 0.3
					Case 1 ; ~ Chicken pox
						If Rand(9000)=1 And m_msg\Txt="" Then
							CreateMsg(GetLocalString("Items","scp1025_1"))
						EndIf
					Case 2 ; ~ Cancer of the lungs
						If fps\Factor[0]>0 Then 
							If Rand(800)=1 Then
								If CoughCHN = 0 Then
									CoughCHN = PlaySound_Strict(CoughSFX[Rand(0, 2)])
								Else
									If Not ChannelPlaying(CoughCHN) Then CoughCHN = PlaySound_Strict(CoughSFX[Rand(0, 2)])
								End If
							EndIf
						EndIf
						Stamina = Stamina - fps\Factor[0] * 0.1
					Case 3 ; ~ Appendicitis
						I_1025\State[i]=I_1025\State[i]+fps\Factor[0]*0.0005
						If I_1025\State[i]>20.0 Then
							If I_1025\State[i]-fps\Factor[0]<=20.0 Then CreateMsg(GetLocalString("Items","scp1025_1"))
							Stamina = Stamina - fps\Factor[0] * 0.3
						ElseIf I_1025\State[i]>10.0
							If I_1025\State[i]-fps\Factor[0]<=10.0 Then CreateMsg(GetLocalString("Items","scp1025_3"))
						EndIf
					Case 4 ; ~ Asthma
						If Stamina < 35 Then
							If Rand(Int(140+Stamina*8))=1 Then
								If CoughCHN = 0 Then
									CoughCHN = PlaySound_Strict(CoughSFX[Rand(0, 2)])
								Else
									If Not ChannelPlaying(CoughCHN) Then CoughCHN = PlaySound_Strict(CoughSFX[Rand(0, 2)])
								End If
							EndIf
							CurrSpeed = CurveValue(0, CurrSpeed, 10+Stamina*15)
						EndIf
					Case 5 ; ~ Cardiac arrest
						I_1025\State[i]=I_1025\State[i]+fps\Factor[0]*0.35
						If I_1025\State[i]>110 Then
							HeartBeatRate=0
							BlurTimer = Max(BlurTimer, 500)
							If I_1025\State[i]>140 Then 
								m_msg\DeathTxt = Chr(34)+GetLocalString("Singleplayer","scp1025_death_1")
								m_msg\DeathTxt = m_msg\DeathTxt + GetLocalString("Singleplayer","scp1025_death_2")+Chr(34)
								Kill()
							EndIf
						Else
							HeartBeatRate=Max(HeartBeatRate, 70+I_1025\State[i])
							HeartBeatVolume = 1.0
						EndIf
				End Select 
			EndIf
		Next
	EndIf
	
	
End Function

; ~ [GUI]

Include "SourceCode\Tasks_Core.bb"

Include "SourceCode\GUI.bb"

; ~ [ENTITIES LOADING]

;[Block]

Function LoadEntities()
	CatchErrors("LoadEntities")
	
	DrawLoading(0,False, "", "ALL GAME ENTITIES")
	
	CreateSPPlayer()
	
	LoadMissingTexture()
	
	InitConsole(1)
	
	Local i%
	
	For i=0 To 9
		TempSounds[i]=0
	Next
	
	MainMenuTab = MenuTab_Default
	
	PauseMenuIMG[0] = LoadImage_Strict("GFX\menu\pausemenu.jpg")
	PauseMenuIMG[1] = LoadImage_Strict("GFX\menu\pausemenu (2).jpg")
	For i = 0 To 1
		MaskImage PauseMenuIMG[i], 255,0,255
		ScaleImage PauseMenuIMG[i],MenuScale,MenuScale
	Next
	
	PlayerIcons[0] = LoadImage_Strict("GFX\HUD\sprint_icon.png")
	PlayerIcons[1] = LoadImage_Strict("GFX\HUD\blink_icon.png")
	PlayerIcons[2] = LoadImage_Strict("GFX\HUD\sneak_icon.png")
	PlayerIcons[3] = LoadImage_Strict("GFX\HUD\hand_symbol.png")
	PlayerIcons[4] = LoadImage_Strict("GFX\HUD\hand_symbol_2.png")
	PlayerIcons[5] = LoadImage_Strict("GFX\HUD\hand_symbol_3.png")
	PlayerIcons[6] = LoadImage_Strict("GFX\HUD\hand_symbol_4.png")
	PlayerIcons[7] = LoadImage_Strict("GFX\HUD\hand_symbol_5.png")
	PlayerIcons[8] = LoadImage_Strict("GFX\HUD\ntf_helmet_icon.png")
	
	FineKevlarIcon% = LoadImage_Strict("GFX\HUD\kevlar_icon_2.png")
	
	; ~ Attachments
	
	AttachmentIMG[0] = LoadImage_Strict("GFX\weapons\Icons\INVsuppressor.jpg")
	AttachmentIMG[1] = LoadImage_Strict("GFX\weapons\Icons\INVreddot.jpg")
	AttachmentIMG[2] = LoadImage_Strict("GFX\weapons\Icons\INVacog.jpg")
	AttachmentIMG[3] = LoadImage_Strict("GFX\weapons\Icons\INVeotech.jpg")
	AttachmentIMG[4] = LoadImage_Strict("GFX\weapons\Icons\INVmp5stock.jpg")
	AttachmentIMG[5] = LoadImage_Strict("GFX\weapons\Icons\INVmp5stock2.jpg")
	AttachmentIMG[6] = LoadImage_Strict("GFX\weapons\Icons\INVverticalgrip.jpg")
	AttachmentIMG[7] = LoadImage_Strict("GFX\weapons\Icons\INVrail.jpg")
	AttachmentIMG[8] = LoadImage_Strict("GFX\weapons\Icons\INVaimpoint.jpg")
	AttachmentIMG[9] = LoadImage_Strict("GFX\weapons\Icons\INVlasersight.jpg")
	AttachmentIMG[10] = LoadImage_Strict("GFX\weapons\Icons\INVextmag_ak12.jpg")
	AttachmentIMG[11] = LoadImage_Strict("GFX\weapons\Icons\INVextmag_556.jpg")
	AttachmentIMG[12] = LoadImage_Strict("GFX\weapons\Icons\INVextmag_mp5.jpg")
	AttachmentIMG[13] = LoadImage_Strict("GFX\weapons\Icons\INVextmag_p90.jpg")
	AttachmentIMG[14] = LoadImage_Strict("GFX\weapons\Icons\INVmui.jpg")
	AttachmentIMG[15] = LoadImage_Strict("GFX\HUD\Attachments\att_locked.png")
	
	; ~ Weapon 2d photos
	
	GunAttachmentPreviewIMG[0] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_null.png")
	GunAttachmentPreviewIMG[1] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_p90.png")
	GunAttachmentPreviewIMG[2] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_usp.png")
	GunAttachmentPreviewIMG[3] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_mp5k.png")
	GunAttachmentPreviewIMG[4] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_beretta.png")
	GunAttachmentPreviewIMG[5] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_glock.png")
	GunAttachmentPreviewIMG[6] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_fiveseven.png")
	GunAttachmentPreviewIMG[7] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_mp7.png")
	GunAttachmentPreviewIMG[8] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_hk416.png")
	GunAttachmentPreviewIMG[9] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_sw500.png")
	GunAttachmentPreviewIMG[10] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_ak12.png")
	GunAttachmentPreviewIMG[11] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_p30l.png")
	GunAttachmentPreviewIMG[12] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_p99.png")
	GunAttachmentPreviewIMG[13] = LoadImage_Strict("GFX\HUD\Attachments\att_wpn_emr-p.png")
	
	SCPIcons[0] = LoadImage_Strict("GFX\HUD\198_Icon.png")
	SCPIcons[1] = LoadImage_Strict("GFX\HUD\207_Icon.png")
	SCPIcons[2] = LoadImage_Strict("GFX\HUD\268_Icon.png")
	SCPIcons[3] = LoadImage_Strict("GFX\HUD\1033ru_Icon.png")
	
	DrawLoading(3,False, "", "IMAGES")
	Panel294 = LoadImage_Strict("GFX\HUD\294_HUD.jpg")
	MaskImage(Panel294, 255,0,255)
	Brightness% = GetINIFloat(gv\OptionFile, "options", "brightness", 20)
	CameraFogNear# = GetINIFloat(gv\OptionFile, "options", "camera fog near", 0.5)
	CameraFogFar# = GetINIFloat(gv\OptionFile, "options", "camera fog far", 6.0)
	StoredCameraFogFar# = CameraFogFar
	
	LoadMaterials("Data\Materials.ini")
	
	AmbientLightRoomTex% = CreateTextureUsingCacheSystem(2,2,1)
	TextureBlend AmbientLightRoomTex,2
	SetBuffer(TextureBuffer(AmbientLightRoomTex))
	ClsColor 0,0,0
	Cls
	SetBuffer BackBuffer()
	AmbientLightRoomVal = 0
	SoundEmitter = CreatePivot()
	
	Camera = CreateCamera()
	CameraViewport Camera,0,0,opt\GraphicWidth,opt\GraphicHeight
	CameraRange(Camera, 0.01, CameraFogFar)
	CameraFogMode (Camera, 1)
	CameraFogRange (Camera, CameraFogNear, CameraFogFar)
	CameraFogColor (Camera, GetINIInt(gv\OptionFile, "options", "fog r"), GetINIInt(gv\OptionFile, "options", "fog g"), GetINIInt(gv\OptionFile, "options", "fog b"))
	AmbientLight Brightness, Brightness, Brightness
	
	m_I\Cam = CreateCamera(Camera)
	CameraRange m_I\Cam,0.01,20
	CameraFogRange m_I\Cam,CameraFogNear,CameraFogFar
	CameraFogColor m_I\Cam,0,0,0
	CameraFogMode m_I\Cam,1
	CameraClsMode m_I\Cam,0,1
	CameraProjMode m_I\Cam,0
	m_I\MenuLogo = CreateMenuLogo(m_I\Cam)
	m_I\Sprite = CreateSprite(m_I\Cam)
	ScaleSprite m_I\Sprite,3,3
	EntityColor m_I\Sprite,0,0,0
	EntityFX m_I\Sprite,1
	EntityOrder m_I\Sprite,-1
	EntityAlpha m_I\Sprite,0.0
	m_I\SpriteAlpha = 0.0
	MoveEntity m_I\Sprite,0,0,1
	
	ScreenTexs[0] = CreateTextureUsingCacheSystem(512, 512, 1)
	ScreenTexs[1] = CreateTextureUsingCacheSystem(512, 512, 1)
	
	CreateBlurImage()
	CameraProjMode Ark_Blur_Cam,0
	
	OverlayTexture[0] = LoadTexture_Strict("GFX\HUD\fog.jpg",1,2)
	Overlay[0] = CreateSprite(Ark_Blur_Cam)
	ScaleSprite(Overlay[0], 1.0, Float(opt\GraphicHeight)/Float(opt\GraphicWidth))
	EntityTexture(Overlay[0], OverlayTexture[0])
	EntityBlend (Overlay[0], 2)
	EntityOrder Overlay[0], -1000
	MoveEntity(Overlay[0], 0, 0, 1.0)
	
	OverlayTexture[1] = LoadTexture_Strict("GFX\HUD\Gasmask_Overlay_2.jpg", 1)
	Overlay[1] = CreateSprite(Ark_Blur_Cam)
	ScaleSprite(Overlay[1], Max(opt\GraphicWidth / 1024.0, 1.0), Max(opt\GraphicHeight / 1024.0 * 0.8, 0.8))
	EntityTexture(Overlay[1], OverlayTexture[1])
	EntityBlend (Overlay[1], 2)
	EntityFX(Overlay[1], 1)
	EntityOrder Overlay[1], -1003
	MoveEntity(Overlay[1], 0, 0, 1.0)
	HideEntity(Overlay[1])
	
	Overlay[2] = LoadSprite("GFX\HUD\Gasmask_Overlay.jpg",1,Ark_Blur_Cam)
	ScaleSprite Overlay[2],1.0,Float(opt\GraphicHeight)/Float(opt\GraphicWidth)
	EntityBlend (Overlay[2], 2)
	EntityFX(Overlay[2], 1)
	EntityOrder Overlay[2], -2000
	MoveEntity(Overlay[2], 0, 0, 1.0)
	ShowEntity(Overlay[2])
	
	OverlayTexture[3] = LoadTexture_Strict("GFX\HUD\Helmet_Overlay.png", 1)
	Overlay[3] = CreateSprite(Ark_Blur_Cam)
	ScaleSprite(Overlay[3], Max(opt\GraphicWidth / 1024.0, 1.0), Max(opt\GraphicHeight / 1024.0 * 0.8, 0.8))
	EntityTexture(Overlay[3], OverlayTexture[3])
	EntityBlend (Overlay[3], 2)
	EntityFX(Overlay[3], 1)
	EntityOrder Overlay[3], -1003
	MoveEntity(Overlay[3], 0, 0, 1.0)
	HideEntity(Overlay[3])
	
	OverlayTexture[4] = LoadTexture_Strict("GFX\HUD\HDS_Overlay.jpg", 1)
	Overlay[4] = CreateSprite(Ark_Blur_Cam)
	ScaleSprite(Overlay[4], Max(opt\GraphicWidth / 1024.0, 1.0), Max(opt\GraphicHeight / 1024.0 * 0.8, 0.8))
	EntityTexture(Overlay[4], OverlayTexture[4])
	EntityBlend (Overlay[4], 2)
	EntityFX(Overlay[4], 1)
	EntityOrder Overlay[4], -1003
	MoveEntity(Overlay[4], 0, 0, 1.0)
	HideEntity(Overlay[4])
	
	OverlayTexture[5] = LoadTexture_Strict("GFX\HUD\Infect_Overlay.jpg",1,2)
	Overlay[5] = CreateSprite(Ark_Blur_Cam)
	ScaleSprite(Overlay[5], 1.0,Float(opt\GraphicHeight)/Float(opt\GraphicWidth))
	EntityTexture(Overlay[5], OverlayTexture[5])
	EntityBlend (Overlay[5], 3)
	EntityFX(Overlay[5], 1)
	EntityOrder Overlay[5], -1003
	MoveEntity(Overlay[5], 0, 0, 1.0)
	HideEntity(Overlay[5])
	
	OverlayTexture[6] = LoadTexture_Strict("GFX\HUD\NightVision_Overlay.jpg", 1)
	Overlay[6] = CreateSprite(Ark_Blur_Cam)
	ScaleSprite(Overlay[6], Max(opt\GraphicWidth / 1024.0, 1.0), Max(opt\GraphicHeight / 1024.0 * 0.8, 0.8))
	EntityTexture(Overlay[6], OverlayTexture[6])
	EntityBlend (Overlay[6], 2)
	EntityFX(Overlay[6], 1)
	EntityOrder Overlay[6], -1003
	MoveEntity(Overlay[6], 0, 0, 1.0)
	HideEntity(Overlay[6])
	NVBlink = CreateSprite(Ark_Blur_Cam)
	ScaleSprite(NVBlink, Max(opt\GraphicWidth / 1024.0, 1.0), Max(opt\GraphicHeight / 1024.0 * 0.8, 0.8))
	EntityColor(NVBlink,0,0,0)
	EntityFX(NVBlink, 1)
	EntityOrder NVBlink, -1005
	MoveEntity(NVBlink, 0, 0, 1.0)
	HideEntity(NVBlink)
	
	OverlayTexture[7] = LoadTexture_Strict("GFX\HUD\SCP_268_Overlay.png",1)
	Overlay[7] = CreateSprite(Ark_Blur_Cam)
	ScaleSprite(Overlay[7], Max(opt\GraphicWidth / 1024.0, 1.0), Max(opt\GraphicHeight / 1024.0 * 0.8, 0.8))
	EntityTexture(Overlay[7], OverlayTexture[7])
	EntityBlend (Overlay[7], 2)
	EntityFX(Overlay[7], 1)
	EntityOrder Overlay[7], -1003
	MoveEntity(Overlay[7], 0, 0, 1.0)
	HideEntity(Overlay[7])
	
	OverlayTexture[8] = LoadTexture_Strict("GFX\HUD\SCP_409_Overlay.png",1)
	Overlay[8] = CreateSprite(Ark_Blur_Cam)
	ScaleSprite(Overlay[8], 1.0,Float(opt\GraphicHeight)/Float(opt\GraphicWidth))
	EntityTexture(Overlay[8], OverlayTexture[8])
	EntityBlend (Overlay[8], 3)
	EntityFX(Overlay[8], 1)
	EntityOrder Overlay[8], -1003
	MoveEntity(Overlay[8], 0, 0, 1.0)
	HideEntity(Overlay[8])
	
	OverlayTexture[9] = LoadTexture_Strict("GFX\Particles\fog_NV.jpg", 1, 2)
	
	DrawLoading(5,False, "", "OVERLAYS")
	
	TeslaTexture = LoadTexture_Strict("GFX\map\Textures\tesla.jpg",1+2,2)
	
	NavBG = CreateImage(opt\GraphicWidth,opt\GraphicHeight)
	
	OverlayTexture[10] = CreateTextureUsingCacheSystem(1024, 1024, 1 + 2) ; ~ DARK
	SetBuffer(TextureBuffer(OverlayTexture[10]))
	Cls()
	SetBuffer(BackBuffer())
	Overlay[10] = CreateSprite(Ark_Blur_Cam)
	ScaleSprite(Overlay[10], 1.0, Float(opt\GraphicHeight) / Float(opt\GraphicWidth))
	EntityTexture(Overlay[10], OverlayTexture[10])
	EntityBlend(Overlay[10], 1)
	EntityOrder(Overlay[10], -1002)
	MoveEntity(Overlay[10], 0.0, 0.0, 1.0)
	EntityAlpha(Overlay[10], 0.0)
	
	OverlayTexture[11] = CreateTextureUsingCacheSystem(1024, 1024, 1 + 2) ; ~ LIGHT
	SetBuffer TextureBuffer(OverlayTexture[11])
	ClsColor 255, 255, 255
	Cls
	ClsColor 0, 0, 0
	SetBuffer BackBuffer()
	Overlay[11] = CreateSprite(Ark_Blur_Cam)
	ScaleSprite(Overlay[11], 1.0, Float(opt\GraphicHeight)/Float(opt\GraphicWidth))
	EntityTexture(Overlay[11], OverlayTexture[11])
	EntityBlend (Overlay[11], 1)
	EntityOrder Overlay[11], -1002
	MoveEntity(Overlay[11], 0, 0, 1.0)
	HideEntity Overlay[11]
	
	Collider = CreatePivot()
	EntityRadius Collider, 0.15, 0.30
	EntityType Collider, HIT_PLAYER
	
	Head = CreatePivot()
	EntityRadius Head, 0.15
	EntityType Head, HIT_PLAYER
	
	DrawLoading(9,False,"", "MODELS")
	
	LiquidObj = LoadMesh_Strict("GFX\items\cupliquid.x")
	HideEntity LiquidObj
	
	DrawLoading(10,False,"", "MODELS")
	
	Monitor = LoadMesh_Strict("GFX\map\monitor.b3d")
	HideEntity Monitor
	
	CamBaseOBJ = LoadMesh_Strict("GFX\map\cambase.x")
	HideEntity(CamBaseOBJ)
	CamOBJ = LoadMesh_Strict("GFX\map\CamHead.b3d")
	HideEntity(CamOBJ)
	
	LightConeModel = LoadMesh_Strict("GFX\Particles\light_cone.b3d")
	HideEntity LightConeModel
	
	DrawLoading(11,False,"", "MODELS")
	
	LeverBaseOBJ = LoadMesh_Strict("GFX\map\leverbase.x")
	HideEntity LeverBaseOBJ
	LeverOBJ = LoadMesh_Strict("GFX\map\leverhandle.x")
	HideEntity LeverOBJ
	
	DrawLoading(12,False,"", "MODELS")
	
	DrawLoading(13,False,"", "MODELS")
	
	DrawLoading(14,False,"", "MODELS")
	
	DrawLoading(15,False,"", "SPRITES")
	
	O5_Screen[0] = LoadTexture_Strict("GFX\Map\Textures\world_map.png",1,2)
	O5_Screen[1] = LoadAnimTexture("GFX\Map\Textures\po_screen.png",1,1024,512,0,2)
	O5_Screen[2] = LoadAnimTexture("GFX\Map\Textures\bcz_screen.png",1,1024,512,0,2)
	
	SCP_963_2_Screen[0] = LoadTexture_Strict("GFX\Map\Textures\963-2_screen.jpg",1,2)
	SCP_963_2_Screen[1] = LoadAnimTexture("GFX\Map\Textures\963-2_screen_2.jpg",1,1024,512,0,4)
	
	Checkpoint_Screen[0] = LoadAnimTexture("GFX\Map\Textures\Checkpoint_screen.jpg",1,512,256,0,4)
	Checkpoint_Screen[2] = LoadAnimTexture("GFX\Map\Textures\Checkpoint_screen_2.jpg",1,512,256,0,4)
	Checkpoint_Screen[1] = LoadTexture_Strict("GFX\Map\Textures\Checkpoint_screen_main.jpg",1,2)
	
	LightSpriteTex[0] = LoadTexture_Strict("GFX\Particles\light_1.jpg",1,2)
	LightSpriteTex[1] = LoadTexture_Strict("GFX\Particles\light_2.jpg",1,2)
	LightSpriteTex[2] = LoadTexture_Strict("GFX\Particles\light_sprite.jpg",1,2)
	LightSpriteTex[3] = LoadTexture_Strict("GFX\Particles\light_3.jpg",1,2)
	
	LoadDoors()
	
	DrawLoading(18,False,"", "SPRITES")
	
	Surveil_Room_Textures[0] = LoadAnimTexture("GFX\HUD\SL_monitors_checkpoint.jpg",1,512,512,0,4)
	Surveil_Room_Textures[1] = LoadAnimTexture("GFX\HUD\Sl_monitors.jpg",1,256,256,0,8)
	
	DrawLoading(20,False,"", "SPRITES")
	
	LoadDecals()
	
	DrawLoading(25,False,"", "SPRITES")
	
	DrawLoading(26,False,"", "SPRITES")
	
	InitItemTemplates()
	
	DrawLoading(27,False,"", "SPRITES")
	
	ParticleTextures[0] = LoadTexture_Strict("GFX\Particles\smoke.png",1+2,2)
	ParticleTextures[1] = LoadTexture_Strict("GFX\Particles\flash.jpg",1+2,2)
	ParticleTextures[2] = LoadTexture_Strict("GFX\Particles\dust.jpg",1+2,2)
	ParticleTextures[3] = LoadAnimTexture("GFX\Particles\flash_gun.png",1+2,256,256,0,4)
	ParticleTextures[4] = LoadTexture_Strict("GFX\Particles\sun.jpg",1+2,2)
	ParticleTextures[5] = LoadTexture_Strict("GFX\Particles\bloodsprite.png",1+2,2)
	ParticleTextures[6] = LoadTexture_Strict("GFX\Particles\smoke2.png",1+2,2)
	ParticleTextures[7] = LoadTexture_Strict("GFX\Particles\spark.jpg",1+2,2)
	ParticleTextures[8] = LoadTexture_Strict("GFX\Particles\particle.png",1+2,2)
	ParticleTextures[9] = LoadAnimTexture("GFX\Particles\fog_textures.png",1+2,256,256,0,4)
	ParticleTextures[11] = LoadTexture_Strict("GFX\Particles\Water_Particle_2.png",1+2,2)
	ParticleTextures[12] = LoadTexture_Strict("GFX\Particles\Water_Particle_3.png",1+2,2)
	ParticleTextures[13] = LoadTexture_Strict("GFX\Particles\fire_particle.png",1+2,2)
	ParticleTextures[14] = LoadAnimTexture("GFX\Particles\flash_rifle.png",1+2,256,256,0,4)
	ParticleTextures[15] = LoadTexture_Strict("GFX\Particles\Explosions\Explosion_Wave.png",1+2,2)
	ParticleTextures[16] = LoadTexture_Strict("GFX\Particles\Explosions\Explosion_Flash_1.png",1+2,2)
	ParticleTextures[17] = LoadTexture_Strict("GFX\Particles\hg.pt",1+2,2)
	ParticleTextures[18] = LoadAnimTexture("GFX\Particles\electrical_flash.png",1+2,256,256,0,4)
	ParticleTextures[19] = LoadAnimTexture("GFX\Particles\flash_suppressed.png",1+2,256,256,0,4)
	ParticleTextures[20] = LoadTexture_Strict("GFX\Particles\laser_red.png",1+2,2)
	ParticleTextures[21] = LoadTexture_Strict("GFX\Particles\laser_green.png",1+2,2)
	
	WaterParticleTexture[0] = LoadTexture_Strict("GFX\Particles\Water_Particle.png",1+2,2)
	WaterParticleTexture[1] = LoadTexture_Strict("GFX\Particles\Water_Particle_2.png",1+2,2)
	
	; ~ [CREDITS]
	
	CreditsScreen[0] = LoadRMesh("GFX\map\rooms\room2_blue_window\room2_blue_window.rmesh", Null)
	CreditsScreen[1] = LoadRMesh("GFX\map\rooms\room2_ventilation\room2_ventilation.rmesh", Null)
	CreditsScreen[2] = LoadRMesh("GFX\map\rooms\room2_1\room2_opt.rmesh", Null)
	
	For i = 0 To (MaxCreditScreens-1)
		ScaleEntity CreditsScreen[i], RoomScale, RoomScale, RoomScale
		PositionEntity CreditsScreen[i], 0, 100 - 0.75, 2048.0 * RoomScale
		HideEntity CreditsScreen[i]
	Next
	
	; ~ [MOD STUFF]
	
	DrawLoading(28,False,"", "PLAYER")
	
	CreateMainPlayer()
	
	CreateDamageOverlay()
	
	Contain173State% = 0
	Contain173Timer# = 0.0
	
	MapCubeMap = CreateCubeMap("MapCubeMap")
	
	NEI = New NewElevatorInstance
	
	NEI\button_number_tex[0] = LoadAnimTexture("GFX\HUD\elevator_HUD.png",1,64,64,0,3)
	NEI\button_number_tex[1] = LoadAnimTexture("GFX\HUD\elevator_HUD_Zones.png",1,64,64,0,3)
	NEI\button_number_tex[2] = LoadAnimTexture("GFX\HUD\elevator_HUD_Zones_2.png",1,64,64,0,3)
	
	InitGuns()
	
	SZL_LoadEntitiesForZone()
	
	CreateCommunicationAndSocialWheel()
	
	DrawLoading(29,False,"", "WEAPONS")
	
	ApplyHitBoxes(NPCtypeGuard,"Guard")
	ApplyHitBoxes(NPCtype049_2,"Zombie")
	ApplyHitBoxes(NPCtype049_3,"008")
	ApplyHitBoxes(NPCtype049_4,"008")
	ApplyHitBoxes(NPCtype049_5,"008")
	ApplyHitBoxes(NPCType049_6,"Zombie")
	ApplyHitBoxes(NPCTypeElias,"Elias")
	ApplyHitBoxes(NPCtype008,"008")
	ApplyHitBoxes(NPCtype008_2,"008")
	ApplyHitBoxes(NPCtype008_3,"008")
	ApplyHitBoxes(NPCtype008_4,"008")
	ApplyHitBoxes(NPCtype008_5,"008")
	ApplyHitBoxes(NPCtypeD,"Class-D")
	ApplyHitBoxes(NPCtypeD2,"Class-D")
	ApplyHitBoxes(NPCtypeD9341,"Class-D")
	ApplyHitBoxes(NPCtype035,"Class-D")
	ApplyHitBoxes(NPCtype076,"SCP-076-2")
	;ApplyHitBoxes(NPCtype939,"939")
	ApplyHitBoxes(NPCtypeTentacle,"Tentacle")
	ApplyHitBoxes(NPCtypeCI,"Chaos Insurgency")
	ApplyHitBoxes(NPCTypeWoundedGuard,"Wounded_Guard")
	ApplyHitBoxes(NPCtypeNTF,"MTF")
	ApplyHitBoxes(NPCtype1048a,"1048A")
;	ApplyHitBoxes(NPCtype682,"SCP-682")
	ApplyHitBoxes(NPCTypeCITest,"MTF_Enemy")
	
	PreloadAllNPCAnimations()
	
	DrawLoading(30,False,"", "TYPE INSTANCES")
	
	; ~ Type Instances 
	
	; ~ [PLAYER]
	wbi = New WearibleItems
	hds = New HazardousDefenceSuit
	; ~ [SCPs]
	I_005 = New SCP005
	I_008 = New SCP008
	I_016 = New SCP016
	I_035 = New SCP035
	I_059 = New SCP059
	I_109 = New SCP109
	I_127 = New SCP127
	I_198 = New SCP198
	I_207 = New SCP207
	I_268 = New SCP268
	I_330 = New SCP330
	I_357 = New SCP357
	I_402 = New SCP402
	I_409 = New SCP409
	I_427 = New SCP427
	I_500 = New SCP500
	I_714 = New SCP714
	I_1025 = New SCP1025
	I_1033RU = New SCP1033RU
	I_1079 = New SCP1079
	I_1102RU = New SCP1102RU
	; ~ [MENU]
	If MainMenuOpen Then
		If mnsh = Null Then
			mnsh.MenuShop = New MenuShop
		EndIf
	EndIf
	
	CatchErrors("Uncaught LoadEntities")
End Function

;[End Block]

; ~ [GAME RELATED]

;[Block]

Function InitChapters()
	CatchErrors("InitChapters")
	Local it.Items,g.Guns
	
	If cpt\Current > 1 Then
		ecst\WasInLCZCore = True
		ecst\WasInRoom2_SL = True
		ecst\UnlockedAirlock = True
	EndIf
	If cpt\Current > 3 Then
		ecst\EzDoorOpened = True
		ecst\WasInHCZ = True
		ecst\NTFArrived = True
		ecst\UnlockedHDS = True
		ecst\WasInReactor = True
	EndIf
	If cpt\Current > 4 Then
		ecst\WasInO5 = True
	EndIf
	If cpt\Current > 6 Then
		ecst\WasIn076 = True
		ecst\NewCavesEvent = True
	EndIf
	If cpt\Current = 8 Then
		IsStartingFromMenu = False
	EndIf
	
	If cpt\Current = 2 Then
		If (Not TaskExists(TASK_FINDWAY_EZDOOR)) Then
			BeginTask(TASK_FINDWAY_EZDOOR)
		EndIf
	ElseIf cpt\Current = 3 Then
		If (Not TaskExists(TASK_TURN_ON_REACTOR)) Then
			BeginTask(TASK_TURN_ON_REACTOR)
		EndIf
		If TaskExists(TASK_FIND_REACTOR) Then
			EndTask(TASK_FIND_REACTOR)
		EndIf
		If TaskExists(TASK_COME_BACK_TO_GUARD) Then
			EndTask(TASK_COME_BACK_TO_GUARD)
		EndIf
		If TaskExists(TASK_FIND_ROOM3_CT) Then
			EndTask(TASK_FIND_ROOM3_CT)
		EndIf
		If TaskExists(TASK_FIND_MEDKIT) Then
			EndTask(TASK_FIND_MEDKIT)
		EndIf
	ElseIf cpt\Current = 4 Then
		If (Not TaskExists(TASK_LAUNCH_ROCKET)) Then
			BeginTask(TASK_LAUNCH_ROCKET)
		EndIf
		If (Not TaskExists(TASK_GET_TOPSIDE)) Then
			BeginTask(TASK_GET_TOPSIDE)
		EndIf
		If TaskExists(TASK_COME_BACK_TO_GUARD) Then
			EndTask(TASK_COME_BACK_TO_GUARD)
		EndIf
		If TaskExists(TASK_FIND_ROOM3_CT) Then
			EndTask(TASK_FIND_ROOM3_CT)
		EndIf
		If TaskExists(TASK_FIND_MEDKIT) Then
			EndTask(TASK_FIND_MEDKIT)
		EndIf
	ElseIf cpt\Current = 5 Then
		If (Not TaskExists(TASK_FIND_AREA_076)) Then
			BeginTask(TASK_FIND_AREA_076)
		EndIf
	ElseIf cpt\Current = 6 Then
		If (Not TaskExists(TASK_FIND_KEY_IN_076)) Then
			BeginTask(TASK_FIND_KEY_IN_076)
		EndIf
	ElseIf cpt\Current = 7 Then
		If (Not TaskExists(TASK_FIND_CAVES)) Then
			BeginTask(TASK_FIND_CAVES)
		EndIf
	ElseIf cpt\Current = 8 Then
		ecst\WasInCaves = True
		ecst\CIArrived = True
		ecst\WasInO5Again = True
		If (Not TaskExists(TASK_GO_TO_BCZ)) Then
			BeginTask(TASK_GO_TO_BCZ)
		EndIf
	ElseIf cpt\Current = 9 Then
		ecst\UnlockedGateDoors = True
		If (Not TaskExists(TASK_GET_TOPSIDE)) Then
			BeginTask(TASK_GET_TOPSIDE)
		EndIf
	EndIf
	
	; ~ [GEAR]
	
	If cpt\Current > 1 Then
		Local n%,keylvl$
		
		If HasBoughtUSP Then
			it = CreateItem("H&K USP", "usp", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[2] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
			For g = Each Guns
				Select g\ID
					Case 5
						g\CurrAmmo = 12
						g\CurrReloadAmmo = 36
				End Select
			Next
		EndIf
		If HasBoughtBeretta Then
			If cpt\Current > 1 And cpt\Current < 3 Then
				keylvl$ = "3"
			ElseIf cpt\Current> 3 And cpt\Current < 5 Then
				keylvl$ = "4"
			Else
				keylvl$ = "5"
			EndIf
			it = CreateItem("Level "+keylvl$+" Key Card", "key"+keylvl$, 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[1] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
		EndIf
		If HasBoughtMP5 Then
			it = CreateItem("H&K MP5K", "mp5k", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[3] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
			For g = Each Guns
				Select g\ID
					Case 12
						g\CurrAmmo = 30
						g\CurrReloadAmmo = 90
				End Select
			Next
		EndIf
		If HasBoughtP90 Then
			it = CreateItem("FN P90", "p90", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[4] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
			For g = Each Guns
				Select g\ID
					Case 11
						g\CurrAmmo = 50
						g\CurrReloadAmmo = 150
				End Select
			Next
		EndIf
		If HasBoughtMedkit Then
			it = CreateItem("First Aid Kit", "firstaid", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[5] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
		EndIf
		If HasBoughtCrowbar Then
			it = CreateItem("Crowbar", "crowbar", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[0] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
		EndIf
		If HasBoughtKevlar Then
			it = CreateItem("Ballistic Helmet", "helmet", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[7] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
			
			wbi\Vest = True
			psp\Kevlar = 100
		EndIf
		If HasBoughtG36 Then
			it = CreateItem("H&K 416", "hk416", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[6] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
			For g = Each Guns
				Select g\ID
					Case 13
						g\CurrAmmo = 30
						g\CurrReloadAmmo = 90
				End Select
			Next
		EndIf
		If HasBoughtSPAS Then
			it = CreateItem("Franchi SPAS-12", "spas12", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[8] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
			For g = Each Guns
				Select g\ID
					Case 16
						g\CurrAmmo = 6
						g\CurrReloadAmmo = 42
				End Select
			Next
		EndIf
		If HasBoughtR870 Then
			it = CreateItem("SCP-500", "scp500", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[9] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
			I_500\Limit = 0
		EndIf
	EndIf
	
	CatchErrors("Uncaught : InitChapters")
End Function

Function AddShopPoints(points)
	
	If SelectedDifficulty\Name = "Euclid" Then
		mnsh\Points = mnsh\Points + points+50
	ElseIf SelectedDifficulty\Name = "Keter" Then
		mnsh\Points = mnsh\Points + points+100
	ElseIf SelectedDifficulty\Name = "Thaumiel" Then
		mnsh\Points = mnsh\Points + points+200
	ElseIf SelectedDifficulty\Name = "Appolyon" Then
		mnsh\Points = mnsh\Points + points+300
	Else
		mnsh\Points = mnsh\Points + points
	EndIf
	
End Function

Function InitNewGame()
	CatchErrors("InitNewGame")
	
	Local i%, de.Decals, d.Doors, it.Items, r.Rooms, sc.SecurityCams, e.Events, g.Guns
	
	DrawLoading(45,False,"", "CREATING NEW GAME WITH SEED: "+RandomSeed)
	
	If SelectedDifficulty\Name = "Thaumiel" Then
		MaxItemAmount = 5
	ElseIf SelectedDifficulty\Name = "Appolyon" Then
		MaxItemAmount = 3
	Else
		MaxItemAmount = 10
	EndIf
	
	I_005\ChanceToSpawn = Rand(3)
	
	ecst\ChanceToSpawnWolfNote = Rand(5)
	
	psp\IsShowingHUD = True
	
	CanPlayerUseGuns = True
	
	If clm\DMode Lor clm\GuardMode Lor clm\NTFMode Then
		clm\GlobalMode = True
	EndIf
	
	SaveChaptersValueFile()
	InitChapters()
	
	HideDistance# = 15.0
	
	HeartBeatRate = 70
	
	Local strtemp$ = ""
	
	For i = 1 To Len(RandomSeed)
		strtemp = strtemp+Asc(Mid(RandomSeed,i,1))
	Next
	SeedRnd Abs(Int(strtemp))
	
	For i = 0 To 3 
		AccessCode[i] = 0
		AccessCode[i] = Rand(1000,9999)
	Next
;	For i = 0 To 3
;		AccessCode = AccessCode + Rand(1,9)*(10^i)
;	Next
	
	CreateMap()
	InitWayPoints()
	ShowEntity Collider
	
	DrawLoading(79,False,"", "GAME STUFF")
	
	Curr173 = CreateNPC(NPCtype173, 0, -30.0, 0)
	Curr106 = CreateNPC(NPCtype106, 0, -30.0, 0)
	Curr106\State = 70 * 60 * Rand(12,17)
	
	DrawLoading(80,False,"", "GAME STUFF")
	
	For d.Doors = Each Doors
		EntityParent(d\obj, 0)
		If d\obj2 <> 0 Then EntityParent(d\obj2, 0)
		If d\frameobj <> 0 Then EntityParent(d\frameobj, 0)
		For i = 0 To 1
			If d\buttons[i] <> 0 Then EntityParent(d\buttons[i], 0)
			If d\ElevatorPanel[i] <> 0 Then EntityParent(d\ElevatorPanel[i], 0)
		Next
		
		If d\obj2 <> 0 And d\dir = 0 Then
			MoveEntity(d\obj, 0, 0, 8.0 * RoomScale)
			MoveEntity(d\obj2, 0, 0, 8.0 * RoomScale)
		EndIf	
	Next
	
	For it.Items = Each Items
		EntityType (it\collider, HIT_ITEM)
		EntityParent(it\collider, 0)
	Next
	
	DrawLoading(85,False,"", "PLAYER SPAWN")
	
	For sc.SecurityCams= Each SecurityCams
		sc\angle = EntityYaw(sc\obj) + sc\angle
		EntityParent(sc\obj, 0)
	Next	
	
	For r.Rooms = Each Rooms
		For i = 0 To MaxRoomLights-1
			If r\Lights[i]<>0 Then EntityParent(r\Lights[i],0)
		Next
		
		If (Not r\RoomTemplate\DisableDecals) Then
			If Rand(4) = 1 Then
				de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BLOODSPLAT), EntityX(r\obj)+Rnd(- 2,2), 0.003, EntityZ(r\obj)+Rnd(-2,2), 90, Rand(360), 0)
				de\Size = Rnd(0.1, 0.4) : ScaleSprite(de\obj, de\Size, de\Size)
				EntityAlpha(de\obj, Rnd(0.85, 0.95))
			EndIf
			
			If Rand(4) = 1 Then
				de.Decals = CreateDecal(DECAL_DECAY, EntityX(r\obj)+Rnd(- 2,2), 0.003, EntityZ(r\obj)+Rnd(-2,2), 90, Rand(360), 0)
				de\Size = Rnd(0.5, 0.7) : EntityAlpha(de\obj, 0.7) : de\ID = 1 : ScaleSprite(de\obj, de\Size, de\Size)
				EntityAlpha(de\obj, Rnd(0.7, 0.85))
			EndIf
		EndIf
		
		DrawLoading(-1,"", "ZONES")
		
		Select gc\CurrZone
			Case GATE_A_INTRO
				If r\RoomTemplate\Name = "gate_a_intro"
					PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
					PlayerRoom = r
				EndIf
			Case GATE_A_ROAD
				If r\RoomTemplate\Name = "gate_a_road"
					PositionEntity (Collider, EntityX(r\obj)-609*RoomScale, EntityY(r\obj)+288*RoomScale, EntityZ(r\obj)-1198*RoomScale)
					PlayerRoom = r
				EndIf
			Case GATE_A_TOPSIDE
				If r\RoomTemplate\Name = "gate_a_topside"
					PositionEntity (Collider, EntityX(r\obj)+1847*RoomScale, 0.5, EntityZ(r\obj)+728*RoomScale)
					RotateEntity (Collider,0,r\angle+270,0)
					PlayerRoom = r
				EndIf
			Case GATE_B_TOPSIDE
				If r\RoomTemplate\Name = "gate_b_topside"
					PositionEntity (Collider, EntityX(r\obj), 0.5, EntityZ(r\obj))
					PlayerRoom = r
				EndIf
			Case GATE_C_TOPSIDE
				If r\RoomTemplate\Name = "gate_c_topside"
					PositionEntity (Collider, EntityX(r\obj), 0.5, EntityZ(r\obj))
					PlayerRoom = r
				EndIf
			Case EZ
				If clm\NTFMode Then
					If r\RoomTemplate\Name = "gate_a_entrance"
						PositionEntity (Collider, EntityX(r\obj), 0.5, EntityZ(r\obj)-1450.0*RoomScale)
						RotateEntity (Collider,0,r\angle+180,0)
						PlayerRoom = r
					EndIf
				ElseIf clm\GuardMode Then
					If opt\IntroEnabled = True Then
						If r\RoomTemplate\Name = "room2_ws"
							PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					Else
						If r\RoomTemplate\Name = "room2_ws"
							PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					EndIf
				Else
					If cpt\Current > 8 Then
						If r\RoomTemplate\Name = "gate_d_entrance"
							PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					Else
						If r\RoomTemplate\Name = "core_ez"
							PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					EndIf
				EndIf
			Case BCZ
				If r\RoomTemplate\Name = "checkpoint_bcz"
					PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
					RotateEntity (Collider,0,r\angle,0)
					PlayerRoom = r
				EndIf
			Case LCZ
				If (Not clm\DMode) And (Not clm\NTFMode) And (Not clm\GuardMode) Then
					If opt\IntroEnabled = True Then
						If r\RoomTemplate\Name = "room1_intro"
							PositionEntity (Collider, EntityX(r\obj)+735*RoomScale, EntityY(r\obj)+2770*RoomScale, EntityZ(r\obj)+7718*RoomScale)
							RotateEntity (Collider,0,r\angle+180,0)
							PlayerRoom = r
						EndIf
					Else
						If IsStartingFromMenu Then
							If cpt\Current = 1 Lor cpt\Current = 7 Then
								If r\RoomTemplate\Name = "room1_start"
									PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
									RotateEntity (Collider,0,r\angle,0)
									PlayerRoom = r
								EndIf
							Else
								If r\RoomTemplate\Name = "core_lcz"
									PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
									RotateEntity (Collider,0,r\angle,0)
									PlayerRoom = r
								EndIf
							EndIf
						Else
							If r\RoomTemplate\Name = "core_lcz"
								PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
								RotateEntity (Collider,0,r\angle,0)
								PlayerRoom = r
							EndIf
						EndIf
					EndIf
				ElseIf clm\DMode Then
					If opt\IntroEnabled = True Then ;! ~ TODO: Add D Intro!
						If r\RoomTemplate\Name = "cont_173"
							PositionEntity (Collider, EntityX(r\obj),1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					Else
						If r\RoomTemplate\Name = "cont_173"
							PositionEntity (Collider, EntityX(r\obj),1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					EndIf
				Else
					If r\RoomTemplate\Name = "core_lcz"
						PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
						RotateEntity (Collider,0,r\angle,0)
						PlayerRoom = r
					EndIf
				EndIf
			Case HCZ
				If cpt\Current = 3 Then
					If r\RoomTemplate\Name = "facility_reactor_entrance"
						PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
						RotateEntity (Collider,0,r\angle,0)
						PlayerRoom = r
					EndIf
				ElseIf cpt\Current = 8 Lor cpt\Current = 6 Lor cpt\Current = 5 Then
					If r\RoomTemplate\Name = "checkpoint_hcz"
						PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
						RotateEntity (Collider,0,r\angle,0)
						PlayerRoom = r
					EndIf
				Else
					If r\RoomTemplate\Name = "core_hcz"
						PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
						RotateEntity (Collider,0,r\angle,0)
						PlayerRoom = r
					EndIf
				EndIf
			Case REACTOR_AREA
				If r\RoomTemplate\Name = "facility_reactor"
					PositionEntity (Collider, EntityX(r\obj)-989*RoomScale, 1.0, EntityZ(r\obj)-9736*RoomScale)
					PlayerRoom = r
				EndIf
			Case RCZ
				If cpt\Current > 5 Then
					If r\RoomTemplate\Name = "area_076_entrance"
						PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
						RotateEntity (Collider,0,r\angle,0)
						PlayerRoom = r
					EndIf
				Else
					If r\RoomTemplate\Name = "checkpoint_rcz"
						PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
						RotateEntity (Collider,0,r\angle,0)
						PlayerRoom = r
					EndIf
				EndIf
			Case AREA_076
				If r\RoomTemplate\Name = "area_076"
					PositionEntity (Collider, EntityX(r\obj), 0.5, EntityZ(r\obj))
					PlayerRoom = r
				EndIf
			Case AREA_106_ESCAPE
				If r\RoomTemplate\Name = "area_106_escape"
					PositionEntity (Collider, EntityX(r\obj)-173*RoomScale, 0.5, EntityZ(r\obj)-17140*RoomScale)
					PlayerRoom = r
				EndIf
		End Select
	Next
	
	Local j%, sf%, b%, t1%, name$, tex%
	
	If clm\GlobalMode Then
		
		If clm\NTFMode Then
			tex = LoadTexture_Strict("GFX\weapons\hands_ntf.png")
		ElseIf clm\DMode Then
			tex = LoadTexture_Strict("GFX\weapons\hands_d.jpg")
		Else
			tex = LoadTexture_Strict("GFX\weapons\hands.png")
		EndIf
		
		For g = Each Guns
			For i = 1 To CountSurfaces(g\obj)
				sf = GetSurface(g\obj,i)
				b = GetSurfaceBrush(sf)
				If b<>0 Then
					For j = 0 To 7
						t1 = GetBrushTexture(b,j)
						If t1<>0 Then
							name$ = StripPath(TextureName(t1))
							If Left(Lower(name),5) = "hands" Then
								BrushTexture b, tex, 0, j
								PaintSurface sf,b
								DeleteSingleTextureEntryFromCache t1
								Exit
							EndIf
							If name<>"" Then DeleteSingleTextureEntryFromCache t1
						EndIf
					Next
					FreeBrush b
				EndIf
			Next
		Next
		
		DeleteSingleTextureEntryFromCache tex
	EndIf
	
	
	If (Not clm\GuardMode Lor clm\NTFMode) Then
		g_I\Weapon_InSlot[GunSlot1] = ""
		g_I\Weapon_InSlot[GunSlot2] = ""
		g_I\Weapon_InSlot[GunSlot3] = ""
		g_I\HoldingGun = 0
	EndIf
	
	DrawLoading(90,False,"", "ROOM TEMPLATES")
	
	Local rt.RoomTemplates
	For rt.RoomTemplates = Each RoomTemplates
		FreeEntity (rt\obj)
	Next
	
	Delete Each TempWayPoints
	Delete Each TempScreens
	
	Local tfll.TempFluLight
	For tfll = Each TempFluLight
		Delete tfll\position
		Delete tfll\rotation
		Delete tfll
	Next
	
	ResetEntity Collider
	
	InitEvents()
	
	For e.Events = Each Events
		If e\EventName = "room2_nuke"
			e\EventState[0] = 1
			DebugLog "room2_nuke"
		EndIf
		If e\EventName = "cont_106"
			e\EventState[1] = 1
			DebugLog "cont_106"
		EndIf	
		If e\EventName = "surveil_room"
			e\EventState[2] = 1
			DebugLog "surveil_room"
		EndIf
	Next
	
	MoveMouse Viewport_Center_X,Viewport_Center_Y
	
	SetFont fo\Font[Font_Default]
	
	HidePointer()
	
	BlinkTimer = -10
	BlurTimer = 100
	Stamina = 100
	
	fps\Factor[0] = 1.0
	
	DrawLoading(95,False,"", "PLAYER MOVEMENT")
	
	MovePlayer()
	
	DrawLoading(96,False,"", "UPDATING DOORS")
	
	UpdateDoors()
	
	DrawLoading(97,False,"", "UPDATING NPCS")
	
	UpdateNPCs()
	
	DrawLoading(98,False,"", "UPDATING WORLD")
	
	UpdateWorld()
	
	DrawLoading(99,False,"", "FINISHING LOADING PROCESS")
	
	DeleteTextureEntriesFromCache(0)
	
	DrawLoading(100,False,"", "GAME LOADED")
	
	ResetInput()
	
	DropSpeed = 0
	
	ecst\FusesAmount = 0
	
	CatchErrors("Uncaught InitNewGame")
End Function

Function InitLoadGame()
	CatchErrors("InitLoadGame")
	
	Local d.Doors, sc.SecurityCams, rt.RoomTemplates, e.Events,i
	
	DrawLoading(80,False,"", "CALCULATING OBJECTS")
	
	For d.Doors = Each Doors
		EntityParent(d\obj, 0)
		If d\obj2 <> 0 Then EntityParent(d\obj2, 0)
		If d\frameobj <> 0 Then EntityParent(d\frameobj, 0)
		For i = 0 To 1
			If d\buttons[i] <> 0 Then EntityParent(d\buttons[i], 0)
			If d\ElevatorPanel[i] <> 0 Then EntityParent(d\ElevatorPanel[i], 0)
		Next
	Next
	For sc.SecurityCams = Each SecurityCams
		sc\angle = EntityYaw(sc\obj) + sc\angle
		EntityParent(sc\obj, 0)
	Next
	
	SaveChaptersValueFile()
	
	ResetEntity Collider
	
	DrawLoading(90,False,"", "PREPAIRING INTERFACE")
	
	MoveMouse Viewport_Center_X,Viewport_Center_Y
	
	SetFont fo\Font[Font_Default]
	
	HidePointer ()
	
	BlinkTimer = BLINKFREQ
	Stamina = 100
	
	For rt.RoomTemplates = Each RoomTemplates
		If rt\obj <> 0 Then FreeEntity(rt\obj) : rt\obj = 0
	Next
	
	DropSpeed = 0.0
	
	DeleteTextureEntriesFromCache(0)
	
	CatchErrors("Uncaught InitLoadGame")
	DrawLoading(100,False,"", "GAME LOADED")
	
	fps\PrevTime = MilliSecs()
	fps\Factor[0] = 0
	ResetInput()
	
End Function

Function NullGame(nomenuload%=False,playbuttonsfx%=True)
	CatchErrors("Uncaught (NullGame)")
	
	Local i%, x%, y%, lvl
	Local itt.ItemTemplates, s.Screens
	Local rt.RoomTemplates
	Local PlayerRoomName$ = PlayerRoom\RoomTemplate\Name
	Local PlayerRoomZone = gc\CurrZone
	Local n.NPCs
	
	KillSounds()
	
	If playbuttonsfx Then PlaySound_Strict ButtonSFX[0]
	
	DeleteNewElevators()
	
	DeleteTextureEntriesFromCache(2)
	
	DestroySPPlayer()
	
	SaveChaptersValueFile()
	
	UnableToMove% = False
	
	QuickLoadPercent = -1
	QuickLoadPercent_DisplayTimer# = 0
	QuickLoad_CurrEvent = Null
	
	m_msg\DeathTxt$=""
	
	UsedConsole = False
	
	RoomTempID = 0
	
	GameSaved = 0
	
	HideDistance# = 15.0
	
	For lvl = 0 To 0
		For x = 0 To MapWidth - 1
			For y = 0 To MapHeight - 1
				MapTemp[x * MapWidth + y] = 0
				MapFound[x * MapWidth + y] = 0
			Next
		Next
	Next
	For itt.ItemTemplates = Each ItemTemplates
		itt\found = False
	Next
	
	DropSpeed = 0
	Shake = 0
	CurrSpeed = 0
	DeathTimer=0
	HeartBeatVolume = 0
	StaminaEffect = 1.0
	StaminaEffectTimer = 0
	BlinkEffect = 1.0
	BlinkEffectTimer = 0
	SelectedEnding = ""
	EndingTimer = 0
	ExplosionTimer = 0
	CameraShake = 0
	BigCameraShake = 0
	Shake = 0
	LightFlash = 0
	GodMode = 0
	NoClip = 0
	WireframeState = 0
	WireFrame 0
	wbi\GasMask = 0
	hds\Wearing = False
	wbi\Hazmat = 0
	wbi\Vest = 0
	If wbi\NightVision Then
		CameraFogFar = StoredCameraFogFar
		wbi\NightVision = 0
	EndIf
	If mpl\NightVisionEnabled Then
		CameraFogFar = StoredCameraFogFar
	EndIf
	ForceMove = 0.0
	ForceAngle = 0.0	
	Playable = True
	
	cpt\Current = 0
	
	ecst\ChanceToSpawnWolfNote = 0
	
	I_005\ChanceToSpawn = 0
	
	I_008\Timer = 0
	
	I_016\Timer = 0
	
	I_035\Possessed = 0
	
	I_059\Timer = 0
	I_059\Using = False
	
	I_109\Used = 0
	I_109\Timer = 0
	
	I_127\RestoreTimer = 0
	
	I_198\Timer = 0
	I_198\DeathTimer = 0
	I_198\Vomit = 0
	I_198\VomitTimer = 0
	I_198\Injuries = 0
	
	I_207\Timer = 0
	I_207\DeathTimer = 0
	I_207\Factor = 0
	I_207\Limit = 0
	
	I_268\Using = 0
    I_268\Timer = 0
	
	I_330\Taken = 0
	I_330\Timer = 0
	
	I_357\Timer = 0
	I_357\Using = False
	
	I_402\Timer = 0
	I_402\Using = 0
	
	I_500\Limit = 0
	
	I_714\Using = 0
	
	For i = 0 To 5
		I_1025\State[i] = 0
	Next
	
	I_1033RU\Using = 0
	I_1033RU\HP = 0
	I_1033RU\DHP = 0
	
	I_1079\Foam = 0
	I_1079\Trigger = 0
	I_1079\Limit = 0
	I_1079\Take = 0
	
	I_1102RU\IsInside = False
	I_1102RU\State = 0
	
	ecst\FusesAmount = 0
	
	Contained106 = False
	
	If Curr173 <> Null Then Curr173\Idle = False
	
	MTFtimer = 0
	For i = 0 To 9
		MTFrooms[i]=Null
		MTFroomState[i]=0
	Next
	
	For s.Screens = Each Screens
		If s\img <> 0 Then FreeImage s\img
		Delete s
	Next
	
	RefinedItems = 0
	ConsoleInput = ""
	ConsoleOpen = False
	EyeIrritation = 0
	EyeStuck = 0
	ShouldPlay = 0
	KillTimer = 0
	FallTimer = 0
	Stamina = 100
	BlurTimer = 0
	SuperMan = False
	SuperManTimer = 0
	InfiniteStamina% = False
	m_msg\Txt = ""
	m_msg\Timer = 0
	SelectedItem = Null
	
	For i = 0 To MaxItemAmount - 1
		Inventory[i] = Null
	Next
	
	SelectedItem = Null
	
	Delete Each Doors
	Delete Each LightTemplates
	Delete Each Materials
	Delete Each WayPoints
	Delete Each TempWayPoints	
	Delete Each Rooms
	Delete Each ItemTemplates
	Delete Each Items
	Delete Each Props
	Delete Each Decals
	Delete Each NPCs
	Delete Each NPCGun
	Delete Each NPCAnim
	Delete Each TempFluLight
	Delete Each Lever
	Delete Each MenuLogo
	Delete Each TempScreens
	Delete Each Water
	Delete Each FuseBox
	Delete Each FuseBox2
	Delete Each Generator
	Delete Each SoundEmittor
	Delete Each ButtonGen
	Delete Each LeverGen
	Delete Each ParticleGen
	Delete Each DamageBossRadius
	Delete Each TextureInCache
	Delete Each SplashText
	
	Delete wbi
	
	Delete I_005
	Delete I_008
	Delete I_016
	Delete I_059
	Delete I_109
	Delete I_127
	Delete I_198
	Delete I_207
	Delete I_268
	Delete I_330
	Delete I_357
	Delete I_402
	Delete I_427
	Delete I_500
	Delete I_714
	Delete I_1025
	Delete I_1033RU
	Delete I_1079
	Delete I_1102RU
	
	Curr173 = Null
	Curr106 = Null
	Curr096 = Null
	For i = 0 To 6
		MTFrooms[i]=Null
	Next
	ForestNPC = 0
	ForestNPCTex = 0
	
	Local e.Events
	
	For e.Events = Each Events
		If e\Sound<>0 Then FreeSound_Strict e\Sound
		If e\Sound2<>0 Then FreeSound_Strict e\Sound2
		Delete e
	Next
	
	Delete Each SecurityCams
	Delete Each Emitters
	Delete Each Particles
	
	For rt.RoomTemplates = Each RoomTemplates
		rt\obj = 0
	Next
	
	For i = 0 To 5
		If ChannelPlaying(RadioCHN[i]) Then StopChannel(RadioCHN[i])
	Next
	
	Delete Each ConsoleMsg
	
	Delete Each NewTask
	
	DeleteElevatorObjects()
	
	OptionsMenu% = -1
	QuitMSG% = -1
	AchievementsMenu% = -1
	DeafPlayer% = False
	DeafTimer# = 0.0
	IsZombie% = False
	
	DeInitZoneEntities()
	
	DeleteGameEntities()
	
	If gc\CurrGamemode% = 1
		DeleteMission()
	EndIf
	gc\CurrGamemode = -1
	
	Delete Each AchievementMsg
	CurrAchvMSGID = 0
	
	Delete Each DoorInstance
	Delete Each GunInstance
	Delete Each Grenades
	
	DeleteVectors2D()
	DeleteVectors3D()
	
	Delete Each FluLight
	
	ClearWorld
	
	ResetTimingAccumulator()
	
	Camera = 0
	Ark_Blur_Cam = 0
	m_I\Cam = 0
	
	InitFastResize()
	
	If (Not nomenuload)
		Local entry$
		;If opt\Menu3DEnabled Then
;			If entry = "menu_2d" Then
;				entry = "intro"
;			EndIf
			If PlayerRoomZone = LCZ
				If PlayerRoomName = "room1_intro" Then
					entry = "intro"
				ElseIf PlayerRoomName = "room1_start" Then
					entry = "beginning"
				Else
					entry = "lcz"
				EndIf
			ElseIf PlayerRoomZone = HCZ
				entry = "hcz"
			ElseIf PlayerRoomZone = EZ
				entry = "ez"
			ElseIf PlayerRoomZone = RCZ
				entry = "rcz"
			ElseIf PlayerRoomZone = BCZ
				entry = "bcz"
			ElseIf PlayerRoomZone = AREA_076
				entry = "area_076"
			ElseIf PlayerRoomZone = GATE_A_TOPSIDE Lor PlayerRoomZone = GATE_B_TOPSIDE Lor PlayerRoomZone = GATE_C_TOPSIDE Then
				entry = "surface"
			ElseIf PlayerRoomZone = GATE_A_INTRO Then
				entry = "ntf_intro"
			ElseIf PlayerRoomZone = REACTOR_AREA
				entry = "reactor"
			EndIf
			If entry = ""
				If PlayerRoomName = "poketdimension"
					entry = "pd"
				Else
					entry = PlayerRoomName
				EndIf
			EndIf
;		Else
;			entry = "menu_2d"
;		EndIf
		PutINIValue(gv\OptionFile,"options","progress",entry)
		InitConsole(2)
		Delete Each Menu3DInstance
		;If entry = "menu_2d" And (Not opt\Menu3DEnabled) Then
		;	Load3DMenu("intro")
		;Else
			Load3DMenu(entry)
		;EndIf
	EndIf
	
	DeleteMenuGadgets()
	
	CatchErrors("NullGame")
End Function

;[End Block]

Include "SourceCode\Save_Core.bb"

;Include "SourceCode\Subtitles_Core.bb"

; ~ [MISC]

;[Block]

Function f2s$(n#, count%)
	Return Left(n, Len(Int(n))+count+1)
End Function

Function Animate2#(entity%, curr#, start%, quit%, speed#, loop=True)
	Local temp%
	Local newTime#
	
	If speed > 0.0 Then 
		newTime = Max(Min(curr + speed * fps\Factor[0],quit),start)
		
		If loop Then
			If newTime => quit Then
				newTime = start
			Else
			EndIf
		Else
			
		EndIf
	Else
		If start < quit Then
			temp% = start
			start = quit
			quit = temp
		EndIf
		
		If loop Then
			newTime = curr + speed * fps\Factor[0]
			
			If newTime < quit Then newTime = start
			If newTime > start Then newTime = quit
			
		Else
			newTime = Max(Min(curr + speed * fps\Factor[0],start),quit)
		EndIf
	EndIf
	
	SetAnimTime entity, newTime
	Return newTime
	
End Function

Function CheckForPlayerInFacility()
	
	If EntityY(Collider)>100.0
		Return False
	EndIf
	If EntityY(Collider)< -10.0
		Return 2
	EndIf
	If EntityY(Collider)> 7.0 And EntityY(Collider)<=100.0
		Return 2
	EndIf
	
	Return True
End Function

Function TeleportEntity(entity%,x#,y#,z#,customradius#=0.3,isglobal%=False,pickrange#=2.0,dir%=0)
	Local pvt,pick
	
	pvt = CreatePivot()
	PositionEntity(pvt, x,y+0.05,z,isglobal)
	If dir%=0
		RotateEntity pvt,90,0,0
	Else
		RotateEntity pvt,-90,0,0
	EndIf
	pick = EntityPick(pvt,pickrange)
	If pick<>0
		If dir%=0
			PositionEntity(entity, x,PickedY()+customradius#+0.02,z,isglobal)
		Else
			PositionEntity(entity, x,PickedY()+customradius#-0.02,z,isglobal)
		EndIf
		DebugLog "Entity teleported successfully"
	Else
		PositionEntity(entity,x,y,z,isglobal)
		DebugLog "Warning: no ground found when teleporting an entity"
	EndIf
	FreeEntity pvt
	ResetEntity entity
	DebugLog "Teleported entity to: "+EntityX(entity)+"/"+EntityY(entity)+"/"+EntityZ(entity)
	
End Function

Global NTF_Group_Arrived% = False
Global CI_Group_Arrived% = False

Function UpdateMTF()
	
	If ecst\NTFArrived Then
		If PlayerRoom\RoomTemplate\Name = "gate_a_entrance" Then Return
		
		Local r.Rooms, n.NPCs
		Local dist#, i%
		Local entrance.Rooms = Null
		If (Not NTF_Group_Arrived%) Then
			For r.Rooms = Each Rooms
				If Lower(r\RoomTemplate\Name) = "gate_a_entrance" Then entrance = r : Exit
			Next
			
			If entrance <> Null Then 
				;If Abs(EntityZ(entrance\obj)-EntityZ(Collider))<30.0 Then
				If PlayerRoom\RoomTemplate\Name = "core_ez" Then
					If (Not PlayerInNewElevator) Then
						PlayAnnouncement("SFX\Character\MTF\Announc_music.ogg")
						
						n.NPCs = CreateNPC(NPCTypePlotNTF, EntityX(entrance\obj)+0.3*(0),1.0,EntityZ(entrance\obj))
						n\State = MTF_WONDERING
						n\PrevX = 0
						n\EnemyX = EntityX(n\Collider)
						n\EnemyY = 0.25
						n\EnemyZ = EntityZ(n\Collider)
						
						n.NPCs = CreateNPC(NPCTypePlotNTF, EntityX(entrance\obj)+0.3*(1),1.0,EntityZ(entrance\obj))
						n\PrevState = MTF_UNIT_MEDIC
						
						n\texture = "GFX\npcs\Humans\MTF\NTF\NineTailedFox_medic.png"
						Local tex = LoadTexture_Strict(n\texture, 1, 2)
						TextureBlend(tex,5)
						EntityTexture(n\obj, tex)
						DeleteSingleTextureEntryFromCache tex
						
						n\State = MTF_WONDERING
						n\PrevX = 1
						n\EnemyX = EntityX(n\Collider)
						n\EnemyY = 0.25
						n\EnemyZ = EntityZ(n\Collider)
						
						n.NPCs = CreateNPC(NPCTypePlotNTF, EntityX(entrance\obj)+0.3*(2),1.0,EntityZ(entrance\obj))
						n\PrevState = MTF_UNIT_COMMANDER
						
						n\texture = "GFX\npcs\Humans\MTF\NTF\NineTailedFox_commander.png"
						tex = LoadTexture_Strict(n\texture, 1, 2)
						TextureBlend(tex,5)
						EntityTexture(n\obj, tex)
						DeleteSingleTextureEntryFromCache tex
						
						n\State = MTF_WONDERING
						n\PrevX = 2
						n\EnemyX = EntityX(n\Collider)
						n\EnemyY = 0.25
						n\EnemyZ = EntityZ(n\Collider)
						
						MTFtimer = 1.0
						
						NTF_Group_Arrived% = True
					EndIf
				EndIf
				;EndIf
			EndIf
		EndIf
	EndIf
	
;	If entrance <> Null Then 
;		If Abs(EntityZ(entrance\obj)-EntityZ(Collider))<30.0 Then
;			If PlayerInReachableRoom()
	
End Function

Function UpdateCI%()
	Local r.Rooms, n.NPCs
	Local dist#, i%
	Local entrance.Rooms = Null
	
	If ecst\CIArrived Then
		
		If PlayerRoom\RoomTemplate\Name = "gate_b_entrance" Then Return
		
		For r.Rooms = Each Rooms
			If Lower(r\RoomTemplate\Name) = "gate_b_entrance" Then entrance = r : Exit
		Next
		
		If (Not CI_Group_Arrived%) Then
			
			If entrance <> Null Then 
				;If PlayerRoom\RoomTemplate\Name = "core_ez" Then
					;If (Not PlayerInNewElevator) Then
						;PlayAnnouncement("SFX\Character\CI\Announc.ogg")
					;EndIf
				;EndIf
				If Abs(EntityZ(entrance\obj)-EntityZ(Collider))<30.0 Then
					
					PlayAnnouncement("SFX\Character\CI\Announc.ogg")
					
					CITimer = 1.0
					
					For i = 0 To 2
						n.NPCs = CreateNPC(NPCtypeCI, EntityX(entrance\obj)+0.3*(i-1),1.5,EntityZ(entrance\obj))
						
						n\PrevX = i
						n\EnemyX = EntityX(n\Collider)
						n\EnemyY = 0.25
						n\EnemyZ = EntityZ(n\Collider)
					Next
					
					CI_Group_Arrived% = True
				EndIf
			EndIf
			
		EndIf
		
		If CI_Group_Arrived Then
			CITimer = CITimer + fps\Factor[0]
		EndIf
		
		If CITimer > 0 Then
			If CITimer > 70*120 And CITimer < 70*120.1
				If PlayerInReachableRoom()
					PlayAnnouncement("SFX\General\Scary_Announcement.ogg")
				EndIf
			ElseIf CITimer > 70*640 And CITimer < 70*640.1
				If PlayerInReachableRoom()
					PlayAnnouncement("SFX\General\Scary_Announcement.ogg")
				EndIf
			EndIf
		EndIf
		
	EndIf
	
End Function

Function UpdateIntercomSystem()
	
	If ecst\IntercomEnabled Then
		If (Not IsPlayerOutside()) Lor (Not ecst\IntercomIsReady) Then
			ecst\IntercomTimer = ecst\IntercomTimer + (fps\Factor[0]/1.5)
		Else
			ecst\IntercomTimer = 0
		EndIf
	EndIf
	
	If ecst\IntercomTimer > 0 Then
		If ecst\IntercomTimer >= 70*90 And ecst\IntercomTimer < 70*90.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\hostiles_detected.ogg")
		ElseIf ecst\IntercomTimer >= 70*120 And ecst\IntercomTimer < 70*120.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\systems_report_status.ogg")
		ElseIf ecst\IntercomTimer >= 70*240 And ecst\IntercomTimer < 70*240.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\report_fuel_status.ogg")
		ElseIf ecst\IntercomTimer >= 70*330 And ecst\IntercomTimer < 70*330.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\check_fuel_status.ogg")
		ElseIf ecst\IntercomTimer >= 70*450 And ecst\IntercomTimer < 70*450.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\check_fuel_status_"+Rand(1,2)+".ogg")
		ElseIf ecst\IntercomTimer >= 70*560 And ecst\IntercomTimer < 70*560.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\fuel_engaged.ogg")
		ElseIf ecst\IntercomTimer >= 70*730 And ecst\IntercomTimer < 70*730.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\check_power_status.ogg")
		ElseIf ecst\IntercomTimer >= 70*870 And ecst\IntercomTimer < 70*870.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\report_power_status.ogg")
		ElseIf ecst\IntercomTimer >= 70*920 And ecst\IntercomTimer < 70*920.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\report_power_status_"+Rand(1,2)+".ogg")
		ElseIf ecst\IntercomTimer >= 70*1100 And ecst\IntercomTimer < 70*1100.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\power_engaged.ogg")
		EndIf
	EndIf
	
	If MTFtimer > 0 Then
		If MTFtimer <= 70*120
			MTFtimer = MTFtimer + fps\Factor[0]
		ElseIf MTFtimer > 70*120 And MTFtimer < 10000
			If PlayerInReachableRoom()
				PlayAnnouncement("SFX\Character\MTF\AnnouncAfter1.ogg")
			EndIf
			MTFtimer = 10000
		ElseIf MTFtimer >= 10000 And MTFtimer <= 10000+(70*120)
			MTFtimer = MTFtimer + fps\Factor[0]
		ElseIf MTFtimer > 10000+(70*120) And MTFtimer < 20000
			If PlayerInReachableRoom()
				PlayAnnouncement("SFX\Character\MTF\AnnouncAfter2.ogg")
			EndIf
			MTFtimer = 20000
		ElseIf MTFtimer >= 20000 And MTFtimer <= 20000+(70*60)
			MTFtimer = MTFtimer + fps\Factor[0]
		ElseIf MTFtimer > 20000+(70*60) And MTFtimer < 25000
			If PlayerInReachableRoom()
				If CurrD9341 <> Null Then
					If (Not CurrD9341\IsDead) Then
						If Rand(1,7)=1
							PlayAnnouncement("SFX\Character\MTF\ThreatAnnouncPossession.ogg")
						Else
							PlayAnnouncement("SFX\Character\MTF\ThreatAnnounc"+Rand(1,3)+".ogg")
						EndIf
					EndIf
				EndIf
			EndIf
			MTFtimer = 25000
		ElseIf MTFtimer >= 25000 And MTFtimer <= 25000+(70*60)
			MTFtimer = MTFtimer + fps\Factor[0]
		ElseIf MTFtimer > 25000+(70*60) And MTFtimer < 30000
			If PlayerInReachableRoom()
				If Rand(5) = 1 Then
					PlayAnnouncement("SFX\Character\MTF\ThreatAnnouncFinal.ogg")
				Else
					PlayAnnouncement("SFX\Character\MTF\ThreatAnnouncObjective.ogg")
				EndIf
			EndIf
			MTFtimer = 30000
		ElseIf MTFtimer > 30000 And MTFtimer < 35000
			If PlayerInReachableRoom()
				If Contained106 And Contained173 Then 
					PlayAnnouncement("SFX\Character\MTF\ThreatAnnouncExposure.ogg")
				EndIf
			EndIf
			MTFtimer = 35000
		EndIf
	EndIf
	
End Function

Function UpdateOmegaWarhead()
	
	If ecst\OmegaWarheadActivated And ecst\OmegaWarheadDetonate Then
		ecst\OmegaWarheadTimer = ecst\OmegaWarheadTimer + fps\Factor[0]
	EndIf
	If ecst\OmegaWarheadTimer > 0 Then
		If (Not IsPlayerOutside()) Then
			
			ShouldPlay = MUS_THE_UNKNOWN
			
			If ecst\OmegaWarheadTimer < 300*70 Then
				If (Not ChannelPlaying(OmegaWarheadCHN)) Then OmegaWarheadCHN = LoopSound2(OmegaWarheadSFX[0],OmegaWarheadCHN,Camera,Collider)
			ElseIf ecst\OmegaWarheadTimer > 300*70 And ecst\OmegaWarheadTimer < 300.05*70 Then
				If (ChannelPlaying(OmegaWarheadCHN)) Then StopChannel(OmegaWarheadCHN)
				If (Not ChannelPlaying(OmegaWarheadCHN)) Then
					OmegaWarheadCHN = PlaySound_Strict(LoadTempSound("SFX\Alarm\Omega_warhead_detonate.ogg"))
				EndIf
			ElseIf ecst\OmegaWarheadTimer >= 308*70 And ecst\OmegaWarheadTimer < 308.05*70 Then
				ExplosionTimer = 1
			EndIf
		Else
			If ecst\OmegaWarheadTimer > 309*70 Then
				PlaySound_Strict(LoadTempSound("SFX\Ending\GateB\Nuke1.ogg"))
			EndIf
		EndIf
	EndIf
	
End Function

;[End Block]

; ~ [SCPs]

;[Block]

Function Update008()
	Local temp#, i%, r.Rooms,e.Events
	Local de.Decals,p.Particles
	
	Local teleportForInfect% = True
	
	If PlayerRoom\RoomTemplate\Name = "testroom_860"
		For e.Events = Each Events
			If e\EventName = "testroom_860"
				If e\EventState[0] = 1.0
					teleportForInfect = False
				EndIf
				Exit
			EndIf
		Next
	ElseIf PlayerRoom\RoomTemplate\Name = "pocketdimension"
		teleportForInfect = False
	EndIf
	
	If I_008\Timer>0 Then
		ShowEntity Overlay[5]
		
		If I_008\Timer < 93.0 Then
			temp=I_008\Timer
			I_008\Timer = Min(I_008\Timer+fps\Factor[0]*0.002,100)
			
			BlurTimer = Max(I_008\Timer*3*(2.0-CrouchState),BlurTimer)
			
			HeartBeatRate = Max(HeartBeatRate, 100)
			HeartBeatVolume = Max(HeartBeatVolume, I_008\Timer/120.0)
			
			EntityAlpha Overlay[5], Min(((I_008\Timer*0.2)^2)/1000.0,0.5) * (Sin(MilliSecs()/8.0)+2.0)
			
			For i = 0 To 6
				If I_008\Timer>i*15+10 And temp =< i*15+10 Then
					PlaySound_Strict LoadTempSound("SFX\SCP\008\Voices"+i+".ogg")
				EndIf
			Next
			
			If I_008\Timer > 20 And temp =< 20.0 Then
				CreateMsg(GetLocalString("Singleplayer","spc008_1"))
			ElseIf I_008\Timer > 40 And temp =< 40.0
				CreateMsg(GetLocalString("Singleplayer","spc008_2"))
			ElseIf I_008\Timer > 60 And temp =< 60.0
				CreateMsg(GetLocalString("Singleplayer","spc008_3"))
			ElseIf I_008\Timer > 80 And temp =< 80.0
				CreateMsg(GetLocalString("Singleplayer","spc008_4"))
			ElseIf I_008\Timer =>91.5
				BlinkTimer = Max(Min(-10*(I_008\Timer-91.5),BlinkTimer),-10)
				IsZombie = True
				UnableToMove = True
				If I_008\Timer >= 92.7 And temp < 92.7 Then
					If teleportForInfect
						For r.Rooms = Each Rooms
							If r\RoomTemplate\Name="cont_008" Then
								PositionEntity Collider, EntityX(r\Objects[7],True),EntityY(r\Objects[7],True),EntityZ(r\Objects[7],True),True
								ResetEntity Collider
								r\NPC[0] = CreateNPC(NPCtypeD, EntityX(r\Objects[6],True),EntityY(r\Objects[6],True)+0.2,EntityZ(r\Objects[6],True))
								r\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\008\KillScientist1.ogg")
								r\NPC[0]\SoundChn = PlaySound_Strict(r\NPC[0]\Sound)
								e\room\NPC[0]\texture = "GFX\npcs\Humans\Personnel\scientist2.jpg"
								Local tex = LoadTexture_Strict(e\room\NPC[0]\texture, 1, 2)
								TextureBlend(tex,5)
								EntityTexture(e\room\NPC[0]\obj, tex)
								DeleteSingleTextureEntryFromCache tex
								r\NPC[0]\State=6
								PlayerRoom = r
								UnableToMove = False
								Exit
							EndIf
						Next
					EndIf
				EndIf
			EndIf
		Else
			
			temp=I_008\Timer
			I_008\Timer = Min(I_008\Timer+fps\Factor[0]*0.004,100)
			
			If teleportForInfect
				If I_008\Timer < 94.7 Then
					EntityAlpha Overlay[5], 0.5 * (Sin(MilliSecs()/8.0)+2.0)
					BlurTimer = 900
					
					If I_008\Timer > 94.5 Then BlinkTimer = Max(Min(-50*(I_008\Timer-94.5),BlinkTimer),-10)
					PointEntity Collider, PlayerRoom\NPC[0]\Collider
					PointEntity PlayerRoom\NPC[0]\Collider, Collider
					PointEntity Camera, PlayerRoom\NPC[0]\Collider,EntityRoll(Camera)
					ForceMove = 0.75
					UnableToMove = False
					
					Animate2(PlayerRoom\NPC[0]\obj, AnimTime(PlayerRoom\NPC[0]\obj), 357, 381, 0.3)
				ElseIf I_008\Timer < 98.5
					
					EntityAlpha Overlay[5], 0.5 * (Sin(MilliSecs()/5.0)+2.0)
					BlurTimer = 950
					I_1079\Foam = 0
					I_1079\Trigger = 0
					ForceMove = 0.0
					UnableToMove = True
					PointEntity Camera, PlayerRoom\NPC[0]\Collider
					
					If temp < 94.7 Then 
						PlayerRoom\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\008\KillScientist2.ogg")
						PlayerRoom\NPC[0]\SoundChn = PlaySound_Strict(PlayerRoom\NPC[0]\Sound)
						
						m_msg\DeathTxt = GetLocalStringR("Singleplayer","spc008_death_1",Designation)
						m_msg\DeathTxt = m_msg\DeathTxt + GetLocalString("Singleplayer","spc008_death_2")
						
						Kill()
						de.Decals = CreateDecal(DECAL_BLOODSPLAT2, EntityX(PlayerRoom\NPC[0]\Collider), 544*RoomScale + 0.01, EntityZ(PlayerRoom\NPC[0]\Collider),90,Rnd(360),0)
						de\Size = 0.8
						ScaleSprite(de\obj, de\Size,de\Size)
					ElseIf I_008\Timer > 96
						BlinkTimer = Max(Min(-10*(I_008\Timer-96),BlinkTimer),-10)
					Else
						KillTimer = Max(-350, KillTimer)
					EndIf
					
					If PlayerRoom\NPC[0]\State2=0 Then
						Animate2(PlayerRoom\NPC[0]\obj, AnimTime(PlayerRoom\NPC[0]\obj), 13, 19, 0.3,False)
						If AnimTime(PlayerRoom\NPC[0]\obj) => 19 Then PlayerRoom\NPC[0]\State2=1
					Else
						Animate2(PlayerRoom\NPC[0]\obj, AnimTime(PlayerRoom\NPC[0]\obj), 19, 13, -0.3)
						If AnimTime(PlayerRoom\NPC[0]\obj) =< 13 Then PlayerRoom\NPC[0]\State2=0
					EndIf
					
					If ParticleAmount>0
						If Rand(50)=1 Then
							p.Particles = CreateParticle(EntityX(PlayerRoom\NPC[0]\Collider),EntityY(PlayerRoom\NPC[0]\Collider),EntityZ(PlayerRoom\NPC[0]\Collider), 5, Rnd(0.05,0.1), 0.15, 200)
							p\speed = 0.01
							p\SizeChange = 0.01
							p\A = 0.5
							p\Achange = -0.01
							RotateEntity p\pvt, Rnd(360),Rnd(360),0
						EndIf
					EndIf
					
					PositionEntity Head, EntityX(PlayerRoom\NPC[0]\Collider,True), EntityY(PlayerRoom\NPC[0]\Collider,True)+0.65,EntityZ(PlayerRoom\NPC[0]\Collider,True),True
					RotateEntity Head, (1.0+Sin(MilliSecs()/5.0))*15, PlayerRoom\angle-180, 0, True
					MoveEntity Head, 0,0,-0.4
					TurnEntity Head, 80+(Sin(MilliSecs()/5.0))*30,(Sin(MilliSecs()/5.0))*40,0
				EndIf
			Else
				Kill()
				BlinkTimer = Max(Min(-10*(I_008\Timer-96),BlinkTimer),-10)
				m_msg\DeathTxt = ""
			EndIf
		EndIf
		
		
	Else
		HideEntity Overlay[5]
	EndIf
End Function

Function Update016()
	Local prev016timer#,infection016
	prev016timer = I_016\Timer
	
	If I_016\Timer > 0 Then
		
		If I_016\Timer =< 94.05 Then
			If (Not I_427\Using And I_427\Timer < 70*360) Then
				I_016\Timer = ((Min(I_016\Timer+fps\Factor[0]*0.004,100)))
			EndIf						
			BlurTimer = Max(I_016\Timer*3*(2.0-CrouchState),BlurTimer)
		ElseIf I_016\Timer > 94.05 And I_016\Timer < 95.26 Then
			I_016\Timer = Min(I_016\Timer+fps\Factor[0]*0.004,100)
			Playable=False
			BlurTimer=4.0
			CameraShake = (I_016\Timer-94)*0.5
		EndIf
		
		If I_016\Timer > 95 And prev016timer =< 95 Then
			PlaySound_Strict LoadTempSound("SFX\SCP\914\PlayerDeath.ogg")
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		
		If I_016\Timer > 93 And prev016timer =< 93 Then
			PlaySound_Strict LoadTempSound("SFX\SCP\016\016Death.ogg")
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		
		If I_016\Timer > 96 And prev016timer =< 96 Then
			PlaySound_Strict LoadTempSound("SFX\Horror\Horror14.ogg")
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		
		If I_016\Timer >= 95.26 Then
			infection016 = True
		EndIf
		
		If I_016\Timer > 40 And prev016timer =< 40 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_1"))
			PlaySound_Strict LoadTempSound("SFX\Character\Player\Cough3.ogg")
		EndIf
		If I_016\Timer > 55 And prev016timer =< 55 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_2"))
		EndIf
		If I_016\Timer > 70 And prev016timer =< 70 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_3"))
		EndIf
		If I_016\Timer > 85 And prev016timer =< 85 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_4"))
			DamageSPPlayer(Rand(2,3),True)
			BlurTimer = 15
		EndIf
		If I_016\Timer > 76 And prev016timer =< 76 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_5"))
			DamageSPPlayer(Rand(2,3),True)
			BlurTimer = 15
		EndIf
		If I_016\Timer > 79 And prev016timer =< 79 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_6"))
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		If I_016\Timer > 83 And prev016timer =< 83 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_7"))
			PlaySound_Strict LoadTempSound("SFX\Character\Player\Cough1.ogg")
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		If I_016\Timer > 86 And prev016timer =< 86 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_8"))
			PlaySound_Strict LoadTempSound("SFX\Character\Player\Cough3.ogg")
			DamageSPPlayer(Rand(2,3),True)
			Stamina = 999
		EndIf
		If I_016\Timer > 89 And prev016timer =< 89 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_9"))
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		If I_016\Timer > 93 And prev016timer =< 93 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_10"))
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		
		If (infection016 = True) And (KillTimer>=0) Then	
			Playable=True
			m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp016_death_1",Designation)
			m_msg\DeathTxt = m_msg\DeathTxt + GetLocalString("Singleplayer","scp016_death_2")
			Kill()
		EndIf
	EndIf
	
End Function

Function Update059()
	Local prev059timer#, infection059
	prev059timer = I_059\Timer
	
	If I_059\Timer > 0 Then
		
		If I_059\Timer =< 94.05 Then
			If (Not I_427\Using And I_427\Timer < 70*360) Then
				I_059\Timer = ((Min(I_059\Timer+fps\Factor[0]*0.004,100)))
			EndIf						
			BlurTimer = Max(I_059\Timer*3*(2.0-CrouchState),BlurTimer)
		ElseIf I_059\Timer > 94.05 And I_059\Timer < 95.26 Then
			I_059\Timer = Min(I_059\Timer+fps\Factor[0]*0.004,100)
			Playable=False
			BlurTimer=4.0
			LightFlash = 5.0
		EndIf
		
		If I_059\Timer > 94 And prev059timer =< 94 Then
			PlaySound_Strict LoadTempSound("SFX\SCP\059\059WorldEnter.ogg")
			DamageSPPlayer(Rand(5,8),True)
		EndIf
		
		If I_059\Timer >= 100 Then
			infection059 = True
		EndIf
		
		If I_059\Timer > 40 And prev059timer =< 40 Then
			CreateMsg(GetLocalString("Singleplayer","scp059_1"))
		EndIf
		
		If I_059\Timer > 55 And prev059timer =< 40 Then
			CreateMsg(GetLocalString("Singleplayer","scp059_2"))
		EndIf
		
		If I_059\Timer > 73 And prev059timer =< 93 Then
			CreateMsg(GetLocalString("Singleplayer","scp059_3"))
		EndIf
		
		If (infection059 = True) And (KillTimer>=0) Then	
			Playable=True
			m_msg\DeathTxt = GetLocalString("Singleplayer","scp059_death_1")
			m_msg\DeathTxt = m_msg\DeathTxt + GetLocalStringR("Singleplayer","scp059_death_2",Designation)
			Kill()
		EndIf
	Else
	EndIf
End Function

Function Update109()
	Local prev109vomittimer# = I_109\VomitTimer
	
	If I_109\Vomit Then
		I_109\VomitTimer = I_109\VomitTimer + fps\Factor[0]
	EndIf
	
	If I_109\VomitTimer > 70*15 And prev109vomittimer# =< 70*15 Then
		VomitTimer = -15
		I_109\Vomit = False
		I_109\VomitTimer = 1
	EndIf
	
End Function

Function Update198()
	Local prev198timer# = I_198\DeathTimer
	Local prev198vomittimer# = I_198\VomitTimer
	
	If I_198\Timer > 0 Then
		I_198\DeathTimer = I_198\DeathTimer + fps\Factor[0]*0.8
		
		If I_198\DeathTimer > 70*30 And prev198timer# =< 70*30 Then
			I_198\Injuries = 20
			If I_1033RU\HP = 0
				DamageSPPlayer(I_198\Injuries,True,0)
			Else
				Damage1033RU(30 + (5 * SelectedDifficulty\AggressiveNPCs))
			EndIf
		ElseIf I_198\DeathTimer > 70*60 And prev198timer# =< 70*60 Then
			I_198\Injuries = 30
			If I_1033RU\HP = 0
				DamageSPPlayer(I_198\Injuries,True,0)
			Else
				Damage1033RU(40 + (10 * SelectedDifficulty\AggressiveNPCs))
			EndIf
		ElseIf I_198\DeathTimer > 70*90 And prev198timer# =< 70*90 Then
			I_198\Injuries = 40
			If I_1033RU\HP = 0
				DamageSPPlayer(I_198\Injuries,True,0)
			Else
				Damage1033RU(50 + (15 * SelectedDifficulty\AggressiveNPCs))
			EndIf
		EndIf
		
		If I_198\Vomit Then
			I_198\VomitTimer = I_198\VomitTimer + fps\Factor[0]
		EndIf
		
		If I_198\VomitTimer > 70*8 And prev198vomittimer# =< 70*8 Then
			VomitTimer = -15
			I_198\Vomit = False
			I_198\VomitTimer = 1
		EndIf
		
		If I_198\DeathTimer > 70*120 And prev198timer# =< 70*120 Then
			m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp198_death",Designation)
			Kill()
		EndIf
		
	EndIf
	
End Function

Function Update127()
	Local g.Guns,it.Items
	
	For g.Guns = Each Guns
		If g\ID = 18 Then
			If g\CurrAmmo < g\MaxCurrAmmo Then
				I_127\RestoreTimer = I_127\RestoreTimer + fps\Factor[0]
				If I_127\RestoreTimer >= 70*5 Then
					I_127\RestoreTimer = 0
					g\CurrAmmo = g\CurrAmmo + 1
				EndIf
			EndIf
			Exit
		EndIf
	Next
	For it.Items = Each Items
		If it\name = "SCP-127" Then
			If it\state < 60 Then
				I_127\RestoreTimer = I_127\RestoreTimer + fps\Factor[0]
				If I_127\RestoreTimer >= 70*5 Then
					I_127\RestoreTimer = 0
					it\state = it\state + 1
				EndIf
			EndIf
			Exit
		EndIf
	Next
	
End Function

Function Update207()
	
    If I_207\Timer > 0.0 Then
        If (Not I_427\Using = 1 And I_427\Timer < 70 * 360) Then
            I_207\Timer = Min(I_207\Timer + fps\Factor[0] * 0.002, 51)
        EndIf
		If I_207\Factor > 1 And I_207\Factor < 5 Then
			If IsPlayerSprinting Then
				I_207\DeathTimer = I_207\DeathTimer + fps\Factor[0]*(I_207\Factor*I_207\Factor - 2)
			Else
				I_207\DeathTimer = Max(I_207\DeathTimer - fps\Factor[0]*(5 - I_207\Factor),0)
			EndIf
		ElseIf I_207\Factor >= 5 Then
			If IsPlayerSprinting Then
				I_207\DeathTimer = I_207\DeathTimer + fps\Factor[0]*(I_207\Factor*I_207\Factor - 2)
			Else
				I_207\DeathTimer = I_207\DeathTimer + fps\Factor[0]*(I_207\Factor*I_207\Factor - 4)
			EndIf
		EndIf
		
		If I_207\Factor > 1 Then
			If I_207\DeathTimer > 70*360 Then
				HeartBeatRate = Max(HeartBeatRate, I_207\DeathTimer/100)
				HeartBeatVolume = 1.0
			EndIf
			If I_207\DeathTimer > 70*420 And KillTimer >= 0 Then
				If I_207\Factor = 2 Then
					m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp207_death_1",Designation)
				ElseIf I_207\Factor = 3 Then
					m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp207_death_2",Designation)
				ElseIf I_207\Factor = 4 Then
					m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp207_death_3",Designation)
				ElseIf I_207\Factor = 5 Then
					m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp207_death_4",Designation)
				ElseIf I_207\Factor = 6 Then
					PlaySound_Strict(LoadTempSound("SFX\Player\Rude_Scream.ogg"))
					m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp207_death_4",Designation)
				EndIf
				Kill()
			EndIf
			If I_207\Factor >= 7 And KillTimer >= 0 Then
				m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp207_death_4",Designation)
				Kill()
			EndIf
		EndIf
    EndIf
End Function

Function Update268()
	
	If I_268\Using > 0 Then
		
		If I_268\Sound[0] = 0 Then
		    I_268\Sound[0] = LoadSound_Strict("SFX\SCP\268\InvisibilityOn.ogg")
	    EndIf
        If I_268\Sound[1] = 0 Then
		    I_268\Sound[1] = LoadSound_Strict("SFX\SCP\268\InvisibilityOff.ogg")
	    EndIf
		
		If I_268\Using = 2 Then 
            I_268\Timer = Max(I_268\Timer - ((fps\Factor[0] / 1.5)), 0)
        Else
            I_268\Timer = Max(I_268\Timer - (fps\Factor[0]), 0)
        EndIf
    Else
        I_268\Timer = Min(I_268\Timer + fps\Factor[0], 600.0)
    EndIf
	
End Function

Function Use294()
	Local x#,y#, xtemp%,ytemp%, strtemp$, temp%
	
	ShowPointer()
	
	x = opt\GraphicWidth/2 - (ImageWidth(Panel294)/2)
	y = opt\GraphicHeight/2 - (ImageHeight(Panel294)/2)
	DrawImage Panel294, x, y
	
	temp = True
	If PlayerRoom\SoundCHN<>0 Then temp = False
	
	Text x+907, y+185, Input294, True,True
	
	If temp Then
		If MouseHit1 Then
			MouseHit1 = False
			xtemp = Floor((ScaledMouseX()-x-228) / 35.5)
			ytemp = Floor((ScaledMouseY()-y-342) / 36.5)
			
			If ytemp => 0 And ytemp < 5 Then
				If xtemp => 0 And xtemp < 10 Then PlaySound_Strict SplashTextSFX[Rand(0,2)]
			EndIf
			
			strtemp = ""
			
			temp = False
			
			Select ytemp
				Case 0
					strtemp = (xtemp + 1) Mod 10
				Case 1
					Select xtemp
						Case 0
							strtemp = "Q"
						Case 1
							strtemp = "W"
						Case 2
							strtemp = "E"
						Case 3
							strtemp = "R"
						Case 4
							strtemp = "T"
						Case 5
							strtemp = "Y"
						Case 6
							strtemp = "U"
						Case 7
							strtemp = "I"
						Case 8
							strtemp = "O"
						Case 9
							strtemp = "P"
					End Select
				Case 2
					Select xtemp
						Case 0
							strtemp = "A"
						Case 1
							strtemp = "S"
						Case 2
							strtemp = "D"
						Case 3
							strtemp = "F"
						Case 4
							strtemp = "G"
						Case 5
							strtemp = "H"
						Case 6
							strtemp = "J"
						Case 7
							strtemp = "K"
						Case 8
							strtemp = "L"
						Case 9 ;~ Dispense
							temp = True
					End Select
				Case 3
					Select xtemp
						Case 0
							strtemp = "Z"
						Case 1
							strtemp = "X"
						Case 2
							strtemp = "C"
						Case 3
							strtemp = "V"
						Case 4
							strtemp = "B"
						Case 5
							strtemp = "N"
						Case 6
							strtemp = "M"
						Case 7
							strtemp = "-"
						Case 8
							strtemp = " "
						Case 9
							Input294 = Left(Input294, Max(Len(Input294)-1,0))
					End Select
				Case 4
					strtemp = " "
			End Select
			
			Input294 = Input294 + strtemp
			
			Input294 = Left(Input294, Min(Len(Input294),15))
			
			If temp And Input294<>"" Then ; ~ Dispense
				Input294 = Trim(Lower(Input294))
				If Left(Input294, Min(7,Len(Input294))) = "cup of " Then
					Input294 = Right(Input294, Len(Input294)-7)
				ElseIf Left(Input294, Min(9,Len(Input294))) = "a cup of " 
					Input294 = Right(Input294, Len(Input294)-9)
				EndIf
				
				If Input294 <> ""
					Local drink$ = FindSCP294Drink(Input294)
				EndIf
				
				If drink <> "Null" Then
					strtemp$ = GetFileLocalString(Data294, drink, "dispensesound")
					If strtemp="" Then
						PlayerRoom\SoundCHN = PlaySound_Strict (LoadTempSound("SFX\SCP\294\dispense1.ogg"))
					Else
						PlayerRoom\SoundCHN = PlaySound_Strict (LoadTempSound(strtemp))
					EndIf
					
					If Integer(GetFileLocalString(Data294, drink, "explosion")) Then 
						ExplosionTimer = 135
						m_msg\DeathTxt = GetFileLocalString(Data294, drink, "deathmessage")
					EndIf
					
					strtemp$ = GetFileLocalString(Data294, drink, "color")
					
					Local sep1 = Instr(strtemp, ",", 1)
					Local sep2 = Instr(strtemp, ",", sep1+1)
					Local r% = Trim(Left(strtemp, sep1-1))
					Local g% = Trim(Mid(strtemp, sep1+1, sep2-sep1-1))
					Local b% = Trim(Right(strtemp, Len(strtemp)-sep2))
					
					Local alpha# = Float(GetFileLocalString(Data294, drink, "alpha",1.0))
					Local glow = Integer(GetFileLocalString(Data294, drink, "glow"))
					If glow Then alpha = -alpha
					
					Local it.Items
					it.Items = CreateItem("Cup", "cup", EntityX(PlayerRoom\Objects[1],True),EntityY(PlayerRoom\Objects[1],True),EntityZ(PlayerRoom\Objects[1],True), r,g,b,alpha)
					it\name = "Cup of " + Input294
					EntityType (it\collider, HIT_ITEM)
					
				Else
					; ~ Out of range
					Input294 = GetLocalString("Items","scp294_out")
					PlayerRoom\SoundCHN = PlaySound_Strict (LoadTempSound("SFX\SCP\294\outofrange.ogg"))
				EndIf
				
			EndIf
			
		EndIf
		
		If MouseHit2 Lor (Not Using294) Lor InvOpen Then 
			HidePointer()
			Using294 = False
			Input294 = ""
		EndIf
		
	Else ;
		If Input294 <> GetLocalString("Items","scp294_out") Then Input294 = GetLocalString("Items","scp294_1")
		
		If Not ChannelPlaying(PlayerRoom\SoundCHN) Then
			If Input294 <> GetLocalString("Items","scp294_out") Then
				HidePointer()
				Using294 = False
				MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
				Local e.Events
				For e.Events = Each Events
					If e\room = PlayerRoom
						e\EventState[1] = 0
						Exit
					EndIf
				Next
			EndIf
			Input294=""
			PlayerRoom\SoundCHN=0
		EndIf
	EndIf
	
End Function

; ~ Update any ailments inflicted by SCP-294 drinks.

Function Update294()
	Local de.Decals,pvt%
	CatchErrors("Uncaught (Update294)")
	
	If CameraShakeTimer > 0 Then
		CameraShakeTimer = CameraShakeTimer - (fps\Factor[0]/70)
		CameraShake = 2
	EndIf
	
	If VomitTimer > 0 Then
		DebugLog VomitTimer
		VomitTimer = VomitTimer - (fps\Factor[0]/70)
		
		If (MilliSecs() Mod 1600) < Rand(200, 400) Then
			If BlurTimer = 0 Then BlurTimer = Rnd(10, 20)*70
			CameraShake = Rnd(0, 2)
		EndIf
		If Rand(50) = 50 And (MilliSecs() Mod 4000) < 200 Then PlaySound_Strict(CoughSFX[Rand(0,2)])
		If VomitTimer < 10 And Rnd(0, 500 * VomitTimer) < 2 Then
			If (Not ChannelPlaying(VomitCHN)) And (Not Regurgitate) Then
				VomitCHN = PlaySound_Strict(LoadTempSound("SFX\SCP\294\Retch" + Rand(1, 2) + ".ogg"))
				Regurgitate = MilliSecs() + 50
			EndIf
		EndIf
		
		If Regurgitate > MilliSecs() And Regurgitate <> 0 Then
			Mouse_Y_Speed_1 = Mouse_Y_Speed_1 + 1.0
		Else
			Regurgitate = 0
		EndIf
		
	ElseIf VomitTimer < 0 Then
		VomitTimer = VomitTimer - (fps\Factor[0]/70)
		
		If VomitTimer > -5 Then
			If (MilliSecs() Mod 400) < 50 Then CameraShake = 4 
			Mouse_X_Speed_1 = 0.0
			If (Not I_198\Vomit) Then
				Playable = False
			Else
				Playable = True
			EndIf
		Else
			Playable = True
		EndIf
		
		If (Not Vomit) Then
			BlurTimer = 40 * 70
			VomitSFX = LoadSound_Strict("SFX\SCP\294\Vomit.ogg")
			VomitCHN = PlaySound_Strict(VomitSFX)
			EyeIrritation = 9 * 70
			pvt = CreatePivot()
			PositionEntity(pvt, EntityX(Camera), EntityY(Collider) - 0.05, EntityZ(Camera))
			TurnEntity(pvt, 90, 0, 0)
			EntityPick(pvt, 0.3)
			de.Decals = CreateDecal(DECAL_173BLOOD2, PickedX(), PickedY() + 0.005, PickedZ(), 90, 180, 0)
			de\Size = 0.001 : de\SizeChange = 0.001 : de\MaxSize = 0.6 : EntityAlpha(de\obj, 1.0) : EntityColor(de\obj, 0.0, Rnd(200, 255), 0.0) : ScaleSprite de\obj, de\Size, de\Size
			FreeEntity pvt
			Vomit = True
		EndIf
		
		UpdateDecals()
		
		Mouse_Y_Speed_1 = Mouse_Y_Speed_1 + Max((1.0 + VomitTimer / 10), 0.0)
		
		If VomitTimer < -15 Then
			FreeSound_Strict(VomitSFX)
			VomitTimer = 0
			PlaySound_Strict(BreathSFX[0])
			Vomit = False
			If I_198\Vomit Then
				I_198\Vomit = False
			EndIf
			If I_109\Vomit Then
				I_109\Vomit = False
			EndIf
		EndIf
	EndIf
	
	CatchErrors("Update294")
End Function

Function Update357() 
	Local prevI357Timer# = I_357\Timer
	Local i%
	
	For i=0 To MaxItemAmount-1
		If Inventory[i]<>Null Then
			If Inventory[i]\itemtemplate\name="SCP-357" Then
				If (Not I_427\Using=1 And I_427\Timer < 70*360) Then
					I_357\Timer = Min(I_357\Timer+fps\Factor[0]*0.004,75)
				EndIf
			EndIf
		EndIf
	Next
	
	If I_357\Timer > 0 Then
		
		If I_357\Timer > 20.0 And prevI357Timer =< 20.0 Then
			BlurTimer = 1900
		EndIf
		If I_357\Timer > 30.0 And prevI357Timer =< 30.0 Then
			BlurTimer = 3000
		EndIf
		If I_357\Timer > 35.0 And prevI357Timer =< 35.0 Then
			BlurTimer = 4000
		EndIf
		If I_357\Timer > 40.0 And prevI357Timer =< 40.0 Then
			BlurTimer = 5000
			CreateMsg(GetLocalString("Items","scp357_4"))
		EndIf
		If I_357\Timer > 56.0 And prevI357Timer =< 56.0 Then
			BlurTimer = 5000
			CreateMsg(GetLocalString("Items","scp357_5"))
		EndIf
		If I_357\Timer > 65.0 Then
			HeartBeatRate=Max(HeartBeatRate, 70 + I_357\Timer)
			HeartBeatVolume = 1.0
			CameraShake = Sin(I_357\Timer / 5.0) * (I_357\Timer / 15.0)
		EndIf 
		If I_357\Timer > 70.0 And prevI357Timer =< 70.0 Then
			BlurTimer = 5600
			If I_1033RU\HP = 0
				DamageSPPlayer(Rand(5,10),True)
				Playable = False
			Else
				Damage1033RU(50 + (Rand(5) * SelectedDifficulty\AggressiveNPCs))
			EndIf
		EndIf
		If I_357\Timer >= 75.0 Then
			If Rand(2) = 1 Then
				m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp357_death_1",Designation)
			Else
				m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp357_death_2",Designation)
			EndIf
			m_msg\DeathTxt = m_msg\DeathTxt + GetLocalString("Singleplayer","scp357_death_3")
			Kill()
		EndIf
	EndIf
	
End Function

Function Update402() 
    Local prevI402Timer# = I_402\Timer
	
    If I_402\Using > 0 Then
        If I_402\Timer >= 0 Then
            I_402\Timer = Min(I_402\Timer+fps\Factor[0]*0.004, 61)
			
            If I_402\Timer > 10.0 And prevI402Timer =< 10.0 Then
                PlaySound_Strict(CoughSFX[Rand(0, 2)])
                CameraShake = 5
                If I_1033RU\HP = 0
					CreateMsg(GetLocalString("items","scp402_2"))
                    DamageSPPlayer(Rand(2,3),True)
                Else
					CreateMsg(GetLocalString("items","scp402_3"))
                    Damage1033RU(10 + (Rand(5) * SelectedDifficulty\AggressiveNPCs))
                EndIf
            EndIf
			
            If I_402\Timer > 15.0 And prevI402Timer =< 15.0 Then
				CreateMsg(GetLocalString("items","scp402_4"))
            EndIf
			
            If I_1033RU\HP > 0
                If I_402\Timer > 12.0 Then
					CreateMsg(GetLocalString("items","scp402_5"))
                    Damage1033RU(10 + (Rand(5) * SelectedDifficulty\AggressiveNPCs))
					PlaySound_Strict(CoughSFX[Rand(0, 2)])
                    I_402\Using = 0
                EndIf
            EndIf
			
            If I_402\Timer > 20.0 And prevI402Timer =< 20.0 Then
				CreateMsg(GetLocalString("items","scp402_6"))
                CameraShake = 5
				DamageSPPlayer(Rand(2,3),True)
            EndIf
			
            If I_402\Timer > 40.0 And prevI402Timer =< 40.0 Then
				CreateMsg(GetLocalString("items","scp402_7"))
                PlaySound_Strict(CoughSFX[Rand(0, 2)])
                CameraShake = 5
				DamageSPPlayer(Rand(2,3),True)
   	        EndIf
			
            If I_402\Timer > 42.0 Then
                HeartBeatRate=Max(HeartBeatRate, 70+I_402\Timer)
			    HeartBeatVolume = 1.0
            EndIf
			
	        If I_402\Timer > 45.0 And prevI402Timer =< 45.0 Then
	            PlaySound_Strict(CoughSFX[Rand(0, 2)])
	        EndIf
			
	        If I_402\Timer > 50.0 And prevI402Timer =< 50.0 Then
	            CreateMsg(Chr(34)+GetLocalString("Singleplayer","cant_breathe")+Chr(34))
				PlaySound_Strict(CoughSFX[Rand(0, 2)])
	        EndIf
			
            If I_402\Timer > 55.0 And prevI402Timer =< 55.0 Then
	            PlaySound_Strict(CoughSFX[Rand(0, 2)])
	        EndIf
			
	        If I_402\Timer >= 60.0 And prevI402Timer =< 60.0 Then
				PlaySound_Strict(CoughSFX[Rand(0, 2)])
                m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp402_death",Designation)
		        Kill()
	        EndIf   
	    EndIf
	Else
	    I_402\Timer = 0
	EndIf
	
End Function

Function Update409%()
	Local PrevI409Timer# = I_409\Timer
	
	If I_409\Timer > 0.0 Then
		If EntityHidden(Overlay[8]) Then ShowEntity(Overlay[8])
		
		If (Not I_427\Using) And I_427\Timer < 70.0 * 360.0 Then
			If I_409\Revert Then
				I_409\Timer = Max(0.0, I_409\Timer - (fps\Factor[0] * 0.01))
			Else
				I_409\Timer = Min(I_409\Timer + (fps\Factor[0] * 0.004), 100.0)
			EndIf
		EndIf	
		EntityAlpha(Overlay[8], Min(((I_409\Timer * 0.2) ^ 2.0) / 1000.0, 0.5))
		BlurTimer = Max(I_409\Timer * 3.0 * (2.0 - CrouchState), BlurTimer)
		
		If I_409\Timer > 40.0 And PrevI409Timer <= 40.0 Then
			If I_409\Revert Then
				CreateMsg(GetLocalString("Items","scp409_2"))
			Else
				CreateMsg(GetLocalString("Items","scp409_3"))
			EndIf
		ElseIf I_409\Timer > 55.0 And PrevI409Timer <= 55.0
			If I_409\Revert Then
				CreateMsg(GetLocalString("Items","scp409_4"))
			Else
				CreateMsg(GetLocalString("Items","scp409_5"))
			EndIf
		ElseIf I_409\Timer > 70.0 And PrevI409Timer <= 70.0
			If I_409\Revert Then
				CreateMsg(GetLocalString("Items","scp409_6"))
			Else
				CreateMsg(GetLocalString("Items","scp409_7"))
			EndIf
		ElseIf I_409\Timer > 85.0 And PrevI409Timer <= 85.0
			If I_409\Revert Then
				CreateMsg(GetLocalString("Items","scp409_8"))
			Else
				CreateMsg(GetLocalString("Items","scp409_8"))
			EndIf
		ElseIf I_409\Timer > 93.0 And PrevI409Timer <= 93.0
			If (Not I_409\Revert) Then
				PlaySound_Strict(DamageSFX[9])
				DamageSPPlayer(10,True)
			EndIf
		ElseIf I_409\Timer > 94.0
			I_409\Timer = Min(I_409\Timer + (fps\Factor[0] * 0.004), 100.0)
			Playable = False
			BlurTimer = 4.0
			CameraShake = 3.0
		EndIf
		If I_409\Timer >= 55.0 Then
			StaminaEffect = 1.2
			StaminaEffectTimer = 1.0
			Stamina = Min(Stamina, 60.0)
		EndIf
		If I_409\Timer >= 96.9222 Then
			m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp409_death_1",Designation)
			m_msg\DeathTxt = m_msg\DeathTxt + GetLocalString("Singleplayer","scp409_death_2")
			Kill()
		EndIf
	Else
		If I_409\Revert Then I_409\Revert = False
		If (Not EntityHidden(Overlay[8])) Then HideEntity(Overlay[8])	
	EndIf
	
End Function

Function Use427()
	Local i%,pvt%,de.Decals,tempchn%
	Local prevI427Timer# = I_427\Timer
	
	If I_427\Timer < 70*360
		If I_427\Using=True Then
			I_427\Timer = I_427\Timer + fps\Factor[0]
			HealSPPlayer(0.05 * fps\Factor[0])
			If I_008\Timer > 0.0 Then
				I_008\Timer = Max(I_008\Timer - 0.001 * fps\Factor[0],0.0)
			EndIf
			For i = 0 To 5
				If I_1025\State[i]>0.0 Then
					I_1025\State[i] = Max(I_1025\State[i] - 0.001 * fps\Factor[0],0.0)
				EndIf
			Next
			If I_1079\Foam > 0.0 And psp\Health =< 70 Then
				I_1079\Foam = Max(I_1079\Foam - 0.001 * fps\Factor[0], 0.0)
			EndIf
			If I_207\Timer > 0.0 Then
				I_207\Timer = Max(I_207\Timer - 0.001 * fps\Factor[0], 0.0)
			EndIf
			If I_357\Timer > 0.0 Then
				I_357\Timer = Max(I_357\Timer - 0.002 * fps\Factor[0],0.0)
			EndIf
			If I_427\Sound[0]=0 Then
				I_427\Sound[0] = LoadSound_Strict("SFX\SCP\427\Effect.ogg")
			EndIf
			If (Not ChannelPlaying(I_427\SoundCHN[0])) Then
				I_427\SoundCHN[0] = PlaySound_Strict(I_427\Sound[0])
			EndIf
			
			If I_427\Timer => 70*180 Then
				If I_427\Sound[1]=0 Then
					I_427\Sound[1] = LoadSound_Strict("SFX\SCP\427\Transform.ogg")
				EndIf
				If (Not ChannelPlaying(I_427\SoundCHN[1])) Then
					I_427\SoundCHN[1] = PlaySound_Strict(I_427\Sound[1])
				EndIf
			EndIf
			If prevI427Timer < 70*60 And I_427\Timer => 70*60 Then
				CreateMsg(GetLocalString("Singleplayer","scp427_1"))
			ElseIf prevI427Timer < 70*180 And I_427\Timer => 70*180 Then
				CreateMsg(GetLocalString("Singleplayer","scp427_2"))
			EndIf
		Else
			For i = 0 To 1
				If I_427\SoundCHN[i]<>0 Then
					If ChannelPlaying(I_427\SoundCHN[i]) Then
						StopChannel(I_427\SoundCHN[i])
					EndIf
				EndIf
			Next
		EndIf
	Else
		If prevI427Timer-fps\Factor[0] < 70*360 And I_427\Timer => 70*360 Then
			CreateMsg(GetLocalString("Singleplayer","scp427_3"))
		ElseIf prevI427Timer-fps\Factor[0] < 70*390 And I_427\Timer => 70*390 Then
			CreateMsg(GetLocalString("Singleplayer","scp427_4"))
		EndIf
		I_427\Timer = I_427\Timer + fps\Factor[0]
		If I_427\Sound[0]=0 Then
			I_427\Sound[0] = LoadSound_Strict("SFX\SCP\427\Effect.ogg")
		EndIf
		If I_427\Sound[1]=0 Then
			I_427\Sound[1] = LoadSound_Strict("SFX\SCP\427\Transform.ogg")
		EndIf
		For i = 0 To 1
			If (Not ChannelPlaying(I_427\SoundCHN[i])) Then
				I_427\SoundCHN[i] = PlaySound_Strict(I_427\Sound[i])
			EndIf
		Next
		If Rnd(200)<2.0 Then
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(Collider)+Rnd(-0.05,0.05),EntityY(Collider)-0.05,EntityZ(Collider)+Rnd(-0.05,0.05)
			TurnEntity pvt, 90, 0, 0
			EntityPick(pvt,0.3)
			de.Decals = CreateDecal(DECAL_FOAM, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
			de\Size = Rnd(0.03,0.08)*2.0 : EntityAlpha(de\obj, 1.0) : ScaleSprite de\obj, de\Size, de\Size
			tempchn% = PlaySound_Strict (DripSFX[Rand(0,3)])
			ChannelVolume tempchn, Rnd(0.0,0.8)*(opt\SFXVolume*opt\MasterVol)
			ChannelPitch tempchn, Rand(20000,30000)
			FreeEntity pvt
			BlurTimer = 800
		EndIf
		If I_427\Timer >= 70*420 Then
			Kill()
			m_msg\DeathTxt = Chr(34)+GetLocalString("Singleplayer","scp427_death")+Chr(34)
		ElseIf I_427\Timer >= 70*390 Then
			Crouch = True
		EndIf
	EndIf
	
End Function

Const ROUGH% = -2, COARSE% = -1, ONETOONE% = 0, FINE% = 1, VERY_FINE% = 2

Function Use914(item.Items, setting%, x#, y#, z#)
	Local g.Guns,d.Decals,n.NPCs,i,it.Items
	
	RefinedItems = RefinedItems+1
	
	Local it2.Items
	Select item\itemtemplate\name
		Case "Gas Mask", "Heavy Gas Mask"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case ONETOONE
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case FINE, VERY_FINE
					it2 = CreateItem("Gas Mask", "supergasmask", x, y, z)
					RemoveItem(item)
			End Select
		Case "Ballistic Vest"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case ONETOONE
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case FINE
					it2 = CreateItem("Heavy Ballistic Vest", "finevest", x, y, z)
					RemoveItem(item)
				Case VERY_FINE
					it2 = CreateItem("Bulky Ballistic Vest", "veryfinevest", x, y, z)
					RemoveItem(item)
			End Select
		Case "Clipboard"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_PAPERSTRIPS, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					For i% = 0 To 19
						If item\SecondInv[i]<>Null Then RemoveItem(item\SecondInv[i])
						item\SecondInv[i]=Null
					Next
					RemoveItem(item)
				Case ONETOONE
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case FINE
					item\invSlots = Max(item\state2,15)
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case VERY_FINE
					item\invSlots = Max(item\state2,20)
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
			End Select
		Case "Wallet"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_PAPERSTRIPS, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					For i% = 0 To 19
						If item\SecondInv[i]<>Null Then RemoveItem(item\SecondInv[i])
						item\SecondInv[i]=Null
					Next
					RemoveItem(item)
				Case ONETOONE
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case FINE
					item\invSlots = Max(item\state2,15)
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case VERY_FINE
					item\invSlots = Max(item\state2,20)
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
			End Select
		Case "Cowbell"
			Select setting
				Case ROUGH,COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8*RoomScale+0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case ONETOONE,FINE,VERY_FINE
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
			End Select
		Case "Night Vision Goggles"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case ONETOONE
					it2 = CreateItem("SCRAMBLE Gear", "scramble", x, y, z)
					RemoveItem(item)
				Case FINE
					it2 = CreateItem("Night Vision Goggles", "finenvgoggles", x, y, z)
					RemoveItem(item)
				Case VERY_FINE
					it2 = CreateItem("Night Vision Goggles", "supernv", x, y, z)
					it2\state = 1000
					RemoveItem(item)
			End Select
		Case "SCRAMBLE Gear"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case ONETOONE
					it2 = CreateItem("Night Vision Goggles", "supernv", x, y, z)
					RemoveItem(item)
				Case FINE
					it2 = CreateItem("Night Vision Goggles", "supernv", x, y, z)
					it2\state = 1000
					RemoveItem(item)
				Case VERY_FINE
					it2 = CreateItem("Nine-Tailed Fox Helmet", "ntf_helmet", x, y, z)
					it2\state = 1000
					RemoveItem(item)
			End Select
		Case "Nine-Tailed Fox Helmet","Upgraded Nine-Tailed Fox Helmet"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case ONETOONE
					If item\itemtemplate\name = "Nine-Tailed Fox Helmet" Then
						it2 = CreateItem("Ballistic Helmet", "helmet", x, y, z)
					Else
						it2 = CreateItem("Heavy Ballistic Helmet", "finehelmet", x, y, z)
					EndIf
					RemoveItem(item)
				Case FINE
					If item\itemtemplate\name = "Nine-Tailed Fox Helmet" Then
						it2 = CreateItem("Upgraded Nine-Tailed Fox Helmet", "fine_ntf_helmet", x, y, z)
						RemoveItem(item)
					Else
						it2 = CreateItem("Nine-Tailed Fox Helmet", "ntf_helmet", x, y, z)
						RemoveItem(item)
					EndIf
				Case VERY_FINE
					it2 = CreateItem("SCRAMBLE Gear", "scramble", x, y, z)
					it2\state = 1000
					RemoveItem(item)
			End Select
		Case "Ballistic Helmet","Heavy Ballistic Helmet"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case ONETOONE
					it2 = CreateItem("Nine-Tailed Fox Helmet", "ntf_helmet", x, y, z)
					RemoveItem(item)
				Case FINE
					If item\itemtemplate\name = "Ballistic Helmet" Then
						it2 = CreateItem("Nine-Tailed Fox Helmet", "ntf_helmet", x, y, z)
						RemoveItem(item)
					Else
						it2 = CreateItem("Upgraded Nine-Tailed Fox Helmet", "fine_ntf_helmet", x, y, z)
						RemoveItem(item)
					EndIf
				Case VERY_FINE
					it2 = CreateItem("SCRAMBLE Gear", "scramble", x, y, z)
					it2\state = 1000
					RemoveItem(item)
			End Select
		Case "Metal Panel", "SCP-148 Ingot"
			Select setting
				Case ROUGH, COARSE
					it2 = CreateItem("SCP-148 Ingot", "scp148ingot", x, y, z)
					RemoveItem(item)
				Case ONETOONE, FINE, VERY_FINE
					it2 = Null
					For it.Items = Each Items
						If it<>item And it\collider <> 0 And it\Picked = False Then
							If DistanceSquared(EntityX(it\collider,True), EntityX(item\collider, True), EntityZ(it\collider,True), EntityZ(item\collider, True)) < PowTwo(180.0 * RoomScale) Lor DistanceSquared(EntityX(it\collider,True), x, EntityZ(it\collider,True), z) < PowTwo(180.0 * RoomScale) Then
								it2 = it
								Exit
							End If
						End If
					Next
					
					If it2<>Null Then
						Select it2\itemtemplate\tempname
							Case "gasmask", "supergasmask"
								RemoveItem (it2)
								RemoveItem (item)
								
								it2 = CreateItem("Heavy Gas Mask", "gasmask3", x, y, z)
							Case "vest"
								RemoveItem (it2)
								RemoveItem(item)
								it2 = CreateItem("Heavy Ballistic Vest", "finevest", x, y, z)
							Case "hazmatsuit","hazmatsuit2"
								RemoveItem (it2)
								RemoveItem(item)
								it2 = CreateItem("Heavy Hazmat Suit", "hazmatsuit3", x, y, z)
						End Select
					Else 
						If item\itemtemplate\name="SCP-148 Ingot" Then
							it2 = CreateItem("Metal Panel", "scp148", x, y, z)
							RemoveItem(item)
						Else
							PositionEntity(item\collider, x, y, z)
							ResetEntity(item\collider)							
						EndIf
					EndIf					
			End Select
		; ~ Weapons
			; ~ [MELEE]
		Case "Crowbar"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem("Crowbar", "crowbar", x, y, z)
				Case FINE
					If Rand(2)=1 Then
						it2 = CreateItem("Crowbar", "crowbar", x, y, z)
					Else
						it2 = CreateItem("Combat Knife", "knife", x, y, z)
					EndIf
				Case VERY_FINE
					If Rand(2)=1 Then
						it2 = CreateItem("H&K USP", "usp", x, y, z)
						it2\state2 = 36
						it2\state = 12
					Else
						it2 = CreateItem("M9 Beretta", "beretta", x, y, z)
						it2\state2 = 45
						it2\state = 15
					EndIf
			End Select
			RemoveItem(item)
		Case "Combat Knife"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem("Combat Knife", "knife", x, y, z)
				Case FINE
					If Rand(2)=1 Then
						it2 = CreateItem("Crowbar", "crowbar", x, y, z)
					Else
						it2 = CreateItem("Combat Knife", "knife", x, y, z)
					EndIf
				Case VERY_FINE
					If Rand(2)=1 Then
						it2 = CreateItem("H&K USP", "usp", x, y, z)
						it2\state2 = 36
						it2\state = 12
					Else
						it2 = CreateItem("M9 Beretta", "beretta", x, y, z)
						it2\state2 = 45
						it2\state = 15
					EndIf
			End Select
			RemoveItem(item)
			; ~ [HANDGUNS]
		Case "H&K USP"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(2)=1 Then
						it2 = CreateItem("Crowbar", "crowbar", x, y, z)
					Else
						it2 = CreateItem("Combat Knife", "knife", x, y, z)
					EndIf
				Case ONETOONE
					If Rand(3)=1 Then
						it2 = CreateItem("H&K USP", "usp", x, y, z)
						it2\state2 = 36
						it2\state = 12
					ElseIf Rand(3)=2
						it2 = CreateItem("FN Five-Seven", "fiveseven", x, y, z)
						it2\state2 = 60
						it2\state = 20
					Else
						it2 = CreateItem("M9 Beretta", "beretta", x, y, z)
						it2\state2 = 45
						it2\state = 15
					EndIf
				Case FINE
					If Rand(3)=1 Then
						it2 = CreateItem("FN P90", "p90", x, y, z)
						it2\state2 = 150
						it2\state = 50
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
						d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					EndIf
				Case VERY_FINE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					it2 = CreateItem("M9 Beretta", "beretta", x, y, z)
					it2\state2 = 45
					it2\state = 15
			End Select
			RemoveItem(item)
		Case "FN Five-Seven"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(2)=1 Then
						it2 = CreateItem("Crowbar", "crowbar", x, y, z)
					Else
						it2 = CreateItem("Combat Knife", "knife", x, y, z)
					EndIf
				Case ONETOONE
					If Rand(3)=1 Then
						it2 = CreateItem("H&K USP", "usp", x, y, z)
						it2\state2 = 36
						it2\state = 12
					ElseIf Rand(3)=2
						it2 = CreateItem("FN Five-Seven", "fiveseven", x, y, z)
						it2\state2 = 60
						it2\state = 20
					Else
						it2 = CreateItem("M9 Beretta", "beretta", x, y, z)
						it2\state2 = 45
						it2\state = 15
					EndIf
				Case FINE
					If Rand(3)=1 Then
						it2 = CreateItem("FN P90", "p90", x, y, z)
						it2\state2 = 150
						it2\state = 50
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
						d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					EndIf
				Case VERY_FINE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					it2 = CreateItem("H&K USP", "usp", x, y, z)
					it2\state2 = 45
					it2\state = 15
			End Select
			RemoveItem(item)
		Case "M9 Beretta"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(2)=1 Then
						it2 = CreateItem("Crowbar", "crowbar", x, y, z)
					Else
						it2 = CreateItem("Combat Knife", "knife", x, y, z)
					EndIf
				Case ONETOONE
					If Rand(3)=1 Then
						it2 = CreateItem("H&K USP", "usp", x, y, z)
						it2\state2 = 36
						it2\state = 12
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K P30L", "p30l", x, y, z)
						it2\state2 = 51
						it2\state = 17
					Else
						it2 = CreateItem("M9 Beretta", "beretta", x, y, z)
						it2\state2 = 45
						it2\state = 15
					EndIf
				Case FINE
					If Rand(3)=1 Then
						it2 = CreateItem("H&K MP7", "mp7", x, y, z)
						it2\state2 = 90
						it2\state = 30
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
						d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					EndIf
				Case VERY_FINE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					it2 = CreateItem("H&K USP", "usp", x, y, z)
					it2\state2 = 36
					it2\state = 12
			End Select
			RemoveItem(item)
		Case "H&K P30L"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(2)=1 Then
						it2 = CreateItem("Crowbar", "crowbar", x, y, z)
					Else
						it2 = CreateItem("Combat Knife", "knife", x, y, z)
					EndIf
				Case ONETOONE
					If Rand(3)=1 Then
						it2 = CreateItem("H&K USP", "usp", x, y, z)
						it2\state2 = 36
						it2\state = 12
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K P30L", "p30l", x, y, z)
						it2\state2 = 51
						it2\state = 17
					Else
						it2 = CreateItem("Glock-20C", "glock", x, y, z)
						it2\state2 = 45
						it2\state = 15
					EndIf
				Case FINE
					If Rand(3)=1 Then
						it2 = CreateItem("H&K MP7", "mp7", x, y, z)
						it2\state2 = 90
						it2\state = 30
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
						d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					EndIf
				Case VERY_FINE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					it2 = CreateItem("FN Five-Seven", "fiveseven", x, y, z)
					it2\state2 = 60
					it2\state = 20
			End Select
			RemoveItem(item)
		Case "Glock-20C"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(2)=1 Then
						it2 = CreateItem("Crowbar", "crowbar", x, y, z)
					Else
						it2 = CreateItem("Combat Knife", "knife", x, y, z)
					EndIf
				Case ONETOONE
					If Rand(3)=1 Then
						it2 = CreateItem("Glock-20C", "glock", x, y, z)
						it2\state2 = 45
						it2\state = 15
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K P30L", "p30l", x, y, z)
						it2\state2 = 51
						it2\state = 17
					Else
						it2 = CreateItem("FN Five-Seven", "fiveseven", x, y, z)
						it2\state2 = 60
						it2\state = 20
					EndIf
				Case FINE
					If Rand(3)=1 Then
						it2 = CreateItem("H&K MP7", "mp7", x, y, z)
						it2\state2 = 90
						it2\state = 30
					ElseIf Rand(3)=2
						it2 = CreateItem("FN P90", "p90", x, y, z)
						it2\state2 = 150
						it2\state = 50
					Else
						d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
						d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					EndIf
				Case VERY_FINE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					it2 = CreateItem("H&K USP", "usp", x, y, z)
					it2\state2 = 36
					it2\state = 12
			End Select
			RemoveItem(item)
		Case "S&W Model 500"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(3)=1 Then
						it2 = CreateItem("Crowbar", "crowbar", x, y, z)
					ElseIf Rand(3)=2
						it2 = CreateItem("Combat Knife", "knife", x, y, z)
					Else
						it2 = CreateItem("M61 Grenade", "grenade", x, y, z)
					EndIf
				Case ONETOONE
					If Rand(3)=1 Then
						it2 = CreateItem("Glock-20C", "glock", x, y, z)
						it2\state2 = 45
						it2\state = 15
					ElseIf Rand(3)=2
						it2 = CreateItem("S&W Model 500", "sw500", x, y, z)
						it2\state2 = 15
						it2\state = 5
					Else
						it2 = CreateItem("H&K P30L", "p30l", x, y, z)
						it2\state2 = 60
						it2\state = 20
					EndIf
				Case FINE
					If Rand(3)=1 Then
						it2 = CreateItem("Franchi SPAS-12", "spas12", x, y, z)
						it2\state2 = 18
						it2\state = 6
					ElseIf Rand(3)=2
						it2 = CreateItem("XM1014", "xm1014", x, y, z)
						it2\state2 = 24
						it2\state = 8
					Else
						d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
						d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					EndIf
				Case VERY_FINE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
					it2\state2 = 90
					it2\state = 30
			End Select
			RemoveItem(item)
			; ~ [SMGs&PDWs]
		Case "FN P90"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(3)=1 Then
						it2 = CreateItem("M9 Beretta", "beretta", x, y, z)
						it2\state2 = 45
						it2\state = 15
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K USP", "usp", x, y, z)
						it2\state2 = 36
						it2\state = 12
					Else
						it2 = CreateItem("FN Five-Seven", "fiveseven", x, y, z)
						it2\state2 = 60
						it2\state = 20
					EndIf
				Case ONETOONE
					If Rand(3)=1 Then
						it2 = CreateItem("FN P90", "p90", x, y, z)
						it2\state2 = 150
						it2\state = 50
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						it2 = CreateItem("H&K MP7","mp7", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case FINE
					If Rand(5)=1 Then
						it2 = CreateItem("Franchi SPAS-12", "spas12", x, y, z)
						it2\state2 = 18
						it2\state = 6
					ElseIf Rand(5)=3
						d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
						d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					Else
						it2 = CreateItem("H&K 416", "hk416", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case VERY_FINE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
					it2\state2 = 90
					it2\state = 30
			End Select
			RemoveItem(item)
		Case "H&K MP5K"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(3)=1 Then
						it2 = CreateItem("M9 Beretta", "beretta", x, y, z)
						it2\state2 =45
						it2\state = 15
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K P30L", "p30l", x, y, z)
						it2\state2 = 51
						it2\state = 17
					Else
						it2 = CreateItem("FN Five-Seven", "fiveseven", x, y, z)
						it2\state2 = 60
						it2\state = 20
					EndIf
				Case ONETOONE
					If Rand(3)=1 Then
						it2 = CreateItem("FN P90", "p90", x, y, z)
						it2\state2 = 150
						it2\state = 50
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						it2 = CreateItem("H&K MP7","mp7", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case FINE
					If Rand(5)=1 Then
						it2 = CreateItem("Franchi SPAS-12", "spas12", x, y, z)
						it2\state2 = 18
						it2\state = 6
					ElseIf Rand(5)=3
						d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
						d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					Else
						it2 = CreateItem("H&K 416", "hk416", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case VERY_FINE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					it2 = CreateItem("H&K MP7", "mp7", x, y, z)
					it2\state2 = 90
					it2\state = 30
			End Select
			RemoveItem(item)
		Case "H&K MP7"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(3)=1 Then
						it2 = CreateItem("Glock-20C", "glock", x, y, z)
						it2\state2 = 45
						it2\state = 15
					ElseIf Rand(3)=2
						it2 = CreateItem("Walther P99", "p99", x, y, z)
						it2\state2 = 51
						it2\state = 17
					Else
						it2 = CreateItem("FN Five-Seven", "fiveseven", x, y, z)
						it2\state2 = 60
						it2\state = 20
					EndIf
				Case ONETOONE
					If Rand(3)=1 Then
						it2 = CreateItem("FN P90", "p90", x, y, z)
						it2\state2 = 150
						it2\state = 50
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						it2 = CreateItem("H&K MP7","mp7", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case FINE
					If Rand(5)=1 Then
						it2 = CreateItem("Franchi SPAS-12", "spas12", x, y, z)
						it2\state2 = 18
						it2\state = 6
					ElseIf Rand(5)=3
						d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
						d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					Else
						it2 = CreateItem("H&K 416", "hk416", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case VERY_FINE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					it2 = CreateItem("FN P90", "p90", x, y, z)
					it2\state2 = 150
					it2\state = 50
			End Select
			RemoveItem(item)
			; ~ [RIFLES]
		Case "H&K 416"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(3)=1 Then
						it2 = CreateItem("FN P90", "p90", x, y, z)
						it2\state2 = 150
						it2\state = 50
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						it2 = CreateItem("H&K MP7","mp7", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case ONETOONE
					If Rand(2)=1 Then
						it2 = CreateItem("H&K 416", "hk416", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						it2 = CreateItem("AK-12", "ak12", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case FINE
					If Rand(5)=1 Then
						it2 = CreateItem("Franchi SPAS-12", "spas12", x, y, z)
						it2\state2 = 18
						it2\state = 6
					ElseIf Rand(5)=3
						d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
						d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					ElseIf Rand(5)=4
						it2 = CreateItem("AK-12", "ak12", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						it2 = CreateItem("H&K 416", "hk416", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case VERY_FINE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					it2 = CreateItem("AK-12", "ak12", x, y, z)
					it2\state2 = 90
					it2\state = 30
			End Select
			RemoveItem(item)
		Case "AK-12"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(3)=1 Then
						it2 = CreateItem("FN P90", "p90", x, y, z)
						it2\state2 = 150
						it2\state = 50
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						it2 = CreateItem("H&K MP7","mp7", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case ONETOONE
					If Rand(2)=1 Then
						it2 = CreateItem("H&K 416", "hk416", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						it2 = CreateItem("AK-12", "ak12", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case FINE
					If Rand(5)=1 Then
						it2 = CreateItem("Franchi SPAS-12", "spas12", x, y, z)
						it2\state2 = 18
						it2\state = 6
					ElseIf Rand(5)=3
						d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
						d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					Else
						it2 = CreateItem("AK-12", "ak12", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case VERY_FINE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					it2 = CreateItem("H&K 416", "hk416", x, y, z)
					it2\state2 = 90
					it2\state = 30
			End Select
			RemoveItem(item)
			; ~ [SHOTGUNS]
		Case "Franchi SPAS-12"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(3)=1 Then
						it2 = CreateItem("FN P90", "p90", x, y, z)
						it2\state2 = 150
						it2\state = 50
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						it2 = CreateItem("H&K MP7","mp7", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case ONETOONE
						it2 = CreateItem("Franchi SPAS-12", "spas12", x, y, z)
						it2\state2 = 18
						it2\state = 6
				Case FINE
					If Rand(4)=1 Then
						it2 = CreateItem("H&K 416", "hk416", x, y, z)
						it2\state2 = 90
						it2\state = 30
					ElseIf Rand(4)=2
						it2 = CreateItem("AK-12", "ak12", x, y, z)
						it2\state2 = 90
						it2\state = 30
					ElseIf Rand(4)=3
						d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
						d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					Else
						it2 = CreateItem("M61 Grenade", "grenade", x, y, z)
					EndIf
				Case VERY_FINE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					it2 = CreateItem("AK-12", "ak12", x, y, z)
					it2\state2 = 90
					it2\state = 30
			End Select
			RemoveItem(item)
			; ~ [MISC]
		Case "M61 Grenade"
			Select setting
				Case ROUGH,COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE,VERY_FINE
					it2 = CreateItem("M61 Grenade", "grenade", x, y, z)
				Case FINE
						it2 = CreateItem("Franchi SPAS-12", "spas12", x, y, z)
						it2\state2 = 18
						it2\state = 6
			End Select
			RemoveItem(item)
		Case "SCP-127"
			Select setting
				Case ROUGH,COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE,FINE,VERY_FINE
					it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
					it2\state2 = 90
					it2\state = 30
			End Select
			RemoveItem(item)
		Case "XM29"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(3)=1 Then
						it2 = CreateItem("FN P90", "p90", x, y, z)
						it2\state2 = 150
						it2\state = 50
					ElseIf Rand(3)=2
						it2 = CreateItem("H&K MP5K", "mp5k", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						it2 = CreateItem("H&K MP7","mp7", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case ONETOONE
					If Rand(2)=1 Then
						it2 = CreateItem("AK-12", "ak12", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						it2 = CreateItem("XM29", "xm29", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case FINE
					If Rand(5)=1 Then
						it2 = CreateItem("Franchi SPAS-12", "spas12", x, y, z)
						it2\state2 = 18
						it2\state = 6
					ElseIf Rand(5)=3
						d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
						d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					ElseIf Rand(5)=4
						it2 = CreateItem("H&K 416", "hk416", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						it2 = CreateItem("AK-12", "ak12", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case VERY_FINE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					it2 = CreateItem("AK-12", "ak12", x, y, z)
					it2\state2 = 90
					it2\state = 30
			End Select
			RemoveItem(item)
		Case "Electrical Magnetive Rifle - Prototype"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					If Rand(3)=1 Then
						it2 = CreateItem("H&K 416", "hk416", x, y, z)
						it2\state2 = 90
						it2\state = 30
					Else
						it2 = CreateItem("AK-12", "ak12", x, y, z)
						it2\state2 = 90
						it2\state = 30
					EndIf
				Case ONETOONE
					it2 = CreateItem("Electrical Magnetive Rifle - Prototype", "emr-p", x, y, z)
					it2\state2 = 20
					it2\state = 10
				Case FINE,VERY_FINE
					it2 = CreateItem("Strange Battery", "killbat", x, y, z)
			End Select
			RemoveItem(item)
		; ~ End
		Case "Severed Hand", "Black Severed Hand", "Yellow Severed Hand"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_BLOODSPLAT2, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE, FINE, VERY_FINE
					If (item\itemtemplate\name = "Severed Hand")
						it2 = CreateItem("Black Severed Hand", "hand2", x, y, z)
					ElseIf (item\itemtemplate\name = "Yellow Severed hand")
						it2 = CreateItem("Severed Hand", "hand", x, y, z)
					Else
						it2 = CreateItem("Yellow Severed hand", "hand3", x, y, z)
					EndIf
			End Select
			RemoveItem(item)
		Case "First Aid Kit", "Blue First Aid Kit"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					If Rand(2)=1 Then
						it2 = CreateItem("Blue First Aid Kit", "firstaid2", x, y, z)
					Else
						it2 = CreateItem("First Aid Kit", "firstaid", x, y, z)
					EndIf
				Case FINE
					it2 = CreateItem("Small First Aid Kit", "finefirstaid", x, y, z)
				Case VERY_FINE
					it2 = CreateItem("Strange Bottle", "veryfinefirstaid", x, y, z)
			End Select
			RemoveItem(item)
		Case "Level 1 Key Card", "Level 2 Key Card", "Level 3 Key Card", "Level 4 Key Card", "Level 5 Key Card", "Key Card"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem("Playing Card", "misc", x, y, z)
				Case FINE
					Select item\itemtemplate\name
						Case "Level 1 Key Card"
							Select SelectedDifficulty\OtherFactors
								Case EASY
									it2 = CreateItem("Level 2 Key Card", "key2", x, y, z)
								Case NORMAL
									If Rand(5)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Level 2 Key Card", "key2", x, y, z)
									EndIf
								Case HARD
									If Rand(4)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Level 2 Key Card", "key2", x, y, z)
									EndIf
								Case HARDER
									If Rand(3)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Level 2 Key Card", "key2", x, y, z)
									EndIf
								Case IMPOSSIBLE
									If Rand(2)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Level 2 Key Card", "key2", x, y, z)
									EndIf
							End Select
						Case "Level 2 Key Card"
							Select SelectedDifficulty\OtherFactors
								Case EASY
									it2 = CreateItem("Level 3 Key Card", "key3", x, y, z)
								Case NORMAL
									If Rand(4)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Level 3 Key Card", "key3", x, y, z)
									EndIf
								Case HARD,HARDER
									If Rand(3)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Level 3 Key Card", "key3", x, y, z)
									EndIf
								Case IMPOSSIBLE
									If Rand(2)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Level 2 Key Card", "key2", x, y, z)
									EndIf
							End Select
						Case "Level 3 Key Card"
							Select SelectedDifficulty\OtherFactors
								Case EASY
									If Rand(10)=1 Then
										it2 = CreateItem("Level 4 Key Card", "key4", x, y, z)
									Else
										it2 = CreateItem("Playing Card", "misc", x, y, z)	
									EndIf
								Case NORMAL
									If Rand(15)=1 Then
										it2 = CreateItem("Level 4 Key Card", "key4", x, y, z)
									Else
										it2 = CreateItem("Playing Card", "misc", x, y, z)	
									EndIf
								Case HARD
									If Rand(20)=1 Then
										it2 = CreateItem("Level 4 Key Card", "key4", x, y, z)
									Else
										it2 = CreateItem("Playing Card", "misc", x, y, z)	
									EndIf
								Case HARDER
									If Rand(25)=1 Then
										it2 = CreateItem("Level 4 Key Card", "key4", x, y, z)
									Else
										it2 = CreateItem("Playing Card", "misc", x, y, z)	
									EndIf
								Case IMPOSSIBLE
									If Rand(30)=1 Then
										it2 = CreateItem("Level 4 Key Card", "key4", x, y, z)
									Else
										it2 = CreateItem("Playing Card", "misc", x, y, z)	
									EndIf
							End Select
						Case "Level 4 Key Card"
							Select SelectedDifficulty\OtherFactors
								Case EASY
									it2 = CreateItem("Level 5 Key Card", "key5", x, y, z)
								Case NORMAL
									If Rand(4)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Level 5 Key Card", "key5", x, y, z)
									EndIf
								Case HARD,HARDER
									If Rand(3)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Level 5 Key Card", "key5", x, y, z)
									EndIf
								Case IMPOSSIBLE
									If Rand(2)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Level 5 Key Card", "key5", x, y, z)
									EndIf
							End Select
						Case "Level 5 Key Card"	
							Select SelectedDifficulty\OtherFactors
								Case EASY
									If Rand(3)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Key Card Omni", "key6", x, y, z)
									EndIf
								Case NORMAL
									If Rand(5)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Key Card Omni", "key6", x, y, z)
									EndIf
								Case HARD
									If Rand(7)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Key Card Omni", "key6", x, y, z)
									EndIf
								Case HARDER
									If Rand(9)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Key Card Omni", "key6", x, y, z)
									EndIf
								Case IMPOSSIBLE
									If Rand(12)=1 Then
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Else
										it2 = CreateItem("Key Card Omni", "key6", x, y, z)
									EndIf
							End Select
					End Select
				Case VERY_FINE
					Select SelectedDifficulty\OtherFactors
						Case EASY
							If Rand(3)=1 Then
								it2 = CreateItem("Mastercard", "misc", x, y, z)
							Else
								it2 = CreateItem("Key Card Omni", "key6", x, y, z)
							EndIf
						Case NORMAL
							If Rand(5)=1 Then
								it2 = CreateItem("Mastercard", "misc", x, y, z)
							Else
								it2 = CreateItem("Key Card Omni", "key6", x, y, z)
							EndIf
						Case HARD
							If Rand(7)=1 Then
								it2 = CreateItem("Mastercard", "misc", x, y, z)
							Else
								it2 = CreateItem("Key Card Omni", "key6", x, y, z)
							EndIf
						Case HARDER
							If Rand(9)=1 Then
								it2 = CreateItem("Mastercard", "misc", x, y, z)
							Else
								it2 = CreateItem("Key Card Omni", "key6", x, y, z)
							EndIf
						Case IMPOSSIBLE
							If Rand(12)=1 Then
								it2 = CreateItem("Mastercard", "misc", x, y, z)
							Else
								it2 = CreateItem("Key Card Omni", "key6", x, y, z)
							EndIf
					End Select
			End Select
			
			RemoveItem(item)
		Case "Key Card Omni"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					If Rand(2)=1 Then
						it2 = CreateItem("Mastercard", "misc", x, y, z)
					Else
						it2 = CreateItem("Playing Card", "misc", x, y, z)			
					EndIf	
				Case FINE, VERY_FINE
					it2 = CreateItem("Level 1 Key Card", "key1", x, y, z)
			End Select			
			
			RemoveItem(item)
		Case "Playing Card", "Coin", "Quarter"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem("Level 1 Key Card", "key1", x, y, z)	
			    Case FINE, VERY_FINE
					it2 = CreateItem("Level 2 Key Card", "key2", x, y, z)
			End Select
			RemoveItem(item)
		Case "Mastercard"
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					it2 = CreateItem("Quarter", "25ct", x, y, z)
					Local it3.Items,it4.Items,it5.Items
					it3 = CreateItem("Quarter", "25ct", x, y, z)
					it4 = CreateItem("Quarter", "25ct", x, y, z)
					it5 = CreateItem("Quarter", "25ct", x, y, z)
					EntityType (it3\collider, HIT_ITEM)
					EntityType (it4\collider, HIT_ITEM)
					EntityType (it5\collider, HIT_ITEM)
				Case ONETOONE
					it2 = CreateItem("Level 1 Key Card", "key1", x, y, z)	
			    Case FINE, VERY_FINE
					it2 = CreateItem("Level 2 Key Card", "key2", x, y, z)
			End Select
			RemoveItem(item)
		Case "S-NAV 300 Navigator", "S-NAV 310 Navigator", "S-NAV Navigator", "S-NAV Navigator Ultimate"
			Select setting
				Case ROUGH, COARSE
					it2 = CreateItem("Electronical components", "misc", x, y, z)
				Case ONETOONE
					it2 = CreateItem("S-NAV Navigator", "nav", x, y, z)
					it2\state = 100
				Case FINE
					it2 = CreateItem("S-NAV 310 Navigator", "nav", x, y, z)
					it2\state = 100
				Case VERY_FINE
					it2 = CreateItem("S-NAV Navigator Ultimate", "nav", x, y, z)
					it2\state = 101
			End Select
			
			RemoveItem(item)
		Case "Radio Transceiver"
			Select setting
				Case ROUGH, COARSE
					it2 = CreateItem("Electronical components", "misc", x, y, z)
				Case ONETOONE
					it2 = CreateItem("Radio Transceiver", "18vradio", x, y, z)
					it2\state = 100
				Case FINE
					it2 = CreateItem("Radio Transceiver", "fineradio", x, y, z)
					it2\state = 101
				Case VERY_FINE
					it2 = CreateItem("Radio Transceiver", "veryfineradio", x, y, z)
					it2\state = 101
			End Select
			
			RemoveItem(item)
		Case "SCP-513"
			Select setting
				Case ROUGH, COARSE
					PlaySound_Strict LoadTempSound("SFX\SCP\513\914Refine.ogg")
					For n.NPCs = Each NPCs
						If n\NPCtype = NPCtype5131 Then RemoveNPC(n)
					Next
					d.Decals = CreateDecal(DECAL_DECAY, x, 8*RoomScale+0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE, FINE, VERY_FINE
					it2 = CreateItem("SCP-513", "scp513", x, y, z)
					
			End Select
			
			RemoveItem(item)
		Case "Some SCP-420-J", "Cigarette"
			Select setting
				Case ROUGH, COARSE			
					d.Decals = CreateDecal(DECAL_DECAY, x, 8*RoomScale+0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem("Cigarette", "cigarette", x + 1.5, y + 0.5, z + 1.0)
				Case FINE
					it2 = CreateItem("Joint", "420s", x + 1.5, y + 0.5, z + 1.0)
				Case VERY_FINE
					it2 = CreateItem("Smelly Joint", "420s", x + 1.5, y + 0.5, z + 1.0)
			End Select
			
			RemoveItem(item)
		Case "9V Battery", "18V Battery", "Strange Battery","Hazardous Defence Suit Power Cell"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem("18V Battery", "18vbat", x, y, z)
				Case FINE
					it2 = CreateItem("Strange Battery", "killbat", x, y, z)
				Case VERY_FINE
					it2 = CreateItem("Hazardous Defence Suit Power Cell", "hds_fuse", x, y, z)
			End Select
			
			RemoveItem(item)
		Case "ReVision Eyedrops", "RedVision Eyedrops", "Eyedrops"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem("RedVision Eyedrops", "eyedrops", x,y,z)
				Case FINE
					it2 = CreateItem("Eyedrops", "fineeyedrops", x,y,z)
				Case VERY_FINE
					it2 = CreateItem("Eyedrops", "supereyedrops", x,y,z)
			End Select
			
			RemoveItem(item)		
		Case "Hazmat Suit"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem("Hazmat Suit", "hazmatsuit", x,y,z)
				Case FINE
					it2 = CreateItem("Hazmat Suit", "hazmatsuit2", x,y,z)
				Case VERY_FINE
					it2 = CreateItem("Hazmat Suit", "hazmatsuit2", x,y,z)
			End Select
			
			RemoveItem(item)
			
		Case "Syringe"
			Select item\itemtemplate\tempname
				Case "syringe"
					Select setting
						Case ROUGH, COARSE
							d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
							d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
						Case ONETOONE
							it2 = CreateItem("Small First Aid Kit", "finefirstaid", x, y, z)	
						Case FINE
							it2 = CreateItem("Syringe", "finesyringe", x, y, z)
						Case VERY_FINE
							it2 = CreateItem("Syringe", "veryfinesyringe", x, y, z)
					End Select
					
				Case "finesyringe"
					Select setting
						Case ROUGH
							d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
							d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
						Case COARSE
							it2 = CreateItem("First Aid Kit", "firstaid", x, y, z)
						Case ONETOONE
							it2 = CreateItem("Blue First Aid Kit", "firstaid2", x, y, z)	
						Case FINE, VERY_FINE
							it2 = CreateItem("Syringe", "veryfinesyringe", x, y, z)
					End Select
					
				Case "veryfinesyringe"
					Select setting
						Case ROUGH, COARSE, ONETOONE, FINE
							it2 = CreateItem("Electronical components", "misc", x, y, z)	
						Case VERY_FINE
							n.NPCs = CreateNPC(NPCtype008,x,y,z)
							n\State = 2
					End Select
			End Select
			
			RemoveItem(item)
			
		Case "SCP-268"
			Select setting
				Case ROUGH, COARSE
				    ;[Block]
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
					;[End Block]
				Case ONETOONE
				    ;[Block]
					If (item\itemtemplate\tempname = "scp268")
					    it2 = CreateItem("SCP-268", "scp268", x,y,z)
					Else
					    it2 = CreateItem("SCP-268", "super268", x,y,z)
                    EndIf
                    ;[End Block]
				Case FINE,VERY_FINE
				    ;[Block]
					it2 = CreateItem("SCP-268", "super268", x,y,z)
					;[End Block]
			End Select
			
			RemoveItem(item)
			;[End Block]
		Case "SCP-1033-RU"
		    ;[Block]
			Select setting
				Case ROUGH, COARSE
				    ;[Block]
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
					;[End Block]
				Case ONETOONE
				    ;[Block]
					If (item\itemtemplate\tempname = "scp1033ru")
					    it2 = CreateItem("SCP-1033-RU", "scp1033ru", x,y,z)
					    I_1033RU\HP = 100
					    I_1033RU\DHP = 0
					Else
					    it2 = CreateItem("SCP-1033-RU", "super1033ru", x,y,z)
					    I_1033RU\HP = 200
					    I_1033RU\DHP = 0
                    EndIf
                    ;[End Block]
				Case FINE,VERY_FINE
				    ;[Block]
					it2 = CreateItem("SCP-1033-RU", "super1033ru", x,y,z)
					;[End Block]
			End Select
			
			RemoveItem(item)
			;[End Block]
		Case "SCP-500-01", "Upgraded pill", "Pill"
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem("Pill", "pill", x, y, z)
					RemoveItem(item)
				Case FINE
					Local no427Spawn% = False
					For it3.Items = Each Items
						If it3\itemtemplate\tempname = "scp427" Then
							no427Spawn = True
							Exit
						EndIf
					Next
					If (Not no427Spawn) Then
						it2 = CreateItem("SCP-427", "scp427", x, y, z)
					Else
						it2 = CreateItem("Upgraded pill", "scp500death", x, y, z)
					EndIf
					RemoveItem(item)
				Case VERY_FINE
					it2 = CreateItem("Upgraded pill", "scp500death", x, y, z)
					RemoveItem(item)
			End Select
			
		Default
			
			Select item\itemtemplate\tempname
				Case "cup"
					Select setting
						Case ROUGH, COARSE
							d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
							d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
						Case ONETOONE
							it2 = CreateItem("cup", "cup", x,y,z, 255-item\r,255-item\g,255-item\b,item\a)
							it2\name = item\name
							it2\state = item\state
						Case FINE
							it2 = CreateItem("cup", "cup", x,y,z, Min(item\r*Rnd(0.9,1.1),255),Min(item\g*Rnd(0.9,1.1),255),Min(item\b*Rnd(0.9,1.1),255),item\a)
							it2\name = item\name
							it2\state = item\state+1.0
						Case VERY_FINE
							it2 = CreateItem("cup", "cup", x,y,z, Min(item\r*Rnd(0.5,1.5),255),Min(item\g*Rnd(0.5,1.5),255),Min(item\b*Rnd(0.5,1.5),255),item\a)
							it2\name = item\name
							it2\state = item\state*2
							If Rand(5)=1 Then
								ExplosionTimer = 135
							EndIf
					End Select	
					
					RemoveItem(item)
				Case "paper"
					Select setting
						Case ROUGH, COARSE
							d.Decals = CreateDecal(DECAL_PAPERSTRIPS, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
							d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
						Case ONETOONE
							Select Rand(6)
								Case 1
									it2 = CreateItem("Document SCP-106", "paper", x, y, z)
								Case 2
									it2 = CreateItem("Document SCP-079", "paper", x, y, z)
								Case 3
									it2 = CreateItem("Document SCP-173", "paper", x, y, z)
								Case 4
									it2 = CreateItem("Document SCP-895", "paper", x, y, z)
								Case 5
									it2 = CreateItem("Document SCP-682", "paper", x, y, z)
								Case 6
									it2 = CreateItem("Document SCP-860", "paper", x, y, z)
							End Select
						Case FINE, VERY_FINE
							it2 = CreateItem("Origami", "misc", x, y, z)
					End Select
					
					RemoveItem(item)
				Default
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)	
			End Select
			
	End Select
	
	If it2 <> Null Then EntityType (it2\collider, HIT_ITEM)
End Function

Function Damage1033RU(dmg%, rndm%=True)
	Local i
	
	For i = 0 To 3
	    If I_1033RU\Sound[i] = 0 Then
	        I_1033RU\Sound[i] = LoadSound_Strict("SFX\SCP\1033RU\Damage" + i + ".ogg")
	    EndIf
	Next
	
	If I_1033RU\Sound2[0] = 0 Then
	    I_1033RU\Sound2[0] = LoadSound_Strict("SFX\SCP\1033RU\Death.ogg")
	EndIf
	
	;If dmg% > 0 And dmg% < 15 Then
	;    Bloodloss = Bloodloss + Rnd(5)
	;ElseIf dmg% >= 15 And dmg% < 30
	;    Bloodloss = Bloodloss + Rnd(7.5)
	;ElseIf dmg% >= 30 And dmg% < 50
	;    Bloodloss = Bloodloss + Rnd(11.25)
	;Else
	;    Bloodloss = Bloodloss + Rnd(16.875)
	;EndIf
	
	LightFlash = 0.2
	
	If rndm% = True Then dmg% = Rand(Int(dmg%))
	
	I_1033RU\HP = I_1033RU\HP - Int(dmg%)
	
    If I_1033RU\HP =< 0 Then I_1033RU\HP = 0
	
	If I_1033RU\Using = 2 Then
	    If I_1033RU\HP > 200 Then I_1033RU\HP = 200
	Else
	    If I_1033RU\HP > 100 Then I_1033RU\HP = 100
    EndIf
	
	I_1033RU\DHP = I_1033RU\DHP + Int(dmg%)
	
	If I_1033RU\DHP =< 0 Then I_1033RU\DHP = 0
	
	If I_1033RU\Using = 2 Then
	    If I_1033RU\DHP > 200 Then I_1033RU\DHP = 200
	Else
	    If I_1033RU\DHP > 100 Then I_1033RU\DHP = 100
	EndIf
	
	If I_1033RU\HP > 0 Then
	    PlaySound_Strict I_1033RU\Sound[Rand(0, 3)]
	Else
	    PlaySound_Strict I_1033RU\Sound2[0]
	EndIf
	
End Function

Function IsItemGoodFor1162(itt.ItemTemplates)
	
	Select itt\tempname
		Case "knife", "usp", "key3"
			Return True
		Case "misc", "420", "cigarette"
			Return True
		Case "vest", "finevest","helmet"
			Return True
		Case "radio","18vradio"
			Return True
		Case "clipboard","eyedrops","wallet"
			Return True
		Case "drawing"
			If itt\img<>0 Then FreeImage itt\img
			itt\img = LoadImage_Strict("GFX\items\1048\1048_"+Rand(1,20)+".jpg")
			Return True
		Default
			If (Not itt\isPlot) Then
				If itt\tempname <> "paper" Then
					Return False
				Else If Instr(itt\name, "Leaflet")
					Return False
				Else
					Return ((Not Instr(itt\name, "Note")) And (Not Instr(itt\name, "Log")))
				EndIf
			EndIf
	End Select
End Function

;[End Block]

; ~ [DECALS]

Include "SourceCode/Decals.bb"

; ~ [INI]

Include "SourceCode/INI_Core.bb"

; ~ [GRAPHICS]

Include "SourceCode/Graphics_Core.bb"

Function ScaledMouseX%()
	Return Float(MouseX()-(RealGraphicWidth*0.5*(1.0-AspectRatioRatio)))*Float(opt\GraphicWidth)/Float(RealGraphicWidth*AspectRatioRatio)
End Function

Function ScaledMouseY%()
	Return Float(MouseY())*Float(opt\GraphicHeight)/Float(RealGraphicHeight)
End Function

Function CatchErrors(location$)
	SetErrorMsg(7, GetLocalString("Errors","error_located")+location)
End Function

Function InitFonts()
	Local txt$
	
	fo\Font[Font_Default] = LoadFont("GFX\font\Courier New.ttf", Int(16 * (opt\GraphicHeight / 1024.0)))
	fo\Font[Font_Default_Medium] = LoadFont("GFX\font\Courier New.ttf", Int(28 * (opt\GraphicHeight / 1024.0)))
	fo\Font[Font_Default_Large] = LoadFont("GFX\font\Courier New.ttf", Int(46 * (opt\GraphicHeight / 1024.0)))
	
	If I_Loc\Localized Then
		fo\Font[Font_Menu] = LoadFont("GFX\font\Capture it.ttf", Int(56 * (opt\GraphicHeight / 1024.0)))
		fo\Font[Font_Menu_Small] = LoadFont("GFX\font\Capture it.ttf",Int(23 * (opt\GraphicHeight / 1024.0)))
		fo\Font[Font_Menu_Medium] = LoadFont("GFX\font\Capture it.ttf",Int(42 * (opt\GraphicHeight / 1024.0)))
	Else
		fo\Font[Font_Menu_Small] = LoadFont("GFX\font\ITC Bauhaus LT Demi.ttf",Int(23 * (opt\GraphicHeight / 1024.0)))
		fo\Font[Font_Menu_Medium] = LoadFont("GFX\font\ITC Bauhaus LT Demi.ttf",Int(42 * (opt\GraphicHeight / 1024.0)))
		fo\Font[Font_Menu] = LoadFont("GFX\font\ITC Bauhaus LT Demi.ttf", Int(56 * (opt\GraphicHeight / 1024.0)))
	EndIf
	
	fo\Font[Font_Digital_Small] = LoadFont("GFX\font\LCDNovaRus.ttf", Int(17 * (opt\GraphicHeight / 1024.0)))
	fo\Font[Font_Digital_Medium] = LoadFont("GFX\font\LCDNovaRus.ttf", Int(24 * (opt\GraphicHeight / 1024.0)))
	fo\Font[Font_Digital_Large] = LoadFont("GFX\font\LCDNovaRus.ttf", Int(47 * (opt\GraphicHeight / 1024.0)))
	fo\Font[Font_Journal] = LoadFont("GFX\font\Journal.ttf", Int(56 * (opt\GraphicHeight / 1024.0)))
	
	fo\ConsoleFont% = LoadFont("GFX\font\Minimal5x7.ttf", Int(28 * (opt\GraphicHeight / 1024.0)))
	
	SetFont fo\Font[Font_Menu]
	
End Function

Function SetMsgColor(r%, g%, b%)
	Local a# = Min(m_msg\Timer / 2, 255)/255.0
	Color a * r, a * g, a * b
End Function

Function CreateMsg(Txt$,Timer=70*6,isSplash%=True,R#=255,G#=255,B#=255)
	
	If HUDenabled And psp\IsShowingHUD Then
		
		m_msg\isSplash = isSplash
		m_msg\R = R
		m_msg\G = G
		m_msg\B = B
		
		If isSplash Then
			Delete Each SplashMsg
			CreateMsgSplash(Txt,(opt\GraphicWidth / 2),(opt\GraphicHeight / 2) + 200,Timer/2,30,True,R,G,B)
		Else
			m_msg\Txt = Txt
			m_msg\Timer = Timer
		EndIf
	EndIf
	
End Function

Function UpdateMsg()
	
	If HUDenabled Then
		If m_msg\Timer > 0 Then
			If gc\CurrGamemode <> 3 Then
				If (Not MainMenuOpen) Then
					If psp\IsShowingHUD Then
						Local temp% = False
						If (Not InvOpen%) Then
							If SelectedItem <> Null Then
								If SelectedItem\itemtemplate\tempname = "paper" Lor SelectedItem\itemtemplate\tempname = "oldpaper" Then
									temp% = True
								EndIf
							EndIf
						EndIf
						If (Not temp%) Then
							If (Not m_msg\isSplash) Then
								SetMsgColor(0, 0, 0)
								Text((opt\GraphicWidth / 2)+1, (opt\GraphicHeight / 2) + 201, m_msg\Txt, True, False)
								SetMsgColor(255, 255, 255)
								Text((opt\GraphicWidth / 2), (opt\GraphicHeight / 2) + 200, m_msg\Txt, True, False)
							EndIf
						Else
							SetMsgColor(0, 0, 0)
							Text((opt\GraphicWidth / 2)+1, (opt\GraphicHeight * 0.94) + 1, m_msg\Txt, True, False)
							SetMsgColor(255, 255, 255)
							Text((opt\GraphicWidth / 2), (opt\GraphicHeight * 0.94), m_msg\Txt, True, False)
						EndIf
					EndIf
				Else
					SetMsgColor(0, 0, 0)
					Text((opt\GraphicWidth / 2)+1, (opt\GraphicHeight / 2) + 201, m_msg\Txt, True, False)
					SetMsgColor(255, 255, 255)
					If Left(m_msg\Txt,20)="Loaded resource pack" Then
						SetMsgColor(0, 255, 0)
					ElseIf Left(m_msg\Txt,14)="Blitz3D Error!" Then
						SetMsgColor(255, 0, 0)
					EndIf
					Text((opt\GraphicWidth / 2), (opt\GraphicHeight / 2) + 200, m_msg\Txt, True, False)
				EndIf
			Else
				SetFont fo\Font[Font_Default]
				SetMsgColor(0, 0, 0)
				Text((opt\GraphicWidth / 2)+1, (opt\GraphicHeight / 2) + 201, m_msg\Txt, True, False)
				SetMsgColor(255, 255, 255)
				If Left(m_msg\Txt,14)="Blitz3D Error!" Then
					SetMsgColor(255, 0, 0)
				EndIf
				Text((opt\GraphicWidth / 2), (opt\GraphicHeight / 2) + 200, m_msg\Txt, True, False)
			EndIf
		EndIf
	EndIf
	
End Function

Function CreateMsgSplash.SplashMsg(txt$,x#,y#,displayamount#,speed#=1,centered%=False,r#=255,g#=255,b#=255)
	Local m_spl.SplashMsg = New SplashMsg
	
	m_spl\Txt = txt
	m_spl\X = x
	m_spl\Y = y
	m_spl\DisplayAmount = displayamount
	m_spl\Centered = centered
	m_spl\Speed = speed
	m_spl\R# = r
	m_spl\G# = g
	m_spl\B# = b
	
	Return m_spl
End Function

Function UpdateSplashMsg()
	Local m_spl.SplashMsg
	
	For m_spl = Each SplashMsg
		If m_spl\CurrentLength < Len(m_spl\Txt) Then
			If m_spl\Timer < 10.0
				m_spl\Timer = m_spl\Timer + fps\Factor[1] * m_spl\Speed
			Else
				m_spl\CurrentLength = m_spl\CurrentLength + 1
				m_spl\Timer = 0.0
			EndIf
		Else
			If m_spl\Timer < m_spl\DisplayAmount+255.0 Then
				m_spl\Timer = Min(m_spl\Timer+fps\Factor[1],m_spl\DisplayAmount+255.0)
			Else
				Delete m_spl
			EndIf
		EndIf
	Next
	
End Function

Function DrawSplashMsg()
	Local m_spl.SplashMsg
	Local amount% = 0
	
	For m_spl = Each SplashMsg
		SetFont fo\Font[Font_Default]
		
		If m_spl\CurrentLength < Len(m_spl\Txt)
			m_spl\R = m_spl\R
			m_spl\G = m_spl\G
			m_spl\B = m_spl\B
		Else
			m_spl\R = Min((m_spl\DisplayAmount+255.0)-m_spl\Timer,m_spl\R)
			m_spl\G = Min((m_spl\DisplayAmount+255.0)-m_spl\Timer,m_spl\G)
			m_spl\B = Min((m_spl\DisplayAmount+255.0)-m_spl\Timer,m_spl\B)
		EndIf
		
		Color m_spl\R,m_spl\G,m_spl\B
		
		If (Not m_spl\Centered) Then
			Text m_spl\X,m_spl\Y+(32*amount),Left(m_spl\Txt,m_spl\CurrentLength)
		Else
			Text m_spl\X-StringWidth(m_spl\Txt)/2,m_spl\Y+(32*amount)-StringHeight(m_spl\Txt)/2,Left(m_spl\Txt,m_spl\CurrentLength)
		EndIf
		
		amount = amount + 1
	Next
	
End Function

Function CreateHintMsg(txt$)
	
	m_msg\HintTxt = txt
	m_msg\HintTimer = 0.0
	m_msg\HintTxt_Y = 0.0
	
End Function

Function UpdateHintMsg()
	Local scale# = opt\GraphicHeight/768.0
	Local width = StringWidth(m_msg\HintTxt)+20*scale
	Local height% = 30*scale
	Local x% = (opt\GraphicWidth/2)-(width/2)
	Local y% = (-height)+m_msg\HintTxt_Y
	
	If m_msg\HintTxt <> ""
		If m_msg\HintTimer < 70*5
			If m_msg\HintTxt_Y < height
				m_msg\HintTxt_Y = Min(m_msg\HintTxt_Y+2*fps\Factor[1],height)
			Else
				m_msg\HintTxt_Y = height
			EndIf
			m_msg\HintTimer = m_msg\HintTimer + fps\Factor[1]
		Else
			If m_msg\HintTxt_Y > 0
				m_msg\HintTxt_Y = Max(m_msg\HintTxt_Y-2*fps\Factor[1],0)
			Else
				CreateHintMsg("")
			EndIf
		EndIf
	EndIf
	
End Function

Function DrawHintMSG()
	Local scale# = opt\GraphicHeight/768.0
	Local width = StringWidth(m_msg\HintTxt)+20*scale
	Local height% = 30*scale
	Local x% = (opt\GraphicWidth/2)-(width/2)
	Local y% = (-height)+m_msg\HintTxt_Y
	
	If m_msg\HintTxt <> ""
		DrawFrame(x,y,width,height)
		Color 255,255,255
		SetFont fo\Font[Font_Default]
		Text(opt\GraphicWidth/2,y+(height/2),m_msg\HintTxt,True,True)
	EndIf
	
End Function

Function PlayStartupVideos()
	
	If opt\PlayStartupVideos = GetINIInt(gv\OptionFile, "options", "play startup videos", 1) = 0 Then Return
	
	HidePointer()
	
	Local ScaledGraphicHeight%,SplashScreenVideo
	Local Ratio# = Float(RealGraphicWidth)/Float(RealGraphicHeight)
	If Ratio>1.76 And Ratio<1.78
		ScaledGraphicHeight = RealGraphicHeight
		DebugLog "Not Scaled"
	Else
		ScaledGraphicHeight% = Float(RealGraphicWidth)/(16.0/9.0)
		DebugLog "Scaled: "+ScaledGraphicHeight
	EndIf
	
	Local i, moviefile$
	For i = 0 To 2
		Select i
			Case 0
				moviefile$ = "GFX\menu\Videos\startup_Undertow"
			Case 1
				moviefile$ = "GFX\menu\Videos\startup_TSS"
			Case 2
				moviefile$ = "GFX\menu\Videos\startup_Warning"
		End Select
		
		SplashScreenVideo = BlitzMovie_OpenD3D(moviefile$+".wmv", SystemProperty("Direct3DDevice7"), SystemProperty("DirectDraw7"))
		
		If SplashScreenVideo = 0 Then
			PutINIValue(gv\OptionFile, "options", "play startup video", "false")
			Return
		EndIf
		
		SplashScreenVideo = BlitzMovie_Play()
		Local SplashScreenAudio = StreamSound_Strict(moviefile$+".ogg",opt\SFXVolume,0)
		
		Repeat
			Cls
			BlitzMovie_DrawD3D(0, (RealGraphicHeight/2-ScaledGraphicHeight/2), RealGraphicWidth, ScaledGraphicHeight)
			Flip 1
			Delay 10
		Until (GetKey() Lor (Not IsStreamPlaying_Strict(SplashScreenAudio)))
		
		StopStream_Strict(SplashScreenAudio)
		BlitzMovie_Stop()
		BlitzMovie_Close()
		Cls
		Flip 1
	Next
	
	ShowPointer()
	
End Function

Function ProjectImage(img, w#, h#, Quad%, Texture%)
    Local img_w# = ImageWidth(img)
    Local img_h# = ImageHeight(img)
    If img_w > 4096 Then img_w = 4096
    If img_h > 4096 Then img_h = 4096
    If img_w < 1 Then img_w = 1
    If img_h < 1 Then img_h = 1
    
    If w > 4096 Then w = 4096
    If h > 4096 Then h = 4096
    If w < 1 Then w = 1
    If h < 1 Then h = 1
    
    Local w_rel# = w# / img_w#
    Local h_rel# = h# / img_h#
    Local g_rel# = 2048.0 / Float(RealGraphicWidth)
    Local dst_x = 1024 - (img_w / 2.0)
    Local dst_y = 1024 - (img_h / 2.0)
    CopyRect 0, 0, img_w, img_h, dst_x, dst_y, ImageBuffer(img), TextureBuffer(Texture)
    ScaleEntity Quad, w_rel * g_rel, h_rel * g_rel, 0.0001
    RenderWorld()
	
End Function

Function CreateQuad()
	Local mesh%,surf%,v0%,v1%,v2%,v3%
	
	mesh = CreateMesh()
	surf = CreateSurface(mesh)
	v0 = AddVertex(surf,-1.0, 1.0, 0, 0, 0)
	v1 = AddVertex(surf, 1.0, 1.0, 0, 1, 0)
	v2 = AddVertex(surf, 1.0,-1.0, 0, 1, 1)
	v3 = AddVertex(surf,-1.0,-1.0, 0, 0, 1)
	AddTriangle(surf, v0, v1, v2)
	AddTriangle(surf, v0, v2, v3)
	UpdateNormals mesh
	Return mesh
	
End Function

Function ResetInput()
	
	MouseXSpeed()
	MouseYSpeed()
	MouseZSpeed()
	Mouse_X_Speed_1#=0.0
	Mouse_Y_Speed_1#=0.0
	
	FlushKeys()
	FlushMouse()
	FlushJoy()
	MouseHit1 = 0
	MouseHit2 = 0
	MouseHit3 = 0
	MouseDown1 = 0
	MouseUp1 = 0
	MouseDown2 = 0
	MouseHit(1)
	MouseHit(2)
	MouseHit(3)
	MouseDown(1)
	GrabbedEntity = 0
	Input_ResetTime# = 10.0
	KeyHitUse = 0
	KeyDownUse = 0
	
End Function

Function UpdateRichPresence()
	If MainMenuOpen Then
		Steam_SetRichPresence("steam_display", "#Status_InMainMenu")
		BlitzcordSetLargeImage("logo")
		BlitzcordSetSmallImage("")
		BlitzcordSetActivityDetails("In Main Menu")
		BlitzcordSetActivityState("")
	ElseIf gc\CurrGamemode = 3 Then
		Select mp_I\Gamemode\ID
			Case Gamemode_Waves
				Steam_SetRichPresence("map", mp_I\MapInList\Name)
				Steam_SetRichPresence("difficulty", mp_I\Gamemode\Difficulty)
				Steam_SetRichPresence("currWave", mp_I\Gamemode\Phase/2)
				Steam_SetRichPresence("maxWaves", mp_I\Gamemode\MaxPhase)
				Steam_SetRichPresence("steam_display", "#Status_Waves")
				BlitzcordSetLargeImage("waves")
			Case Gamemode_Deathmatch
				Steam_SetRichPresence("map", mp_I\MapInList\Name)
				Steam_SetRichPresence("steam_display", "#Status_TeamDeathmatch")
				BlitzcordSetLargeImage("tdm")
		End Select
		BlitzcordSetSmallImage("logo")
		BlitzcordSetActivityDetails(mp_I\Gamemode\name+" ("+mp_I\PlayerCount+" of "+mp_I\MaxPlayers+" players)")
		BlitzcordSetActivityState(mp_I\MapInList\Name)
	ElseIf gc\CurrGamemode = 0 Then
		Steam_SetRichPresence("difficulty", SelectedDifficulty\Name)
		Steam_SetRichPresence("seed", RandomSeed)
		Steam_SetRichPresence("steam_display", "#Status_Singleplayer")
		BlitzcordSetLargeImage("singleplayer")
		BlitzcordSetSmallImage("logo")
		BlitzcordSetActivityDetails("Singleplayer")
		BlitzcordSetActivityState("Difficulty: "+SelectedDifficulty\Name+" | Seed: "+RandomSeed)
	EndIf
	BlitzcordUpdateActivity()
	BlitzcordRunCallbacks()
End Function	
;~IDEal Editor Parameters:
;~C#Blitz3D