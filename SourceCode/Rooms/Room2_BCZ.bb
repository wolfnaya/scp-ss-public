
Function FillRoom_Room2_BCZ(r.Rooms)
	Local r2.Rooms
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = r\RoomTemplate\Name And r2 <> r Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	r\Objects[1] = LoadRMesh("GFX\map\rooms\room2_bcz\room2_bcz.rmesh",Null)
	ScaleEntity (r\Objects[1],RoomScale,RoomScale,RoomScale)
	EntityType GetChild(r\Objects[1],2), HIT_MAP
	EntityPickMode GetChild(r\Objects[1],2), 2
	PositionEntity(r\Objects[1],r\x,r\y,r\z,True)
	EntityParent(r\Objects[1], r\obj)
	
	If firstRoom Then
		InitFluLight(0,FLU_STATE_FLICKER,r)
		InitFluLight(1,FLU_STATE_ON,r)
		InitFluLight(2,FLU_STATE_OFF,r)
		InitFluLight(3,FLU_STATE_ON,r)
	Else
		InitFluLight(0,FLU_STATE_ON,r)
		InitFluLight(1,FLU_STATE_ON,r)
		InitFluLight(2,FLU_STATE_OFF,r)
		InitFluLight(3,FLU_STATE_ON,r)
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D