Function FillRoom_Room2C_Research(r.Rooms)
	Local d.Doors,it.Items
	
	d = CreateDoor(r\zone,r\x-244.0*RoomScale, 0.0, r\z-736.0*RoomScale,-90, r, False, DOOR_OFFICE, KEY_CARD_3)
	PositionEntity(d\buttons[0], r\x-264.0 * RoomScale, EntityY(d\buttons[1],True), r\z-870.0 * RoomScale, True)
	PositionEntity(d\buttons[1], r\x-224.0 * RoomScale, EntityY(d\buttons[0],True), r\z-870.0 * RoomScale, True)
	
	d = CreateDoor(r\zone,r\x+736.0*RoomScale, 0.0, r\z+244.0*RoomScale,0, r, False, DOOR_OFFICE, KEY_CARD_3)
	PositionEntity(d\buttons[0], r\x+870.0 * RoomScale, EntityY(d\buttons[0],True), r\z+224.0 * RoomScale, True)
	PositionEntity(d\buttons[1], r\x+870.0 * RoomScale, EntityY(d\buttons[1],True), r\z+264.0 * RoomScale, True)
	
	d = CreateDoor(r\zone,r\x-944.0*RoomScale, 0.0, r\z-792.0*RoomScale,90, r, False, DOOR_OFFICE_2)
	d\locked = True
	
	InitFluLight(0,FLU_STATE_OFF,r)
	InitFluLight(1,FLU_STATE_ON,r)
	
	r\Objects[0] = LoadRMesh("GFX\map\rooms\room2c_research\room2c_research.rmesh",Null)
	ScaleEntity (r\Objects[0],RoomScale,RoomScale,RoomScale)
	EntityType GetChild(r\Objects[0],2), HIT_MAP
	EntityPickMode GetChild(r\Objects[0],2), 2
	PositionEntity(r\Objects[0],r\x,r\y,r\z,True)
	EntityParent(r\Objects[0], r\obj)
	
	r\Objects[1]=LoadMesh_Strict("GFX\map\rooms\room2c_research\room2c_research_props.b3d")
	PositionEntity r\Objects[1],r\x,r\y,r\z
	ScaleEntity r\Objects[1],RoomScale,RoomScale,RoomScale
	EntityParent r\Objects[1],r\obj
	
	If ecst\ChanceToSpawnWolfNote = 5 Then
		it = CreateItem("Wolfnaya's Room Note", "paper", r\x-880.0*RoomScale, r\y+258.0*RoomScale, r\z-335.0*RoomScale)
		EntityParent(it\collider, r\obj)
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D