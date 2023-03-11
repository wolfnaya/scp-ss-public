
Function FillRoom_Room2_Offices_5(r.Rooms)
	Local fll.FluLight,it.Items
	
	InitFluLight(0,FLU_STATE_OFF,r)
	InitFluLight(1,FLU_STATE_ON,r)
	InitFluLight(2,FLU_STATE_FLICKER,r)
	
	r\Objects[0] = LoadMesh_Strict("GFX\map\rooms\room2_offices_5\room2_offices_5_projector_wall.b3d",r\obj)
	EntityPickMode(r\Objects[0],2)
	
	r\Objects[1] = LoadRMesh("GFX\map\rooms\room2_offices_5\room2_offices_5.rmesh",Null)
	ScaleEntity (r\Objects[1],RoomScale,RoomScale,RoomScale)
	EntityType GetChild(r\Objects[1],2), HIT_MAP
	EntityPickMode GetChild(r\Objects[1],2), 2
	PositionEntity(r\Objects[1],r\x,r\y,r\z,True)
	EntityParent(r\Objects[1], r\obj)
	
	If I_005\ChanceToSpawn = 2 Then
		it.Items = CreateItem("SCP-005", "scp005", r\x +684.0 * RoomScale, r\y +183.0 * RoomScale, r\z + 507.0 * RoomScale)
		EntityParent(it\Collider, r\obj)
	EndIf
	
	it = CreateItem("Mysterious Note", "paper", r\x + 856.0 * RoomScale, r\y + 130.0 * RoomScale, r\z +433.0 * RoomScale)
	EntityParent(it\Collider, r\obj)	
	it = CreateItem("Ballistic Vest", "vest", r\x + 306 * RoomScale, r\y + 130.0 * RoomScale, r\z +739.0 * RoomScale)
	EntityParent(it\Collider, r\obj) : RotateEntity(it\Collider, 0, 90, 0)
	
	it = CreateItem("Incident Report SCP-106-0204", "paper", r\x + 695.0 * RoomScale, r\y + 130.0 * RoomScale, r\z - 584.0 * RoomScale)
	EntityParent(it\Collider, r\obj)
	it = CreateItem("Journal Page", "paper", r\x + 300.0 * RoomScale, r\y + 130.0 * RoomScale, r\z -665.0 * RoomScale)
	EntityParent(it\Collider, r\obj)
	it = CreateItem("First Aid Kit", "firstaid", r\x + 630.0 * RoomScale, r\y + 140.0 * RoomScale, r\z + 48.0 * RoomScale)
	EntityParent(it\Collider, r\obj) : RotateEntity(it\Collider, 0, 90, 0)
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 240.0 * RoomScale, 0.0, r\z-128*RoomScale, 90, r, False,DOOR_OFFICE,False,"7816")
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x + 240.0 * RoomScale, 0.0, r\z+128*RoomScale, 90, r, False,DOOR_OFFICE,False,AccessCode[0])
	PositionEntity r\RoomDoors[1]\buttons[1], r\x+224*RoomScale,r\y+180*RoomScale,r\z,True
	
	r\RoomDoors[2] = CreateDoor(r\zone, r\x - 544.0 * RoomScale, 0.0, r\z+272*RoomScale, 0, r, False,DOOR_OFFICE)
	
	r\RoomDoors[3] = CreateDoor(r\zone, r\x - 768.0 * RoomScale, 0.0, r\z-272*RoomScale, 0, r, False,DOOR_OFFICE)
	r\RoomDoors[3]\locked = True
	
	r\RoomDoors[4] = CreateDoor(r\zone, r\x - 1024.0 * RoomScale, 0.0, r\z-273*RoomScale, 0, r, False,DOOR_OFFICE)
	r\RoomDoors[4]\locked = True
	
	r\RoomDoors[5] = CreateDoor(r\zone, r\x - 1546.0 * RoomScale, 0.0, r\z+3*RoomScale, -90, r, False,DOOR_OFFICE_2)
	r\RoomDoors[5]\locked = True
	
	r\Objects[0] = CreatePivot()
	PositionEntity r\Objects[0],r\x+684.0*RoomScale,r\y+120.0*RoomScale,r\z-569.0*RoomScale
	EntityParent r\Objects[0],r\obj
	
    it = CreateItem("Document SCP-330", "paper", r\x + 712.0 * RoomScale, r\y + 135.0 * RoomScale, r\z - 662.0 * RoomScale)
    EntityParent(it\Collider, r\obj)
	
