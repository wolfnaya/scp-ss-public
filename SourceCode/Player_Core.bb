; ~ Player constants

Global Designation$
Designation$ = GetLocalString("Singleplayer", "designation")

Const MaxStepSounds% = 8
Const MaxMaterialSounds% = 3

; ~ Wheel constants

Const WHEEL_CLOSED = 0
Const WHEEL_COMMAND = 1
Const WHEEL_SOCIAL = 2
Const WHEEL_MAX = 9
Const WHEEL_OUTPUT_UNKNOWN = 254 															  ; ~ Will change it back once that bug is fixed where 255 seems to create another byte

; ~ Command wheel constants

Const COMMAND_OVERHERE = 0
Const COMMAND_HELPME = 2
Const COMMAND_WAITHERE = 3
Const COMMAND_TESLA = 4																		  ; ~ Singleplayer Only
Const COMMAND_CANCEL = 5
Const COMMAND_CAMERA = 6																	  ; ~ Singleplayer Only
Const COMMAND_FOLLOWME = 7
Const COMMAND_COVERME = 8

; ~ Social wheel constants

Const SOCIAL_LETSGO = 0
Const SOCIAL_ACTRUDE = 2
Const SOCIAL_NEGATIVE = 3
Const SOCIAL_SORRY = 4
Const SOCIAL_CANCEL = 5
Const SOCIAL_THANKYOU = 6
Const SOCIAL_AFFIRMATIVE = 7
Const SOCIAL_GOODJOB = 8

; ~ Command wheel look here constants

Const ICON_LOOK_DEFAULT = 0
Const ICON_LOOK_AMMO = 1
Const ICON_LOOK_GUN = 2
Const ICON_LOOK_ENEMY = 3
Const WHEEL_MAX_LOOK_ICONS = 4

; ~ Dialogue constants

Const MTF_DIALOGUE_TIMER_MIN = 70*45
Const MTF_DIALOGUE_TIMER_MAX = 70*120
Const MTF_DIALOGUE_MAX = 4
Const MTF_DIALOGUE_NUM_OF_BITS = 2

Type MainPlayer
	Field Camera
	Field CameraPivot%
	Field StepSoundWalk%[MaxStepSounds*MaxMaterialSounds]
	Field StepSoundRun%[MaxStepSounds*MaxMaterialSounds]
	Field HasNTFGasmask%
	Field NightVisionEnabled%
	Field SlotsDisplayTimer#
	; ~ Communication and Social wheel
	Field WheelOpened%
	Field WheelSprite%
	Field WheelMiddle%
	Field WheelSelect%
	Field WheelOutput%[WHEEL_MAX]
	Field WheelSelectedOutput%
	Field WheelCurrentMouseXSpeed#
	Field WheelCurrentMouseYSpeed#
	Field WheelLookHereIcons%[WHEEL_MAX_LOOK_ICONS]
	Field WheelLookHereSelectedIcon%
	Field HealthIcon
	Field KevlarIcon
	Field HelmetIcon
	Field HDSIcon
	Field DamageOverlay
	Field DamageTimer#
End Type

Type PlayerSP
	Field SharedPlayer.MainPlayer
	Field Pos.Vector3D
	Field Rot.Vector3D
	Field Model
	Field ModelVest
	Field ModelHazmat
	Field ModelHDS
	Field ModelNTF
	Field ModelD
	Field ModelCollider
	Field PlayerRoom.Rooms
	Field DropSpeed#
	Field DeployState#
	Field ShootState#
	Field ReloadState#
	Field SoundCHN%
	Field Helmet#
	Field Kevlar#
	Field Health#
	;Field Karma#
	Field GuardArms%
	Field NTFArms%
	Field DArms%
	Field HDSArms%
	Field HazmatArms%
	Field IsShowingHUD%
	Field ShowPlayerModel%
	Field NoMove%
	Field NoRotation%
End Type

; ~ [CONTROLLER]

Global CK_Blink = 2		; ~ Cross
Global CK_Use = 6		; ~ R1
Global CK_LMouse = 8	; ~ R2
Global CK_RMouse = 7	; ~ L2
Global CK_MMouse = 5	; ~ L1
Global CK_Sprint = 11	; ~ L3
Global CK_Crouch = 12	; ~ R3
Global CK_Inv = 14		; ~ Touchpad
Global CK_Pause = 10	; ~ OPTIONS
Global CK_Save = 9		; ~ SHARE
Global CK_Reload = 1	; ~ Square
Global CK_Chat = 3		; ~ Circle
Global CK_Radio = 4		; ~ Triangle
; ~ Menu Controls
Global CKM_Press = 2	; ~ Cross
Global CKM_Back = 3		; ~ Circle
Global CKM_Next = 6		; ~ R1
Global CKM_Prev = 5		; ~ L1

Function CreateMainPlayer()
	Local i

	; ~ Create player type
	
	mpl = New MainPlayer
	
	; ~ Init player step sounds
	
	For i = 0 To (MaxStepSounds-1)
		mpl\StepSoundWalk[i] = LoadSound_Strict("SFX\Player\StepSounds\Concrete_Walk"+(i+1)+".ogg")
		mpl\StepSoundRun[i] = LoadSound_Strict("SFX\Player\StepSounds\Concrete_Run"+(i+1)+".ogg")
		mpl\StepSoundWalk[i+MaxStepSounds] = LoadSound_Strict("SFX\Player\StepSounds\Metal_Walk"+(i+1)+".ogg")
		mpl\StepSoundRun[i+MaxStepSounds] = LoadSound_Strict("SFX\Player\StepSounds\Metal_Run"+(i+1)+".ogg")
		If gc\CurrGamemode <> 3 Then
			mpl\StepSoundWalk[i+MaxStepSounds*2] = LoadSound_Strict("SFX\Player\StepSounds\Water_Walk"+(i+1)+".ogg")
			mpl\StepSoundRun[i+MaxStepSounds*2] = LoadSound_Strict("SFX\Player\StepSounds\Water_Run"+(i+1)+".ogg")
		EndIf
	Next
	
	mpl\CameraPivot = CreatePivot()
	mpl\HealthIcon = LoadImage_Strict("GFX\HUD\hp_icon.png")
	mpl\KevlarIcon = LoadImage_Strict("GFX\HUD\kevlar_Icon.png")
	mpl\HelmetIcon = LoadImage_Strict("GFX\HUD\helmet_Icon.png")
	If gc\CurrGamemode <> 3 Then
		mpl\HDSIcon = LoadImage_Strict("GFX\HUD\HDS_Icon.png")
	EndIf
	
	If gc\CurrGamemode = 3 Then
		mpl\HasNTFGasmask = True
	EndIf
	
End Function

Function CreateCommunicationAndSocialWheel()
	Local i%
	
	mpl\WheelOpened% = WHEEL_CLOSED
	mpl\WheelSelectedOutput = WHEEL_OUTPUT_UNKNOWN
	For i = 0 To (WHEEL_MAX-1)
		mpl\WheelOutput[i] = WHEEL_OUTPUT_UNKNOWN
	Next
	If gc\CurrGamemode = 3 Then
		Players[mp_I\PlayerID]\VoiceLine = WHEEL_OUTPUT_UNKNOWN
	EndIf
	
	mpl\WheelSprite% = LoadSprite("GFX\HUD\communication_wheel\com_wheel.png", 1+2, Ark_Blur_Cam)
	MoveEntity mpl\WheelSprite,0,0,1
	ScaleSprite mpl\WheelSprite,0.5,0.5
	EntityOrder mpl\WheelSprite,-5001
	HideEntity mpl\WheelSprite
	
	mpl\WheelMiddle% = LoadSprite("GFX\HUD\communication_wheel\com_wheel_center.png", 1+2, mpl\WheelSprite)
	ScaleSprite mpl\WheelMiddle,0.5,0.5
	EntityOrder mpl\WheelMiddle,-5000
	
	mpl\WheelSelect% = LoadSprite("GFX\HUD\communication_wheel\com_wheel_selection.png", 1+2, mpl\WheelSprite)
	ScaleSprite mpl\WheelSelect,0.5,0.5
	EntityOrder mpl\WheelSelect,-5000
	
	mpl\WheelLookHereIcons[ICON_LOOK_DEFAULT] = LoadTexture_Strict("GFX\HUD\communication_wheel\look_default.png",1+2,1)
	mpl\WheelLookHereIcons[ICON_LOOK_AMMO] = LoadTexture_Strict("GFX\HUD\communication_wheel\look_ammo.png",1+2,1)
	mpl\WheelLookHereIcons[ICON_LOOK_GUN] = LoadTexture_Strict("GFX\HUD\communication_wheel\look_guns.png",1+2,1)
	mpl\WheelLookHereIcons[ICON_LOOK_ENEMY] = LoadTexture_Strict("GFX\HUD\communication_wheel\look_enemy.png",1+2,1)
	
