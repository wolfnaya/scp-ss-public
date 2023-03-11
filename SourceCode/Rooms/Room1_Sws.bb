Function FillRoom_Room1_Sws(r.Rooms)
	Local it.Items
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 10.0 * RoomScale, 0, r\z+208*RoomScale, 0, r, True, DOOR_HCZ, KEY_CARD_5)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x - 1174.0 * RoomScale, 0, r\z+208*RoomScale, 0, r, True, DOOR_HCZ, KEY_CARD_5)
	r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False
	
	r\RoomDoors[2] = CreateDoor(r\zone, r\x - 2547.0 * RoomScale, 0, r\z+1432*RoomScale, 90, r, True, False, KEY_CARD_5)
	r\RoomDoors[2]\AutoClose = False : r\RoomDoors[2]\open = False
	
	r\RoomDoors[3] = CreateDoor(r\zone, r\x -2796.0 * RoomScale, 0, r\z-729*RoomScale, 90, r, True, DOOR_LCZ, False, "4892")
	r\RoomDoors[3]\AutoClose = False : r\RoomDoors[3]\open = False : r\RoomDoors[3]\dir = 1
	r\RoomDoors[4] = CreateDoor(r\zone, r\x - 3166.0 * RoomScale, 0, r\z+643*RoomScale, 90, r, True, False, False, "2871")
	r\RoomDoors[4]\AutoClose = False : r\RoomDoors[4]\open = False
	
	If (Not clm\GlobalMode) Then
		it = CreateItem("Hazardous Defence Suit", "hds_suit", r\x-3855.0*RoomScale, r\y+30.0*RoomScale, r\z-728.0*RoomScale)
		RotateEntity it\Collider, 0, -90, 0
		EntityParent(it\Collider, r\obj)
		
		it = CreateItem("Electrical Magnetive Rifle - Prototype", "emr-p", r\x-3370.0*RoomScale, r\y+186.0*RoomScale, r\z+258.0*RoomScale)
		RotateEntity it\Collider, 0, r\angle+270,r\angle+90
		it\state = 0 : it\state2 = 20
		EntityParent(it\Collider, r\obj)
		
		it = CreateItem("XM29", "xm29", r\x-1530.0*RoomScale, r\y+20.0*RoomScale, r\z+1523.0*RoomScale)
		RotateEntity it\Collider, 0, r\angle+45, 0
		it\state = 30 : it\state2 = 120
		EntityParent(it\Collider, r\obj)
		
		it = CreateItem("5 Rnd 20mm HE Grenade Magazine", "grenade_mag", r\x-1586.0*RoomScale, r\y+20.0*RoomScale, r\z+1372.0*RoomScale)
		EntityParent(it\Collider, r\obj)
		it = CreateItem("5 Rnd 20mm HE Grenade Magazine", "grenade_mag", r\x-1586.0*RoomScale, r\y+20.0*RoomScale, r\z+1372.0*RoomScale)
		EntityParent(it\Collider, r\obj)
		
		it = CreateItem("Hazardous Defence Suit Power Cell", "hds_fuse", r\x-3144.0*RoomScale, r\y+56.0*RoomScale, r\z-477.0*RoomScale)
		EntityParent(it\Collider, r\obj)
		it = CreateItem("Hazardous Defence Suit Power Cell", "hds_fuse", r\x-3144.0*RoomScale, r\y+56.0*RoomScale, r\z-477.0*RoomScale)
		EntityParent(it\Collider, r\obj)
		it = CreateItem("Hazardous Defence Suit Power Cell", "hds_fuse", r\x-3144.0*RoomScale, r\y+56.0*RoomScale, r\z-477.0*RoomScale)
		EntityParent(it\Collider, r\obj)
		
		it = CreateItem("Electrical Battery", "emr-p_mag", r\x-3315.0*RoomScale, r\y+10.0*RoomScale, r\z-147.0*RoomScale)
		EntityParent(it\Collider, r\obj)
		it = CreateItem("Electrical Battery", "emr-p_mag", r\x-2508.0*RoomScale, r\y+10.0*RoomScale, r\z-167.0*RoomScale)
		EntityParent(it\Collider, r\obj)
		it = CreateItem("Electrical Battery", "emr-p_mag", r\x-2508.0*RoomScale, r\y+10.0*RoomScale, r\z-167.0*RoomScale)
		EntityParent(it\Collider, r\obj)
		it = CreateItem("Electrical Battery", "emr-p_mag", r\x-2314.0*RoomScale, r\y+70.0*RoomScale, r\z+120.0*RoomScale)
		EntityParent(it\Collider, r\obj)
		it = CreateItem("Electrical Battery", "emr-p_mag", r\x-2201.0*RoomScale, r\y+70.0*RoomScale, r\z+128.0*RoomScale)
		EntityParent(it\Collider, r\obj)
	EndIf
	
	it = CreateItem("Ammo Crate", "ammocrate", r\x+323.0*RoomScale, r\y+10.0*RoomScale, r\z+1429.0*RoomScale)
	EntityParent(it\Collider, r\obj)
	it = CreateItem("Ammo Crate", "ammocrate", r\x+323.0*RoomScale, r\y+10.0*RoomScale, r\z+1429.0*RoomScale)
	EntityParent(it\Collider, r\obj)
	
	it = CreateItem("Big Ammo Crate", "bigammocrate", r\x-452.0*RoomScale, r\y+10.0*RoomScale, r\z+1429.0*RoomScale)
	EntityParent(it\Collider, r\obj)
	it = CreateItem("Ammo Crate", "ammocrate", r\x-452.0*RoomScale, r\y+10.0*RoomScale, r\z+1429.0*RoomScale)
	EntityParent(it\Collider, r\obj)
	it = CreateItem("Ammo Crate", "ammocrate", r\x-452.0*RoomScale, r\y+10.0*RoomScale, r\z+1429.0*RoomScale)
	EntityParent(it\Collider, r\obj)
	
	it = CreateItem("Big Ammo Crate", "bigammocrate", r\x-1191.0*RoomScale, r\y+10.0*RoomScale, r\z+1429.0*RoomScale)
	EntityParent(it\Collider, r\obj)
	it = CreateItem("Ammo Crate", "ammocrate", r\x-1191.0*RoomScale, r\y+10.0*RoomScale, r\z+1429.0*RoomScale)
	EntityParent(it\Collider, r\obj)
	
	it = CreateItem("H&K P30L", "p30l", r\x-2726.0*RoomScale, r\y+20.0*RoomScale, r\z+453.0*RoomScale)
	RotateEntity it\Collider, 0, r\angle+45, 0
	it\state = 17 : it\state2 = 85
	EntityParent(it\Collider, r\obj)
	
	r\Objects[0] = CreatePivot()
	PositionEntity r\Objects[0],r\x-908*RoomScale,0.1,r\z+908*RoomScale,True
	EntityParent r\Objects[0],r\obj
	
