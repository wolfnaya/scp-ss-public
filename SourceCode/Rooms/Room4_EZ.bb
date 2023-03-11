
Function FillRoom_Room4_EZ(r.Rooms)
	Local r2.Rooms
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = r\RoomTemplate\Name And r2 <> r Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	r\Objects[0] = LoadMesh_Strict("GFX\map\rooms\room4_ez\room4_ez.b3d",r\obj)
	EntityPickMode r\Objects[0],2
	EntityType r\Objects[0],HIT_MAP
	EntityFX r\Objects[0], 2
	LightMesh r\Objects[0],-120,-120,-120
	
	If firstRoom Then
		
		r\Objects[1] = LoadMesh_Strict("GFX\map\alarm_siren.b3d")
		ScaleEntity r\Objects[1],RoomScale,RoomScale,RoomScale
		PositionEntity r\Objects[1],r\x,r\y+450.0*RoomScale,r\z,True
		EntityParent r\Objects[1],r\obj
		
		r\Objects[2] = LoadMesh_Strict("GFX\map\Props\alarm_cylinder.b3d")
		ScaleEntity r\Objects[2],RoomScale,RoomScale,RoomScale
		PositionEntity r\Objects[2],r\x,r\y+430.0*RoomScale,r\z,True
		EntityParent r\Objects[2],r\obj
		
		r\Objects[3] = LoadMesh_Strict("GFX\map\Props\alarm_rotor.b3d")
		ScaleEntity r\Objects[3],RoomScale,RoomScale,RoomScale
		PositionEntity r\Objects[3],r\x,r\y+430.0*RoomScale,r\z,True
		EntityParent r\Objects[3],r\obj
		
		r\Objects[4] = LoadRMesh("GFX\map\rooms\room4_ez\room4_ez_rotor.rmesh",Null)
		ScaleEntity (r\Objects[4],RoomScale,RoomScale,RoomScale)
		EntityType GetChild(r\Objects[4],2), HIT_MAP
		EntityPickMode GetChild(r\Objects[4],2), 2
		PositionEntity(r\Objects[4],r\x,r\y,r\z,True)
		EntityParent(r\Objects[4], r\obj)
		
		r\AlarmRotor[0] = r\Objects[3]
		r\AlarmRotorLight[0] = CreateLight(3,r\Objects[3])
		MoveEntity r\AlarmRotorLight[0],0,0,0.001
		LightRange r\AlarmRotorLight[0],1.5
		LightColor r\AlarmRotorLight[0],255*3,0,0
		RotateEntity r\AlarmRotorLight[0],45,0,0
		LightConeAngles r\AlarmRotorLight[0],0,75
	EndIf
	
End Function

Function UpdateEvent_Room4_EZ(e.Events)
	Local r2.Rooms
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = e\room\RoomTemplate\Name And r2 <> e\room Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	If firstRoom Then
		If PlayerRoom = e\room Lor IsRoomAdjacent(e\room,PlayerRoom) Then
			UpdateAlarmRotor(e\room\AlarmRotor[0],7)
			ShowEntity e\room\AlarmRotor[0]
		Else
			HideEntity e\room\AlarmRotor[0]
		EndIf
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D