End Function

Function CreateDarkSprite(r.Rooms, DarkSpriteID%)
	
	; ~ Dark sprite
	r\Objects[DarkSpriteID] = CreateSprite(Ark_Blur_Cam)
	ScaleSprite(r\Objects[DarkSpriteID], Max(opt\GraphicWidth / 1240.0, 1.0), Max(opt\GraphicHeight / 960.0 * 0.8, 0.8))
	EntityTexture(r\Objects[DarkSpriteID], OverlayTexture[10])
	EntityBlend (r\Objects[DarkSpriteID], 1)
	EntityOrder r\Objects[DarkSpriteID], -1002
	MoveEntity(r\Objects[DarkSpriteID], 0, 0, 1.0)
	EntityAlpha r\Objects[DarkSpriteID], 0.0
	
End Function

Function UpdateHazardousDefenceSuit()
	Local isTimeForLogo%,isTimeForLogo2%,n.NPCs
	Local Red#,Green#,Blue#
	Local g.Guns, j%, sf%, b%, t1%, name$, tex%, i%
	
	; ~ Suit effects
	
	If hds\Wearing Then
		BlinkEffect = 0.2
		BlinkEffectTimer = 1
		StaminaEffect = 0.2
		StaminaEffectTimer = 1
	EndIf
	
	; ~ Explosion
	
	If hds\Health = 0 Then
		hds\isBroken = True
	Else
		hds\isBroken = False
	EndIf
	
	If hds\isBroken Then
		psp\NoMove = True
		If hds\ExplodeTimer <> -1 Then
			hds\ExplodeTimer = hds\ExplodeTimer + fps\Factor[0]
		EndIf
	Else
		psp\NoMove = False
	EndIf
	
	If hds\ExplodeTimer > 0 And hds\ExplodeTimer < 70*0.025 Then
		If ChannelPlaying(hds\SoundCHN) Then StopChannel(hds\SoundCHN)
		If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[24])
	ElseIf hds\ExplodeTimer >= 70*4.2 And hds\ExplodeTimer < 70*4.3 Then
		hds\Wearing = False
		psp\Health = Max(psp\Health - 50,0)
		BigCameraShake = 15
		CreateExplosionWaveParticle(EntityX(Camera),EntityY(Camera),EntityZ(Camera))
		hds\ExplodeTimer = -1
		psp\NoMove = False
		For n = Each NPCs
			If EntityDistanceSquared(n\Collider,Collider) < PowTwo(2.5) Then
				n\HP = n\HP - (80+(EntityDistanceSquared(n\Collider,Collider)*2))
			EndIf
		Next
	EndIf
	
	; ~ Suit boot up interface
	
	Red# = 10
	Green# = 150
	Blue# = 200
	
	If hds\Wearing Then
		
		If hds\BootUpTimer >= 0 And hds\BootUpTimer < 70*5 Then
			hds\BootUpTimer# = hds\BootUpTimer# + fps\Factor[0]
		Else
			hds\BootUpTimer = -1
		EndIf
		
		If hds\BootUpTimer > 0 And hds\BootUpTimer < 70*0.2 Then
			BlinkTimer = -10
		EndIf
		
		If hds\BootUpTimer >= 70*0.2 And hds\BootUpTimer < 70*0.21 Then
			isTimeForLogo = True
			If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[0])
		ElseIf hds\BootUpTimer >= 70*4 And hds\BootUpTimer < 70*4.01 Then
			isTimeForLogo2 = True
		EndIf
		
		If isTimeForLogo Then
			If opt\MusicVol > 0 Then
				PlaySound_Strict(LoadTempSound("sfx\music\misc\hds_pickup.ogg"))
			EndIf
			CreateSplashText(GetLocalString("Hazardous Suit","welcome"),opt\GraphicWidth/2,opt\GraphicHeight/2,10,2,Font_Menu,True,False,Red,Green,Blue)
			
			CreateSplashText(GetLocalString("Hazardous Suit","boot_1"),20,opt\GraphicHeight-1000,100,2,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_2"),20,opt\GraphicHeight-980,100,2,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_3"),20,opt\GraphicHeight-960,80,2,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_4"),20,opt\GraphicHeight-940,80,2,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_5"),20,opt\GraphicHeight-920,80,2,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_6"),20,opt\GraphicHeight-900,80,2,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_7"),20,opt\GraphicHeight-880,70,2,Font_Digital_Small,False,False,Red,Green,Blue)
			
			CreateSplashText(GetLocalString("Hazardous Suit","boot_8"),20,opt\GraphicHeight-860,120,2,Font_Digital_Small,False,False,Red,Green,Blue)
		EndIf
		If isTimeForLogo2 Then
			CreateSplashText(GetLocalString("Hazardous Suit","boot_9"),20,opt\GraphicHeight-840,150,3,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_10"),20,opt\GraphicHeight-820,150,3,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_11"),20,opt\GraphicHeight-800,150,3,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_12"),20,opt\GraphicHeight-780,150,3,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_13"),20,opt\GraphicHeight-760,150,3,Font_Digital_Small,False,False,Red,Green,Blue)
			
			CreateSplashText(GetLocalString("Hazardous Suit","boot_14"),20,opt\GraphicHeight-740,120,2,Font_Digital_Small,False,False,Red,Green,Blue)
		EndIf
		
	EndIf
	
End Function

