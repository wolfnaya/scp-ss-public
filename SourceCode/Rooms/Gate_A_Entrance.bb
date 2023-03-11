
Function FillRoom_Gate_A_Entrance(r.Rooms)
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x+1048.0*RoomScale, 0, r\z+656.0*RoomScale, True)
	EntityParent r\Objects[0], r\obj
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x, 0, r\z + 1008.0 * RoomScale, 0, r, False, True, KEY_CARD_5)
	r\RoomDoors[1]\dir = 1 : r\RoomDoors[1]\AutoClose = False
	PositionEntity(r\RoomDoors[1]\buttons[1], r\x+416*RoomScale, r\y + 0.7, r\z+1200.0*RoomScale, True)
	RotateEntity r\RoomDoors[1]\buttons[1],0,r\angle-90,0,True
	r\RoomDoors[1]\buttons[0] = FreeEntity_Strict(r\RoomDoors[1]\buttons[0])
	r\RoomDoors[1]\MTFClose = False
	
	r\Objects[2] = CreateButton(r\x,r\y+160*RoomScale,r\z+1760*RoomScale,0,0,0)
	EntityParent r\Objects[2], r\obj
	
	r\RoomDoors[2] = CreateDoor(r\zone,r\x-688*RoomScale,r\y,r\z -404.0*RoomScale,270,r,False,DOOR_OFFICE)
	r\RoomDoors[2]\locked = True
	r\RoomDoors[3] = CreateDoor(r\zone,r\x+688*RoomScale,r\y,r\z+528.0*RoomScale,90,r,False,DOOR_OFFICE)
	r\RoomDoors[3]\locked = True
	
	CreateDarkSprite(r, 3)
	
	r\Objects[4] = LoadMesh_Strict("GFX\map\rooms\gate_a_entrance\gateaentrance_new.b3d",r\obj)
	EntityPickMode r\Objects[4],2
	EntityType r\Objects[4],HIT_MAP
	EntityFX r\Objects[4], 2
	LightMesh r\Objects[4],-120,-120,-120
	
	r\Objects[5] = LoadMesh_Strict("GFX\map\Props\alarm_cylinder.b3d")
	ScaleEntity r\Objects[5],RoomScale,RoomScale,RoomScale
	PositionEntity r\Objects[5],r\x,r\y+908.0*RoomScale,r\z-40.0*RoomScale,True
	EntityParent r\Objects[5],r\obj
	
	r\Objects[6] = LoadMesh_Strict("GFX\map\Props\alarm_rotor.b3d")
	ScaleEntity r\Objects[6],RoomScale,RoomScale,RoomScale
	PositionEntity r\Objects[6],r\x,r\y+908.0*RoomScale,r\z-40.0*RoomScale,True
	EntityParent r\Objects[6],r\obj
	
	r\Objects[9] = LoadMesh_Strict("GFX\map\alarm_siren.b3d")
	ScaleEntity r\Objects[9],RoomScale,RoomScale,RoomScale
	PositionEntity r\Objects[9],r\x,r\y+928.0*RoomScale,r\z-40.0*RoomScale,True
	EntityParent r\Objects[9],r\obj
	
	r\AlarmRotor[0] = r\Objects[6]
	r\AlarmRotorLight[0] = CreateLight(3,r\Objects[6])
	MoveEntity r\AlarmRotorLight[0],0,0,0.001
	LightRange r\AlarmRotorLight[0],1.5
	LightColor r\AlarmRotorLight[0],255*3,100*3,0
	RotateEntity r\AlarmRotorLight[0],45,0,0
	LightConeAngles r\AlarmRotorLight[0],0,75
End Function

