
Function CreateCheckpointElevator(r.Rooms, ElevatorID%, ElevatorX#, ElevatorY#, ElevatorZ#, ElevatorYaw#, doorID%, DoorX#, DoorY#, DoorZ#, DoorYaw#)
	
	; ~ Elevator mesh
	r\Objects[ElevatorID] = LoadRMesh("GFX\map\Elevators\elevator_cabin_5.rmesh", Null)
	ScaleEntity(r\Objects[ElevatorID], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[ElevatorID], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[ElevatorID], 2), 2)
	PositionEntity(r\Objects[ElevatorID], r\x + ElevatorX * RoomScale, r\y + ElevatorY * RoomScale, r\z + ElevatorZ * RoomScale)
	RotateEntity(r\Objects[ElevatorID], 0, ElevatorYaw, 0)
	EntityParent(r\Objects[ElevatorID], r\obj)
	
	; ~ Elevator door
	r\RoomDoors[doorID] = CreateDoor(r\zone, r\x + DoorX * RoomScale, r\y + DoorY * RoomScale, r\z + DoorZ * RoomScale, DoorYaw, r, True, 5)
	r\RoomDoors[doorID]\open = True
	r\RoomDoors[doorID]\AutoClose = False
	r\RoomDoors[doorID]\DisableWaypoint = True
	MoveEntity(r\RoomDoors[doorID]\buttons[0], -25, 0, 2.5)
	
End Function