Function UpdateCommunicationAndSocialWheel()
	Local it.Items, n.NPCs, p.Particles
	Local closeWheel% = False
	Local i%
	Local direction%, prevSelectedOutput%
	Local goesUp% = False
	Local voiceLine%, voiceLineNumber%
	Local voiceLineStr$, commandLineID%
	
	; ~ This is only temporary for 0.2.0, will be unlocked for singleplayer in versions afterwards
	
	If gc\CurrGamemode <> 3 Then
		Return
	EndIf
	
	; ~ CHECK FOR IMPLEMENTATION
	
	If ((gc\CurrGamemode = 3 And (Not InLobby()) And (Not mp_I\ChatOpen) And (Not IsSpectator(mp_I\PlayerID)) And Players[mp_I\PlayerID]\CurrHP > 0) Lor (gc\CurrGamemode <> 3 And EndingTimer >= 0 And KillTimer >= 0 And (Not AttachmentOpen) And (Not InvOpen) And (Not MenuOpen))) And (Not ConsoleOpen) And (Not IsModerationOpen()) And (Not IsInVote()) Then
		If KeyDown(kb\CommandWheelKey) Then
			If mpl\WheelOpened = WHEEL_CLOSED Then
				MoveMouse Viewport_Center_X, Viewport_Center_Y
				ResetInput()
				mpl\WheelOpened = WHEEL_COMMAND
				mpl\WheelSelectedOutput = COMMAND_OVERHERE
			EndIf
		ElseIf KeyDown(kb\SocialWheelKey) Then
			If mpl\WheelOpened = WHEEL_CLOSED Then
				MoveMouse Viewport_Center_X, Viewport_Center_Y
				ResetInput()
				mpl\WheelOpened = WHEEL_SOCIAL
				mpl\WheelSelectedOutput = SOCIAL_LETSGO
			EndIf
		ElseIf mpl\WheelOpened <> WHEEL_CLOSED Then
			closeWheel = True
		EndIf
	Else
		closeWheel = True
		mpl\WheelOpened = WHEEL_CLOSED
	EndIf
	
	If closeWheel And mpl\WheelOpened <> WHEEL_CLOSED Then
		voiceLine% = mpl\WheelSelectedOutput+(WHEEL_MAX*(mpl\WheelOpened = WHEEL_SOCIAL))
		voiceLineNumber% = Rand(1,4)
		If voiceLine = COMMAND_OVERHERE Then
			For it = Each Items
				EntityRadius it\collider,0.2
			Next
			For n = Each NPCs
				ShowNPCHitBoxes(n)
			Next
			For i = 0 To (mp_I\MaxPlayers-1)
				If Players[i]<>Null And i<>mp_I\PlayerID Then
					ShowPlayerHitBoxes(i)
				EndIf
			Next
			CameraPick(Camera,opt\GraphicWidth/2,opt\GraphicHeight/2)
			For it = Each Items
				EntityRadius it\collider,0.01
			Next
			For n = Each NPCs
				HideNPCHitBoxes(n)
			Next
			For i = 0 To (mp_I\MaxPlayers-1)
				If Players[i]<>Null And i<>mp_I\PlayerID Then
					HidePlayerHitBoxes(i)
				EndIf
			Next
			If PickedEntity()<>0 Then
				CreateOverHereParticle(PickedX(), PickedY(), PickedZ())
				If gc\CurrGamemode = 3 Then
					Players[mp_I\PlayerID]\OverHerePosition = CreateVector3D(PickedX(), PickedY(), PickedZ())
				EndIf
			EndIf
			
			commandLineID = GetPlayerCommandLineCategory()
			voiceLineStr = "SFX\Player\Voice\Chat\"
			Select commandLineID
				Case ICON_LOOK_DEFAULT
					voiceLineStr = voiceLineStr + "Look"
					mp_I\CurrChatMSG = "wheel_look"
				Case ICON_LOOK_ENEMY
					voiceLineStr = voiceLineStr + "Enemy"
					mp_I\CurrChatMSG = "wheel_enemy"
				Case ICON_LOOK_AMMO
					voiceLineStr = voiceLineStr + "Ammo"
					mp_I\CurrChatMSG = "wheel_ammo"
				Case ICON_LOOK_GUN
					voiceLineStr = voiceLineStr + "Gun"
					mp_I\CurrChatMSG = "wheel_gun"
			End Select
			CreateChatMSG(True)
			voiceLineStr = voiceLineStr + "Here" + voiceLineNumber + ".ogg"
			mpl\WheelLookHereSelectedIcon = commandLineID
		Else
			voiceLineStr = GetPlayerVoiceLine(voiceLine, voiceLineNumber, mp_I\PlayerID)
		EndIf
		If gc\CurrGamemode = 3 Then
			If voiceLine <> COMMAND_CANCEL And voiceLine <> (SOCIAL_CANCEL+WHEEL_MAX) Then
				If ChannelPlaying(Players[mp_I\PlayerID]\Sound_CHN) Then
					StopChannel(Players[mp_I\PlayerID]\Sound_CHN)
				EndIf
				Players[mp_I\PlayerID]\Sound_CHN = PlaySound_Strict(LoadTempSound(voiceLineStr))
				ChannelVolume(Players[mp_I\PlayerID]\Sound_CHN,opt\VoiceVol*opt\MasterVol)
				Players[mp_I\PlayerID]\VoiceLine = voiceLine
				Players[mp_I\PlayerID]\VoiceLineNumber = voiceLineNumber
			EndIf
			If voiceLine = COMMAND_OVERHERE Then
				EntityTexture Players[mp_I\PlayerID]\OverHereSprite,mpl\WheelLookHereIcons[commandLineID]
				ShowEntity Players[mp_I\PlayerID]\OverHereSprite
				PositionEntity Players[mp_I\PlayerID]\OverHereSprite,PickedX(),PickedY(),PickedZ()
				Players[mp_I\PlayerID]\OverHereSpriteTime = 70*5
			EndIf
		Else
			If voiceLine <> COMMAND_CANCEL And voiceLine <> (SOCIAL_CANCEL+WHEEL_MAX) Then
				If ChannelPlaying(psp\SoundCHN) Then
					StopChannel(psp\SoundCHN)
				EndIf
				psp\SoundCHN = PlaySound_Strict(LoadTempSound(voiceLineStr))
				SingleplayerVoiceActions(voiceLine)
				If voiceLine = COMMAND_OVERHERE Then
					
				EndIf
			EndIf	
		EndIf
		mpl\WheelOpened = WHEEL_CLOSED
		ResetInput()
	EndIf
	
	If mpl\WheelOpened <> WHEEL_CLOSED Then
		ShowEntity mpl\WheelSprite
		
		If mpl\WheelOpened = WHEEL_COMMAND Then
			mpl\WheelOutput[0] = COMMAND_OVERHERE
			mpl\WheelOutput[2] = COMMAND_COVERME
			mpl\WheelOutput[3] = COMMAND_FOLLOWME
			mpl\WheelOutput[5] = COMMAND_CANCEL
			mpl\WheelOutput[7] = COMMAND_WAITHERE
			mpl\WheelOutput[8] = COMMAND_HELPME
			If gc\CurrGamemode <> 3 Then
				mpl\WheelOutput[4] = COMMAND_TESLA
				mpl\WheelOutput[6] = COMMAND_CAMERA
			EndIf
		ElseIf mpl\WheelOpened = WHEEL_SOCIAL Then
			mpl\WheelOutput[0] = SOCIAL_LETSGO
			mpl\WheelOutput[2] = SOCIAL_GOODJOB
			mpl\WheelOutput[3] = SOCIAL_AFFIRMATIVE
			mpl\WheelOutput[4] = SOCIAL_THANKYOU
			mpl\WheelOutput[5] = SOCIAL_CANCEL
			mpl\WheelOutput[6] = SOCIAL_SORRY
			mpl\WheelOutput[7] = SOCIAL_NEGATIVE
			mpl\WheelOutput[8] = SOCIAL_ACTRUDE
		EndIf
		
		direction = -1
		If (MouseY() < Mouse_Top_Limit) Then
			direction = 0
			goesUp = True
		EndIf
		If (MouseY() > Mouse_Bottom_Limit) Then
			direction = 180
			goesUp = True
		EndIf
		If goesUp Then
			If (MouseX() > (opt\GraphicWidth/2.0) + ((opt\GraphicWidth-Mouse_Right_Limit) / 2.0)) Then
				direction = direction - 45 + (90*(direction=180))
			ElseIf (MouseX() < (opt\GraphicWidth/2.0) - (Mouse_Left_Limit / 2.0)) Then
				direction = direction + 45 - (90*(direction=180))
			EndIf
			
			If direction = -45 Then
				direction = 315
			EndIf
		EndIf
		If direction = -1 Then
			If (MouseX() > Mouse_Right_Limit) Then
				direction = 270
			EndIf
			If (MouseX() < Mouse_Left_Limit) Then
				direction = 90
			EndIf
		EndIf
		
		If direction > -1 Then
			prevSelectedOutput = mpl\WheelSelectedOutput
			If mpl\WheelSelectedOutput = 0 Then
				mpl\WheelSelectedOutput = (direction/45)+1
			Else
				If Abs(((mpl\WheelSelectedOutput-1)*45.0) - direction) >= 135.0 Then
					mpl\WheelSelectedOutput = 0
				Else
					mpl\WheelSelectedOutput = (direction/45)+1
				EndIf
			EndIf
			If mpl\WheelOutput[mpl\WheelSelectedOutput] = WHEEL_OUTPUT_UNKNOWN Then
				mpl\WheelSelectedOutput = prevSelectedOutput
			EndIf
		EndIf
		
		If (MouseX() > Mouse_Right_Limit) Lor (MouseX() < Mouse_Left_Limit) Lor (MouseY() > Mouse_Bottom_Limit) Lor (MouseY() < Mouse_Top_Limit) Then
			MoveMouse Viewport_Center_X, Viewport_Center_Y
		EndIf
		
		If mpl\WheelSelectedOutput = 0 Then
			HideEntity mpl\WheelSelect
			ShowEntity mpl\WheelMiddle
		Else
			ShowEntity mpl\WheelSelect
			HideEntity mpl\WheelMiddle
			RotateSprite mpl\WheelSelect,(mpl\WheelSelectedOutput-1)*45.0
		EndIf
	Else
		HideEntity mpl\WheelSprite
		HideEntity mpl\WheelSelect
		ShowEntity mpl\WheelMiddle
		mpl\WheelSelectedOutput = WHEEL_OUTPUT_UNKNOWN
		For i = 0 To (WHEEL_MAX-1)
			mpl\WheelOutput[i] = WHEEL_OUTPUT_UNKNOWN
		Next
		mpl\WheelCurrentMouseXSpeed = 0.0
		mpl\WheelCurrentMouseYSpeed = 0.0
	EndIf
	
	If gc\CurrGamemode = 3 Then
		For i = 0 To (mp_I\MaxPlayers-1)
			If Players[i]<>Null Then
				Players[i]\OverHereSpriteTime = Max(Players[i]\OverHereSpriteTime - fps\Factor[0], 0.0)
				If Players[i]\OverHereSpriteTime > 0.0 Then
					ShowEntity Players[i]\OverHereSprite
				Else
					HideEntity Players[i]\OverHereSprite
				EndIf
			EndIf
		Next
	Else
		
		; ~ TODO
		
	EndIf
	
