
Function FillRoom_Room2_Offices_1(r.Rooms)
	Local it.Items,r2.Rooms
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = r\RoomTemplate\Name And r2 <> r Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	r\Objects[1] = LoadRMesh("GFX\map\rooms\room2_offices_1\room2offices_opt.rmesh",Null)
	ScaleEntity (r\Objects[1],RoomScale,RoomScale,RoomScale)
	EntityType GetChild(r\Objects[1],2), HIT_MAP
	EntityPickMode GetChild(r\Objects[1],2), 2
	PositionEntity(r\Objects[1],r\x,r\y,r\z,True)
	EntityParent(r\Objects[1], r\obj)
	
	InitFluLight(0,FLU_STATE_OFF,r)
	InitFluLight(1,FLU_STATE_ON,r)
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x+192*RoomScale,r\y,r\z+160*RoomScale,270,r,False,DOOR_OFFICE_2)
	r\RoomDoors[1]\locked = True
	r\RoomDoors[2] = CreateDoor(r\zone,r\x-372*RoomScale,r\y,r\z+528*RoomScale,0,r,False,DOOR_OFFICE_2)
	r\RoomDoors[2]\locked = True
	
	If firstRoom Then
		r\RoomDoors[0] = CreateDoor(r\zone,r\x+192*RoomScale,r\y,r\z-160*RoomScale,90,r,False,DOOR_OFFICE_2)
		r\RoomDoors[0]\locked = False
				CreateScreen(r\x + 385.0 * RoomScale, r\y + 160.0 * RoomScale, r\z - 960.0 * RoomScale, "office.sc", r) ;Manually adding the screen...
;		CreateWaypoint(r\x + 900.0 * RoomScale, r\y + 64.0 * RoomScale, r\z - 200.0 * RoomScale, Null, r) 		;... and waypoints
;		CreateWaypoint(r\x + 900.0 * RoomScale, r\y + 64.0 * RoomScale, r\z - 768.0 * RoomScale, Null, r)
;		CreateWaypoint(r\x + 400.0 * RoomScale, r\y + 64.0 * RoomScale, r\z - 768.0 * RoomScale, Null, r)
		
		it = CreateItem("Document SCP-106", "paper", r\x + 750.0 * RoomScale, r\y + 150.0 * RoomScale, r\z - 450.0 * RoomScale)
		EntityParent(it\Collider, r\obj)
		
		it = CreateItem("Level 2 Key Card", "key2", r\x + 750.0 * RoomScale, r\y + 150.0 * RoomScale, r\z - 925.0 * RoomScale)
		EntityParent(it\Collider, r\obj)
		
		it = CreateItem("S-NAV 300 Navigator", "nav", r\x + 870.0 * RoomScale, r\y + 150.0 * RoomScale, r\z - 50.0 * RoomScale)
		it\state = 25 : EntityParent(it\Collider, r\obj)
		
		it = CreateItem("Notification", "paper", r\x + 550.0 * RoomScale, r\y + 150.0 * RoomScale, r\z - 440.0 * RoomScale)
		EntityParent(it\Collider, r\obj)
		
		InitFluLight(2,FLU_STATE_FLICKER,r)
	Else
		r\RoomDoors[0] = CreateDoor(r\zone,r\x+192*RoomScale,r\y,r\z-160*RoomScale,90,r,False,DOOR_OFFICE_2)
		r\RoomDoors[0]\locked = True
		
		InitFluLight(2,FLU_STATE_OFF,r)
	EndIf
	
End Function

Function UpdateEvent_Room2_Offices_1(e.Events)
;	Local e2.Events
;	Local is035released = False
;	
;	For e2.Events = Each Events
;		If e2<>e And e2\EventName="cont_035"
;			If e2\EventState[0]<0.0
;				is035released=True
;				Exit
;			EndIf
;		EndIf
;	Next
;	
;	If is035released
;		If e\room\dist < 8
;			If e\room\NPC[0]=Null
;				e\room\NPC[0]=CreateNPC(NPCtypeD,e\room\x,0.5,e\room\z)
;				RotateEntity e\room\NPC[0]\Collider,0,e\room\angle+180,0
;				MoveEntity e\room\NPC[0]\Collider,0,0,-0.5
;				e\room\NPC[0]\State = 3
;				e\room\NPC[0]\texture = "GFX\npcs\035victim.jpg"
;				ChangeNPCTextureID(e\room\NPC[0],7)
;				SetNPCFrame(e\room\NPC[0],19)
;			EndIf
;			If e\room\NPC[1]=Null
;				If EntityDistanceSquared(e\room\NPC[0]\Collider,Collider)<PowTwo(2.5)
;					e\room\NPC[1]=CreateNPC(NPCtypeTentacle,EntityX(e\room\NPC[0]\Collider),0.0,EntityZ(e\room\NPC[0]\Collider))
;					RotateEntity e\room\NPC[1]\Collider,0,e\room\angle,0
;					MoveEntity e\room\NPC[1]\Collider,0,0,0.6
;				EndIf
;			EndIf
;		Else
;			If e\room\dist>HideDistance
;				If e\room\NPC[1]<>Null
;					RemoveNPC(e\room\NPC[1])
;					e\room\NPC[1]=Null
;				EndIf
;			EndIf
;		EndIf
;	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D