End Function

Function UpdateEvent_Room1_Sws(e.Events)
	
	If (Not clm\GlobalMode) Then
	
		If PlayerRoom = e\room
			If e\room\RoomDoors[0]\open Then
				
				If e\EventState[0] = 0 Then
					If e\room\NPC[0] = Null Then
						e\room\NPC[0] = CreateNPC(NPCtypeD9341,EntityX(e\room\Objects[0],True),1.0,EntityZ(e\room\Objects[0],True))
					EndIf
					e\EventState[0] = 1
				EndIf
				
				GiveAchievement(AchvSWS)
				ecst\WasInSWS = True
			EndIf
			If e\room\RoomDoors[3]\open Then
				GiveAchievement(AchvHDS)
			EndIf
			If e\room\RoomDoors[4]\open Then
				GiveAchievement(AchvEMRP)
			EndIf
		EndIf
		
		If ecst\UnlockedEMRP Then
			e\room\RoomDoors[4]\locked = False
		Else
			e\room\RoomDoors[4]\locked = True
		EndIf
		
		If ecst\UnlockedHDS Then
			e\room\RoomDoors[3]\locked = False
		Else
			e\room\RoomDoors[3]\locked = True
		EndIf
		
	EndIf
	
	If ecst\WasInLWS And ecst\WasInWS And ecst\WasInHWS And ecst\WasInEWS And ecst\WasInSWS Then
		ecst\WasInAllSupplies = True
	EndIf
	
	If ecst\WasInAllSupplies Then
		GiveAchievement(AchvWeapons)
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D