End Function

Function GetPlayerVoiceLine$(voiceLine%, voiceLineNumber%, playerID%=-1)
	Local voice$
	Local n.NPCs
	
	voice = "SFX\Player\Voice\Chat\"
	Select voiceLine
		Case COMMAND_HELPME
			voice = voice + "HelpMe"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_helpme"
			EndIf
		Case COMMAND_WAITHERE
			voice = voice + "Wait"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_wait"
			EndIf
		Case COMMAND_FOLLOWME
			voice = voice + "FollowMe"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_followme"
			EndIf
		Case COMMAND_COVERME
			voice = voice + "CoverMe"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_coverme"
			EndIf
		Case COMMAND_TESLA
			voice = voice + "Tesla"
		Case COMMAND_CAMERA
			voice = voice + "Camera"
		Case SOCIAL_LETSGO+WHEEL_MAX
			voice = voice + "LetsGo"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_letsgo"
			EndIf
		Case SOCIAL_ACTRUDE+WHEEL_MAX
			voice = voice + "Rude"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_actsrude"
			EndIf
		Case SOCIAL_NEGATIVE+WHEEL_MAX
			voice = voice + "Negative"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_negative"
			EndIf
		Case SOCIAL_SORRY+WHEEL_MAX
			voice = voice + "Sorry"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_sorry"
			EndIf
		Case SOCIAL_THANKYOU+WHEEL_MAX
			voice = voice + "Thanks"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_thanks"
			EndIf
		Case SOCIAL_AFFIRMATIVE+WHEEL_MAX
			voice = voice + "Affirmative"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_affirmative"
			EndIf
		Case SOCIAL_GOODJOB+WHEEL_MAX
			voice = voice + "GoodJob"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_goodjob"
			EndIf
	End Select
	
	If playerID >= 0 Then
		CreateChatMSG(True)
	EndIf
	
	voice = voice + voiceLineNumber + ".ogg"
	
	Return voice
End Function

Function GetPlayerCommandLineCategory$()
	Local n.NPCs, p.Player, it.Items
	Local i%
	
	; ~ Check for the correct voice line to be played
	
	; ~ First: Check the NPCs and players (aka enemy)
	
	For n = Each NPCs
		For i = 0 To (MaxHitBoxes-1)
			If n\HP > 0 Then
				If n\HitBox1[i]<>0 And n\HitBox1[i] = PickedEntity() Then
					Return ICON_LOOK_ENEMY
				ElseIf n\HitBox2[i]<>0 And n\HitBox2[i] = PickedEntity() Then
					Return ICON_LOOK_ENEMY
				ElseIf n\HitBox3[i]<>0 And n\HitBox3[i] = PickedEntity() Then
					Return ICON_LOOK_ENEMY
				EndIf
			EndIf
		Next
		
		If n\obj = PickedEntity() Then
			Return ICON_LOOK_ENEMY
		EndIf
	Next
	
	; ~ Check if a player is an enemy
	
	For p = Each Player
		If p\Team <> Players[mp_I\PlayerID]\Team Then
			For i = 0 To (MaxHitBoxes-1)
				If p\HitBox1[i]<>0 And p\HitBox1[i] = PickedEntity() Then
					Return ICON_LOOK_ENEMY
				ElseIf p\HitBox2[i]<>0 And p\HitBox2[i] = PickedEntity() Then
					Return ICON_LOOK_ENEMY
				ElseIf p\HitBox3[i]<>0 And p\HitBox3[i] = PickedEntity() Then
					Return ICON_LOOK_ENEMY
				EndIf
			Next
		EndIf
	Next
	
	; ~ Check for ammo crate or weapon items
	
	For it = Each Items
		If it\collider = PickedEntity() Then
			Select it\itemtemplate\tempname
				Case "ammocrate","bigammocrate"
					Return ICON_LOOK_AMMO
				Default
					If it\itemtemplate\isGun Then
						Return ICON_LOOK_GUN
					EndIf
			End Select
		EndIf
	Next
	
	; ~ Last resort: No targetted object found, use default
	
	Return ICON_LOOK_DEFAULT
	
End Function

