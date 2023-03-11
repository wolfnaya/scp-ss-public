Function FillRoom_Room2_Ws(r.Rooms)
	Local it.Items
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0, r\z-640*RoomScale, 270, r, True, False, KEY_CARD_3)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x - 264.0 * RoomScale, 0, r\z+640*RoomScale, 90, r, True, False, KEY_CARD_3)
	r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False
	
	it = CreateItem("FN P90","p90",r\x+716*RoomScale,r\y+160*RoomScale,r\z-96*RoomScale)
	it\state = 50 : it\state2 = 300
	EntityParent it\collider, r\obj
	
	it = CreateItem("Ballistic Vest","vest",r\x+716*RoomScale,r\y+160*RoomScale,r\z-96*RoomScale)
	EntityParent it\collider, r\obj
	
	it = CreateItem("M61 Grenade","grenade",r\x-723*RoomScale,r\y+180*RoomScale,r\z-682*RoomScale)
	EntityParent it\collider, r\obj
	it = CreateItem("M61 Grenade","grenade",r\x-723*RoomScale,r\y+180*RoomScale,r\z-682*RoomScale)
	EntityParent it\collider, r\obj
	it = CreateItem("M61 Grenade","grenade",r\x-723*RoomScale,r\y+180*RoomScale,r\z-682*RoomScale)
	EntityParent it\collider, r\obj
	
	it = CreateItem("Ammo Crate", "ammocrate", r\x-778.0*RoomScale, r\y+10.0*RoomScale, r\z+274.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem("Ammo Crate", "ammocrate", r\x-778.0*RoomScale, r\y+10.0*RoomScale, r\z+274.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Big Ammo Crate", "bigammocrate", r\x+778.0*RoomScale, r\y+10.0*RoomScale, r\z-274.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem("Ammo Crate", "ammocrate", r\x+778.0*RoomScale, r\y+10.0*RoomScale, r\z-274.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem("Ammo Crate", "ammocrate", r\x+778.0*RoomScale, r\y+10.0*RoomScale, r\z-274.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Room2_Ws(e.Events)
	Local it.Items,g.Guns
	
	If PlayerRoom = e\room
		If e\room\RoomDoors[0]\open Lor e\room\RoomDoors[1]\open Then
			GiveAchievement(AchvWS)
			ecst\WasInWS = True
		EndIf
		
		If clm\GuardMode Then
			If e\EventState[0] = 0 Then
				
				psp\Kevlar = 100
				
				Select Rand(0,2)
					Case 0
						;P90
						it = CreateItem("FN P90", "p90", 1, 1, 1)
						it\Picked = True
						it\Dropped = -1
						it\itemtemplate\found=True
						Inventory[0] = it
						HideEntity(it\collider)
						EntityType (it\collider, HIT_ITEM)
						EntityParent(it\collider, 0)
						ItemAmount = ItemAmount + 1
						;F/S
						it = CreateItem("FN Five-Seven", "fiveseven", 1, 1, 1)
						it\Picked = True
						it\Dropped = -1
						it\itemtemplate\found=True
						Inventory[1] = it
						HideEntity(it\collider)
						EntityType (it\collider, HIT_ITEM)
						EntityParent(it\collider, 0)
						ItemAmount = ItemAmount + 1
						;Knife
						it = CreateItem("Combat Knife", "knife", 1, 1, 1)
						it\Picked = True
						it\Dropped = -1
						it\itemtemplate\found=True
						Inventory[2] = it
						HideEntity(it\collider)
						EntityType (it\collider, HIT_ITEM)
						EntityParent(it\collider, 0)
						ItemAmount = ItemAmount + 1
						;Level 3 Key Card
						it = CreateItem("Level 3 Key Card", "key3", 1, 1, 1)
						it\Picked = True
						it\Dropped = -1
						it\itemtemplate\found=True
						Inventory[3] = it
						HideEntity(it\collider)
						EntityType (it\collider, HIT_ITEM)
						EntityParent(it\collider, 0)
						ItemAmount = ItemAmount + 1
						
						g_I\Weapon_InSlot[GunSlot1] = "p90"
						g_I\Weapon_InSlot[GunSlot2] = "fiveseven"
						g_I\Weapon_InSlot[GunSlot3] = "knife"
						g_I\Weapon_CurrSlot% = GunSlot1+1
						g_I\HoldingGun = GUN_p90
						For g = Each Guns
							g\PickedUpEotech = True
							Select g\ID
								Case GUN_P90
									g\CurrReloadAmmo = 150
									g\HasEoTech = True
									g\HasToggledEotech = True
									AddEoTech(g)
								Case GUN_FIVESEVEN
									g\CurrReloadAmmo = 60
							End Select
						Next
					Case 1
						;MP5
						it = CreateItem("H&K MP5K", "mp5k", 1, 1, 1)
						it\Picked = True
						it\Dropped = -1
						it\itemtemplate\found=True
						Inventory[0] = it
						HideEntity(it\collider)
						EntityType (it\collider, HIT_ITEM)
						EntityParent(it\collider, 0)
						ItemAmount = ItemAmount + 1
						;M9
						it = CreateItem("M9 Beretta", "beretta", 1, 1, 1)
						it\Picked = True
						it\Dropped = -1
						it\itemtemplate\found=True
						Inventory[1] = it
						HideEntity(it\collider)
						EntityType (it\collider, HIT_ITEM)
						EntityParent(it\collider, 0)
						ItemAmount = ItemAmount + 1
						;Knife
						it = CreateItem("Combat Knife", "knife", 1, 1, 1)
						it\Picked = True
						it\Dropped = -1
						it\itemtemplate\found=True
						Inventory[2] = it
						HideEntity(it\collider)
						EntityType (it\collider, HIT_ITEM)
						EntityParent(it\collider, 0)
						ItemAmount = ItemAmount + 1
						;Level 3 Key Card
						it = CreateItem("Level 3 Key Card", "key3", 1, 1, 1)
						it\Picked = True
						it\Dropped = -1
						it\itemtemplate\found=True
						Inventory[3] = it
						HideEntity(it\collider)
						EntityType (it\collider, HIT_ITEM)
						EntityParent(it\collider, 0)
						ItemAmount = ItemAmount + 1
						
						g_I\Weapon_InSlot[GunSlot1] = "mp5k"
						g_I\Weapon_InSlot[GunSlot2] = "beretta"
						g_I\Weapon_InSlot[GunSlot3] = "knife"
						g_I\Weapon_CurrSlot% = GunSlot1+1
						g_I\HoldingGun = GUN_MP5K
						For g = Each Guns
							Select g\ID
								Case GUN_MP5K
									g\CurrReloadAmmo = 90
								Case GUN_BERETTA
									g\CurrReloadAmmo = 45
							End Select
						Next
					Case 2
						;H&K MP7
						it = CreateItem("H&K MP7", "mp7", 1, 1, 1)
						it\Picked = True
						it\Dropped = -1
						it\itemtemplate\found=True
						Inventory[0] = it
						HideEntity(it\collider)
						EntityType (it\collider, HIT_ITEM)
						EntityParent(it\collider, 0)
						ItemAmount = ItemAmount + 1
						;USP
						it = CreateItem("H&K USP", "usp", 1, 1, 1)
						it\Picked = True
						it\Dropped = -1
						it\itemtemplate\found=True
						Inventory[1] = it
						HideEntity(it\collider)
						EntityType (it\collider, HIT_ITEM)
						EntityParent(it\collider, 0)
						ItemAmount = ItemAmount + 1
						;Knife
						it = CreateItem("Combat Knife", "knife", 1, 1, 1)
						it\Picked = True
						it\Dropped = -1
						it\itemtemplate\found=True
						Inventory[2] = it
						HideEntity(it\collider)
						EntityType (it\collider, HIT_ITEM)
						EntityParent(it\collider, 0)
						ItemAmount = ItemAmount + 1
						;Level 3 Key Card
						it = CreateItem("Level 3 Key Card", "key3", 1, 1, 1)
						it\Picked = True
						it\Dropped = -1
						it\itemtemplate\found=True
						Inventory[3] = it
						HideEntity(it\collider)
						EntityType (it\collider, HIT_ITEM)
						EntityParent(it\collider, 0)
						ItemAmount = ItemAmount + 1
						
						g_I\Weapon_InSlot[GunSlot1] = "mp7"
						g_I\Weapon_InSlot[GunSlot2] = "usp"
						g_I\Weapon_InSlot[GunSlot3] = "knife"
						g_I\Weapon_CurrSlot% = GunSlot1+1
						g_I\HoldingGun = GUN_MP7
						For g = Each Guns
							Select g\ID
								Case GUN_MP7
									g\CurrReloadAmmo = 90
								Case GUN_USP
									g\CurrReloadAmmo = 36
							End Select
						Next
				End Select
			EndIf
			e\EventState[0] = 1
		EndIf
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D