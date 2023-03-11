
Function FillRoom_Facility_Reactor_Entrance(r.Rooms)
	Local ne.NewElevator
	
	CreateDarkSprite(r, 1)
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x -339.0 * RoomScale, r\y, r\z-288*RoomScale, 270, r, False, DOOR_OFFICE_2)
	EntityAlpha r\RoomDoors[1]\frameobj, 0
	r\RoomDoors[2] = CreateDoor(r\zone,r\x -339.0 * RoomScale, r\y, r\z+279*RoomScale, 90, r, False, DOOR_OFFICE_2)
	EntityAlpha r\RoomDoors[2]\frameobj, 0
	r\Objects[2] = CreateButton(r\x-1249*RoomScale,r\y+182*RoomScale,r\z+137*RoomScale,0,90,0)
	EntityParent(r\Objects[2],r\obj)
	
End Function

Function UpdateEvent_Facility_Reactor_Entrance(e.Events)
	Local ne.NewElevator,e2.Events,r.Rooms
	
	If (Not clm\GlobalMode) Then
		If PlayerRoom = e\room Then
			If (Not ecst\WasInReactor) Then
				
				UpdateButton(e\room\Objects[2])
				
				If TaskExists(TASK_FIND_REACTOR) And (Not TaskExists(TASK_TURN_ON_REACTOR)) Then
					EndTask(TASK_FIND_REACTOR)
					BeginTask(TASK_TURN_ON_REACTOR)
				EndIf
				
				If TaskExists(TASK_TURN_ON_REACTOR) Then
					If d_I\ClosestButton = e\room\Objects[2] Then
						If KeyHitUse Then
							e\EventState[1] = 1
							CreateMsg(GetLocalString("Doors", "elevator_called"))
							PlaySound_Strict(ButtonSFX[0])
						EndIf
					EndIf
				EndIf
				
				If e\EventState[1] = 1 Then
					e\EventState[0] = e\EventState[0] + (fps\Factor[0]*0.02)
					EntityAlpha e\room\Objects[1],Min(e\EventState[0],1.0)
				EndIf
				
				If e\EventState[0] > 2.1 Then
					SaveGame(SavePath + CurrSave\Name + "\")
					
					gc\CurrZone = REACTOR_AREA
					
					ResetControllerSelections()
					DropSpeed = 0
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
					For r.Rooms = Each Rooms
						If r\RoomTemplate\Name = "facility_reactor" Then
							PlayerRoom = r
							UpdateRooms()
							UpdateDoors()
							Exit
						EndIf
					Next
					MainMenuOpen = False
					FlushKeys()
					FlushMouse()
					FlushJoy()
					ResetInput()
					For e2 = Each Events
						If e2\room = PlayerRoom Then
							e2\EventState[5] = 0
							Exit
						EndIf
					Next
					SaveGame(SavePath + CurrSave\Name + "\")
					Return
				EndIf
			Else
				e\EventState[0] = 0
				e\EventState[1] = 0
			EndIf
		EndIf
	EndIf
	
End Function

Function FillRoom_Facility_Reactor(r.Rooms)
	Local wa.Water, it.Items
	Local ne.NewElevator,ne2.NewElevator
	
	Local StorageElevatorOBJ = LoadRMesh("GFX\map\Elevators\storage_elevator.rmesh",Null)
	HideEntity StorageElevatorOBJ
	r\Objects[13] = CopyEntity(StorageElevatorOBJ,r\obj)
	
	Local ElevatorOBJ = LoadRMesh("GFX\map\Elevators\elevator_cabin_3.rmesh",Null)
	HideEntity ElevatorOBJ
	r\Objects[7] = CopyEntity(ElevatorOBJ,r\obj)
	
	PositionEntity(r\Objects[7],-989,0,-9736)
	RotateEntity r\Objects[7],0,270,0
	EntityType r\Objects[7],HIT_MAP
	EntityPickMode r\Objects[7],2
	
	PositionEntity(r\Objects[13],+1283,0,-9736)
	RotateEntity r\Objects[13],0,270,0
	EntityType r\Objects[13],HIT_MAP
	EntityPickMode r\Objects[13],2
	
	it = CreateItem("Franchi SPAS-12", "spas12", r\x+464.0*RoomScale,r\y-1722.0*RoomScale,r\z-5777.0*RoomScale)
	RotateEntity it\collider,0,r\angle+90,r\angle+90
	EntityParent(it\collider, r\obj)
	
	CreateDarkSprite(r, 9)
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x-989.0 * RoomScale, r\y, r\z-9406*RoomScale, 0, r, True, DOOR_ELEVATOR_3FLOOR)
	r\RoomDoors[0]\DisableWaypoint = True
	
	r\RoomDoors[5] = CreateDoor(r\zone,r\x+1283.0 * RoomScale, r\y, r\z-9406*RoomScale, 0, r, True, DOOR_ELEVATOR_3FLOOR, False, "", 1)
	r\RoomDoors[5]\DisableWaypoint = True
	PositionEntity(r\RoomDoors[5]\buttons[0],r\x+910*RoomScale,EntityY(r\RoomDoors[5]\buttons[0],True),r\z-9747*RoomScale,True)
	RotateEntity r\RoomDoors[5]\buttons[0],0,90,0
	EntityAlpha r\RoomDoors[5]\frameobj, 0
	FreeEntity_Strict(r\RoomDoors[5]\obj)
	FreeEntity_Strict(r\RoomDoors[5]\obj2)
	
	ne = CreateNewElevator(r\Objects[13],3,r\RoomDoors[5],1,r,-2770.0,-2000.0,0.0,2,True)
	ne\floorlocked[0] = True
	
	wa.Water = CreateWater("GFX\map\rooms\facility_reactor\facility_reactor_water.b3d", "coolant_water", 0, 10639*RoomScale, 0, r\obj, (-3228.0*RoomScale))
	EntityAlpha wa\obj,0.6
	EntityColor wa\obj,100,100,100
	r\Objects[0] = LoadTexture_Strict("GFX\map\textures\SLH_water3.png",1,1)
	EntityTexture wa\obj,r\Objects[0]
	ScaleTexture r\Objects[0],0.1,0.1
	TextureBlend r\Objects[0],2
	EntityPickMode wa\obj,2
	EntityType wa\obj, HIT_MAP
	
	Local Part2 = LoadRMesh("GFX\map\rooms\facility_reactor\facility_reactor_2.rmesh",Null)
	HideEntity Part2
	r\Objects[4] = CopyEntity(Part2,r\obj)
	Local Part3 = LoadRMesh("GFX\map\rooms\facility_reactor\facility_reactor_3.rmesh",Null)
	HideEntity Part3
	r\Objects[5] = CopyEntity(Part3,r\obj)
	Local Part4 = LoadRMesh("GFX\map\rooms\facility_reactor\facility_reactor_4.rmesh",Null)
	HideEntity Part4
	r\Objects[6] = CopyEntity(Part4,r\obj)
	Local Part5 = LoadRMesh("GFX\map\rooms\facility_reactor\facility_reactor_bridges.rmesh",Null)
	HideEntity Part5
	r\Objects[10] = CopyEntity(Part5,r\obj)
	
	r\Objects[11] = CreatePivot()
	PositionEntity r\Objects[11],0,-1991*RoomScale, -5017*RoomScale
	EntityParent r\Objects[11],r\obj
	
	r\Objects[12] = LoadMesh_Strict("GFX\map\props\doors\doorhcz.b3d")
	PositionEntity r\Objects[12],-3568*RoomScale, -2001*RoomScale, -7009*RoomScale
	RotateEntity r\Objects[12],0,90,0
	ScaleEntity r\Objects[12],1.35*RoomScale,RoomScale,RoomScale
	EntityPickMode r\Objects[12],2
	EntityType r\Objects[12], HIT_MAP
	EntityParent r\Objects[12],r\obj
	HideEntity r\Objects[12]
	
	r\Objects[8] = LoadAnimMesh_Strict("GFX\Map\rooms\facility_reactor\Reactor_shield.b3d")
	ScaleEntity r\Objects[8],RoomScale,RoomScale,RoomScale
	PositionEntity r\Objects[8],r\x,r\y+10639*RoomScale,r\z
	EntityParent(r\Objects[8],r\obj)
	EntityPickMode(r\Objects[8],2)
	
	r\Objects[1] = LoadAnimMesh_Strict("GFX\Map\Props\Reactor.b3d")
	ScaleEntity r\Objects[1],47*RoomScale,47*RoomScale,47*RoomScale
	PositionEntity r\Objects[1],r\x,r\y-2466*RoomScale,r\z
	EntityParent(r\Objects[1],r\obj)
	EntityPickMode(r\Objects[1],2)
	
	r\Objects[2] = CreateButton(r\x-4378*RoomScale,r\y-1022*RoomScale,r\z-213*RoomScale,443,270,0)
	EntityParent(r\Objects[2],r\obj)
	
	r\Objects[3] = CreateButton(r\x-303*RoomScale,r\y-1810*RoomScale,r\z,450,0,270)
	EntityParent(r\Objects[3],r\obj)
	
	r\RoomDoors[2] = CreateDoor(r\zone, r\x, r\y - 2001.0 * RoomScale, r\z -4743.0 * RoomScale, 0, r, False, DOOR_DEFAULT)
	PositionEntity r\RoomDoors[2]\buttons[0],r\x+135*RoomScale, r\y - 1829.0 * RoomScale, r\z -4838.0 * RoomScale,True
	RotateEntity r\RoomDoors[2]\buttons[0],0,270,0
	
	r\RoomDoors[4] = CreateDoor(r\zone, r\x, r\y - 2001.0 * RoomScale, r\z + 4743.0 * RoomScale, 0.0, r, True, DOOR_DEFAULT)
	
End Function

Function UpdateEvent_Facility_Reactor(e.Events)
	Local p.Particles,i
	Local n.NPCs,e2.Events
	Local xtemp#,ztemp#,temp%,angle#,dist#,pvt%,r.Rooms
	
	If (Not clm\GlobalMode) Then
		
		If gc\CurrZone = REACTOR_AREA Then
			
			If gc\CurrZone = REACTOR_AREA Then
				If PlayerRoom = e\room
					Curr173\Idle = SCP173_DISABLED
					HideEntity Curr173\obj
					HideEntity Curr173\obj2
					HideEntity Curr173\Collider
				Else
					Curr173\Idle = SCP173_ACTIVE
					If EntityHidden(Curr173\obj) Then ShowEntity Curr173\obj
					If EntityHidden(Curr173\obj2) Then ShowEntity Curr173\obj2
					If EntityHidden(Curr173\Collider) Then ShowEntity Curr173\Collider
				EndIf
			EndIf
			
			If PlayerRoom = e\room
				
				Local ne.NewElevator
				
				If EntityY(Collider)<-12000.0*RoomScale ; ~ Reactor
					
					If (Not PlayerInNewElevator)
						PositionEntity e\room\RoomDoors[5]\frameobj,EntityX(e\room\RoomDoors[5]\frameobj),-2000.0*RoomScale,EntityZ(e\room\RoomDoors[5]\frameobj)
						PositionEntity e\room\RoomDoors[5]\buttons[0],EntityX(e\room\RoomDoors[5]\buttons[0]),-2000.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[5]\buttons[0])
						PositionEntity e\room\RoomDoors[5]\buttons[1],EntityX(e\room\RoomDoors[5]\buttons[1]),-2000.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[5]\buttons[1])
						For ne = Each NewElevator
							If ne\door = e\room\RoomDoors[5]
								If ne\currfloor = 1 And ne\state = 0.0
									e\room\RoomDoors[5]\open = True
								Else
									e\room\RoomDoors[5]\open = False
								EndIf
							EndIf
						Next
					EndIf
					
				ElseIf EntityY(Collider)<-10000.0*RoomScale ; ~ Entrance
					
					If (Not PlayerInNewElevator)
						PositionEntity e\room\RoomDoors[5]\frameobj,EntityX(e\room\RoomDoors[5]\frameobj),0,EntityZ(e\room\RoomDoors[5]\frameobj)
						PositionEntity e\room\RoomDoors[5]\buttons[0],EntityX(e\room\RoomDoors[5]\buttons[0]),0.6,EntityZ(e\room\RoomDoors[5]\buttons[0])
						PositionEntity e\room\RoomDoors[5]\buttons[1],EntityX(e\room\RoomDoors[5]\buttons[1]),0.7,EntityZ(e\room\RoomDoors[5]\buttons[1])
						For ne = Each NewElevator
							If ne\door = e\room\RoomDoors[5]
								If ne\currfloor = 2 And ne\state = 0.0
									e\room\RoomDoors[5]\open = True
								Else
									e\room\RoomDoors[5]\open = False
								EndIf
							EndIf
						Next
					EndIf
					
				EndIf
				
				ShouldPlay = MUS_NULL
				
				If EntityY(Collider)<=-3228*RoomScale And KillTimer=>0 Then
					m_msg\DeathTxt=""
					PlaySound_Strict LoadTempSound("SFX\General\Water_Splash.ogg")
					EntityAlpha e\room\Objects[9],1
					KillTimer=-1.0
				Else
					EntityAlpha e\room\Objects[9],0
				EndIf
				PlayerFallingPickDistance = 10.0
				
				If e\EventState[4] = 0 Then
					If (Not TaskExists(TASK_TURN_ON_REACTOR)) Then
						BeginTask(TASK_TURN_ON_REACTOR)
					EndIf
					If ChannelPlaying(e\SoundCHN)=False Then
						If opt\MusicVol > 0 Then
							e\SoundCHN = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Reactor_Core.ogg"))
						EndIf
					EndIf
					
					If HUDenabled And psp\IsShowingHUD Then
						CreateSplashText(GetLocalString("Singleplayer","chapter_3"),opt\GraphicWidth/2,opt\GraphicHeight/2,100,5,Font_Default_Large,True,False)
					EndIf
				
					AddShopPoints(200)
					
					cpt\Unlocked = 3
					e\EventState[4] = 1
				EndIf
				
				For n.NPCs = Each NPCs
					If n <> Curr106 And n <> Curr173
						RemoveNPC(n)
					EndIf
				Next
				Curr173\Idle = True
				Curr106\Idle = True
				Contained106 = True
				
				CameraFogMode(Camera, 0)
				SecondaryLightOn = True
				
				HideDistance = 35.0
				ShowEntity Overlay[0]
				
				CameraFogRange Camera, 5,50
				
				angle = Max(Sin(EntityYaw(Collider)+90),0.0)
				CameraFogColor (Camera,190+(angle*40),190+(angle*20),190)
				CameraClsColor (Camera,190+(angle*40),190+(angle*20),190)
				CameraRange(Camera, 0.01, 65)
				
				AmbientLight (190, 190, 190)
				
				ShowEntity e\room\Objects[4]
				ShowEntity e\room\Objects[5]
				
				ShowEntity e\room\Objects[10]
				
				If e\room\RoomDoors[2]\open Then
					ShowEntity e\room\Objects[6]
					ShowEntity e\room\Objects[10]
					ShowEntity e\room\Objects[1]
				Else
					HideEntity e\room\Objects[6]
					HideEntity e\room\Objects[10]
					HideEntity e\room\Objects[1]
				EndIf
				
				ShowEntity e\room\Objects[8]
				
				ShowEntity e\room\Objects[1]
				
				;e\EventState[3] = e\EventState[3] + fps\Factor[0]
				
				;PositionTexture e\room\Objects[0], 0, e\EventState[3] * 0.005
				;ShouldUpdateWater = "coolant_water"
				
				If e\EventState[0] = 0 Then
					Animate2(e\room\Objects[1], AnimTime(e\room\Objects[1]), 1.0, 500.0, 0.3,True)
				Else
					Animate2(e\room\Objects[1], AnimTime(e\room\Objects[1]), 501.0, 1390.0, 0.3,False)
				EndIf
				
				If EntityDistanceSquared(Collider, e\room\Objects[1]) < PowTwo(15.0) Then
					If e\EventState[2] = 0 Then
						If e\EventState[0] = 0 Then
							If ChannelPlaying(e\SoundCHN2)=False Then
								e\SoundCHN2 = PlaySound_Strict(LoadTempSound("SFX\General\Reactor_Idle.ogg"))
							EndIf
						EndIf
					EndIf
				EndIf
				If e\EventState[0] > 0 And e\EventState[0] < 3 Then
					If ChannelPlaying(e\SoundCHN2)=False Then
						e\SoundCHN2 = PlaySound_Strict(LoadTempSound("SFX\General\Reactor_Power_Up.ogg"))
					EndIf
				ElseIf e\EventState[0] = 3 Then
					If ChannelPlaying(e\SoundCHN2) Then
						StopChannel(e\SoundCHN2)
					EndIf
				EndIf
				
				e\Sound = LoadSound_Strict("SFX\General\Spark_Short.ogg")
				
				If TaskExists(TASK_TURN_ON_REACTOR) Then
					
					If e\EventState[0] = 0 Then
						UpdateButton(e\room\Objects[2])
						If d_I\ClosestButton = e\room\Objects[2] And KeyHitUse Then
							SaveGame(SavePath + CurrSave\Name + "\")
							PlaySound_Strict ButtonSFX[3]
							e\EventState[0] = 1
							e\EventState[1] = e\EventState[1] + fps\Factor[0]
							e\room\RoomDoors[2]\open = True
							ShouldPlay = MUS_NULL
							If e\EventState[2] = 0 Then
								If ChannelPlaying(e\SoundCHN)=False Then
									If opt\MusicVol > 0 Then
										e\SoundCHN = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Reactor_Explosion.ogg"))
									EndIf
								EndIf
							EndIf
							If e\EventState[2] <> 0 Then
								If ChannelPlaying(e\SoundCHN)=False Then
									e\SoundCHN = PlaySound_Strict(LoadTempSound("SFX\General\Reactor_Stop.ogg"))
								EndIf
							EndIf
							If TaskExists(TASK_TURN_ON_REACTOR) And (Not TaskExists(TASK_STOP_REACTOR)) Then
								FailTask(TASK_TURN_ON_REACTOR)
								BeginTask(TASK_STOP_REACTOR)
							EndIf
						EndIf
					EndIf
					
				EndIf
				
				If e\EventState[0] <> 1 Then
					SetAnimTime(e\room\Objects[8],1)
				EndIf
				
				If e\EventState[0] = 1 Then
					Animate2(e\room\Objects[8], AnimTime(e\room\Objects[8]), 1.0, 20.0, 0.13,False)
				Else
					SetAnimTime(e\room\Objects[8],1)
				EndIf
				
				If e\EventState[0] = 1 Then
					If EntityHidden(e\room\Objects[6]) Then ShowEntity e\room\Objects[6]
					InfiniteStamina = True
				Else
					InfiniteStamina = False
				EndIf
				
				If e\room\RoomDoors[4]\open = False And e\room\RoomDoors[4]\locked = True Then
					HideEntity e\room\Objects[4]
					HideEntity e\room\Objects[5]
					ShowEntity e\room\Objects[12]
				EndIf
				
				If TaskExists(TASK_STOP_REACTOR) Then
					If e\EventState[0] = 1 Then
						UpdateButton(e\room\Objects[3])
						If d_I\ClosestButton = e\room\Objects[3] And KeyHitUse Then
							SaveGame(SavePath + CurrSave\Name + "\")
							PlaySound_Strict ButtonSFX[3]
							e\EventState[0] = 2
							SetAnimTime(e\room\Objects[8],1)
						EndIf
					EndIf
					
					If e\EventState[0] <> 0 Then
						e\EventState[1] = e\EventState[1] + fps\Factor[0]
						ShouldPlay = MUS_NULL
					EndIf
					
					If e\EventState[1] > 0 And e\EventState[1] < 70*50 Then
						If Rand(50) = 1 Then
							PlaySound2(e\Sound, Camera, e\room\Objects[2], 3.0, 0.4)
							If ParticleAmount > 0 Then
								For i = 0 To (2 + (1 * (ParticleAmount - 1)))
									p.Particles = CreateParticle(EntityX(e\room\Objects[2],True), EntityY(e\room\Objects[2],True)+Rnd(0.0,0.05), EntityZ(e\room\Objects[2],True),7, 0.002, 0.0, 25.0)
									p\speed = Rnd(0.005, 0.03) : p\size = Rnd(0.005, 0.0075) : p\Achange = -0.05
									RotateEntity(p\pvt, Rnd(-20.0, 0.0), e\room\angle, 0.0)
									ScaleSprite(p\obj, p\size, p\size)
								Next
							EndIf	
						EndIf
					EndIf
					
					If e\EventState[0] <> 2 Then
						If e\EventState[1] >= 70*46.7 And e\EventState[1] < 70*46.8 Then
							If TaskExists(TASK_STOP_REACTOR) Then
								FailTask(TASK_STOP_REACTOR)
							EndIf
							ExplosionTimer = 1
							m_msg\DeathTxt=GetLocalString("Singleplayer","reactor_explosion")
						EndIf
					Else
						e\EventState[2] = e\EventState[2] + fps\Factor[0]
						If e\EventState[2] > 0 And e\EventState[2] < 70*5 Then
							SetAnimTime(e\room\Objects[8],1)
							e\room\RoomDoors[4]\open = False : e\room\RoomDoors[4]\locked = True
							e\EventState[0] = 3
							If e\EventState[0] = 3 Then
								If ChannelPlaying(e\SoundCHN2) Then
									StopChannel(e\SoundCHN2)									
								EndIf
							EndIf
							If ChannelPlaying(e\SoundCHN) Then
								StopChannel(e\SoundCHN)
								If opt\MusicVol > 0 Then
									e\SoundCHN2 = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Reactor_Explosion_End.ogg"))
									e\SoundCHN2_isStream = True
								EndIf
								e\EventState[0] = 4
							EndIf
							BigCameraShake = 10
							If TaskExists(TASK_STOP_REACTOR) And (Not TaskExists(TASK_COME_BACK_TO_GUARD_2)) Then
								EndTask(TASK_STOP_REACTOR)
								If (Not ecst\KilledGuard) Then
									BeginTask(TASK_COME_BACK_TO_GUARD_2)
								EndIf
								If ChannelPlaying(e\SoundCHN)=False Then
									If opt\MusicVol > 0 Then
										e\SoundCHN = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Reactor_Core_2.ogg"))
										EndIf
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
				
				If e\EventState[0] = 4 Then
					If EntityDistanceSquared(Collider,e\room\Objects[11])<PowTwo(0.6) Then
						e\room\RoomDoors[2]\open = False
						e\room\RoomDoors[2]\locked = True
					EndIf
					If EntityDistanceSquared(Collider, e\room\Objects[7])<PowTwo(1.5) Then
						DrawHandIcon = True
						If KeyHitUse Then
							e\EventState[5] = 0.1
						EndIf
					EndIf
				EndIf
					
				If e\EventState[5] > 0 Then
					e\EventState[5] = e\EventState[5] + (0.01*fps\Factor[0])
					EntityAlpha e\room\Objects[9],Min(e\EventState[5],1.0)
				EndIf
				
				If e\EventState[5] > 1.05 Then
					
					ecst\WasInReactor = True
					
					SaveGame(SavePath + CurrSave\Name + "\")
					
					FreeEntity_Strict(e\room\Objects[1])
					FreeEntity_Strict(e\room\Objects[4])
					FreeEntity_Strict(e\room\Objects[5])
					FreeEntity_Strict(e\room\Objects[6])
					FreeEntity_Strict(e\room\Objects[8])
					FreeEntity_Strict(e\room\Objects[10])
					FreeEntity_Strict(e\room\Objects[12])
					
					gc\CurrZone = HCZ
					
					ResetControllerSelections()
					DropSpeed = 0
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
					For r.Rooms = Each Rooms
						If r\RoomTemplate\Name = "facility_reactor_entrance" Then
							PlayerRoom = r
							UpdateRooms()
							UpdateDoors()
							Exit
						EndIf
					Next
					MainMenuOpen = False
					FlushKeys()
					FlushMouse()
					FlushJoy()
					ResetInput()
					
					For e2 = Each Events
						If e2\room = PlayerRoom Then
							e2\EventState[0] = 0
							TeleportEntity(Collider, EntityX(e2\room\obj), 1.0, EntityZ(e2\room\obj), 0.3, True)
							RotateEntity(Collider, 0, e2\room\angle, 0)
							Exit
						EndIf
					Next
					SaveGame(SavePath + CurrSave\Name + "\")
					Return
				EndIf
				
			Else
				FreeEntity_Strict(e\room\Objects[1])
				FreeEntity_Strict(e\room\Objects[4])
				FreeEntity_Strict(e\room\Objects[5])
				FreeEntity_Strict(e\room\Objects[6])
				FreeEntity_Strict(e\room\Objects[8])
				FreeEntity_Strict(e\room\Objects[10])
				FreeEntity_Strict(e\room\Objects[12])
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D