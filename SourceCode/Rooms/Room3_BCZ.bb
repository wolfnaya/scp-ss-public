
Function FillRoom_Room3_BCZ(r.Rooms)
	Local r2.Rooms
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = r\RoomTemplate\Name And r2 <> r Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	r\Objects[1] = LoadRMesh("GFX\map\rooms\room3_bcz\room3_bcz.rmesh",Null)
	ScaleEntity (r\Objects[1],RoomScale,RoomScale,RoomScale)
	EntityType GetChild(r\Objects[1],2), HIT_MAP
	EntityPickMode GetChild(r\Objects[1],2), 2
	PositionEntity(r\Objects[1],r\x,r\y,r\z,True)
	EntityParent(r\Objects[1], r\obj)
	
	If firstRoom Then
		InitFluLight(0,FLU_STATE_ON,r)
		InitFluLight(1,FLU_STATE_ON,r)
	Else
		InitFluLight(0,FLU_STATE_ON,r)
		InitFluLight(1,FLU_STATE_OFF,r)
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D