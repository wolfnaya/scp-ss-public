
Function FillRoom_Room1_Sewers(r.Rooms)
	Local lt.LightTemplates, tw.TempWayPoints,wa.Water, it.Items, fb.FuseBox, ne.NewElevator
	Local newlt%, i%
	
	fb = CreateFuseBox("Fusebox.b3d", CreateVector3D(r\x - 408.0 * RoomScale, r\y - 5416.0 * RoomScale, r\z + 32.0 * RoomScale), CreateVector3D(0, 270, 0), CreateVector3D(0.4 * RoomScale, 0.4 * RoomScale, 0.4 * RoomScale))
	EntityParent fb\obj, r\obj
	fb\fuses = 2
	r\Objects[3] = fb\obj
	
	r\Objects[0] = LoadRMesh("GFX\map\elevators\elevator_cabin_2.rmesh", Null)
	ScaleEntity(r\Objects[0], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[0], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[0], 2), 2)
	PositionEntity(r\Objects[0], r\x, r\y - 5632.0 * RoomScale, r\z + 730.0 * RoomScale)
	EntityParent(r\Objects[0], r\obj)
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x, r\y - 5632.0 * RoomScale, r\z + 448.0 * RoomScale, 180, r, True, 5)
	r\RoomDoors[0]\AutoClose = False
	r\RoomDoors[0]\DisableWaypoint = True
	MoveEntity(r\RoomDoors[0]\buttons[0], -25, 0, 2.5)
	
	ne = CreateNewElevator(r\Objects[0], 1, r\RoomDoors[0], 1, r, -5632.0, 0.0, 2000.0)
	ne\floorlocked[2] = True
	
	Local rt.RoomTemplates = New RoomTemplates
	
	rt\objPath = "GFX\map\rooms\room1_sewers\room1_sewers_chunk2.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[1] = rt\obj
	ScaleEntity(r\Objects[1], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[1], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[1], 2), 2)
	PositionEntity(r\Objects[1],r\x,r\y,r\z)
	EntityParent r\Objects[1],r\obj
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[1])
			If newlt <> 0 Then
				If lt\ltype = 3 Then
					LightConeAngles(newlt, lt\innerconeangle, lt\outerconeangle)
					RotateEntity(newlt, lt\pitch, lt\yaw, 0)
				EndIf
			EndIf
		EndIf
	Next
	For tw.TempWayPoints = Each TempWayPoints
		If tw\roomtemplate = rt Then
			CreateWaypoint(r\x+tw\x, r\y+tw\y, r\z+tw\z, Null, r)
		EndIf
	Next
	For i = 0 To MaxRoomEmitters-1
		If r\RoomTemplate\TempSoundEmitter[i]<>0 Then
			r\SoundEmitterObj[i]=CreatePivot(r\Objects[1])
			PositionEntity r\SoundEmitterObj[i], r\x+rt\TempSoundEmitterX[i],r\y+rt\TempSoundEmitterY[i],r\z+rt\TempSoundEmitterZ[i],True
			EntityParent(r\SoundEmitterObj[i],r\Objects[1])
			
			r\SoundEmitter[i] = r\RoomTemplate\TempSoundEmitter[i]
			r\SoundEmitterRange[i] = r\RoomTemplate\TempSoundEmitterRange[i]
		EndIf
	Next
	Delete rt
	HideEntity(r\Objects[1])
	
	it = CreateItem("Crowbar", "crowbar", r\x - 128.0 * RoomScale, r\y - 5580.0 * RoomScale, r\z + 32.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Fuse", "fuse", r\x - 776.0 * RoomScale, r\y - 5580.0 * RoomScale, r\z - 3728.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	wa.Water = CreateWater("GFX\map\rooms\room1_sewers\room1_sewers_water.b3d", "room1_sewers_water", 0, 0, 0, r\obj, (-6208.0*RoomScale))
	EntityAlpha wa\obj,0.8
	EntityColor wa\obj,100,100,100
	r\Objects[2] = LoadTexture_Strict("GFX\map\textures\SLH_water2.jpg",1,1)
	EntityTexture wa\obj,r\Objects[2]
	ScaleTexture r\Objects[2],0.1,0.1
	TextureBlend r\Objects[2],2
	
	r\Objects[4] = CreatePivot()
	PositionEntity(r\Objects[4], r\x - 5016.0 * RoomScale, r\y - 4810.0 * RoomScale, r\z - 5648.0 * RoomScale)
	EntityParent(r\Objects[4], r\obj)
	
	r\Objects[5] = CreatePivot()
	PositionEntity(r\Objects[5], r\x - 5016.0 * RoomScale, r\y - 5454.0 * RoomScale, r\z - 5648.0 * RoomScale)
	EntityParent(r\Objects[5], r\obj)
	
	r\Objects[6] = CreatePivot()
	PositionEntity(r\Objects[6], r\x - 4824.0 * RoomScale, r\y - 5566.0 * RoomScale, r\z - 5648.0 * RoomScale)
	EntityParent(r\Objects[6], r\obj)
	
	r\Objects[7] = CreatePivot()
	PositionEntity(r\Objects[7], r\x - 4600.0 * RoomScale, r\y - 6180.0 * RoomScale, r\z - 5648.0 * RoomScale)
	EntityParent(r\Objects[7], r\obj)
	
	r\Objects[8] = LoadRMesh("GFX\map\rooms\room1_sewers\room1_sewers_door.rmesh", Null)
	ScaleEntity r\Objects[8],RoomScale,RoomScale,RoomScale
	EntityType(GetChild(r\Objects[8], 2), HIT_MAP)
	EntityPickMode r\Objects[8],2
	PositionEntity r\Objects[8],r\x,r\y,r\z,True
	EntityParent r\Objects[8],r\obj
	HideEntity r\Objects[8]
	
	r\Objects[9] = CreatePivot()
	PositionEntity r\Objects[9],r\x - 4464.0 * RoomScale, r\y - 6206.0 * RoomScale, r\z - 6032.0 * RoomScale,True
	EntityParent r\Objects[9],r\obj
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x - 2664.0 * RoomScale, r\y - 5632.0 * RoomScale, r\z - 5680.0 * RoomScale, 90, r, True)
	r\RoomDoors[2] = CreateDoor(r\zone,r\x - 5544.0 * RoomScale, r\y - 5632.0 * RoomScale, r\z - 6032.0 * RoomScale, 90, r, True)
	r\RoomDoors[3] = CreateDoor(r\zone,r\x - 5544.0 * RoomScale, r\y - 5632.0 * RoomScale, r\z - 4624.0 * RoomScale, 90, r)
	r\RoomDoors[4] = CreateDoor(r\zone,r\x - 2928.0 * RoomScale, r\y - 5632.0 * RoomScale, r\z - 4016.0 * RoomScale, 0, r)
	r\RoomDoors[4]\locked = True
	r\RoomDoors[5] = CreateDoor(r\zone,r\x - 416.0 * RoomScale, r\y - 5632.0 * RoomScale, r\z - 5680.0 * RoomScale, 90, r, True)
	r\RoomDoors[6] = CreateDoor(r\zone,r\x - 2048.0 * RoomScale, r\y - 5632.0 * RoomScale, r\z - 5424.0 * RoomScale, 0, r, False, False, KEY_CARD_2)
	r\RoomDoors[7] = CreateDoor(r\zone,r\x - 416.0 * RoomScale, r\y - 5632.0 * RoomScale, r\z - 4016.0 * RoomScale, 90, r, False, False, KEY_CARD_2)
	If Rand(0,1)=1 Then
		r\RoomDoors[6]\open = True
	Else
		r\RoomDoors[7]\open = True
	EndIf	
	