Function UpdateEvent_Gate_A_Entrance(e.Events)
	Local i%, tex%,it.Items,g.Guns
	Local n.NPCs
	If (Not clm\NTFMode) Then
		If PlayerRoom = e\room
			If e\EventState[2] = 0 Then
				e\EventState[2] = 1
			EndIf
			If e\EventState[2] = 1 And e\room\RoomDoors[1]\open = True Then
				e\SoundCHN = PlaySound2(LoadTempSound("SFX\Door\BigDoorStartsOpenning.ogg"),Camera,e\room\RoomDoors[1]\frameobj,10,2.5)
				StopChannel(e\room\RoomDoors[1]\SoundCHN)
				e\EventState[2] = 2
			ElseIf e\EventState[2] = 2 Then
				UpdateSoundOrigin(e\SoundCHN,Camera,e\room\RoomDoors[1]\frameobj,10,2.5)
				e\room\RoomDoors[1]\openstate = 0.01
				e\room\RoomDoors[1]\open = False	
			EndIf
			If e\EventState[2] = 2 And (Not ChannelPlaying(e\SoundCHN)) Then
				OpenCloseDoor(e\room\RoomDoors[1])
				e\EventState[2] = 3
			EndIf
			
			If ecst\UnlockedGateDoors Then
				e\room\RoomDoors[1]\locked = False : e\room\RoomDoors[1]\open = True
			Else
				e\room\RoomDoors[1]\locked = True
			EndIf
			
			If e\EventState[1] = 0 Then
				If TaskExists(TASK_GET_TOPSIDE) And (Not ecst\UnlockedGateDoors) Then
					FailTask(TASK_GET_TOPSIDE)
					BeginTask(TASK_FIND_ROOM1_O5)
					e\EventState[1] = 1
				EndIf
			EndIf
			
			If e\EventState[3] = 0 Then
				UpdateButton(e\room\Objects[2])
				If d_I\ClosestButton = e\room\Objects[2] Then
					;DrawHandIcon = True
					If KeyHitUse Then
						e\EventState[4] = 1.0
					EndIf
				EndIf
			EndIf
			
			If e\EventState[4] > 0 Then
				e\EventState[3] = e\EventState[3] + (fps\Factor[0]*0.01)
			EndIf
			
			EntityAlpha e\room\Objects[3],Min(e\EventState[3],1.0)
			
			If e\EventState[3] > 1.05 Then
				SaveGame(SavePath + CurrSave\Name + "\")
				
				gc\CurrZone = GATE_A_TOPSIDE
				
				ResetControllerSelections()
				DropSpeed = 0
				NullGame(True,False)
				LoadEntities()
				LoadAllSounds()
				Local zonecache% = gc\CurrZone
				If FileType(SavePath + CurrSave\Name + "\" + gc\CurrZone + ".sav") = 1 Then
					LoadGame(SavePath + CurrSave\Name + "\", gc\CurrZone)
					InitLoadGame()
				Else
					InitNewGame()
					LoadDataForZones(SavePath + CurrSave\Name + "\")
				EndIf
				gc\CurrZone = zonecache
				MainMenuOpen = False
				FlushKeys()
				FlushMouse()
				FlushJoy()
				ResetInput()
				
				SaveGame(SavePath + CurrSave\Name + "\")
				Return
			EndIf
			
			If e\EventState[3] > 0 Then
				If TaskExists(TASK_HELP_GUARD) Then
					;psp\Karma = psp\Karma - (Rand(2,3))
					FailTask(TASK_HELP_GUARD)
				EndIf
			EndIf
			
		EndIf
		
		If PlayerRoom = e\room Lor IsRoomAdjacent(e\room,PlayerRoom) Then
			UpdateAlarmRotor(e\room\AlarmRotor[0],4)
			ShowEntity e\room\AlarmRotor[0]
		Else
			HideEntity e\room\AlarmRotor[0]
		EndIf
	Else
		
		If PlayerRoom = e\room Then
			If e\EventState[0] = 0 Then
				e\EventState[0] = 1
				
				If TaskExists(TASK_CLASSIC_ENTER_SITE) Then
					EndTask(TASK_CLASSIC_ENTER_SITE)
				EndIf
				
				For i = 0 To 1
					n.NPCs = CreateNPC(NPCtypeNTF, EntityX(e\room\obj),0.25,EntityZ(e\room\obj)-1650.0*RoomScale)
					RotateEntity n\Collider,0,e\room\angle+180,0
					MoveEntity n\Collider,-i*1.5,0,0
					
					e\room\NPC[i] = n
					n\PrevX = i
					n\PrevState = (i+1)
					n\State = MTF_TOTARGET
					n\EnemyX = EntityX(n\Collider)
					n\EnemyY = 0.25
					n\EnemyZ = EntityZ(n\Collider)
					PointEntity(n\Collider,e\room\RoomDoors[1]\frameobj)
					SetNPCFrame(n,Rand(962, 1259))
				Next
				
				e\room\NPC[1]\PrevState = MTF_UNIT_MEDIC
				e\room\NPC[1]\texture = "GFX\npcs\Humans\MTF\NTF\NineTailedFox_medic.png"
				tex = LoadTexture_Strict(e\room\NPC[1]\texture, 1, 2)
				TextureBlend(tex,5)
				EntityTexture(e\room\NPC[1]\obj, tex)
				DeleteSingleTextureEntryFromCache tex
				
				MTFtimer = 1.0
				
				Curr106\Idle = True
				Curr173\Idle = SCP173_DISABLED
				
				PlayNewDialogue(0,%01)
				
				;BeginTask(TASK_OPENINV)
				;BeginTask(TASK_CLICKKEYCARD)
				;BeginTask(TASK_OPENDOOR)
				
				;Other tasks for this event are located in UpdateGUI!
				
				;If clm\NTFMode Then
				
				If (Not opt\IntroEnabled) Then
					
					mpl\HasNTFGasmask = True
					wbi\Vest = True
					psp\Kevlar = 150
					psp\Helmet = 150
					
					Select Rand(0,3)
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
								;Level 5 Key Card
							it = CreateItem("Level 5 Key Card", "key5", 1, 1, 1)
							it\Picked = True
							it\Dropped = -1
							it\itemtemplate\found=True
							Inventory[3] = it
							HideEntity(it\collider)
							EntityType (it\collider, HIT_ITEM)
							EntityParent(it\collider, 0)
							ItemAmount = ItemAmount + 1
							;NAV
							it = CreateItem("S-NAV 310 Navigator", "nav", 1, 1, 1)
							it\Picked = True
							it\Dropped = -1
							it\itemtemplate\found=True
							it\state = 1000
							Inventory[4] = it
							HideEntity(it\collider)
							EntityType (it\collider, HIT_ITEM)
							EntityParent(it\collider, 0)
							ItemAmount = ItemAmount + 1
							
							g_I\Weapon_InSlot[GunSlot1] = "p90"
							g_I\Weapon_InSlot[GunSlot2] = "fiveseven"
							g_I\Weapon_InSlot[GunSlot3] = "knife"
							g_I\Weapon_CurrSlot% = GunSlot1+1
							g_I\HoldingGun = GUN_P90
							For g = Each Guns
								g\PickedUpRedDot = True
								g\PickedUpSuppressor = True
								Select g\ID
									Case GUN_P90
										g\CurrReloadAmmo = 150
										g\HasRedDot = True
										g\HasToggledRedDot = True
										AddRedDot(g)
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
							;Level 5 Key Card
							it = CreateItem("Level 5 Key Card", "key5", 1, 1, 1)
							it\Picked = True
							it\Dropped = -1
							it\itemtemplate\found=True
							Inventory[3] = it
							HideEntity(it\collider)
							EntityType (it\collider, HIT_ITEM)
							EntityParent(it\collider, 0)
							ItemAmount = ItemAmount + 1
							;NAV
							it = CreateItem("S-NAV 310 Navigator", "nav", 1, 1, 1)
							it\Picked = True
							it\Dropped = -1
							it\itemtemplate\found=True
							it\state = 1000
							Inventory[4] = it
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
								g\PickedUpVerticalGrip = True
								g\PickedUpFoldingStock = True
								Select g\ID
									Case GUN_MP5K
										g\CurrReloadAmmo = 90
										g\HasVerticalGrip = True
										g\HasFoldingStock = True
										g\HasToggledFoldingStock = True
										g\HasToggledVerticalGrip = True
										AddVerticalGrip(g)
										AddFoldingStock(g)
									Case GUN_BERETTA
										g\CurrReloadAmmo = 45
								End Select
							Next
						Case 2
							;HK416
							it = CreateItem("H&K 416", "hk416", 1, 1, 1)
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
							;Level 5 Key Card
							it = CreateItem("Level 5 Key Card", "key5", 1, 1, 1)
							it\Picked = True
							it\Dropped = -1
							it\itemtemplate\found=True
							Inventory[3] = it
							HideEntity(it\collider)
							EntityType (it\collider, HIT_ITEM)
							EntityParent(it\collider, 0)
							ItemAmount = ItemAmount + 1
							;NAV
							it = CreateItem("S-NAV 310 Navigator", "nav", 1, 1, 1)
							it\Picked = True
							it\Dropped = -1
							it\itemtemplate\found=True
							it\state = 1000
							Inventory[4] = it
							HideEntity(it\collider)
							EntityType (it\collider, HIT_ITEM)
							EntityParent(it\collider, 0)
							ItemAmount = ItemAmount + 1
							
							g_I\Weapon_InSlot[GunSlot1] = "hk416"
							g_I\Weapon_InSlot[GunSlot2] = "usp"
							g_I\Weapon_InSlot[GunSlot3] = "knife"
							g_I\Weapon_CurrSlot% = GunSlot1+1
							g_I\HoldingGun = GUN_HK416
							For g = Each Guns
								g\PickedUpSuppressor = True
								Select g\ID
									Case GUN_HK416
										g\CurrReloadAmmo = 90
									Case GUN_USP
										g\CurrReloadAmmo = 36
								End Select
							Next
						Case 3
							;SPAS
							it = CreateItem("Franchi SPAS-12", "spas12", 1, 1, 1)
							it\Picked = True
							it\Dropped = -1
							it\itemtemplate\found=True
							Inventory[0] = it
							HideEntity(it\collider)
							EntityType (it\collider, HIT_ITEM)
							EntityParent(it\collider, 0)
							ItemAmount = ItemAmount + 1
							;P30L
							it = CreateItem("H&K P30L", "p30l", 1, 1, 1)
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
							;Level 5 Key Card
							it = CreateItem("Level 5 Key Card", "key5", 1, 1, 1)
							it\Picked = True
							it\Dropped = -1
							it\itemtemplate\found=True
							Inventory[3] = it
							HideEntity(it\collider)
							EntityType (it\collider, HIT_ITEM)
							EntityParent(it\collider, 0)
							ItemAmount = ItemAmount + 1
							;NAV
							it = CreateItem("S-NAV 310 Navigator", "nav", 1, 1, 1)
							it\Picked = True
							it\Dropped = -1
							it\itemtemplate\found=True
							it\state = 1000
							Inventory[4] = it
							HideEntity(it\collider)
							EntityType (it\collider, HIT_ITEM)
							EntityParent(it\collider, 0)
							ItemAmount = ItemAmount + 1
							;Grenade
							it = CreateItem("M61 Grenade", "grenade", 1, 1, 1)
							it\Picked = True
							it\Dropped = -1
							it\itemtemplate\found=True
							Inventory[5] = it
							HideEntity(it\collider)
							EntityType (it\collider, HIT_ITEM)
							EntityParent(it\collider, 0)
							ItemAmount = ItemAmount + 1
							
							g_I\Weapon_InSlot[GunSlot1] = "spas12"
							g_I\Weapon_InSlot[GunSlot2] = "p30l"
							g_I\Weapon_InSlot[GunSlot3] = "knife"
							g_I\Weapon_CurrSlot% = GunSlot1+1
							g_I\HoldingGun = 16
							For g = Each Guns
								Select g\ID
									Case GUN_SPAS12
										g\CurrReloadAmmo = 30
									Case GUN_P30L
										g\CurrReloadAmmo = 51
								End Select
							Next
					End Select
					
				EndIf
				;EndIf
			Else
				CanSave = True
			EndIf
			
			If e\EventState[0] = 1 Then
				If e\room\RoomDoors[1]\open Then
					e\SoundCHN = PlaySound2(LoadTempSound("SFX\Door\BigDoorStartsOpenning.ogg"),Camera,e\room\RoomDoors[1]\frameobj,10,2.5)
					StopChannel(e\room\RoomDoors[1]\SoundCHN)
					e\EventState[0] = 2
					;EndTask(TASK_OPENDOOR)
				ElseIf SelectedItem <> Null And Left(SelectedItem\itemtemplate\tempname, 3) = "key" Then
					;EndTask(TASK_CLICKKEYCARD)
				EndIf
			ElseIf e\EventState[0] = 2 Then
				UpdateSoundOrigin(e\SoundCHN,Camera,e\room\RoomDoors[1]\frameobj,10,2.5)
				e\room\RoomDoors[1]\openstate = 0.01
				e\room\RoomDoors[1]\open = False
			EndIf
			If e\EventState[0] = 2 And (Not ChannelPlaying(e\SoundCHN)) Then
				If HUDenabled And psp\IsShowingHUD Then
					CreateSplashText(GetLocalString("Singleplayer","ntf_chapter_1"),opt\GraphicWidth/2,opt\GraphicHeight/2,100,5,Font_Default_Large,True,False)
				EndIf
				OpenCloseDoor(e\room\RoomDoors[1])
				e\EventState[0] = 3
				PlayAnnouncement("SFX\Character\MTF\Announc.ogg")
				If opt\MusicVol > 0 Then
					e\SoundCHN2 = StreamSound_Strict("SFX\Music\Misc\NTF_Arrival.ogg", opt\MusicVol/2, 0)
					e\SoundCHN2_isStream = True
				EndIf
				e\room\NPC[0]\IdleTimer = 70*15
				e\room\NPC[0]\State = MTF_SEARCH
				e\room\NPC[1]\IdleTimer = 70*17
				e\room\NPC[1]\State = MTF_SEARCH
				mtfd\Enabled = True
				SaveGame(SavePath + CurrSave\Name + "\")
			EndIf
			
			LightVolume = Min(LightVolume+0.05,TempLightVolume*1.25)
		EndIf
		
		If e\EventState[0] = 3 And (Not IsStreamPlaying_Strict(IntercomStreamCHN)) Then
			BeginTask(TASK_CLASSIC_CORE)
			e\EventState[0] = 4
		ElseIf e\EventState[0] = 4 And e\room\dist >= 25.0 Then
			If SelectedDifficulty\SaveType = SAVEONSCREENS Then
				CreateHintMsg(GetLocalString("Menu","hint_saveonscreens"))
			ElseIf SelectedDifficulty\SaveType = SAVEONQUIT Then
				CreateHintMsg(GetLocalString("Menu","hint_saveonquit"))
			Else
				CreateHintMsg(GetLocalStringR("Menu","hint_saveanywhere",KeyName[KEY_SAVE]))
			EndIf
			e\EventState[0] = 5
		EndIf
		
		If e\room\RoomDoors[1]\openstate > 0.01 Then
			If PlayerRoom = e\room Lor IsRoomAdjacent(e\room,PlayerRoom) Then
				UpdateAlarmRotor(e\room\AlarmRotor[0],4)
				ShowEntity e\room\AlarmRotor[0]
			Else
				HideEntity e\room\AlarmRotor[0]
			EndIf
		Else
			HideEntity e\room\AlarmRotor[0]
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D