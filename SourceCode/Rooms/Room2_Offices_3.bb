Function FillRoom_Room2_Offices_3(r.Rooms)
	Local it.Items,r2.Rooms,i
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = r\RoomTemplate\Name And r2 <> r Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	r\Objects[3] = CreatePivot()
	PositionEntity(r\Objects[3],r\x,r\y+1.0,r\z-50*RoomScale,True)
	EntityParent(r\Objects[3], r\obj)
	
	r\Objects[1] = LoadRMesh("GFX\map\rooms\room2_offices_3\room2offices3_opt.rmesh",Null)
	ScaleEntity (r\Objects[1],RoomScale,RoomScale,RoomScale)
	EntityType GetChild(r\Objects[1],2), HIT_MAP
	EntityPickMode GetChild(r\Objects[1],2), 2
	PositionEntity(r\Objects[1],r\x,r\y,r\z,True)
	EntityParent(r\Objects[1], r\obj)
	
	InitFluLight(0,FLU_STATE_OFF,r)
	
	If firstRoom Then
		
		If (Not clm\GlobalMode) Then
			it = CreateItem("SCP-035","scp035",r\x,r\y,r\z)
			EntityParent it\collider, r\obj
		EndIf
			
		r\Objects[0] = LoadRMesh("GFX\map\rooms\room2_offices_3\room2_offices_3_obj_1.rmesh",Null)
		
		ScaleEntity (r\Objects[0],RoomScale,RoomScale,RoomScale)
		EntityType GetChild(r\Objects[0],2), HIT_MAP
		EntityPickMode GetChild(r\Objects[0],2), 2
		PositionEntity(r\Objects[0],r\x,r\y,r\z,True)
		EntityParent(r\Objects[0], r\obj)
		
		it = CreateItem("Crowbar", "crowbar", r\x + 403.0 * RoomScale, r\y + 460.0 * RoomScale, r\z + 862.0 * RoomScale); ~ TODO: Add Axe weapon
		EntityParent(it\collider, r\obj)
		
		InitFluLight(1,FLU_STATE_OFF,r)
		InitFluLight(2,FLU_STATE_ON,r)
	Else
		r\Objects[0] = LoadRMesh("GFX\map\rooms\room2_offices_3\room2_offices_3_obj_2.rmesh",Null)
		
		ScaleEntity (r\Objects[0],RoomScale,RoomScale,RoomScale)
		EntityType GetChild(r\Objects[0],2), HIT_MAP
		EntityPickMode GetChild(r\Objects[0],2), 2
		PositionEntity(r\Objects[0],r\x,r\y,r\z,True)
		EntityParent(r\Objects[0], r\obj)
		
		If Rand(2)=1 Then 
			it = CreateItem("Mobile Task Forces", "paper", r\x + 1243.0 * RoomScale, r\y +140.0 * RoomScale, r\z - 5.0 * RoomScale)
			EntityParent(it\collider, r\obj)	
		Else
			it = CreateItem("Security Clearance Levels", "paper", r\x + 1243.0 * RoomScale, r\y +140.0 * RoomScale, r\z - 5.0 * RoomScale)
			EntityParent(it\collider, r\obj)			
		EndIf
		
		it = CreateItem("Object Classes", "paper", r\x + 938.0 * RoomScale, r\y +120.0 * RoomScale, r\z + 124.0 * RoomScale)
		EntityParent(it\collider, r\obj)	
		
		it = CreateItem("Document", "paper", r\x + 754.0 * RoomScale, r\y +120.0 * RoomScale, r\z + 5.0 * RoomScale)
		EntityParent(it\collider, r\obj)	
		
		it = CreateItem("Radio Transceiver", "radio", r\x + 614.0 * RoomScale, r\y + 120.0 * RoomScale, r\z + 935.0 * RoomScale)
		EntityParent(it\collider, r\obj)				
		
		For i = 0 To Rand(0,1)
			it = CreateItem("ReVision Eyedrops", "eyedrops", r\x + 1238.0*RoomScale, r\y + 220.0 * RoomScale, r\z + 220.0*RoomScale + i*0.05)
			EntityParent(it\collider, r\obj)				
		Next
		
		it = CreateItem("9V Battery", "bat", r\x + 841.0 * RoomScale, r\y + 135.0 * RoomScale, r\z + 955.0 * RoomScale)
		EntityParent(it\collider, r\obj)
		
		InitFluLight(1,FLU_STATE_ON,r)
		InitFluLight(2,FLU_STATE_FLICKER,r)
	EndIf
	
End Function

Function UpdateEvent_Room2_Offices_3(e.Events)
	Local r2.Rooms,it.Items
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = e\room\RoomTemplate\Name And r2 <> e\room Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	If firstRoom Then
		If e\room\NPC[0] = Null Then
			e\room\NPC[0] = CreateNPC(NPCtypeD,EntityX(e\room\Objects[3],True),EntityY(e\room\Objects[3],True),EntityZ(e\room\Objects[3],True))
			SetNPCFrame e\room\NPC[0], 559
			e\room\NPC[0]\texture = "GFX\npcs\Humans\Personnel\035victim.jpg"
			Local tex = LoadTexture_Strict(e\room\NPC[0]\texture, 1, 2)
			TextureBlend(tex,5)
			EntityTexture(e\room\NPC[0]\obj, tex)
			DeleteSingleTextureEntryFromCache tex
			e\room\NPC[0]\State=3
			e\room\NPC[0]\IsDead=True
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D