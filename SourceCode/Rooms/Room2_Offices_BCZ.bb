Function FillRoom_Room2_Offices_BCZ(r.Rooms)
	
	r\Objects[1] = LoadRMesh("GFX\map\rooms\room2_offices_bcz\room2_offices_bcz.rmesh",Null)
	ScaleEntity (r\Objects[1],RoomScale,RoomScale,RoomScale)
	EntityType GetChild(r\Objects[1],2), HIT_MAP
	EntityPickMode GetChild(r\Objects[1],2), 2
	PositionEntity(r\Objects[1],r\x,r\y,r\z,True)
	EntityParent(r\Objects[1], r\obj)
	
	
		InitFluLight(0,FLU_STATE_OFF,r)
		InitFluLight(1,FLU_STATE_ON,r)
		InitFluLight(2,FLU_STATE_OFF,r)
		InitFluLight(3,FLU_STATE_FLICKER,r)
		
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D