Function FillRoom_Checkpoints(r.Rooms)
	Local d.Doors,d2.Doors,it.Items
	Local i%,ne.NewElevator,fb.FuseBox
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x,r\y,r\z - 400.0*RoomScale,180,r,True,DOOR_WINDOWED,False,"1234")
	r\RoomDoors[2] = CreateDoor(r\zone,r\x,r\y,r\z + 400.0*RoomScale,0,r,False,DOOR_WINDOWED,False,"1234")
	For i = 0 To 1
		FreeEntity r\RoomDoors[1]\buttons[i] : r\RoomDoors[1]\buttons[i] = 0
		FreeEntity r\RoomDoors[2]\buttons[i] : r\RoomDoors[2]\buttons[i] = 0
	Next
	
	If gc\CurrZone = HCZ Then
		r\RoomDoors[1]\open = True
		r\RoomDoors[2]\open = False
	Else
		r\RoomDoors[1]\open = False
		r\RoomDoors[2]\open = True
	EndIf
	
	r\Objects[11] = CreateButton(r\x - 125.0 * RoomScale, r\y + 134 * RoomScale, r\z, 430, 90, 0)
	EntityParent(r\Objects[11],r\obj)
	
	r\Objects[12] = CreateButton(r\x - 276.0 * RoomScale, r\y + 111 * RoomScale, r\z+97*RoomScale, 440, 270, 0)
	EntityParent(r\Objects[12],r\obj)
	
	If gc\CurrZone = HCZ Then
		r\Objects[13] = CreateButton(r\x + 156.0 * RoomScale, r\y + 186 * RoomScale, r\z+410*RoomScale, 0, 180, 0)
	Else
		r\Objects[13] = CreateButton(r\x - 156.0 * RoomScale, r\y + 186 * RoomScale, r\z-410*RoomScale, 0, 0, 0)
	EndIf
	EntityParent(r\Objects[13],r\obj)
	
	Select gc\CurrZone
		Case RCZ,BCZ
			r\RoomDoors[7] = CreateDoor(r\zone,r\x-662*RoomScale,r\y,r\z-673.0*RoomScale,90,r,False,DOOR_OFFICE_2)
			r\RoomDoors[7]\locked = True
		Case HCZ
			r\RoomDoors[11] = CreateDoor(r\zone,r\x+753*RoomScale,r\y,r\z -673.0*RoomScale,90,r,False,DOOR_OFFICE_2)
			r\RoomDoors[11]\locked = True
	End Select
	
	Select gc\CurrZone
		Case BCZ
			r\RoomDoors[9] = CreateDoor(r\zone,r\x - 768.0*RoomScale,r\y,r\z - 158.0*RoomScale,90,r,False,DOOR_OFFICE,KEY_CARD_3)
		Case HCZ
			r\RoomDoors[12] = CreateDoor(r\zone,r\x - 768.0*RoomScale,r\y,r\z + 672.0*RoomScale,90,r,False,DOOR_OFFICE_2)
			r\RoomDoors[12]\locked = True
	End Select
	
	r\Levers[7] = CreateLever(r, r\x -200.0 * RoomScale, r\y +211.0 * RoomScale, r\z +318 * RoomScale,270,False)
	
	r\Objects[10] = CreatePivot()
	PositionEntity r\Objects[10],r\x,128*RoomScale,r\z
	EntityParent r\Objects[10],r\obj
	
	Select gc\CurrZone
		Case RCZ
			CreateCheckpointElevator(r, 0, 224, 0, 2312, 0, 0, 224, 0, 2030, 180)
			PositionEntity(r\RoomDoors[0]\buttons[1], r\x, r\y + 0.7, r\z + 1990 * RoomScale, True)
			CreateNewElevator(r\Objects[0], 1, r\RoomDoors[0], 1, r, 0.0, 4096.0, 8192.0)
			r\RoomDoors[3] = CreateDoor(r\zone,r\x-416*RoomScale,r\y,r\z +400.0*RoomScale,0,r,False,DOOR_OFFICE)
		Case HCZ
			CreateCheckpointElevator(r, 0, 224, 0, 2760, 0, 0, 224, 0, 2478, 180)
			CreateCheckpointElevatorCounterWeight(r, 1, 224, -7300, 2760, 0)
			PositionEntity(r\RoomDoors[0]\buttons[1], r\x, r\y + 0.7, r\z + 2438 * RoomScale, True)
			CreateNewElevator(r\Objects[0], 2, r\RoomDoors[0], 1, r, -4096.0, 0.0, 4096.0)
			CreateCheckpointFakeDoor(r, 14, 224, 0, 2478, 0)
			r\RoomDoors[5] = CreateDoor(r\zone,r\x+1265*RoomScale,r\y,r\z+1392.0*RoomScale,90,r,False,DOOR_OFFICE_2)
			r\RoomDoors[5]\locked = True
			r\RoomDoors[6] = CreateDoor(r\zone,r\x+1265*RoomScale,r\y,r\z+944.0*RoomScale,270,r,False,DOOR_OFFICE_2)
			r\RoomDoors[6]\locked = True
			
			r\RoomDoors[10] = CreateDoor(r\zone,r\x-480*RoomScale,r\y,r\z -400.0*RoomScale,0,r,False,DOOR_OFFICE)
			
		Case BCZ
			CreateCheckpointElevator(r, 0, 224, 0, 1926, 0, 0, 224, 0, 1644, 180)
			CreateCheckpointElevatorCounterWeight(r, 1, 224, 0, 1926, 0)
			PositionEntity(r\RoomDoors[0]\buttons[1], r\x, r\y + 0.7, r\z + 1604 * RoomScale, True)
			CreateNewElevator(r\Objects[0], 3, r\RoomDoors[0], 1, r, -8192.0, -4096.0, 0.0)
			CreateCheckpointFakeDoor(r, 14, 224, 0, 1644, 0)
			fb = CreateFuseBox("Fusebox.b3d", CreateVector3D(r\x - 1356.0 * RoomScale, r\y + 632.0 * RoomScale, r\z + 2233.0 * RoomScale), CreateVector3D(0, 180, 0), CreateVector3D(0.4 * RoomScale, 0.4 * RoomScale, 0.4 * RoomScale))
			EntityParent fb\obj, r\obj
			fb\fuses = 3
			
			r\RoomDoors[8] = CreateDoor(r\zone,r\x-400*RoomScale,r\y,r\z +640.0*RoomScale,90,r,False,DOOR_OFFICE)
			
			r\Objects[9] = LoadMesh_Strict("GFX\Map\rooms\Core_ez\core_ez_monitor.b3d")
			PositionEntity r\Objects[9],r\x,r\y,r\z,True
			ScaleEntity r\Objects[9],RoomScale,RoomScale,RoomScale
			EntityParent r\Objects[9],r\obj
			HideEntity r\Objects[9]
			
			EntityTexture r\Objects[9], Checkpoint_Screen[1]
			
			it = CreateItem("Hazmat Suit", "hazmatsuit", r\x+292.0*RoomScale,r\y+17.0*RoomScale,r\z)
			RotateEntity it\collider,0,270,0
			EntityParent(it\collider, r\obj)
			
			r\Objects[9] = LoadMesh_Strict("GFX\Map\rooms\Core_ez\core_ez_monitor.b3d")
			PositionEntity r\Objects[9],r\x,r\y,r\z,True
			ScaleEntity r\Objects[9],RoomScale,RoomScale,RoomScale
			EntityParent r\Objects[9],r\obj
			HideEntity r\Objects[9]
			
	End Select
	
	CreateDarkSprite(r, 2)
	