End Function

Function UpdateEvent_Room2_Offices_5(e.Events)
	Local de.Decals,LeftHand,RightHand,i,it.Items,pvt
	
	If PlayerRoom = e\room
		LeftHand% = CreatePivot(e\room\obj)
		
		PositionEntity(LeftHand, 728.0, 11.0, -403)
		RotateEntity(LeftHand, -79.4, 0.0, 0.0)
		
		RightHand% = CreatePivot(e\room\obj)
		
		PositionEntity(RightHand, 549.0, 11.0, -591)
		RotateEntity(RightHand, -86.4, 0.0, 0.0)
		
		If InteractWithObject(e\room\Objects[0],0.65) And I_330\Taken < 3 Then
			If e\EventState[0] = 0.0 Then
				CreateMsg(GetLocalString("Items","scp330_1"))
				e\EventState[0] = 1.0 
			Else
				If ItemAmount < MaxItemAmount Then
					For i = 0 To MaxItemAmount - 1
						If Inventory[i] = Null Then
							Select Rand(3)
								Case 1
									;[Block]
									Inventory[i] = CreateItem("Wrapped Candy", "red_candy", 1.0, 1.0, 1.0)
									;[End Block]
								Case 2
									;[Block]
									Inventory[i] = CreateItem("Wrapped Candy", "blue_candy", 1.0, 1.0, 1.0)
									;[End Block]
								Case 3
									;[Block]
									Inventory[i] = CreateItem("Wrapped Candy", "yellow_candy", 1.0, 1.0, 1.0)
									;[End Block]
							End Select
							HideEntity(Inventory[i]\Collider)
							Inventory[i]\Picked = True
							Inventory[i]\Dropped = -1
							Inventory[i]\itemtemplate\found = True
							If Inventory[i]\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[Inventory[i]\itemtemplate\sound])
							If I_330\Taken < 2 Then
								CreateMsg(GetLocalString("Items","scp330_2"))
							Else
								CreateMsg(GetLocalString("Items","scp330_3"))
							EndIf
							Exit
						EndIf
					Next
				Else
					CreateMsg(GetLocalString("Items","cannot_carry"))
				EndIf
				I_330\Taken = I_330\Taken + 1
			EndIf
			KeyHitUse = False
		EndIf
		
		If I_330\Taken = 3 Then
			I_330\Taken = 4
			psp\IsShowingHUD = False
			Crouch = True
			PlaySound_Strict(LoadTempSound("SFX\SCP\1162\BodyHorrorExchange" + Rand(1, 4) + ".ogg"))
			LightFlash = 2.0
			BlurTimer = 2000
			pvt = CreatePivot()
			PositionEntity(pvt, EntityX(Collider), EntityY(Collider) - 0.05, EntityZ(Collider))
			TurnEntity(pvt, 90.0, 0.0, 0.0)
			EntityPick(pvt, 0.3)
			de.Decals = CreateDecal(3, PickedX(), PickedY() + 0.005, PickedZ(), 90.0, Rnd(360.0), 0.0)
			de\Size = 0.75 : ScaleSprite(de\obj, de\Size, de\Size)
			FreeEntity(pvt)
			
			it = CreateItem("Severed Hand", "ryan_hand", EntityX(LeftHand, True), EntityY(LeftHand, True), EntityZ(LeftHand, True))
			EntityType(it\Collider, HIT_ITEM)
			it = CreateItem("Severed Hand", "ryan_hand2", EntityX(RightHand, True), EntityY(RightHand, True), EntityZ(RightHand, True))
			EntityType(it\Collider, HIT_ITEM)
			
			Kill()
			m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp330_death",Designation)
			; ~ Remove the event, because player will die anyway and event won't work if I_330\Taken >= 3
			RemoveEvent(e)
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D