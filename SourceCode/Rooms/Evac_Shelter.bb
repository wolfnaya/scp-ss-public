
Function FillRoom_Evac_Shelter(r.Rooms)
	Local r2.Rooms
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = r\RoomTemplate\Name And r2 <> r Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	If firstRoom Then
		InitFluLight(0,FLU_STATE_ON,r)
	Else
		InitFluLight(0,FLU_STATE_OFF,r)
	EndIf
	
	r\Objects[0] = LoadRMesh("GFX\map\rooms\evac_shelter\evac_shelter.rmesh",Null)
	ScaleEntity (r\Objects[0],RoomScale,RoomScale,RoomScale)
	EntityType GetChild(r\Objects[0],2), HIT_MAP
	EntityPickMode GetChild(r\Objects[0],2), 2
	PositionEntity(r\Objects[0],r\x,r\y,r\z,True)
	EntityParent(r\Objects[0], r\obj)
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D