Function CreateOverHereParticle(x#, y#, z#)
	
	Local p.Particles = CreateParticle(x,y,z,10,0.05,0.0,500)
	p\SizeChange = 0.01
	p\Achange = -0.01
	SpriteViewMode p\obj,1
	EntityOrder p\obj,-1
	
End Function

Function CreateExplosionWaveParticle(x#, y#, z#)
	
	Local p.Particles = CreateParticle(x,y,z,15,1.0,0.0,500)
	p\SizeChange = 0.01
	p\Achange = -0.03
	SpriteViewMode p\obj,1
	EntityOrder p\obj,-1
	RotateEntity p\obj,90,0,0
	
End Function

Function RenderCommunicationAndSocialWheel()
	Local x# = opt\GraphicWidth / 2.0
	Local y# = opt\GraphicHeight / 2.0
	Local i%
	Local namespace$
	
	If mpl\WheelOpened <> WHEEL_CLOSED Then
		Color 255,255,255
		SetFont fo\Font[Font_Default_Medium]
		If mpl\WheelOpened = WHEEL_COMMAND Then
			namespace = "command"
		Else
			namespace = "social"
		EndIf
		For i = 0 To (WHEEL_MAX-1)
			If mpl\WheelOutput[i] <> WHEEL_OUTPUT_UNKNOWN Then
				If i > 0 Then
					x = (opt\GraphicWidth/2.0) + 300.0 * MenuScale * Sin((mpl\WheelOutput[i]-1) * 45.0)
					y = (opt\GraphicHeight/2.0) - 300.0 * MenuScale * Cos((mpl\WheelOutput[i]-1) * 45.0)
				EndIf
				Text x,y,GetLocalString("Chatwheel", namespace+i),True,True
			EndIf
		Next
	EndIf
	
End Function

Function SingleplayerVoiceActions(voiceLine%)
	Local ev.Events, n.NPCs
	
;	Select voiceLine
;		Case COMMAND_MEDICAL
;		Case COMMAND_ENEMYSPOTTED
;		Case COMMAND_ENEMYLOST
;		Case COMMAND_TESLA
;			For ev = Each Events
;				If ev\EventName = "room2_tesla"
;					If ev\room=PlayerRoom Then
;						If ev\EventState2 <= 70*3.5 Then
;							ev\EventState2 = 70*100
;							ev\EventState3=ev\EventState3+140
;							Exit
;						EndIf
;					EndIf
;				EndIf
;			Next
;		Case COMMAND_CAMERA
;		Case COMMAND_SPLIT
;			For n = Each NPCs
;				If n<>Null Then
;					If n\NPCtype = NPCtypeMTF Then
;						n\State = MTF_SPLIT_UP
;						Exit
;					EndIf
;				EndIf
;			Next
;		Case COMMAND_SCP
;			If EntityDistanceSquared(Collider, Curr173\Collider) < PowTwo(7) Then
;				If EntityVisible(Collider, Curr173\Collider) Then
;					Curr173\ContainmentState = -1
;				EndIf
;			EndIf
;	End Select
	
End Function

Function DestroyMainPlayer()
	Local i,n
	
	For i = 0 To (MaxStepSounds-1)
		For n = 0 To (MaxMaterialSounds-1)
			FreeSound_Strict(mpl\StepSoundWalk[i+(n*MaxStepSounds)])
			FreeSound_Strict(mpl\StepSoundRun[i+(n*MaxStepSounds)])
		Next
	Next
	FreeImage mpl\HealthIcon
	FreeImage mpl\KevlarIcon
	FreeImage mpl\HelmetIcon
	FreeImage mpl\HDSIcon
	
	Delete mpl
	
End Function

Function UpdateNightVision()
	Local dist#
	
	; ~ CHECK FOR IMPLEMENTATION
	
	If (gc\CurrGamemode=3 And IsSpectator(mp_I\PlayerID)) Lor (Not mpl\HasNTFGasmask) Then
		If mpl\NightVisionEnabled Then
			TurnNVOff()
		EndIf
		mpl\NightVisionEnabled = False
		Return
	EndIf
	
	; ~ TODO: Change this to InteractHit to add the controller key as well!
	
	If KeyHit(kb\NVToggleKey) Then
		mpl\NightVisionEnabled = Not mpl\NightVisionEnabled
		If mpl\NightVisionEnabled Then
			PlaySound_Strict LoadTempSound("SFX\Interact\NVGOn.ogg")
			TurnNVOn()
		Else
			PlaySound_Strict LoadTempSound("SFX\Interact\NVGOff.ogg")
			TurnNVOff()
		EndIf
	EndIf
	If gc\CurrGamemode <> 3 Then
		If KeyHit(KEY_SCRAMBLE) Then
			wbi\SCRAMBLE = (Not wbi\SCRAMBLE) * 2
			If wbi\SCRAMBLE = 2 Then
				PlaySound_Strict LoadTempSound("SFX\Interact\SCRAMBLEOn.ogg")
			Else
				PlaySound_Strict LoadTempSound("SFX\Interact\SCRAMBLEOff.ogg")
			EndIf
		EndIf
	EndIf
	
End Function

Function TurnNVOn()
	
	AmbientLightRooms(100)
	If mpl\HasNTFGasmask = 1 Then
		EntityColor Overlay[2],30,220,30
	Else
		EntityColor Overlay[2],0,0,255
	EndIf
	If gc\CurrGamemode = 3 Then
		If mp_I\Gamemode\ID = Gamemode_Deathmatch Then
			If Players[mp_I\PlayerID]\Team = Team_CI Then
				EntityColor Overlay[2],220,30,30
			Else
				EntityColor Overlay[2],30,220,30
			EndIf
		ElseIf mp_I\Gamemode\ID = Gamemode_Gungame Then
			If Players[mp_I\PlayerID]\Team = Team_CI Then
				EntityColor Overlay[2],220,30,220
			Else
				EntityColor Overlay[2],30,30,220
			EndIf
		Else
			EntityColor Overlay[2],30,220,30
		EndIf
	Else
		StoredCameraFogFar = CameraFogFar
		CameraFogFar = 30
		CameraFogRange(Camera,CameraFogFar,CameraFogFar)
	EndIf
	CameraRange(Camera,0.01,GetCameraFogRangeFar(Camera)*2.0)
	
End Function

Function TurnNVOff()
	
	AmbientLightRooms(0)
	EntityColor Overlay[2],255,255,255
	If gc\CurrGamemode = 3 Then
		If mp_O\Gamemode\ID = Gamemode_Deathmatch Then
			CameraFogRange(Camera,CameraFogNear,CameraFogFar*5)
		ElseIf mp_O\Gamemode\ID = Gamemode_Waves Then
			CameraFogRange(Camera,CameraFogNear,CameraFogFar*3)
		Else
			CameraFogRange(Camera,CameraFogNear,CameraFogFar)
		EndIf
	Else
		CameraFogFar = StoredCameraFogFar
		CameraFogRange(Camera,CameraFogFar,CameraFogFar)
	EndIf
	CameraRange(Camera,0.01,GetCameraFogRangeFar(Camera)*2.0)
	
End Function

Function CreateDamageOverlay()
	
	mpl\DamageOverlay = LoadSprite("GFX\HUD\Blood_Overlay.jpg",1+2,Ark_Blur_Cam)
	ScaleSprite mpl\DamageOverlay,1.0,Float(opt\GraphicHeight)/Float(opt\GraphicWidth)
	EntityFX(mpl\DamageOverlay, 1)
	EntityOrder(mpl\DamageOverlay, -2001)
	MoveEntity(mpl\DamageOverlay, 0, 0, 1.0)
	HideEntity(mpl\DamageOverlay)
	
End Function

Function UpdateDamageOverlay()
	
	If gc\CurrGamemode = 3 Then
		If mpl\DamageTimer > 0.0 Then
			ShowEntity mpl\DamageOverlay
			EntityAlpha mpl\DamageOverlay,Clamp(mpl\DamageTimer / 70.0, 0.0, 1.0)
			mpl\DamageTimer = Max(mpl\DamageTimer - fps\Factor[0], 0)
		Else
			HideEntity mpl\DamageOverlay
		EndIf
	Else
		If (Not hds\Wearing) Lor hds\Wearing And hds\Health <= 35 Then
			If mpl\DamageTimer > 0.0 Then
				ShowEntity mpl\DamageOverlay
				EntityAlpha mpl\DamageOverlay,Clamp(mpl\DamageTimer / 70.0, 0.0, 1.0)
				mpl\DamageTimer = Max(mpl\DamageTimer - fps\Factor[0], 0)
			Else
				HideEntity mpl\DamageOverlay
			EndIf
		EndIf
	EndIf
	
End Function

Function CreateSPPlayer()
	Local temp#, temp2#
	
	temp# = (0.033 / 2.5)
	
	psp = New PlayerSP
	psp\Health = 100
	;psp\Karma = 0
	If opt\PlayerModelEnabled Then
		psp\ShowPlayerModel = True
	EndIf
	
	If clm\NTFMode Then
		Designation$ = GetLocalString("Singleplayer", "designation_ntf")
	ElseIf clm\DMode
		Designation$ = GetLocalStringR("Singleplayer", "designation_d",ClassDNumber)
	Else
		Designation$ = GetLocalString("Singleplayer", "designation")
	EndIf
	
; ~ [BODY MODELS]
	
	psp\ModelCollider = CreatePivot()
	
; ~ Story Mode
	; ~ Default
	psp\Model = LoadAnimMesh_Strict("GFX\Npcs\Player\Guard_Body.b3d", psp\ModelCollider)
	MeshCullBox (psp\Model, -MeshWidth(psp\Model), -MeshHeight(psp\Model), -MeshDepth(psp\Model)*5, MeshWidth(psp\Model)*2, MeshHeight(psp\Model)*2, MeshDepth(psp\Model)*10)
	ScaleEntity psp\Model, temp, temp, temp
	PositionEntity psp\Model,0,0,-0.1
	HideEntity(psp\Model)
	; ~ Vest
	psp\ModelVest = LoadAnimMesh_Strict("GFX\Npcs\Player\Guard_Body_Vest.b3d", psp\ModelCollider)
	MeshCullBox (psp\ModelVest, -MeshWidth(psp\ModelVest), -MeshHeight(psp\ModelVest), -MeshDepth(psp\ModelVest)*5, MeshWidth(psp\ModelVest)*2, MeshHeight(psp\ModelVest)*2, MeshDepth(psp\ModelVest)*10)
	ScaleEntity psp\ModelVest, temp, temp, temp
	PositionEntity psp\ModelVest,0,0,-0.1
	HideEntity(psp\ModelVest)
	; ~ Hazmat
	psp\ModelHazmat = LoadAnimMesh_Strict("GFX\Npcs\Player\Hazmat_Body.b3d", psp\ModelCollider)
	MeshCullBox (psp\ModelHazmat, -MeshWidth(psp\ModelHazmat), -MeshHeight(psp\ModelHazmat), -MeshDepth(psp\ModelHazmat)*5, MeshWidth(psp\ModelHazmat)*2, MeshHeight(psp\ModelHazmat)*2, MeshDepth(psp\ModelHazmat)*10)
	ScaleEntity psp\ModelHazmat, temp, temp, temp
	PositionEntity psp\ModelHazmat,0,0,-0.1
	HideEntity(psp\ModelHazmat)
	; ~ HDS
	psp\ModelHDS = LoadAnimMesh_Strict("GFX\Npcs\Player\HDS_Suit_Body.b3d", psp\ModelCollider)
	MeshCullBox (psp\ModelHDS, -MeshWidth(psp\ModelHDS), -MeshHeight(psp\ModelHDS), -MeshDepth(psp\ModelHDS)*5, MeshWidth(psp\ModelHDS)*2, MeshHeight(psp\ModelHDS)*2, MeshDepth(psp\ModelHDS)*10)
	ScaleEntity psp\ModelHDS, temp, temp, temp
	PositionEntity psp\ModelHDS,0,0,-0.1
	HideEntity(psp\ModelHDS)
; ~ Classic Mode
	; ~ NTF
	psp\ModelNTF = LoadAnimMesh_Strict("GFX\Npcs\Player\NTF_Body.b3d", psp\ModelCollider)
	MeshCullBox (psp\ModelNTF, -MeshWidth(psp\ModelNTF), -MeshHeight(psp\ModelNTF), -MeshDepth(psp\ModelNTF)*5, MeshWidth(psp\ModelNTF)*2, MeshHeight(psp\ModelNTF)*2, MeshDepth(psp\ModelNTF)*10)
	ScaleEntity psp\ModelNTF, temp, temp, temp
	PositionEntity psp\ModelNTF,0,0,-0.1
	HideEntity(psp\ModelNTF)
	; ~ Class-D
	psp\ModelD = LoadAnimMesh_Strict("GFX\Npcs\Player\D_Body.b3d", psp\ModelCollider)
	MeshCullBox (psp\ModelD, -MeshWidth(psp\ModelD), -MeshHeight(psp\ModelD), -MeshDepth(psp\ModelD)*5, MeshWidth(psp\ModelD)*2, MeshHeight(psp\ModelD)*2, MeshDepth(psp\ModelD)*10)
	ScaleEntity psp\ModelD, temp, temp, temp
	PositionEntity psp\ModelD,0,0,-0.1
	HideEntity(psp\ModelD)
; ~ End
	
; ~ [END]
	
	mtfd = New MTFDialogue
	mtfd\CurrentDialogue = -1
	mtfd\Timer = Rand(MTF_DIALOGUE_TIMER_MIN, MTF_DIALOGUE_TIMER_MAX) ; ~ Starting timer
	mtfd\Dialogues[0] = %10
	mtfd\Dialogues[1] = %111011
	mtfd\Dialogues[2] = %111011
	mtfd\Dialogues[3] = %1011
	
End Function

Function UpdatePlayerModel()
	Local model%
	Local g.Guns, j%, sf%, b%, t1%, name$, tex%, i%, armsStr$
	
	If opt\PlayerModelEnabled Then
		psp\ShowPlayerModel = True
	Else
		psp\ShowPlayerModel = False
	EndIf
	
	If (Not clm\NTFMode) And (Not clm\DMode) Then
		If wbi\Vest Then
			model = psp\ModelVest
			HideEntity(psp\Model)
			HideEntity(psp\ModelHazmat)
			HideEntity(psp\ModelHDS)
		ElseIf wbi\Hazmat Then
			model = psp\ModelHazmat
			HideEntity(psp\Model)
			HideEntity(psp\ModelVest)
			HideEntity(psp\ModelHDS)
		ElseIf hds\Wearing Then
			model = psp\ModelHDS
			HideEntity(psp\Model)
			HideEntity(psp\ModelHazmat)
			HideEntity(psp\ModelVest)
		Else
			model = psp\Model
			HideEntity(psp\ModelVest)
			HideEntity(psp\ModelHazmat)
			HideEntity(psp\ModelHDS)
		EndIf
		If hds\Wearing Then
			tex = psp\HDSArms
		ElseIf wbi\Hazmat Then
			tex = psp\HazmatArms
		Else
			tex = psp\GuardArms
		EndIf
	ElseIf clm\NTFMode Then
		tex = psp\NTFArms
		model = psp\ModelNTF
	Else
		tex = psp\DArms
		model = psp\ModelD
	EndIf
	
	; ~ Arms Texture
	
	If hds\Wearing Then
		If (Not psp\HDSArms) Then
			tex = LoadTexture_Strict("GFX\weapons\hands_hds.png")
			
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
			
			psp\GuardArms = False
			psp\HDSArms = True
			psp\HazmatArms = False
		EndIf
	ElseIf wbi\Hazmat Then
		If (Not psp\HazmatArms) Then
			tex = LoadTexture_Strict("GFX\weapons\hands_hazmat.png")
			
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
			
			psp\GuardArms = False
			psp\HDSArms = False
			psp\HazmatArms = True
		EndIf
	Else
		If (Not psp\GuardArms) Then
			
			If clm\DMode Then
				armsStr = "hands_d.jpg"
			ElseIf clm\NTFMode
				armsStr = "hands_ntf.png"
			Else
				armsStr = "hands.png"
			EndIf
			
			tex = LoadTexture_Strict("GFX\weapons\"+armsStr)
			
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
			
			psp\GuardArms = True
			psp\HDSArms = False
			psp\HazmatArms = False
		EndIf
	EndIf
	
	ShowEntity(model)
	PositionEntity psp\ModelCollider, EntityX(Camera), EntityY(Camera)-0.9, EntityZ(Camera)
	RotateEntity psp\ModelCollider, 0,EntityYaw(Camera), 0
	
	If opt\PlayerModelEnabled Then
		ShowEntity(model)
	Else
		HideEntity(model)
	EndIf
	If psp\ShowPlayerModel Then
		ShowEntity(model)
	Else
		HideEntity(model)
	EndIf
	
	If CurrSpeed > 0 And (Not IsPlayerSprinting) And (Not Crouch) Then
		If KeyDown(KEY_UP) Then															; ~ Walk North
			Animate2(model,AnimTime(model),2,32,0.45)
		ElseIf KeyDown(KEY_DOWN) Then													; ~ Walk South
			Animate2(model,AnimTime(model),33,61,0.45)
		ElseIf KeyDown(KEY_RIGHT) Then													; ~ Walk East
			Animate2(model,AnimTime(model),62,88,0.45)
		ElseIf KeyDown(KEY_LEFT) Then													; ~ Walk West
			Animate2(model,AnimTime(model),89,115,0.45)
		EndIf
	ElseIf CurrSpeed > 0 And IsPlayerSprinting And (Not Crouch) Then
		If KeyDown(KEY_UP) Then															; ~ Run North
			Animate2(model,AnimTime(model),2,32,0.7)
		ElseIf KeyDown(KEY_DOWN) Then													; ~ Run South
			Animate2(model,AnimTime(model),33,61,0.7)
		ElseIf KeyDown(KEY_RIGHT) Then													; ~ Run East
			Animate2(model,AnimTime(model),62,88,0.7)
		ElseIf KeyDown(KEY_LEFT) Then													; ~ Run West
			Animate2(model,AnimTime(model),89,115,0.7)
		EndIf
	ElseIf CurrSpeed < 1 And (Not IsPlayerSprinting) And (Not Crouch) Then				; ~ Idle
		SetAnimTime(model,1)
	ElseIf CurrSpeed = 0 And Crouch Then												; ~ Crouch
		Animate2(model,AnimTime(model),116,135,0.5,False)
	ElseIf CurrSpeed > 0 And Crouch Then
		If KeyDown(KEY_UP) Then															; ~ Crouch Walk North
			Animate2(model,AnimTime(model),136,166,0.24)
		ElseIf KeyDown(KEY_DOWN) Then													; ~ Crouch Walk South
			Animate2(model,AnimTime(model),167,197,0.24)
		ElseIf KeyDown(KEY_RIGHT) Then													; ~ Crouch Walk East
			Animate2(model,AnimTime(model),198,228,0.24)
		ElseIf KeyDown(KEY_LEFT) Then													; ~ Crouch Walk West
			Animate2(model,AnimTime(model),229,259,0.24)
		EndIf
	Else																				; ~ (Should be standup from crouch, but apparently doesn't work)
		Animate2(model,AnimTime(model),135,116,-0.5,False)
	EndIf
	
End Function

Function DestroySPPlayer()
	
	Delete psp
	
	Delete mtfd
	
End Function

Function IsSPPlayerAlive()
	
	If psp\Health > 0 Then Return True
	
	Return False
	
End Function

Function DamageSPPlayer(amount#, only_health%=False, kevlar_protect_factor#=4.0, helmet_protect_factor#=6.0)
	
	If (Not GodMode) Then
		If (Not hds\Wearing) Then
			If only_health Lor psp\Kevlar = 0.0 Then
				psp\Health = Max(psp\Health - amount, 0.0)
			Else
				psp\Kevlar = Max(psp\Kevlar - amount, 0.0)
				psp\Helmet = Max(psp\Helmet - amount, 0.0)
				If kevlar_protect_factor > 0.0 And wbi\Vest Then
					psp\Health = Max(psp\Health - amount / kevlar_protect_factor, 0.0)
				EndIf
				If helmet_protect_factor > 0.0 And wbi\Helmet Lor mpl\HasNTFGasmask Then
					psp\Health = Max(psp\Health - amount / helmet_protect_factor, 0.0)
				EndIf
			EndIf
		ElseIf hds\Wearing And hds\Health >= 35 Then
			hds\Health = Max(hds\Health - amount/20, 0.0)
			If hds\Health <= 90 And hds\Health > 89 Lor hds\Health <= 76 And hds\Health > 75 Then
				If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[1])
			EndIf
			If hds\Health <= 50 And hds\Health > 49 Lor hds\Health <= 36 And hds\Health > 35 Then
				If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[2])
			EndIf
			If hds\Health <= 35 And hds\Health > 34 Lor hds\Health <= 30 And hds\Health > 29 Then
				If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[3])
			EndIf
			If hds\Health <= 20 And hds\Health > 19 Lor hds\Health <= 15 And hds\Health > 14 Then
				If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[4])
			EndIf
			If hds\Health < 5 Then
				Select Rand(100)
					Case 0
						If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[(Rand(8,21))])
				End Select
			EndIf
		Else
			hds\Health = Max(hds\Health - amount/15, 0.0)
			psp\Health = Max(psp\Health - amount/2, 0.0)
		EndIf
	EndIf
	
	If (Not wbi\Helmet Lor mpl\HasNTFGasmask) Then
		psp\Helmet = 0
	EndIf
	
	If wbi\Vest > 0 And psp\Kevlar < 1 Then
		wbi\Vest = 0
		CreateMsg(GetLocalString("Items","vest_destroyed"))
	EndIf
	
	If wbi\Helmet > 0 And psp\Helmet < 1 Then
		wbi\Helmet = 0
		CreateMsg(GetLocalString("Items","helmet_destroyed"))
	EndIf
	
	If hds\Wearing Then
		If hds\Health <= 10 And psp\Health <= 10 Then
			If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[5])
		EndIf
		
		If hds\Health <= 5 And psp\Health <= 5 And (Not hds\isBroken) Then
			If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[6])
		ElseIf hds\isBroken Then
			If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[(Rand(8,21))])
		EndIf
		
		If psp\Health < 1 Then
			If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[7])
		EndIf
	EndIf
		
