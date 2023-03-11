Function FillRoom_Room2C_Ews(r.Rooms)
	Local it.Items
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 64.0 * RoomScale, 0.0, r\z + 368.0 * RoomScale, 180, r, False, False, KEY_CARD_3)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
	
	it = CreateItem("H&K MP5K", "mp5k",r\x-1399*RoomScale,r\y+1060*RoomScale,r\z-210*RoomScale)
	it\state = 30 : it\state2 = 90
	EntityParent it\collider, r\obj
	
	it = CreateItem("Glock-20C","glock",r\x-1399*RoomScale,r\y+1060*RoomScale,r\z-210*RoomScale)
	it\state = 15 : it\state2 = 90
	EntityParent it\collider, r\obj
	
	it = CreateItem("MP5 Vertical Grip","verticalgrip",r\x-2111*RoomScale,r\y+1060*RoomScale,r\z+48*RoomScale)
	EntityParent it\collider, r\obj
	it = CreateItem("MP5 Stock","mp5stock",r\x-2111*RoomScale,r\y+1060*RoomScale,r\z+48*RoomScale)
	EntityParent it\collider, r\obj
	
	it = CreateItem("EoTech Sight","eotech",r\x-2058*RoomScale,r\y+1060*RoomScale,r\z+634*RoomScale)
	EntityParent it\collider, r\obj
	
	If (Not clm\NTFMode) Then
		it = CreateItem("Nine-Tailed Fox Helmet", "ntf_helmet",r\x-2058*RoomScale,r\y+1060*RoomScale,r\z+634*RoomScale)
		EntityParent it\collider, r\obj
	EndIf
	
	it = CreateItem("Ammo Crate", "ammocrate", r\x-848.0*RoomScale, r\y+900.0*RoomScale, r\z+582.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem("Ammo Crate", "ammocrate", r\x-848.0*RoomScale, r\y+900.0*RoomScale, r\z+582.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Big Ammo Crate", "bigammocrate", r\x-2111.0*RoomScale, r\y+900.0*RoomScale, r\z+352.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem("Ammo Crate", "ammocrate", r\x-2111.0*RoomScale, r\y+900.0*RoomScale, r\z+352.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem("Ammo Crate", "ammocrate", r\x-2111.0*RoomScale, r\y+900.0*RoomScale, r\z+352.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Room2C_Ews(e.Events)
	
	If PlayerRoom = e\room
		If e\room\RoomDoors[0]\open Then
			GiveAchievement(AchvEWS)
			ecst\WasInEWS = True
		EndIf
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D