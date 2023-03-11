Type ConsoleMsg
	Field txt$
	Field isCommand%
	Field r%,g%,b%
End Type

Function CreateConsoleMsg(txt$,r%=-1,g%=-1,b%=-1,isCommand%=False)
	Local c.ConsoleMsg = New ConsoleMsg
	Insert c Before First ConsoleMsg
	
	c\txt = txt
	c\isCommand = isCommand
	
	c\r = r
	c\g = g
	c\b = b
	
	If (c\r<0) Then c\r = ConsoleR
	If (c\g<0) Then c\g = ConsoleG
	If (c\b<0) Then c\b = ConsoleB
End Function

;! ~ 1: Ingame
;! ~ 2: 3D Menu
;! ~ 3: Multiplayer

Function UpdateConsole(commandSet%)
	If opt\ConsoleEnabled = False Then
		ConsoleOpen = False
		Return
	EndIf
	
	If ConsoleOpen Then
		Local cm.ConsoleMsg
		
		ConsoleR = 255 : ConsoleG = 255 : ConsoleB = 255
		
		Local x% = 0, y% = opt\GraphicHeight-300*MenuScale, width% = opt\GraphicWidth, height% = 300*MenuScale-30*MenuScale
		Local StrTemp$, temp%,  i%
		Local ev.Events, r.Rooms, it.Items
		
		DrawFrame x,y,width,height+30*MenuScale
		
		Local consoleHeight% = 0
		Local scrollbarHeight% = 0
		For cm.ConsoleMsg = Each ConsoleMsg
			consoleHeight = consoleHeight + 15*MenuScale
		Next
		scrollbarHeight = (Float(height)/Float(consoleHeight))*height
		If scrollbarHeight>height Then scrollbarHeight = height
		If consoleHeight<height Then consoleHeight = height
		
		Color 50,50,50
		inBar% = MouseOn(x+width-26*MenuScale,y,26*MenuScale,height)
		If inBar Then Color 70,70,70
		Rect x+width-26*MenuScale,y,26*MenuScale,height,True
		
		
		Color 120,120,120
		inBox% = MouseOn(x+width-23*MenuScale,y+height-scrollbarHeight+(ConsoleScroll*scrollbarHeight/height),20*MenuScale,scrollbarHeight)
		If inBox Then Color 200,200,200
		If ConsoleScrollDragging Then Color 255,255,255
		Rect x+width-23*MenuScale,y+height-scrollbarHeight+(ConsoleScroll*scrollbarHeight/height),20*MenuScale,scrollbarHeight,True
		
		If Not MouseDown(1) Then
			ConsoleScrollDragging=False
		ElseIf ConsoleScrollDragging Then
			ConsoleScroll = ConsoleScroll+((ScaledMouseY()-ConsoleMouseMem)*height/scrollbarHeight)
			ConsoleMouseMem = ScaledMouseY()
		EndIf
		
		If (Not ConsoleScrollDragging) Then
			If MouseHit1 Then
				If inBox Then
					ConsoleScrollDragging=True
					ConsoleMouseMem = ScaledMouseY()
				ElseIf inBar Then
					ConsoleScroll = ConsoleScroll+((ScaledMouseY()-(y+height))*consoleHeight/height+(height/2))
					ConsoleScroll = ConsoleScroll/2
				EndIf
			EndIf
		EndIf
		
		mouseScroll = MouseZSpeed()
		If mouseScroll=1 Then
			ConsoleScroll = ConsoleScroll - 15*MenuScale
		ElseIf mouseScroll=-1 Then
			ConsoleScroll = ConsoleScroll + 15*MenuScale
		EndIf
		
		Local reissuePos%
		If KeyHit(200) Then
			reissuePos% = 0
			If (ConsoleReissue=Null) Then
				ConsoleReissue=First ConsoleMsg
				
				While (ConsoleReissue<>Null)
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos - 15*MenuScale
					ConsoleReissue = After ConsoleReissue
				Wend
				
			Else
				cm.ConsoleMsg = First ConsoleMsg
				While cm<>Null
					If cm=ConsoleReissue Then Exit
					reissuePos = reissuePos-15*MenuScale
					cm = After cm
				Wend
				ConsoleReissue = After ConsoleReissue
				reissuePos = reissuePos-15*MenuScale
				
				While True
					If (ConsoleReissue=Null) Then
						ConsoleReissue=First ConsoleMsg
						reissuePos = 0
					EndIf
					
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos - 15*MenuScale
					ConsoleReissue = After ConsoleReissue
				Wend
			EndIf
			
			If ConsoleReissue<>Null Then
				ConsoleInput = ConsoleReissue\txt
				ConsoleScroll = reissuePos+(height/2)
				CursorPos = Len(ConsoleInput)
			EndIf
		EndIf
		
		If KeyHit(208) Then
			reissuePos% = -consoleHeight+15*MenuScale
			If (ConsoleReissue=Null) Then
				ConsoleReissue=Last ConsoleMsg
				
				While (ConsoleReissue<>Null)
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos + 15*MenuScale
					ConsoleReissue = Before ConsoleReissue
				Wend
				
			Else
				cm.ConsoleMsg = Last ConsoleMsg
				While cm<>Null
					If cm=ConsoleReissue Then Exit
					reissuePos = reissuePos+15*MenuScale
					cm = Before cm
				Wend
				ConsoleReissue = Before ConsoleReissue
				reissuePos = reissuePos+15*MenuScale
				
				While True
					If (ConsoleReissue=Null) Then
						ConsoleReissue=Last ConsoleMsg
						reissuePos=-consoleHeight+15*MenuScale
					EndIf
					
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos + 15*MenuScale
					ConsoleReissue = Before ConsoleReissue
				Wend
			EndIf
			
			If ConsoleReissue<>Null Then
				ConsoleInput = ConsoleReissue\txt
				ConsoleScroll = reissuePos+(height/2)
				CursorPos = Len(ConsoleInput)
			EndIf
		EndIf
		
		If ConsoleScroll<-consoleHeight+height Then ConsoleScroll = -consoleHeight+height
		If ConsoleScroll>0 Then ConsoleScroll = 0
		
		Color 255, 255, 255
		
		SelectedInputBox = 2
		Local oldConsoleInput$ = ConsoleInput
		ConsoleInput = InputBoxConsole(x, y + height, width, 30*MenuScale, ConsoleInput, 2, 100,Font_Default)
		If oldConsoleInput<>ConsoleInput Then
			ConsoleReissue = Null
		EndIf
		
		If KeyHit(28) And ConsoleInput <> "" Then
			ConsoleReissue = Null
			ConsoleScroll = 0
			CreateConsoleMsg(ConsoleInput,255,255,0,True)
			If Instr(ConsoleInput, " ") > 0 Then
				StrTemp$ = Lower(Left(ConsoleInput, Instr(ConsoleInput, " ") - 1))
			Else
				StrTemp$ = Lower(ConsoleInput)
			End If
			
			Select commandSet
				Case 1
					Local Cheat.Cheats = First Cheats
					Local e.Events
					Local n.NPCs, n2.NPCs
					Select Lower(StrTemp)
						Case "help"
							;[Block]
							;CreateConsoleMsg("Console is for pussies! Or developers.",255,150,0)
							;[End Block]
						Case "asd"
							;[Block]
							WireFrame 1
							WireframeState=1
							GodMode = 1
							NoClip = 1
							CameraFogNear = 15
							CameraFogFar = 20
							;[End Block]
						Case "status"
							;[Block]
							ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 0
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Status: ")
							CreateConsoleMsg("Coordinates: ")
							CreateConsoleMsg("    - Collider: "+EntityX(Collider)+", "+EntityY(Collider)+", "+EntityZ(Collider))
							CreateConsoleMsg("    - camera: "+EntityX(Camera)+", "+EntityY(Camera)+", "+EntityZ(Camera))
							
							CreateConsoleMsg("Rotation: ")
							CreateConsoleMsg("    - Collider: "+EntityPitch(Collider)+", "+EntityYaw(Collider)+", "+EntityRoll(Collider))
							CreateConsoleMsg("    - camera: "+EntityPitch(Camera)+", "+EntityYaw(Camera)+", "+EntityRoll(Camera))
							
							CreateConsoleMsg("Room: "+PlayerRoom\RoomTemplate\Name)
							For ev.Events = Each Events
								If ev\room = PlayerRoom Then
									CreateConsoleMsg("Room event: "+ev\EventName)
									For i = 0 To MaxEventStates - 1
										CreateConsoleMsg("-    state: "+ev\EventState[i])
									Next
									Exit
								EndIf
							Next
							
							CreateConsoleMsg("Room coordinates: "+Floor(EntityX(PlayerRoom\obj) / 8.0 + 0.5)+", "+ Floor(EntityZ(PlayerRoom\obj) / 8.0 + 0.5))
							CreateConsoleMsg("Stamina: "+Stamina)
							CreateConsoleMsg("Death timer: "+KillTimer)					
							CreateConsoleMsg("Blinktimer: "+BlinkTimer)
							CreateConsoleMsg("Health: "+psp\Health)
							CreateConsoleMsg("Kevlar: "+psp\Kevlar)
							;CreateConsoleMsg("Karma: "+psp\Karma)
							CreateConsoleMsg("HDS health: "+hds\Health)
							CreateConsoleMsg("******************************")
							;[End Block]
						Case "camerapick"
							;[Block]
							ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 0
							c = CameraPick(Camera,opt\GraphicWidth/2, opt\GraphicHeight/2)
							If c = 0 Then
								CreateConsoleMsg("******************************")
								CreateConsoleMsg("No entity  picked")
								CreateConsoleMsg("******************************")								
							Else
								CreateConsoleMsg("******************************")
								CreateConsoleMsg("Picked entity:")
								sf = GetSurface(c,1)
								b = GetSurfaceBrush( sf )
								t = GetBrushTexture(b,0)
								texname$ =  StripPath(TextureName(t))
								CreateConsoleMsg("Texture name: "+texname)
								CreateConsoleMsg("Coordinates: "+EntityX(c)+", "+EntityY(c)+", "+EntityZ(c))
								CreateConsoleMsg("******************************")							
							EndIf
							;[End Block]
						Case "hidedistance"
							;[Block]
							HideDistance = Float(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							CreateConsoleMsg("Hidedistance set to "+HideDistance)
							;[End Block]
						Case "ending"
							;[Block]
							SelectedEnding = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							;[End Block]
						Case "noclipspeed"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							NoClipSpeed = Float(StrTemp)
							;[End Block]
						Case "reset1033ru" 
							;[Block]
							I_1033RU\HP = 200
							I_1033RU\DHP = 0
							;[End Block]
						Case "unlockattachments","unatts","allatts","unat"
							;[Block]
							Local g.Guns
							
							For g.Guns = Each Guns
								g\PickedUpSuppressor = True
								g\PickedUpAimPoint = True
								g\PickedUpRedDot = True
								g\PickedUpAcog = True
								g\PickedUpStock = True
								g\PickedUpFoldingStock = True
								g\PickedUpVerticalGrip = True
								g\PickedUpRail = True
								g\PickedUpEotech = True
								g\PickedUpExtMag = True
								g\PickedUpLaserSight = True
								g\PickedUpMUI = True
							Next
							;[End Block]
						Case "resetexplodetimer"
							hds\ExplodeTimer = 0
						Case "explodehds"
							hds\isBroken = True
						Case "damagehds"
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							hds\Health = hds\Health - Float(StrTemp)
						Case "addammo", "maxammo"
							;[Block]
							For g.Guns = Each Guns
								If g_I\HoldingGun > 0 And g_I\HoldingGun <> 1 And g_I\HoldingGun <> 2 And g_I\HoldingGun <> 17 Then
									If g\ID = g_I\HoldingGun Then
										g\CurrAmmo = g\MaxCurrAmmo
										g\CurrReloadAmmo = g\MaxReloadAmmo
										If g_I\HoldingGun = 15 Then
											g\CurrAmmo = g\MaxCurrAmmo
											g\CurrReloadAmmo = g\MaxReloadAmmo
											g\CurrAltAmmo = g\MaxCurrAltAmmo
											g\CurrReloadAltAmmo = g\MaxReloadAltAmmo
										EndIf
									EndIf
								EndIf
							Next
							;[End Block]
							;! ~ TODO: Remove on public release!!!
						Case "skip_room3_ct","fuckfuses","nor3ct"
							;[Block]
							If (Not TaskExists(TASK_COME_BACK_TO_GUARD)) Then
								BeginTask(TASK_COME_BACK_TO_GUARD)
							EndIf
							If TaskExists(TASK_FIND_ROOM3_CT) Then
								EndTask(TASK_FIND_ROOM3_CT)
							EndIf
							If TaskExists(TASK_FIND_MEDKIT) Then
								EndTask(TASK_FIND_MEDKIT)
							EndIf
							;[End Block]
						Case "detonateomega","omegad"
							;[Block]
							ecst\OmegaWarheadActivated = True
							ecst\OmegaWarheadDetonate = True
							;[End Block]
						Case "unlockez","foundez","wasinhcz"
							;[Block]
							ecst\EzDoorOpened = True
							ecst\WasInHCZ = True
							ecst\NTFArrived = True
							If TaskExists(TASK_FINDWAY_EZDOOR) Then
								EndTask(TASK_FINDWAY_EZDOOR)
							EndIf
							If TaskExists(TASK_FINDWAY_EZDOOR_ALT) Then
								EndTask(TASK_FINDWAY_EZDOOR_ALT)
							EndIf
							;[End Block]
						Case "gotoo5","needo5"
							;[Block]
							ecst\NTFArrived = True
							If (Not TaskExists(TASK_FIND_ROOM1_O5)) Then
								BeginTask(TASK_FIND_ROOM1_O5)
							EndIf
							;[End Block]
						Case "wasino5"
							;[Block]
							ecst\WasInO5 = True
							If (Not TaskExists(TASK_FIND_AREA_076)) Then
								BeginTask(TASK_FIND_AREA_076)
							EndIf
							;[End Block]
						Case "no076"
							;[Block]
							ecst\WasIn076 = True
							If (Not TaskExists(TASK_COME_BACK_TO_JANITOR)) Then
								BeginTask(TASK_COME_BACK_TO_JANITOR)
							EndIf
							;[End Block]
						Case "need008"
							;[Block]
							ecst\NewCavesEvent = True
							If (Not TaskExists(TASK_FIND_CAVES)) Then
								BeginTask(TASK_FIND_CAVES)
							EndIf
							;[End Block]
						Case "gotoo5again","needo5again"
							;[Block]
							ecst\WasInCaves = True
							ecst\CIArrived = True
							If (Not TaskExists(TASK_COME_BACK_TO_O5)) Then
								BeginTask(TASK_COME_BACK_TO_O5)
							EndIf
							;[End Block]
						Case "needbcz"
							;[Block]
							If (Not TaskExists(TASK_GO_TO_BCZ)) Then
								BeginTask(TASK_GO_TO_BCZ)
							EndIf
							;[End Block]
						Case "gotoo5again2","needo5again2","3o5"
							;[Block]
							ecst\WasInCaves = True
							ecst\WasInO5Again = True
							ecst\WasInBCZ = True
							If (Not TaskExists(TASK_COME_BACK_TO_O5_AGAIN)) Then
								BeginTask(TASK_COME_BACK_TO_O5_AGAIN)
							EndIf
							;[End Block]
						Case "wasinpo"
							;[Block]
							ecst\UnlockedGateDoors = True
							If (Not TaskExists(TASK_GET_TOPSIDE)) Then
								BeginTask(TASK_GET_TOPSIDE)
							EndIf
							If (Not TaskExists(TASK_LAUNCH_ROCKET)) Then
								BeginTask(TASK_LAUNCH_ROCKET)
							EndIf
							;[End Block]
							;!-------------
						Case "injure"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							psp\Health = psp\Health - Float(StrTemp)
							;[End Block]
						Case "infect"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							I_008\Timer = Float(StrTemp)
							;[End Block]
;						Case "give_karma","karma"
;							;[Block]
;							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
;							
;							psp\Karma = psp\Karma + Float(StrTemp)
;							;[End Block]
						Case "healarmor"
							;[Block]
							If hds\Wearing Then
								HealHazardousDefenceSuit(100)
							EndIf
							If wbi\Vest Then
								psp\Kevlar = 100
							EndIf
							;[End Block]
						Case "heal"
							;[Block]
							HealSPPlayer(100)
							Sanity = 0
							
							I_008\Timer = 0
							
							I_016\Timer = 0
							
							I_059\Timer = 0
							I_059\Using = False
							
							I_109\Timer = 0
							I_109\Used = 0
							I_109\Vomit = False
							I_109\VomitTimer = 0
							
							I_198\Timer = 0
							I_198\DeathTimer = 0
							I_198\Injuries = 0
							I_198\Vomit = False
							I_198\VomitTimer = 0
							
							I_207\Timer = 0
							I_207\DeathTimer = 0
							I_207\Factor = 0
							
							I_357\Timer = 0
							I_357\Using = False
							
							I_409\Timer = 0
							
							For i = 0 To 5
								I_1025\State[i]=0
							Next
							
							I_1079\Foam = 0
							I_1079\Trigger = 0
							;[End Block]
						Case "aimcross"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select StrTemp
								Case "on", "1", "true"
									IsUsingAimCross = True							
								Case "off", "0", "false"
									IsUsingAimCross = False
								Default
									IsUsingAimCross = Not IsUsingAimCross
							End Select
							
							If WireframeState Then
								CreateConsoleMsg("WIREFRAME ON")
							Else
								CreateConsoleMsg("WIREFRAME OFF")	
							EndIf
							;[End Block]
						Case "teleport", "tp"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select StrTemp
								Case "895", "scp-895"
									StrTemp = "coffin"
								Case "scp-914"
									StrTemp = "914"
								Case "offices", "office"
									StrTemp = "room2offices"
							End Select
							
							For r.Rooms = Each Rooms
								If r\RoomTemplate\Name = StrTemp Then
									PositionEntity (Collider, EntityX(r\obj), EntityY(r\obj)+0.7, EntityZ(r\obj))
									ResetEntity(Collider)
									UpdateDoors()
									UpdateRooms()
									For it.Items = Each Items
										it\disttimer = 0
									Next
									PlayerRoom = r
									Exit
								EndIf
							Next
							
							If PlayerRoom\RoomTemplate\Name <> StrTemp Then CreateConsoleMsg("Room not found.",255,150,0)
							;[End Block]
						Case "spawnitem","si","giveitem","gi"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							temp = False
							
							For itt.Itemtemplates = Each ItemTemplates
								If (Lower(itt\name) = StrTemp) Then
									temp = True
									CreateConsoleMsg(itt\name + " spawned.")
									it.Items = CreateItem(itt\name, itt\tempname, EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
									EntityType(it\Collider, HIT_ITEM)
									If it\name = "SCP-127" Then
										it\state = 30
									EndIf
									Exit
								ElseIf (Lower(itt\tempname) = StrTemp) Then
									temp = True
									CreateConsoleMsg(itt\name + " spawned.")
									it.Items = CreateItem(itt\name, itt\tempname, EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
									EntityType(it\Collider, HIT_ITEM)
									Exit
								EndIf
							Next
							
							If temp = False Then CreateConsoleMsg("Item not found.",255,150,0)
							;[End Block]
						Case "wireframe","wh"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select StrTemp
								Case "on", "1", "true"
									WireframeState = True							
								Case "off", "0", "false"
									WireframeState = False
								Default
									WireframeState = Not WireframeState
							End Select
							
							If WireframeState Then
								CreateConsoleMsg("WIREFRAME ON")
							Else
								CreateConsoleMsg("WIREFRAME OFF")	
							EndIf
							
							WireFrame WireframeState
							;[End Block]
						Case "173speed"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Curr173\Speed = Float(StrTemp)
							CreateConsoleMsg("173's speed set to " + StrTemp)
							;[End Block]
						Case "106speed"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Curr106\Speed = Float(StrTemp)
							CreateConsoleMsg("106's speed set to " + StrTemp)
							;[End Block]
						Case "173state"
							;[Block]
							CreateConsoleMsg("SCP-173")
							CreateConsoleMsg("Position: " + EntityX(Curr173\obj) + ", " + EntityY(Curr173\obj) + ", " + EntityZ(Curr173\obj))
							CreateConsoleMsg("Idle: " + Curr173\Idle)
							CreateConsoleMsg("State: " + Curr173\State)
							;[End Block]
						Case "106state"
							;[Block]
							CreateConsoleMsg("SCP-106")
							CreateConsoleMsg("Position: " + EntityX(Curr106\obj) + ", " + EntityY(Curr106\obj) + ", " + EntityZ(Curr106\obj))
							CreateConsoleMsg("Idle: " + Curr106\Idle)
							CreateConsoleMsg("State: " + Curr106\State)
							;[End Block]
						Case "reset096"
							;[Block]
							For n.NPCs = Each NPCs
								If n\NPCtype = NPCtype096 Then
									n\State = 0
									StopStream_Strict(n\SoundChn) : n\SoundChn=0
									If n\SoundChn2<>0
										StopStream_Strict(n\SoundChn2) : n\SoundChn2=0
									EndIf
									Exit
								EndIf
							Next
							;[End Block]
						Case "disable173"
							;[Block]
							Curr173\Idle = SCP173_DISABLED
							HideEntity Curr173\obj
							HideEntity Curr173\obj2
							HideEntity Curr173\Collider
							;[End Block]
						Case "enable173"
							;[Block]
							Curr173\Idle = SCP173_ACTIVE
							ShowEntity Curr173\obj
							ShowEntity Curr173\obj2
							ShowEntity Curr173\Collider
							;[End Block]
						Case "disable106"
							;[Block]
							Curr106\Idle = True
							Curr106\State = 200000
							Contained106 = True
							;[End Block]
						Case "enable106"
							;[Block]
							Curr106\Idle = False
							Contained106 = False
							ShowEntity Curr106\Collider
							ShowEntity Curr106\obj
							;[End Block]
						Case "disable096"
							;[Block]
							Curr096\Idle = True
							HideEntity Curr096\obj
							HideEntity Curr096\Collider
							;[End Block]
						Case "enable096"
							;[Block]
							Curr096\Idle = False
							ShowEntity Curr096\obj
							ShowEntity Curr096\Collider
							;[End Block]
						Case "defeat076"
							;[Block]
							For n = Each NPCs
								If n\NPCtype = NPCtype076 Then
									If n\HP > 0 Then
										If TaskExists(TASK_DEFEAT_076) Then
											EndTask(TASK_DEFEAT_076)
										EndIf
										n\HP = 0
										n\IsDead = True
									EndIf
								EndIf
							Next
							;[End Block]
						Case "halloween"
							;[Block]
							HalloweenTex = Not HalloweenTex
							If HalloweenTex Then
								Local tex = LoadTexture_Strict("GFX\npcs\173h.pt", 1)
								TextureBlend(tex,5)
								EntityTexture Curr173\obj, tex, 0, 0
								DeleteSingleTextureEntryFromCache tex
								CreateConsoleMsg("173 JACK-O-LANTERN ON")
							Else
								Local tex2 = LoadTexture_Strict("GFX\npcs\173texture.jpg", 1)
								EntityTexture Curr173\obj, tex2, 0, 0
								DeleteSingleTextureEntryFromCache tex2
								CreateConsoleMsg("173 JACK-O-LANTERN OFF")
							EndIf
							;[End Block]
						Case "sanic"
							;[Block]
							SuperMan = Not SuperMan
							If SuperMan = True Then
								CreateConsoleMsg("GOTTA GO FAST")
							Else
								CreateConsoleMsg("WHOA SLOW DOWN")
							EndIf
							;[End Block]
						Case "scp-420-j","420","weed"
							;[Block]
							For i = 1 To 20
								If Rand(2)=1 Then
									it.Items = CreateItem("Some SCP-420-J","420", EntityX(Collider,True)+Cos((360.0/20.0)*i)*Rnd(0.3,0.5), EntityY(Camera,True), EntityZ(Collider,True)+Sin((360.0/20.0)*i)*Rnd(0.3,0.5))
								Else
									it.Items = CreateItem("Joint","420s", EntityX(Collider,True)+Cos((360.0/20.0)*i)*Rnd(0.3,0.5), EntityY(Camera,True), EntityZ(Collider,True)+Sin((360.0/20.0)*i)*Rnd(0.3,0.5))
								EndIf
								EntityType (it\collider, HIT_ITEM)
							Next
							If opt\MusicVol > 0 Then
								PlaySound_Strict LoadTempSound("SFX\Music\SCPs\420-J_"+Rand(1,2)+".ogg")
							EndIf
							;[End Block]
						Case "godmode","god"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select StrTemp
								Case "on", "1", "true"
									GodMode = True		
									HealSPPlayer(100)
									If hds\Wearing Then
										HealHazardousDefenceSuit(100)
									EndIf
									If wbi\Vest Then
										psp\Kevlar = 100
									EndIf
								Case "off", "0", "false"
									GodMode = False
								Default
									GodMode = Not GodMode
							End Select	
							If GodMode Then
								CreateConsoleMsg("GODMODE ON")
							Else
								CreateConsoleMsg("GODMODE OFF")	
							EndIf
							;[End Block]
						Case "revive","undead","resurrect"
							;[Block]
							DropSpeed = -0.1
							HeadDropSpeed = 0.0
							Shake = 0
							CurrSpeed = 0
							
							HealSPPlayer(100)
							If hds\Wearing Then
								HealHazardousDefenceSuit(100)
							EndIf
							If wbi\Vest Then
								psp\Kevlar = 100
							EndIf
							Sanity = 0
							
							I_008\Timer = 0
							
							I_016\Timer = 0
							
							I_059\Timer = 0
							I_059\Using = False
							
							I_109\Timer = 0
							I_109\Used = 0
							I_109\Vomit = False
							I_109\VomitTimer = 0
							
							I_198\Timer = 0
							I_198\DeathTimer = 0
							I_198\Injuries = 0
							I_198\Vomit = False
							I_198\VomitTimer = 0
							
							I_207\Timer = 0
							I_207\DeathTimer = 0
							I_207\Factor = 0
							
							I_357\Timer = 0
							I_357\Using = False
							
							For i = 0 To 5
								I_1025\State[i]=0
							Next
							
							I_1079\Foam = 0
							I_1079\Trigger = 0
							
							HeartBeatVolume = 0
							CameraShake = 0
							Shake = 0
							LightFlash = 0
							BlurTimer = 0
							FallTimer = 0
							MenuOpen = False
							GodMode = 0
							NoClip = 0
							KillTimer = 0
							KillAnim = 0
							
							ShowEntity Collider
							
							;[End Block]
						Case "noclip","fly"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select StrTemp
								Case "on", "1", "true"
									NoClip = True
									Playable = True
								Case "off", "0", "false"
									NoClip = False	
									RotateEntity Collider, 0, EntityYaw(Collider), 0
								Default
									NoClip = Not NoClip
									If NoClip = False Then		
										RotateEntity Collider, 0, EntityYaw(Collider), 0
									Else
										Playable = True
									EndIf
							End Select
							
							If NoClip Then
								CreateConsoleMsg("NOCLIP ON")
							Else
								CreateConsoleMsg("NOCLIP OFF")
							EndIf
							
							DropSpeed = 0
							;[End Block]
						Case "showFPS"
							;[Block]
							opt\ShowFPS = Not opt\ShowFPS
							CreateConsoleMsg("ShowFPS: "+Str(opt\ShowFPS))
							;[End Block]
						Case "096state"
							;[Block]
							For n.NPCs = Each NPCs
								If n\NPCtype = NPCtype096 Then
									CreateConsoleMsg("SCP-096")
									CreateConsoleMsg("Position: " + EntityX(n\obj) + ", " + EntityY(n\obj) + ", " + EntityZ(n\obj))
									CreateConsoleMsg("Idle: " + n\Idle)
									CreateConsoleMsg("State: " + n\State)
									Exit
								EndIf
							Next
							CreateConsoleMsg("SCP-096 has not spawned.")
							;[End Block]
						Case "debughud"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Select StrTemp
								Case "on", "1", "true"
									DebugHUD = True
								Case "off", "0", "false"
									DebugHUD = False
								Default
									DebugHUD = Not DebugHUD
							End Select
							
							If DebugHUD Then
								CreateConsoleMsg("Debug Mode On")
							Else
								CreateConsoleMsg("Debug Mode Off")
							EndIf
							;[End Block]
						Case "stopsound", "stfu"
							;[Block]
							For snd.Sound = Each Sound
								For i = 0 To 31
									If snd\channels[i]<>0 Then
										StopChannel snd\channels[i]
									EndIf
								Next
							Next
							CreateConsoleMsg("Stopped all sounds.")
							;[End Block]
						Case "camerafog"
							;[Block]
							args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							CameraFogNear = Float(Left(args, Len(args) - Instr(args, " ")))
							CameraFogFar = Float(Right(args, Len(args) - Instr(args, " ")))
							CreateConsoleMsg("Near set to: " + CameraFogNear + ", far set to: " + CameraFogFar)
							;[End Block]
						Case "gamma"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							ScreenGamma = Int(StrTemp)
							CreateConsoleMsg("Gamma set to " + ScreenGamma)
							;[End Block]
						Case "spawn","summon","s"
							;[Block]
							args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							StrTemp$ = Piece$(args$, 1)
							StrTemp2$ = Piece$(args$, 2)
							
							If (StrTemp <> StrTemp2) Then
								Console_SpawnNPC(StrTemp, StrTemp2)
							Else
								Console_SpawnNPC(StrTemp)
							EndIf
							;[End Block]
						Case "infinitestamina","infstam"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select StrTemp
								Case "on", "1", "true"
									InfiniteStamina% = True						
								Case "off", "0", "false"
									InfiniteStamina% = False
								Default
									InfiniteStamina% = Not InfiniteStamina%
							End Select
							
							If InfiniteStamina
								CreateConsoleMsg("INFINITE STAMINA ON")
							Else
								CreateConsoleMsg("INFINITE STAMINA OFF")	
							EndIf
							;[End Block]
						Case "asd2"
							;[Block]
							GodMode = 1
							InfiniteStamina = 1
							Curr173\Idle = SCP173_DISABLED
							Curr106\Idle = True
							Curr106\State = 200000
							Contained106 = True
							;[End Block]
						Case "toggle_warhead_lever"
							;[Block]
							For e.Events = Each Events
								If e\EventName = "room2nuke" Then
									e\EventState[0] = (Not e\EventState[0])
									Exit
								EndIf
							Next
							;[End Block]
						Case "unlockexits"
							;[Block]
							ecst\UnlockedGateDoors = True
							CreateConsoleMsg("Gates are now unlocked.")
							;[End Block]
						Case "kill","suicide"
							;[Block]
							KillTimer = -1
							Select Rand(4)
								Case 1
									m_msg\DeathTxt = "[REDACTED]"
								Case 2
									m_msg\DeathTxt = Designation+" found dead in Sector [REDACTED]. "
									m_msg\DeathTxt = m_msg\DeathTxt + "The subject appears to have attained no physical damage, and there is no visible indication as to what killed him. "
									m_msg\DeathTxt = m_msg\DeathTxt + "Body was sent for autopsy."
								Case 3
									m_msg\DeathTxt = "EXCP_ACCESS_VIOLATION"
								Case 4
									m_msg\DeathTxt = Designation+" found dead in Sector [REDACTED]. "
									m_msg\DeathTxt = m_msg\DeathTxt + "The subject appears to have scribbled the letters "+Chr(34)+"kys"+Chr(34)+" in his own blood beside him. "
									m_msg\DeathTxt = m_msg\DeathTxt + "No other signs of physical trauma or struggle can be observed. Body was sent for autopsy."
							End Select
							;[End Block]
						Case "playmusic"
							;[Block]
							If Instr(ConsoleInput, " ")<>0 Then
								StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Else
								StrTemp$ = ""
							EndIf
							
							If StrTemp$ <> ""
								PlayCustomMusic% = True
								If CustomMusic <> 0 Then FreeSound_Strict CustomMusic : CustomMusic = 0
								If MusicCHN <> 0 Then StopChannel MusicCHN
								CustomMusic = LoadSound_Strict("SFX\Music\Custom\"+StrTemp$)
								If CustomMusic = 0
									PlayCustomMusic% = False
								EndIf
							Else
								PlayCustomMusic% = False
								If CustomMusic <> 0 Then FreeSound_Strict CustomMusic : CustomMusic = 0
								If MusicCHN <> 0 Then StopChannel MusicCHN
							EndIf
							;[End Block]
						Case "tele"
							;[Block]
							args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							StrTemp$ = Piece$(args$,1," ")
							StrTemp2$ = Piece$(args$,2," ")
							StrTemp3$ = Piece$(args$,3," ")
							PositionEntity Collider,Float(StrTemp$),Float(StrTemp2$),Float(StrTemp3$)
							PositionEntity Camera,Float(StrTemp$),Float(StrTemp2$),Float(StrTemp3$)
							ResetEntity Collider
							ResetEntity Camera
							CreateConsoleMsg("Teleported to coordinates (X|Y|Z): "+EntityX(Collider)+"|"+EntityY(Collider)+"|"+EntityZ(Collider))
							;[End Block]
						Case "notarget"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select StrTemp
								Case "on", "1", "true"
									I_268\Using = 3						
								Case "off", "0", "false"
									I_268\Using = 0
								Default
									I_268\Using = Not I_268\Using
							End Select
							
							If I_268\Using = False Then
								CreateConsoleMsg("NOTARGET OFF")
							ElseIf I_268\Using = 3 Then
								CreateConsoleMsg("NOTARGET ON")	
							EndIf
							;[End Block]
						Case "spawnradio"
							;[Block]
							it.Items = CreateItem("Radio Transceiver", "fineradio", EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
							EntityType(it\Collider, HIT_ITEM)
							it\state = 101
							;[End Block]
						Case "spawnnvg"
							;[Block]
							it.Items = CreateItem("Night Vision Goggles", "nvgoggles", EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
							EntityType(it\Collider, HIT_ITEM)
							it\state = 1000
							;[End Block]
						Case "spawnpumpkin","pumpkin"
							;[Block]
							CreateConsoleMsg("What pumpkin?")
							;[End Block]
						Case "teleport173"
							;[Block]
							PositionEntity Curr173\Collider,EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider)
							ResetEntity Curr173\Collider
							;[End Block]
						Case "seteventstate"
							;[Block]
							args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							StrTemp$ = Piece$(args$,1," ")
							StrTemp2$ = Piece$(args$,2," ")
							StrTemp3$ = Piece$(args$,3," ")
							Local pl_room_found% = False
							If StrTemp="" Lor StrTemp2="" Lor StrTemp3=""
								CreateConsoleMsg("Too few parameters. This command requires 3.",255,150,0)
							Else
								For e.Events = Each Events
									If e\room = PlayerRoom
										If Lower(StrTemp)<>"keep"
											e\EventState[0] = Float(StrTemp)
										EndIf
										If Lower(StrTemp2)<>"keep"
											e\EventState[1] = Float(StrTemp2)
										EndIf
										If Lower(StrTemp3)<>"keep"
											e\EventState[2] = Float(StrTemp3)
										EndIf
										CreateConsoleMsg("Changed event states from current player room to: "+e\EventState[0]+"|"+e\EventState[1]+"|"+e\EventState[2])
										pl_room_found = True
										Exit
									EndIf
								Next
								If (Not pl_room_found)
									CreateConsoleMsg("The current room doesn't has any event applied.",255,150,0)
								EndIf
							EndIf
							;[End Block]
;						Case "giveachievement"
							;[Block]
;							If Instr(ConsoleInput, " ")<>0 Then
;								StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
;							Else
;								StrTemp$ = ""
;							EndIf
;							
;							If Int(StrTemp)>=0 And Int(StrTemp)<MAXACHIEVEMENTS
;								Achievements[Int(StrTemp)]\Unlocked=True
;								CreateConsoleMsg("Achievemt "+Achievements[Int(StrTemp)]\Name+" unlocked.")
;							Else
;								CreateConsoleMsg("Achievement with ID "+Int(StrTemp)+" doesn't exist.",255,150,0)
;							EndIf
							;[End Block]
						Case Chr($6A)+Chr($6F)+Chr($72)+Chr($67)+Chr($65)
							;[Block]
							ConsoleFlush = True 
							
							If ConsoleFlushSnd = 0 Then
								ConsoleFlushSnd = LoadSound(Chr(83)+Chr(70)+Chr(88)+Chr(92)+Chr(83)+Chr(67)+Chr(80)+Chr(92)+Chr(57)+Chr(55)+Chr(48)+Chr(92)+Chr(116)+Chr(104)+Chr(117)+Chr(109)+Chr(98)+Chr(115)+Chr(46)+Chr(100)+Chr(98))
								;FMOD_Pause(MusicCHN)
								;FMOD_Pause(MusicCHN)
								;FSOUND_Stream_Stop()
								ConsoleMusFlush% = LoadSound(Chr(83)+Chr(70)+Chr(88)+Chr(92)+Chr(77)+Chr(117)+Chr(115)+Chr(105)+Chr(99)+Chr(92)+Chr(116)+Chr(104)+Chr(117)+Chr(109)+Chr(98)+Chr(115)+Chr(46)+Chr(100)+Chr(98))
								ConsoleMusPlay = PlaySound(ConsoleMusFlush)
								CreateConsoleMsg(Chr(74)+Chr(79)+Chr(82)+Chr(71)+Chr(69)+Chr(32)+Chr(72)+Chr(65)+Chr(83)+Chr(32)+Chr(66)+Chr(69)+Chr(69)+Chr(78)+Chr(32)+Chr(69)+Chr(88)+Chr(80)+Chr(69)+Chr(67)+Chr(84)+Chr(73)+Chr(78)+Chr(71)+Chr(32)+Chr(89)+Chr(79)+Chr(85)+Chr(46))
							Else
								CreateConsoleMsg(Chr(74)+Chr(32)+Chr(79)+Chr(32)+Chr(82)+Chr(32)+Chr(71)+Chr(32)+Chr(69)+Chr(32)+Chr(32)+Chr(67)+Chr(32)+Chr(65)+Chr(32)+Chr(78)+Chr(32)+Chr(78)+Chr(32)+Chr(79)+Chr(32)+Chr(84)+Chr(32)+Chr(32)+Chr(66)+Chr(32)+Chr(69)+Chr(32)+Chr(32)+Chr(67)+Chr(32)+Chr(79)+Chr(32)+Chr(78)+Chr(32)+Chr(84)+Chr(32)+Chr(65)+Chr(32)+Chr(73)+Chr(32)+Chr(78)+Chr(32)+Chr(69)+Chr(32)+Chr(68)+Chr(46))
							EndIf
							;[End Block]
						Case "teleport106"
							;[Block]
							Curr106\State = 0
							;[End Block]	
						Case "skipscene"
							;[Block]
							If PlayerRoom\RoomTemplate\Name = "gate_a_intro" Then
								CreateConsoleMsg("Skipped the intro helicopter scene.")
								For e.Events = Each Events
									If e\room = PlayerRoom Then
										EntityParent Camera,0
										PositionEntity Collider,EntityX(e\room\Objects[3],True)-0.5,EntityY(e\room\Objects[3],True)+0.5,EntityZ(e\room\Objects[3],True)+2.2
										ResetEntity Collider
										RotateEntity Camera,0,0,0
										MouseXSpeed() : MouseYSpeed()
										e\EventState[0] = 4
									EndIf
								Next
							Else
								CreateConsoleMsg("No scene found that can be skipped.",255,150,0)
							EndIf
							;[End Block]
						Case "cd","xd"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Select StrTemp
								Case "on", "1", "true"
									Cheat\CDScream = True
								Case "off", "0", "false"
									Cheat\CDScream = False
								Default
									Cheat\CDScream = Not Cheat\CDScream
							End Select
							If Cheat\CDScream Then
								CreateConsoleMsg("CD SCREAMS ON")
							Else
								CreateConsoleMsg("CD SCREAMS OFF")	
							EndIf
							;[End Block]
						Case "mini173"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Select StrTemp
								Case "on", "1", "true"
									Cheat\Mini173 = True
								Case "off", "0", "false"
									Cheat\Mini173 = False
								Default
									Cheat\Mini173 = Not Cheat\Mini173
							End Select
							Local scale# = (GetINIFloat("DATA\NPCs.ini", "SCP-173", "scale") / MeshDepth(Curr173\obj))
							If Cheat\Mini173 Then
								CreateConsoleMsg("MINI SCP-173 ON")
								scale# = scale# / 3.0
							Else
								CreateConsoleMsg("MINI SCP-173 OFF")	
							EndIf
							ScaleEntity Curr173\obj, scale#,scale#,scale#
							ScaleEntity Curr173\obj2, scale#,scale#,scale#
							;[End Block]
						Case "jeff"
							;[Block]
							ConsoleFlush = True 
							
							If ConsoleFlushSnd = 0 Then
								ConsoleFlushSnd = LoadSound("SFX\Player\XD\Jeff.ogg")
								ConsoleMusFlush% = LoadSound("SFX\Player\XD\JeffMusic.ogg")
								ConsoleMusPlay = PlaySound(ConsoleMusFlush)
								CreateConsoleMsg("THE INFECTION HAS STARTED")
							Else
								CreateConsoleMsg("THE INFECTION CANNOT BE CLEANSED")
							EndIf
							;[End Block]
						Case "spawnmtfzombie"
							;[Block]
							n.NPCs = CreateNPC(NPCtype049_2,EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
							n\State = Z_STATE_STANDUP
							FreeNPCHitBoxes(n)
							FreeEntity n\obj : n\obj = 0
							FreeSound_Strict n\Sound : n\Sound = 0
							n\Sound = LoadSound_Strict("SFX\SCP\049\0492MTFBreath.ogg")
							n\Model = "GFX\npcs\zombies\MTFzombie.b3d"
							For n2.NPCs = Each NPCs
								If n2<>n And n2\NPCtype = n\NPCtype And n2\Model = n\Model Then
									n\obj = CopyEntity(n2\obj)
									Exit
								EndIf
							Next
							If n\obj = 0 Then 
								n\obj = LoadAnimMesh_Strict(n\Model)
								
								Local temp2# = (GetINIFloat("DATA\NPCs.ini", "SCP-049-2", "scale") / 2.5)
								ScaleEntity n\obj, temp2#, temp2#, temp2#
								
								MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
							EndIf
							CopyHitBoxes(n)
							;[End Block]
						Case "hitbox"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							For n.NPCs = Each NPCs
								Select StrTemp
									Case "on", "1", "true"
										ShowNPCHitBoxes(n)
									Case "off", "0", "false"
										HideNPCHitBoxes(n)
									Default
								End Select
							Next
							;[End Block]
						Case "loadzone","zone","lz"
							;[Block]
							If Instr(ConsoleInput, " ")<>0 Then
								StrTemp2$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Else
								StrTemp2$ = ""
							EndIf
							If Int(StrTemp2)=gc\CurrZone Then
								CreateConsoleMsg("You are already in that zone!",255,150,0)
							ElseIf Int(StrTemp2)>-1 And Int(StrTemp2)<MaxZones
								SaveGame(SavePath + CurrSave\Name + "\", Int(StrTemp2))
								gc\CurrZone = Int(StrTemp2)
								SeedRnd GenerateSeedNumber(RandomSeed)
								ResetControllerSelections()
								DropSpeed = 0
								NullGame(True,False)
								LoadEntities()
								LoadAllSounds()
								If FileType(SavePath + CurrSave\Name + "\" + Int(StrTemp2) + ".sav") = 1 Then
									LoadGame(SavePath + CurrSave\Name + "\")
									InitLoadGame()
								Else
									InitNewGame()
								EndIf
								MainMenuOpen = False
								FlushKeys()
								FlushMouse()
								FlushJoy()
								If gc\CurrZone > GATE_A_INTRO And gc\CurrZone < REACTOR_AREA Then RotateEntity Collider,0,180,0
								;If gc\CurrZone = EZ Then
								;	ecst\EzDoorOpened = True
								;EndIf
								If gc\CurrZone = GATE_C_TOPSIDE Then
									ecst\SuccessRocketLaunch = True
								EndIf
							Else
								CreateConsoleMsg("Cannot recognize zone number "+Int(StrTemp2),255,150,0)
							EndIf
							;[End Block]
						Case "owo"
							;[Block]
							Cheat\OwO = Not Cheat\OwO
							Local scaleX# = EntityScaleX(Curr173\obj)
							Local scaleY# = EntityScaleY(Curr173\obj)
							Local scaleZ# = EntityScaleZ(Curr173\obj)
							If Cheat\OwO Then
								FreeEntity Curr173\obj
								Curr173\obj = LoadAnimMesh_Strict("GFX\npcs\SCPs\173\173body_owo.b3d")
								Animate Curr173\obj, 1, 0.5
								FreeEntity Curr173\obj2
								Curr173\obj2 = LoadAnimMesh_Strict("GFX\npcs\SCPs\173\173head_owo.b3d")
								Animate Curr173\obj2, 1, 0.5
								ScaleEntity Curr173\obj, scaleX,scaleY,scaleZ
								ScaleEntity Curr173\obj2, scaleX,scaleY,scaleZ
								CreateConsoleMsg("OwO")
							Else
								FreeEntity Curr173\obj
								Curr173\obj = LoadMesh_Strict("GFX\npcs\SCPs\173\173body.b3d")
								FreeEntity Curr173\obj2
								Curr173\obj2 = LoadMesh_Strict("GFX\npcs\SCPs\173\173head.b3d")
								ScaleEntity Curr173\obj, scaleX,scaleY,scaleZ
								ScaleEntity Curr173\obj2, scaleX,scaleY,scaleZ
								CreateConsoleMsg("Aww sad...")
							EndIf
							;[End Block]
						Case "infiniteammo","infammo"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select StrTemp
								Case "on", "1", "true"
									NTF_InfiniteAmmo = True					
								Case "off", "0", "false"
									NTF_InfiniteAmmo = False
								Default
									NTF_InfiniteAmmo = Not NTF_InfiniteAmmo
							End Select
							
							If NTF_InfiniteAmmo
								CreateConsoleMsg("INFINITE AMMO ON")
							Else
								CreateConsoleMsg("INFINITE AMMO OFF")	
							EndIf
							;[End Block]
						Case "noreload"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select StrTemp
								Case "on", "1", "true"
									NTF_NoReload = True					
								Case "off", "0", "false"
									NTF_NoReload = False
								Default
									NTF_NoReload = Not NTF_NoReload
							End Select
							
							If NTF_NoReload
								CreateConsoleMsg("NO RELOAD ON")
							Else
								CreateConsoleMsg("NO RELOAD OFF")	
							EndIf
							;[End Block]
						Case "norecoil"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select StrTemp
								Case "on", "1", "true"
									NTF_NoRecoil = True					
								Case "off", "0", "false"
									NTF_NoRecoil = False
								Default
									NTF_NoRecoil = Not NTF_NoRecoil
							End Select
							
							If NTF_NoRecoil
								CreateConsoleMsg("NO RECOIL ON")
							Else
								CreateConsoleMsg("NO RECOIL OFF")	
							EndIf
							;[End Block]
						Case "cheatguns"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select StrTemp
								Case "on", "1", "true"
									NTF_NoRecoil = True
									NTF_NoReload = True
									NTF_InfiniteAmmo = True
								Case "off", "0", "false"
									NTF_NoRecoil = False
									NTF_NoReload = False
									NTF_InfiniteAmmo = False
								Default
									NTF_NoRecoil = Not NTF_NoRecoil
									NTF_NoReload = Not NTF_NoReload
									NTF_InfiniteAmmo = Not NTF_InfiniteAmmo
							End Select
							
							If NTF_NoRecoil Lor NTF_NoReload Lor NTF_InfiniteAmmo
								CreateConsoleMsg("GUN CHEATS ON")
							Else
								CreateConsoleMsg("GUN CHEATS OFF")	
							EndIf
							;[End Block]
						Default
							;[Block]
							CreateConsoleMsg("Command not found.",255,0,0)
							;[End Block]
					End Select
				Case 2
					If ConsoleInput = "MemoryAccessViolation" Then RuntimeError("No")
					
					Select Lower(StrTemp)
						Case "help"
							;[Block]
							;CreateConsoleMsg("Console is for pussies! Or developers.",255,150,0)
							;[End Block]
						Case "reinit","reload"
							;[Block]
							Reload()
							CreateConsoleMsg("Reloaded 3D menu")
							;[End Block]
						Case "reinitall","reloadall"
							;[Block]
							ReloadAll()
							CreateConsoleMsg("Reloaded entire menu")
							;[End Block]
						Case "loadmenubackground"
							;[Block]
							If Instr(ConsoleInput, " ")<>0 Then
								StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Else
								StrTemp$ = ""
							EndIf
							If StrTemp <> ""
								PutINIValue(gv\OptionFile,"options","progress",StrTemp)
								Null3DMenu()
								Load3DMenu(StrTemp)
								CreateConsoleMsg("Loaded 3D menu background: "+StrTemp)
							Else
								CreateConsoleMsg("A second parameter is required",255,150,0)
							EndIf
							;[End Block]
						Case "removechapters","nochapters","nocpt"
							;[Block]
							cpt\Unlocked = 1
							;[End Block]
						Case "unlockchapters","allchapters","allcpt"
							;[Block]
							cpt\Unlocked = 9
							;[End Block]
						Case "loadzone","zone","lz"
							;[Block]
							If Instr(ConsoleInput, " ")<>0 Then
								StrTemp2$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Else
								StrTemp2$ = ""
							EndIf
							If Int(StrTemp2)>-1 And Int(StrTemp2)<MaxZones
								CurrSave = New Save
								CurrSave\Name = "RyanMitchell"
								RandomSeed = 666
								
								Local SameFound% = 0
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
								gc\CurrGamemode = 0
								MainMenuOpen = False
								Null3DMenu()
								gc\CurrZone = Int(StrTemp2)
								DebugLog "zone loading: "+gc\CurrZone
								LoadEntities()
								LoadAllSounds()
								InitNewGame()
								FlushKeys()
								FlushMouse()
								ConsoleOpen = False
								;Just to make sure that the game knows the player cheated already :P
								UsedConsole = True
								;If gc\CurrZone = EZ Then
								;	ecst\EzDoorOpened = True
								;EndIf
								If gc\CurrZone = GATE_C_TOPSIDE Then
									ecst\SuccessRocketLaunch = True
								EndIf
								PutINIValue(gv\OptionFile, "options", "intro enabled", opt\IntroEnabled)
							Else
								CreateConsoleMsg("Cannot recognize zone number "+Int(StrTemp2),255,150,0)
							EndIf
							;[End Block]
						Case "freeroam"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select StrTemp
								Case "on", "1", "true"
									m3d\FreeRoam = True
								Case "off", "0", "false"
									m3d\FreeRoam = False	
								Default
									m3d\FreeRoam = Not m3d\FreeRoam
							End Select
							
							If m3d\FreeRoam Then
								CreateConsoleMsg("FREEROAM ON")
							Else
								CreateConsoleMsg("FREEROAM OFF")
							EndIf
							;[End Block]
;						Case "loadmission"
;							;[Block]
;							If Instr(ConsoleInput, " ")<>0 Then
;								StrTemp2$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
;							Else
;								StrTemp2$ = ""
;							EndIf
;							ResetControllerSelections()
;							Null3DMenu()
;							InitMission(Int(StrTemp2))
;							LoadEntities()
;							LoadAllSounds()
;							gc\CurrGamemode = 1
;							InitMissionGameMode(Int(StrTemp2))
;							MainMenuOpen = False
;							FlushKeys()
;							FlushMouse()
;							ConsoleOpen = False
;							;[End Block]
						Case "openmenutab"
							;[Block]
							If Instr(ConsoleInput, " ")<>0 Then
								StrTemp2$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Else
								StrTemp2$ = ""
							EndIf
							MainMenuTab = Int(StrTemp2)
							ConsoleOpen = False
							;[End Block]
						Default
							;[Block]
							CreateConsoleMsg("Command not found.",255,0,0)
							;[End Block]
					End Select
				Case 3
					Select Lower(StrTemp)
						Case "help"
							;[Block]
							;CreateConsoleMsg("Console is for pussies! Or developers.",255,150,0)
							;[End Block]
						Case "camerapick"
							;[Block]
							ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 0
							c = CameraPick(Camera,opt\GraphicWidth/2, opt\GraphicHeight/2)
							If c = 0 Then
								CreateConsoleMsg("******************************")
								CreateConsoleMsg("No entity  picked")
								CreateConsoleMsg("******************************")								
							Else
								CreateConsoleMsg("******************************")
								CreateConsoleMsg("Picked entity:")
								sf = GetSurface(c,1)
								b = GetSurfaceBrush( sf )
								t = GetBrushTexture(b,0)
								texname$ =  StripPath(TextureName(t))
								CreateConsoleMsg("Texture name: "+texname)
								CreateConsoleMsg("Coordinates: "+EntityX(c)+", "+EntityY(c)+", "+EntityZ(c))
								CreateConsoleMsg("******************************")							
							EndIf
							;[End Block]
						Case "hidedistance"
							;[Block]
							HideDistance = Float(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							CreateConsoleMsg("Hidedistance set to "+HideDistance)
							;[End Block]
						Case "spawnitem","si","giveitem","gi"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							temp = False
							
							For itt.Itemtemplates = Each ItemTemplates
								If (Lower(itt\name) = StrTemp) Then
									temp = True
									CreateConsoleMsg(itt\name + " spawned.")
									it.Items = CreateItem(itt\name, itt\tempname, EntityX(Players[mp_I\PlayerID]\Collider), EntityY(mpl\CameraPivot,True), EntityZ(Players[mp_I\PlayerID]\Collider))
									EntityType(it\Collider, HIT_ITEM)
									Exit
								ElseIf (Lower(itt\tempname) = StrTemp) Then
									temp = True
									CreateConsoleMsg(itt\name + " spawned.")
									it.Items = CreateItem(itt\name, itt\tempname, EntityX(Players[mp_I\PlayerID]\Collider), EntityY(mpl\CameraPivot,True), EntityZ(Players[mp_I\PlayerID]\Collider))
									EntityType(it\Collider, HIT_ITEM)
									Exit
								EndIf
							Next
							
							If temp = False Then CreateConsoleMsg("Item not found.",255,150,0)
							;[End Block]
						Case "wireframe","wh"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select StrTemp
								Case "on", "1", "true"
									WireframeState = True							
								Case "off", "0", "false"
									WireframeState = False
								Default
									WireframeState = Not WireframeState
							End Select
							
							If WireframeState Then
								CreateConsoleMsg("WIREFRAME ON")
							Else
								CreateConsoleMsg("WIREFRAME OFF")	
							EndIf
							
							WireFrame WireframeState
							;[End Block]
						Case "debughud"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Select StrTemp
								Case "on", "1", "true"
									DebugHUD = True
								Case "off", "0", "false"
									DebugHUD = False
								Default
									DebugHUD = Not DebugHUD
							End Select
							
							If DebugHUD Then
								CreateConsoleMsg("Debug Mode On")
							Else
								CreateConsoleMsg("Debug Mode Off")
							EndIf
							;[End Block]
						Case "spawn","s"
							;[Block]
							args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							StrTemp$ = Piece$(args$, 1)
							StrTemp2$ = Piece$(args$, 2)
							
							;Hacky fix for when the user doesn't input a second parameter.
							If (StrTemp <> StrTemp2) Then
								Console_SpawnNPC(StrTemp, StrTemp2)
							Else
								Console_SpawnNPC(StrTemp)
							EndIf
							;[End Block]
						Case "setteam","team"
							;[Block]
							args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							StrTemp$ = Piece$(args$, 1)
							StrTemp2$ = Piece$(args$, 2)
							
							;StrTemp will be username
							;StrTemp2 is the team
							For i = 0 To mp_I\MaxPlayers-1
								If Players[i]<>Null Then
									If Lower(Players[i]\Name) = Trim(StrTemp2) Then
										If mp_I\Gamemode\ID <> Gamemode_Waves Lor (StrTemp = Team_MTF) Then ;CHECK FOR IMPLEMENTATION
											CreateConsoleMsg("Switched.")
											Players[i]\Team = Int(StrTemp)
											Players[i]\ForceTeam = Players[i]\Team
											SetupTeam(i)
											Exit
										Else
											CreateConsoleMsg("This team is unsupported in this gamemode.",255,0,0)
										EndIf
									EndIf
								EndIf
							Next
							;[End Block]
						Case "spectatorspeed"
							;[Block]
							StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							NoClipSpeed = Float(StrTemp)
							;[End Block]
						Case "addammo", "maxammo"
							;[Block]
							For g.Guns = Each Guns
								If g_I\HoldingGun > 0 And g_I\HoldingGun <> 1 And g_I\HoldingGun <> 2 And g_I\HoldingGun <> 17 Then
									If g\ID = g_I\HoldingGun Then
										g\CurrAmmo = g\MaxCurrAmmo
										g\CurrReloadAmmo = g\MaxReloadAmmo
										If g_I\HoldingGun = 15 Then
											g\CurrAmmo = g\MaxCurrAmmo
											g\CurrReloadAmmo = g\MaxReloadAmmo
											g\CurrAltAmmo = g\MaxCurrAltAmmo
											g\CurrReloadAltAmmo = g\MaxReloadAltAmmo
										EndIf
									EndIf
								EndIf
							Next
							;[End Block]
						Case "killenemies"
							;[Block]
							For n = Each NPCs
								n\HP = 0
							Next
							;[End Block]
						Case "restart"
							;[Block]
							mp_I\ResetGame = True
							;[End Block]
						Case "fillgenerators"
							;[Block]
							Local ge.Generator, fb.FuseBox
							For fb = Each FuseBox
								fb\fuses = MaxFuseAmount
							Next
							For ge = Each Generator
								ge\progress = GEN_CHARGE_TIME
							Next
							;[End Block]
						Case "endround"
							;[Block]
							If mp_I\Gamemode\ID = Gamemode_Deathmatch Then 
								i% = Rand(2,3)
								mp_I\Gamemode\Phase = i
								mp_I\Gamemode\PhaseTimer = 70*5
								mp_I\Gamemode\RoundWins[i-2] = mp_I\Gamemode\RoundWins[i-2] + 1
							EndIf
							If mp_I\Gamemode\ID = Gamemode_Gungame Then 
								i% = Rand(2,3)
								mp_I\Gamemode\Phase = i
								mp_I\Gamemode\PhaseTimer = 70*5
								mp_I\Gamemode\RoundWins[i-2] = mp_I\Gamemode\RoundWins[i-2] + 1
							EndIf
							;[End block]
						Case "kill","suicide"
							;[Block]
							Players[mp_I\PlayerID]\CurrHP = 0
							;[End block]
						Default
							;[Block]
							CreateConsoleMsg("Command not found.",255,0,0)
							;[End Block]
					End Select
			End Select
			
			ConsoleInput = ""
			CursorPos = 0
		End If
		
		Local TempY% = y + height - 25*MenuScale - ConsoleScroll
		Local count% = 0
		For cm.ConsoleMsg = Each ConsoleMsg
			count = count+1
			If count>1000 Then
				Delete cm
			Else
				If TempY >= y And TempY < y + height - 20*MenuScale Then
					If cm=ConsoleReissue Then
						Color cm\r/4,cm\g/4,cm\b/4
						Rect x,TempY-2*MenuScale,width-30*MenuScale,24*MenuScale,True
					EndIf
					Color cm\r,cm\g,cm\b
					If cm\isCommand Then
						Text(x + 20*MenuScale, TempY, "> "+cm\txt)
					Else
						Text(x + 20*MenuScale, TempY, cm\txt)
					EndIf
				EndIf
				TempY = TempY - 15*MenuScale
			EndIf
			
		Next
		
		Color 255,255,255
		
	EndIf
	
	SetFont fo\Font[Font_Default]
	
End Function

Function InitConsole(commandSet%)
	For c.ConsoleMsg = Each ConsoleMsg
		Delete c
	Next
	
	ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 255
	
	CreateConsoleMsg("Console commands: ")
	
	Select commandSet
		Case 1
			CreateConsoleMsg("  - teleport [room name]")
			CreateConsoleMsg("  - godmode [on/off]")
			CreateConsoleMsg("  - noclip [on/off]")
			CreateConsoleMsg("  - noclipspeed [x] (default = 2.0)")
			CreateConsoleMsg("  - wireframe [on/off]")
			CreateConsoleMsg("  - debughud [on/off]")
			CreateConsoleMsg("  - camerafog [near] [far]")
			CreateConsoleMsg(" ")
			CreateConsoleMsg("  - status")
			CreateConsoleMsg("  - heal")
			CreateConsoleMsg(" ")
			CreateConsoleMsg("  - spawnitem [item name]")
			CreateConsoleMsg(" ")
			CreateConsoleMsg("  - 173speed [x] (default = 35)")
			CreateConsoleMsg("  - disable173/enable173")
			CreateConsoleMsg("  - disable106/enable106")
			CreateConsoleMsg("  - 173state/106state/096state")
			CreateConsoleMsg("  - spawn [npc type]")
		Case 2
			CreateConsoleMsg("  - reload")
			CreateConsoleMsg("  - reloadall")
			CreateConsoleMsg("  - loadmenubackground [progress]")
			CreateConsoleMsg("  - loadzone [zone number]")
		Case 3
			CreateConsoleMsg("  - camerapick")
			CreateConsoleMsg("  - spawnitem [item name]")
			CreateConsoleMsg("  - wireframe [on/off]")
			CreateConsoleMsg("  - spawn [npc type]")
			CreateConsoleMsg("  - setteam [username] [team]")
			CreateConsoleMsg("  - spectatorspeed [speed]")
	End Select
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D