End Function

Function HealSPPlayer(amount#)
	
	psp\Health = Min(psp\Health + amount, 100.0)
	
End Function

Function HealHazardousDefenceSuit(amount#)
	
	hds\Health = Min(hds\Health + amount, 100.0)
	
End Function

Type MTFDialogue
	Field Enabled%
	Field Timer#
	Field IsPlaying%
	Field CurrentDialogue%
	Field Dialogues%[MTF_DIALOGUE_MAX]
	Field CurrentSequence%
	Field CurrentProgress%
	Field CurrentChannel%
	Field EntityReference%
	Field PrevDialogue%
End Type

Function UpdatePlayerDialogue()
	Local value%, n.NPCs, suffix$
	
	If (mtfd\Enabled And psp\Health > 0) Then
		mtfd\Timer = mtfd\Timer - fps\Factor[0]
	EndIf
	
	If mtfd\Timer <= 0.0 Then
		If (Not mtfd\IsPlaying) Then
			mtfd\CurrentProgress = 0
			mtfd\CurrentChannel = 0
			mtfd\PrevDialogue = mtfd\CurrentDialogue
			While mtfd\CurrentDialogue = mtfd\PrevDialogue
				mtfd\CurrentDialogue = Rand(0, MTF_DIALOGUE_MAX-1)
			Wend
			mtfd\CurrentSequence = mtfd\Dialogues[mtfd\CurrentDialogue]
			mtfd\IsPlaying = True
		EndIf
		
		If (Not mtfd\CurrentChannel) Lor (Not ChannelPlaying(mtfd\CurrentChannel)) Then
			value = ((mtfd\CurrentSequence Shr mtfd\CurrentProgress*MTF_DIALOGUE_NUM_OF_BITS) Mod (2^MTF_DIALOGUE_NUM_OF_BITS))
				
			Select value
				Case 1
					suffix = "player"
				Case 2
					suffix = "regular"
				Case 3
					suffix = "medic"
			End Select
			
			If value > 0 Then
				If clm\NTFMode Then
					mtfd\CurrentChannel = PlaySound_Strict(LoadTempSound("SFX\Player\Dialogue\NTF\line_" + (mtfd\CurrentDialogue+1) + "_" + (mtfd\CurrentProgress+1) + "_" + suffix + ".ogg"))
					
					If value = 1 Then
						;Player
						psp\SoundCHN = mtfd\CurrentChannel
					Else
						;NPC
						For n = Each NPCs
							If n\NPCtype = NPCtypeNTF And n\PrevState = (value-1) Then
								n\SoundChn = mtfd\CurrentChannel
								mtfd\EntityReference = n\Collider
								Exit
							EndIf
						Next
					EndIf
				Else
					mtfd\CurrentChannel = PlaySound_Strict(LoadTempSound("SFX\Player\Dialogue\line_" + (mtfd\CurrentDialogue+1) + "_" + (mtfd\CurrentProgress+1) + "_player.ogg"))
					
					psp\SoundCHN = mtfd\CurrentChannel
				EndIf
			Else
				mtfd\Timer = Rand(MTF_DIALOGUE_TIMER_MIN, MTF_DIALOGUE_TIMER_MAX)
				mtfd\IsPlaying = False
			EndIf
			
			mtfd\CurrentProgress = mtfd\CurrentProgress + 1
		EndIf
		
		If ChannelPlaying(mtfd\CurrentChannel) And mtfd\CurrentChannel <> psp\SoundCHN Then
			UpdateSoundOrigin(mtfd\CurrentChannel, Camera, mtfd\EntityReference,10,opt\MasterVol*opt\VoiceVol)
		Else
			ChannelVolume(psp\SoundCHN,opt\MasterVol*opt\VoiceVol)
		EndIf
		
	EndIf
	
End Function

Function PlayNewDialogue(id%, sequence%)
	
	mtfd\Timer = 0.0
	mtfd\CurrentProgress = 0
	mtfd\IsPlaying = True
	mtfd\CurrentDialogue = (id-1)
	mtfd\CurrentSequence = sequence
	
End Function

; ~ Inlcuding the Player Animations file

Include "SourceCode\Player_Animations.bb"

; ~ Controller

Function GetLeftAnalogStickPitch#(onlydir%=False,invert%=True)
	
	If (Not co\Enabled) Then Return
	If Abs(JoyY())<0.15 And (Not onlydir) Then Return
	If onlydir
		If invert
			Return -JoyYDir()
		EndIf
		Return JoyYDir()
	EndIf
	If invert Then Return -JoyY()
	Return JoyY()
	
End Function

Function GetLeftAnalogStickYaw#(onlydir%=False,invert%=False)
	
	If (Not co\Enabled) Then Return
	If Abs(JoyX())<0.15 And (Not onlydir) Then Return
	If onlydir
		If invert
			Return -JoyXDir()
		EndIf
		Return JoyXDir()
	EndIf
	If invert Then Return -JoyX()
	Return JoyX()
	
End Function

Function GetRightAnalogStickPitch#(onlydir%=False,invert%=True)
	
	If (Not co\Enabled) Then Return
	If Abs(JoyRoll()/180.0)<0.15 Then Return
	If co\InvertAxis[Controller_YAxis] Then invert = Not invert
	If onlydir
		If invert
			Return -Sgn(JoyRoll()/180.0)
		EndIf
		Return Sgn(JoyRoll()/180.0)
	EndIf
	If invert Then Return -(JoyRoll()/180.0)
	Return JoyRoll()/180.0
	
End Function

Function GetRightAnalogStickYaw#(onlydir%=False,invert%=False)
	
	If (Not co\Enabled) Then Return
	If Abs(JoyZ())<0.15 And (Not onlydir) Then Return
	If onlydir
		If invert
			Return -JoyZDir()
		EndIf
		Return JoyZDir()
	EndIf
	If invert Then Return -JoyZ()
	Return JoyZ()
	
End Function

Function InteractHit(key%,controllerkey%)
	
	If (Not co\Enabled)
		If KeyHit(key%) Then Return True
	Else
		If JoyHit(controllerkey%) Then Return True
	EndIf
	Return False
	
End Function

Function GetDPadButtonPress()
	
	If (Not co\Enabled) Then Return
	Return JoyHat()
	
End Function

Function UpdateMenuControllerSelection(maxbuttons%,currTab%,system%=0,maxcurrbuttons%=1)
	
	If (Not co\Enabled) Then Return
	Select system
		Case 0,2,3
			If co\WaitTimer = 0.0
				If system <> 3
					If GetDPadButtonPress()=0
						co\CurrButton[currTab] = co\CurrButton[currTab] - 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
						If co\CurrButton[currTab] < 0
							co\CurrButton[currTab] = maxbuttons-1
						EndIf
					ElseIf GetDPadButtonPress()=180
						co\CurrButton[currTab] = co\CurrButton[currTab] + 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
						If co\CurrButton[currTab] > maxbuttons-1
							co\CurrButton[currTab] = 0
						EndIf
					EndIf
					
					If GetLeftAnalogStickPitch(True) > 0.0
						co\CurrButton[currTab] = co\CurrButton[currTab] - 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
						If co\CurrButton[currTab] < 0
							co\CurrButton[currTab] = maxbuttons-1
						EndIf
					ElseIf GetLeftAnalogStickPitch(True) < 0.0
						co\CurrButton[currTab] = co\CurrButton[currTab] + 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
						If co\CurrButton[currTab] > maxbuttons-1
							co\CurrButton[currTab] = 0
						EndIf
					EndIf
				EndIf
				
				If system = 2 Lor system = 3
					If GetDPadButtonPress()=270
						co\CurrButtonSub[currTab] = co\CurrButtonSub[currTab] - 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
						If co\CurrButtonSub[currTab] < 0
							co\CurrButtonSub[currTab] = maxcurrbuttons-1
						EndIf
					ElseIf GetDPadButtonPress()=90
						co\CurrButtonSub[currTab] = co\CurrButtonSub[currTab] + 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
						If co\CurrButtonSub[currTab] > maxcurrbuttons-1
							co\CurrButtonSub[currTab] = 0
						EndIf
					EndIf
					
					If GetLeftAnalogStickYaw(True) > 0.0
						co\CurrButtonSub[currTab] = co\CurrButtonSub[currTab] + 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
						If co\CurrButtonSub[currTab] > maxcurrbuttons-1
							co\CurrButtonSub[currTab] = 0
						EndIf
					ElseIf GetLeftAnalogStickYaw(True) < 0.0
						co\CurrButtonSub[currTab] = co\CurrButtonSub[currTab] - 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
						If co\CurrButtonSub[currTab] < 0
							co\CurrButtonSub[currTab] = maxcurrbuttons-1
						EndIf
					EndIf
					
					If co\PressedPrev
						co\CurrButtonSub[currTab] = co\CurrButtonSub[currTab] - 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
						If co\CurrButtonSub[currTab] < 0
							co\CurrButtonSub[currTab] = maxcurrbuttons-1
						EndIf
					EndIf
					If co\PressedNext
						co\CurrButtonSub[currTab] = co\CurrButtonSub[currTab] + 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
						If co\CurrButtonSub[currTab] > maxcurrbuttons-1
							co\CurrButtonSub[currTab] = 0
						EndIf
					EndIf
				EndIf
			EndIf
		Case 1
			If co\WaitTimer = 0.0
				If GetDPadButtonPress()=0
					If co\ScrollBarY > 0
						co\ScrollBarY = Max(co\ScrollBarY-0.05,0)
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
					Else
						co\ScrollBarY = 0
					EndIf
				ElseIf GetDPadButtonPress()=180
					If co\ScrollBarY < 1
						co\ScrollBarY = Min(co\ScrollBarY+0.05,1.0)
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
					Else
						co\ScrollBarY = 1
					EndIf
				EndIf
				
				If GetLeftAnalogStickPitch(True) > 0.0
					If co\ScrollBarY > 0
						co\ScrollBarY = Max(co\ScrollBarY-0.05,0)
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
					Else
						co\ScrollBarY = 0
					EndIf
				ElseIf GetLeftAnalogStickPitch(True) < 0.0
					If co\ScrollBarY < 1
						co\ScrollBarY = Min(co\ScrollBarY+0.05,1.0)
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = fps\Factor[1]
					Else
						co\ScrollBarY = 1
					EndIf
				EndIf
			EndIf
			
			co\CurrButtonSub[currTab] = 0
			ScrollBarY = CurveValue(co\ScrollBarY,ScrollBarY,20.0)
	End Select
	
	If co\WaitTimer > 0.0 And co\WaitTimer < 15.0
		co\WaitTimer = co\WaitTimer + fps\Factor[1]
	ElseIf co\WaitTimer >= 15.0
		co\WaitTimer = 0.0
	EndIf
	
End Function

Function MouseAndControllerSelectBox(x%,y%,width%,height%,currButton%=-1,currButtonTab%=0,currButtonSub%=0)
	
	If (Not co\Enabled)
		If MouseOn(x,y,width,height)
			Return True
		EndIf
	Else
		If co\CurrButton[currButtonTab]=currButton
			If co\CurrButtonSub[currButtonTab]=currButtonSub
				Return True
			EndIf
		EndIf
	EndIf
	Return False
	
End Function

Function UpdateControllerSideSelection#(value#,minvalue#,maxvalue#,valuestep#=2.0)
	
	If (Not co\Enabled) Then Return
	If co\WaitTimer=0
		If GetDPadButtonPress()=270
			If value# > minvalue#
				value# = Max(value#-valuestep#,minvalue#)
				PlaySound_Strict co\SelectSFX
				co\WaitTimer = fps\Factor[1]
			Else
				value# = minvalue#
			EndIf
		ElseIf GetDPadButtonPress()=90
			If value# < maxvalue#
				value# = Min(value#+valuestep#,maxvalue#)
				PlaySound_Strict co\SelectSFX
				co\WaitTimer = fps\Factor[1]
			Else
				value# = maxvalue#
			EndIf
		EndIf
		
		If GetLeftAnalogStickYaw(True) > 0.0
			If value# < maxvalue#
				value# = Min(value#+valuestep#,maxvalue#)
				PlaySound_Strict co\SelectSFX
				co\WaitTimer = fps\Factor[1]
			Else
				value# = maxvalue#
			EndIf
		ElseIf GetLeftAnalogStickYaw(True) < 0.0
			If value# > minvalue#
				value# = Max(value#-valuestep#,minvalue#)
				PlaySound_Strict co\SelectSFX
				co\WaitTimer = fps\Factor[1]
			Else
				value# = minvalue#
			EndIf
		EndIf
		
		If co\PressedNext
			If value# < maxvalue#
				value# = Min(value#+valuestep#,maxvalue#)
				PlaySound_Strict co\SelectSFX
				co\WaitTimer = fps\Factor[1]
			Else
				value# = maxvalue#
			EndIf
		EndIf
		If co\PressedPrev
			If value# > minvalue#
				value# = Max(value#-valuestep#,minvalue#)
				PlaySound_Strict co\SelectSFX
				co\WaitTimer = fps\Factor[1]
			Else
				value# = minvalue#
			EndIf
		EndIf
	EndIf
	
	Return value#
	
End Function

Function ResetControllerSelections()
	Local i%
	
	co\WaitTimer# = 0.0
	co\PressedButton% = False
	For i = 0 To Controller_MaxButtons-1
		co\CurrButton[i] = 0
		co\CurrButtonSub[i] = 0
	Next
	co\ScrollBarY# = 0.0
	co\PressedNext = 0
	co\PressedPrev = 0
	co\KeyPad_CurrButton% = 0
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D