End Function

Function UpdateEvent_Checkpoints(e.Events)
	Local ne.NewElevator,r.Rooms,e2.Events
	Local playerElev%,prevZone%, pvt%, pvt2%,n.NPCs,de.Decals,i,it.Items
	
	If (Not clm\GlobalMode) Then
		
		; ~ Plot Events
		
		Select gc\CurrZone
			Case BCZ
				
				If ecst\WasInBCZ Then
					
					If e\EventState[5] = 0 Then
						it = CreateItem("SCP-500","scp500",EntityX(e\room\Objects[10],True),0.05,EntityZ(e\room\Objects[10],True))
						EntityParent it\collider, e\room\obj
						EntityType it\collider, HIT_MAP
						
						it = CreateItem("Unknown Note", "paper",EntityX(e\room\Objects[10],True),0.05,EntityZ(e\room\Objects[10],True))
						EntityParent it\collider, e\room\obj
						EntityType it\collider, HIT_MAP
						
						e\EventState[5] = 1
					EndIf
					
					I_008\Timer = 0
					I_409\Timer = 0
				EndIf
				
				If I_008\Timer > 0 Lor I_409\Timer > 0 Then
					CanSave = False
				Else
					CanSave = True
				EndIf
				If (Not PlayerInNewElevator) Then
					If PlayerRoom\RoomTemplate\Name <> "checkpoint_bcz" And PlayerRoom\RoomTemplate\Name <> "cont_409" Then
						If (Not wbi\Hazmat Lor hds\Wearing) Then
							I_008\Timer = 1
							CanSave = False
						Else
							CanSave = True
						EndIf
					EndIf
				EndIf
				
		End Select
		
		If PlayerRoom = e\room Then
			
			Select gc\CurrZone
				Case BCZ
					If PlayerRoom = e\room Then
						
						If (Not PlayerInNewElevator) Then
							If TaskExists(TASK_GO_TO_BCZ) Then
								EndTask(TASK_GO_TO_BCZ)
								If (Not hds\Wearing) Then
									If (Not TaskExists(TASK_SEARCH_FOR_HAZMAT)) Then
										BeginTask(TASK_SEARCH_FOR_HAZMAT)
									EndIf
								Else
									If (Not TaskExists(TASK_FIND_008)) Then
										BeginTask(TASK_FIND_008)
									EndIf
									If (Not TaskExists(TASK_FIND_409)) Then
										BeginTask(TASK_FIND_409)
									EndIf
								EndIf
							EndIf
							If TaskExists(TASK_SEARCH_FOR_HAZMAT) Then
								For i = 0 To MaxItemAmount-1
									If Inventory[i] <> Null Then
										If Inventory[i]\itemtemplate\tempname = "hazmatsuit" Then
											EndTask(TASK_SEARCH_FOR_HAZMAT)
											If (Not TaskExists(TASK_FIND_008)) Then
												BeginTask(TASK_FIND_008)
											EndIf
											If (Not TaskExists(TASK_FIND_409)) Then
												BeginTask(TASK_FIND_409)
											EndIf
										EndIf
									EndIf
								Next
							EndIf
						EndIf
						If ecst\WasInO5Again Then
							If e\EventState[8] = 0 Then
								If (Not PlayerInNewElevator) Then
									
									If HUDenabled And psp\IsShowingHUD Then
										CreateSplashText(GetLocalString("Singleplayer","chapter_8"),opt\GraphicWidth/2,opt\GraphicHeight/2,100,5,Font_Default_Large,True,False)
									EndIf
									AddShopPoints(200)
									cpt\Unlocked = 8
									If opt\MusicVol > 0 Then
										If (Not ChannelPlaying(e\SoundCHN)) Then e\SoundCHN = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Biological_Containment_Entrance.ogg"))
									EndIf
									e\EventState[8] = 1
								EndIf
							EndIf
						EndIf
						
						ShowEntity e\room\Objects[9]
						
						If ecst\WasInO5Again Then
							e\EventState[7] = 0
						ElseIf (Not ecst\WasInO5Again) Then
							e\EventState[7] = e\EventState[7] + fps\Factor[0]
						EndIf
						
						If e\EventState[7] > 70*5 Then
							EntityTexture e\room\Objects[9], Checkpoint_Screen[2], Floor(((e\EventState[7]-70*5)/70) Mod 4.0)
						EndIf
					EndIf
				Case RCZ
					If PlayerRoom = e\room Then
							;If cpt\Current < 6 Then
						If TaskExists(TASK_FIND_AREA_076) Then
							If ecst\WasInO5 Then
								If e\EventState[6] = 0 Then
									If (Not PlayerInNewElevator) Then
										
										If HUDenabled And psp\IsShowingHUD Then
											CreateSplashText(GetLocalString("Singleplayer","chapter_5"),opt\GraphicWidth/2,opt\GraphicHeight/2,100,5,Font_Default_Large,True,False)
										EndIf
										AddShopPoints(200)
										cpt\Unlocked = 5
										If opt\MusicVol > 0 Then
											If (Not ChannelPlaying(e\SoundCHN)) Then e\SoundCHN = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Reinforced_Containment_Entrance.ogg"))
										EndIf
										e\EventState[6] = 1
									EndIf
								EndIf
							EndIf
						EndIf
					EndIf
			End Select
			
		; ~ Actual checkpoint functionality
			
			If e\room\Objects[1] <> 0 Then
				PositionEntity e\room\Objects[1],EntityX(e\room\Objects[1]),-EntityY(e\room\Objects[0]) - (7300 * (gc\CurrZone = BCZ)),EntityZ(e\room\Objects[1])
			EndIf
			If e\EventState[1] = 0 And EntityY(Collider) > 2800.0*RoomScale Lor EntityY(Collider) <- 2800.0*RoomScale Then
				e\EventState[0] = e\EventState[0] + (0.01*fps\Factor[0])
				EntityAlpha e\room\Objects[2],Min(e\EventState[0],1.0)
				If e\EventState[0] > 1.05 Then
					SaveGame(SavePath + CurrSave\Name + "\")
					prevZone = gc\CurrZone
					For ne = Each NewElevator
						If PlayerNewElevator = ne\ID And ne\room = e\room Then
							Select ne\tofloor
								Case 3
									gc\CurrZone = BCZ
								Case 2
									gc\CurrZone = HCZ
								Case 1
									gc\CurrZone = RCZ
							End Select
							Exit
						EndIf
					Next
					If RandomSeed = "" Then
						RandomSeed = Abs(MilliSecs())
					EndIf
					SeedRnd GenerateSeedNumber(RandomSeed)
					ResetControllerSelections()
					DropSpeed = 0
					playerElev = PlayerNewElevator
					NullGame(True,False)
					LoadEntities()
					LoadAllSounds()
					Local zonecache% = gc\CurrZone
					If FileType(SavePath + CurrSave\Name + "\" + gc\CurrZone + ".sav") = 1 Then
						LoadGame(SavePath + CurrSave\Name + "\", gc\CurrZone)
						InitLoadGame()
					Else
						InitNewGame()
						LoadDataForZones(SavePath + CurrSave\Name + "\")
					EndIf
					gc\CurrZone = zonecache
					MainMenuOpen = False
					FlushKeys()
					FlushMouse()
					FlushJoy()
					ResetInput()
					For ne = Each NewElevator
						If playerElev = ne\ID And ne\room = PlayerRoom Then
							PositionEntity ne\obj, EntityX(ne\obj), 0.0, EntityZ(ne\obj)
							Local translation# = 2700.0
							Select prevZone
								Case BCZ
									TranslateEntity ne\obj, 0, translation, 0
								Case HCZ
									If gc\CurrZone = BCZ Then
										TranslateEntity ne\obj, 0, -translation, 0
									Else
										TranslateEntity ne\obj, 0, translation, 0
									EndIf
								Case RCZ
									TranslateEntity ne\obj, 0, -translation, 0
							End Select
							Select gc\CurrZone
								Case BCZ
									ne\tofloor = 3
									ne\currfloor = 2
								Case HCZ
									ne\tofloor = 2
									If prevZone = BCZ Then
										ne\currfloor = 3
									Else
										ne\currfloor = 1
									EndIf
								Case RCZ
									ne\tofloor = 1
									ne\currfloor = 2
							End Select
							RotateEntity Collider,0,180,0
							TeleportEntity(Collider,EntityX(ne\obj,True),EntityY(ne\obj,True)+0.5,EntityZ(ne\obj,True),0.3,True)
							StopStream_Strict(ne\soundchn)
							ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Checkpoint\Loop.ogg",opt\SFXVolume,Mode)
							ne\currsound = 2
							ne\state = 200
							ne\door\open = False
							ne\door\openstate = 0.0
							PlayerInNewElevator = True
							PlayerNewElevator = ne\ID
							Exit
						EndIf
					Next
					For e2 = Each Events
						If e2\room = PlayerRoom Then
							e2\EventState[0] = 1.05
							e2\EventState[1] = 1
							Exit
						EndIf
					Next
					SaveGame(SavePath + CurrSave\Name + "\")
					Return
				EndIf
			Else
				e\EventState[0] = Max(e\EventState[0] - (0.01*fps\Factor[0]), 0.0)
				EntityAlpha e\room\Objects[2],Min(e\EventState[0],1.0)
				If e\room\RoomDoors[0]\open Then
					e\EventState[1] = 0
				EndIf
			EndIf
		EndIf
	EndIf
	
	Local p.Particles
	
	If PlayerRoom = e\room
		
		Local zoneStr$,pn
		
		If e\room\RoomTemplate\Name = "checkpoint_bcz" Then
			zoneStr$ = "_BCZ"
		ElseIf e\room\RoomTemplate\Name = "checkpoint_hcz" Then
			zoneStr$ = "_HCZ"
		Else
			zoneStr$ = "_RCZ"
		EndIf
		
		If e\EventState[12] = 0.0
			
			If gc\CurrZone = BCZ Then
				If (Not ecst\WasInBCZ) Then
					If (Not hds\Wearing) Then
						If (Not TaskExists(TASK_SEARCH_FOR_HAZMAT)) And wbi\Hazmat Then
							For pn = 11 To 13
								UpdateButton(e\room\Objects[pn])
							Next
						EndIf
					Else
						For pn = 11 To 13
							UpdateButton(e\room\Objects[pn])
						Next
					EndIf
				Else
					For pn = 11 To 13
						UpdateButton(e\room\Objects[pn])
					Next
				EndIf 
			Else
				For pn = 11 To 13
					UpdateButton(e\room\Objects[pn])
				Next
			EndIf			
			
			If d_I\ClosestButton = e\room\Objects[11] Lor d_I\ClosestButton = e\room\Objects[12] Lor d_I\ClosestButton = e\room\Objects[13] Then
				If KeyHitUse Then
					PlaySound_Strict(ButtonSFX[0])
					e\EventState[12] = 1.0
					StopChannel e\SoundCHN2
					e\SoundCHN2 = 0
					e\room\RoomDoors[1]\locked = False
					e\room\RoomDoors[2]\locked = False
					
					If e\room\RoomDoors[1]\open Then
						e\EventState[9] = 0
					EndIf
					If e\room\RoomDoors[2]\open Then
						e\EventState[9] = 1
					EndIf
					
					If e\EventState[9] = 0.0 Then
						UseDoor(e\room\RoomDoors[1])
					Else
						UseDoor(e\room\RoomDoors[2])
					EndIf
					
					PlaySound_Strict(AlarmSFX[3])
				EndIf
			EndIf
		Else
			If e\EventState[11] < 70*7
				e\EventState[11] = e\EventState[11] + fps\Factor[0]
				e\room\RoomDoors[1]\open = False
				e\room\RoomDoors[2]\open = False
				If e\EventState[11] < 70*1
					
					
				ElseIf e\EventState[11] > 70*3 And e\EventState[9] < 70*5.5
					pvt% = CreatePivot(e\room\obj)								
					For i = 0 To 3
						
						If i = 0
							PositionEntity pvt%,-96.0,318.0,176.0,False
						ElseIf i = 1
							PositionEntity pvt%,96.0,318.0,176.0,False
						ElseIf i = 2
							PositionEntity pvt%,-96.0,318.0,-176.0,False
						Else
							PositionEntity pvt%,96.0,318.0,-176.0,False
						EndIf
						
						p.Particles = CreateParticle(EntityX(pvt,True), EntityY(pvt,True), EntityZ(pvt,True),  6, 0.6, 0, 50)
						p\speed = 0.025
						RotateEntity(p\pvt, 90, 0, 0)
						
						p\Achange = -0.02
					Next
					
					FreeEntity pvt
					If e\SoundCHN2 = 0 Then e\SoundCHN2 = PlaySound2(AirlockSFX[1],Camera,e\room\Objects[10],5)
				EndIf
			Else
				
				e\EventState[12] = 0.0
				e\EventState[11] = 0.0
				e\EventState[10] = 1.0
				If e\room\RoomDoors[1]\open = False
					e\room\RoomDoors[1]\locked = False
					e\room\RoomDoors[2]\locked = False
					
					If e\EventState[9] = 0.0 Then
						UseDoor(e\room\RoomDoors[2])
						If e\Sound = 0 Then LoadEventSound(e,"SFX\Alarm\Airlock\Decont_Checkpoint.ogg")
						If (Not ChannelPlaying(e\SoundCHN3)) Then e\SoundCHN3 = PlaySound2(e\Sound,Camera,e\room\RoomDoors[2]\obj)
						e\Sound = 0
					Else
						UseDoor(e\room\RoomDoors[1])
						If e\Sound = 0 Then LoadEventSound(e,"SFX\Alarm\Airlock\Decont"+zoneStr+".ogg")
						If (Not ChannelPlaying(e\SoundCHN3)) Then e\SoundCHN3 = PlaySound2(e\Sound,Camera,e\room\RoomDoors[1]\obj)
						e\Sound = 0
					EndIf
					
					e\EventState[10] = 0.0
					
					For pn = 11 To 13
						UpdateButton(e\room\Objects[pn])
					Next
					
				EndIf
			EndIf
		EndIf
		
		If ChannelPlaying(e\SoundCHN2)
			UpdateSoundOrigin(e\SoundCHN2,Camera,e\room\Objects[10],5)
		EndIf
	Else
		e\EventState[10] = 0.0
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D