End Function

Function UpdateEvent_Room1_Sewers(e.Events)
	Local r.Rooms, ne.NewElevator, fb.FuseBox, de.Decals
	Local i%,it.Items
	Local elevY#, elevFloor%
	
	If (Not WasInPD) Then
		If EntityHidden(e\room\Objects[8]) Then ShowEntity e\room\Objects[8]
	EndIf
	
	If PlayerRoom = e\room
		
		If WasInPD Then
			
			HideEntity e\room\Objects[8]
		
			If EntityY(Collider)<-3500.0*RoomScale ;Sewers Area
				ShowEntity e\room\Objects[1]
				For r.Rooms = Each Rooms
					HideEntity r\obj
				Next
				ShowEntity PlayerRoom\obj
				EntityAlpha(GetChild(PlayerRoom\obj,2),0.0)
				EntityAlpha(GetChild(e\room\Objects[1],2),1.0)
				If PlayerInNewElevator Then
					For ne = Each NewElevator
						If ne\room = e\room And PlayerNewElevator = ne\ID And ne\door\openstate = 0 Then
							EntityAlpha(GetChild(e\room\Objects[1],2),0.0)
							Exit
						EndIf
					Next
				EndIf
				If (Not PlayerInNewElevator) Then
					ShouldPlay =MUS_SEWERS
				EndIf
				e\EventState[2] = e\EventState[2] + fps\Factor[0]
				PositionTexture e\room\Objects[2], 0, e\EventState[2] * 0.002 ;0.005
				ShouldUpdateWater = "room1_sewers_water"
				elevY = -5632.0
				elevFloor = 1
			Else ;Top Level
				HideEntity e\room\Objects[1]
				If PlayerInNewElevator Then
					For ne = Each NewElevator
						If ne\room = e\room And PlayerNewElevator = ne\ID And ne\door\openstate = 0 Then
							EntityAlpha(GetChild(PlayerRoom\obj,2),0.0)
							For i = 0 To 3
								If PlayerRoom\Adjacent[i] <> Null Then
									EntityAlpha(GetChild(PlayerRoom\Adjacent[i]\obj,2),0.0)
								EndIf
							Next
							Exit
						EndIf
					Next
				EndIf
				elevY = 0.0
				elevFloor = 2
			EndIf
			If (Not PlayerInNewElevator) Then
				PositionEntity e\room\RoomDoors[0]\frameobj,EntityX(e\room\RoomDoors[0]\frameobj),elevY*RoomScale,EntityZ(e\room\RoomDoors[0]\frameobj)
				PositionEntity e\room\RoomDoors[0]\obj,EntityX(e\room\RoomDoors[0]\obj),elevY*RoomScale,EntityZ(e\room\RoomDoors[0]\obj)
				PositionEntity e\room\RoomDoors[0]\obj2,EntityX(e\room\RoomDoors[0]\obj2),elevY*RoomScale,EntityZ(e\room\RoomDoors[0]\obj2)
				PositionEntity e\room\RoomDoors[0]\buttons[0],EntityX(e\room\RoomDoors[0]\buttons[0]),elevY*RoomScale+0.6,EntityZ(e\room\RoomDoors[0]\buttons[0])
				PositionEntity e\room\RoomDoors[0]\buttons[1],EntityX(e\room\RoomDoors[0]\buttons[1]),elevY*RoomScale+0.7,EntityZ(e\room\RoomDoors[0]\buttons[1])
				For ne = Each NewElevator
					If ne\door = e\room\RoomDoors[0] Then
						If ne\currfloor = elevFloor And ne\state = 0.0 Then
							e\room\RoomDoors[0]\open = True
						Else
							e\room\RoomDoors[0]\open = False
						EndIf
					EndIf
				Next
			EndIf
			
			If e\EventState[1] = 0.0 Then
				For fb = Each FuseBox
					If fb\obj = e\room\Objects[3] Then
						If fb\fuses = 3 Then
							e\room\RoomDoors[0]\locked = False
							e\room\RoomDoors[0]\open = False
							UseDoor(e\room\RoomDoors[0], False)
							e\EventState[1] = 1.0
						EndIf
						Exit
					EndIf
				Next
				e\room\RoomDoors[0]\open = False
				e\room\RoomDoors[0]\locked = True
			Else
				e\room\RoomDoors[0]\locked = False
			EndIf
			
			If e\EventState[0] < 7 Then
				If e\EventState[0] < 3 Then
					HideEntity Collider
				EndIf
				Crouch = True
				psp\NoMove = True
				psp\NoRotation = True
			EndIf
			
			Select e\EventState[0]
				Case 0
					BlurTimer = 0
					BlinkTimer = -10.0
					PositionEntity Collider, EntityX(e\room\Objects[4], True), EntityY(e\room\Objects[4], True), EntityZ(e\room\Objects[4], True)
					User_Camera_Pitch = 89
					CrouchState = 1.0
					RotateEntity Collider, 0, e\room\angle - 90, 0
					ResetEntity Collider
					DropSpeed = 0
					mpl\HasNTFGasmask = True
					de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[4], True), EntityY(e\room\Objects[4], True) - 0.01, EntityZ(e\room\Objects[4], True), -90, Rand(360), 0)
					de\Size = 0.1 : de\SizeChange = 0.01 : de\MaxSize = 1.75 : EntityAlpha(de\obj, 1.0)
					e\EventState[0] = 1
				Case 1
					If EntityY(Camera) < EntityY(e\room\Objects[5], True) Then
						PlaySound_Strict(LoadTempSound("SFX\Player\railing_head_hit.ogg"))
						PlaySound_Strict(NTF_PainWeakSFX[Rand(0,1)])
						mpl\HasNTFGasmask = False
						BlurTimer = 1200
						If wbi\Helmet Then
							
							For i = 0 To MaxItemAmount-1
								If Inventory[i] <> Null Then
									If Inventory[i]\itemtemplate\tempname = "helmet" Lor Inventory[i]\itemtemplate\tempname = "finehelmet" Then
										RemoveItem(Inventory[i])
										Exit
									EndIf
								EndIf
							Next
							
							it = CreateItem("Broken Helmet", "brokenhelmet", EntityX(e\room\Objects[9],True), EntityY(e\room\Objects[9],True), EntityZ(e\room\Objects[9],True))
							EntityParent(it\Collider, e\room\obj)
							EntityType it\Collider, HIT_MAP
							wbi\Helmet = False
							psp\Helmet = 0
						EndIf
						e\EventState[0] = 2
					EndIf
				Case 3
					If EntityY(Collider) < EntityY(e\room\Objects[7], True) Then
						PlaySound_Strict(LoadTempSound("SFX\Player\splash.ogg"))
						PlayNewDialogue(108,%01)
						e\EventState[0] = 4
					EndIf
				Case 7
					For i = 0 To MaxItemAmount-1
						If Inventory[i] <> Null Then
							If Inventory[i]\itemtemplate\tempname = "crowbar" Then
								;If Rand(1,10) = 10 Then
									;PlayNewDialogue(111,%01)
								;Else
									;PlayNewDialogue(110,%01)
								;EndIf	
								e\EventState[0] = 8
								Exit
							EndIf
						EndIf
					Next
			End Select
			
			If e\EventState[0] >= 2.0 And e\EventState[0] < 3.0 Then
				BlinkTimer = -10.0
				PositionEntity Collider, EntityX(e\room\Objects[6], True), EntityY(e\room\Objects[6], True), EntityZ(e\room\Objects[6], True)
				ResetEntity Collider
				e\EventState[0] = e\EventState[0] + (fps\Factor[0] / 70.0)
				User_Camera_Pitch = -89
				If e\EventState[0] >= 3.0 Then
					e\EventState[0] = 3
					ShowEntity Collider
				EndIf
				DropSpeed = 0
			ElseIf e\EventState[0] >= 4.0 And e\EventState[0] < 6.0 Then
				e\EventState[0] = e\EventState[0] + (fps\Factor[0] / 210.0)
				If e\EventState[0] >= 5.0 Then
					User_Camera_Pitch = 89.0 * (e\EventState[0] - 6.0)
					CrouchState = (6.0 - e\EventState[0])
					RotateEntity Camera, EntityPitch(Camera), EntityYaw(Camera), 20.0 - Abs(Min((5.5 - e\EventState[0]) * 40.0, 20.0))
					If e\EventState[0] >= 6.0 Then
						psp\NoMove = False
						psp\NoRotation = False
						ResetInput()
						Crouch = False
						CanSave = True
						e\EventState[0] = 7
						;PlayNewDialogue(109,%01)
					EndIf
				EndIf
			EndIf
		EndIf
	Else
		HideEntity e\room\Objects[1]
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D