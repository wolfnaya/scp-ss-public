
Function FillRoom_Room1_Intro(r.Rooms)
	Local it.Items,ne.NewElevator,d.Doors
	
	r\Objects[1] = CreatePivot()
	PositionEntity(r\Objects[1], r\x +16.0 * RoomScale, r\y +20.0 * RoomScale, r\z -2416.0 * RoomScale)
	EntityParent(r\Objects[1], r\obj)
	
	r\Objects[4] = CreatePivot()
	PositionEntity(r\Objects[4], r\x -1.0 * RoomScale, r\y +222.0 * RoomScale, r\z -3603.0 * RoomScale)
	EntityParent(r\Objects[4], r\obj)
	
	r\Objects[2] = CreatePivot()
	PositionEntity(r\Objects[2], r\x -2.0 * RoomScale, r\y +597.0 * RoomScale, r\z -3057.0 * RoomScale)
	EntityParent(r\Objects[2], r\obj)
	
	r\Objects[3] = CreatePivot()
	PositionEntity(r\Objects[3], r\x -2.0 * RoomScale, r\y +837.0 * RoomScale, r\z -2933.0 * RoomScale)
	EntityParent(r\Objects[3], r\obj)
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x -2 * RoomScale, r\y +7.0 * RoomScale, r\z -1390.0 * RoomScale)
	EntityParent(r\Objects[0], r\obj)
	
	r\Objects[5] = CreatePivot()
	PositionEntity(r\Objects[5], r\x +3.0 * RoomScale, r\y +938.0 * RoomScale, r\z -2925.0 * RoomScale)
	EntityParent(r\Objects[5], r\obj)
	
	r\Objects[8] = CreatePivot()
	PositionEntity(r\Objects[8], r\x -1262.0 * RoomScale, r\y -5.0 * RoomScale, r\z -1924.0 * RoomScale)
	EntityParent(r\Objects[8], r\obj)
	
	r\Objects[6] =	LoadAnimMesh_Strict("GFX\Map\props\Intro_Pipe.b3d")
	ScaleEntity r\Objects[6], RoomScale*3,RoomScale*3,RoomScale*3
	PositionEntity(r\Objects[6],r\x -2*RoomScale,r\y+837*RoomScale,r\z-2653*RoomScale)
	RotateEntity(r\Objects[6],EntityPitch(r\Objects[6])+360,EntityYaw(r\Objects[6])+90,EntityRoll(r\Objects[6])+450)
	
	r\Objects[9] =	LoadAnimMesh_Strict("GFX\Map\props\Tram.b3d")
	ScaleEntity r\Objects[9], RoomScale*3,RoomScale*3,RoomScale*3
	PositionEntity(r\Objects[9],r\x -1718*RoomScale,r\y-81*RoomScale,r\z)
	
	r\Objects[7] = r\obj
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x-2.0*RoomScale,r\y-6.0*RoomScale,r\z-3697.0*RoomScale,0,r,False,False)
	r\RoomDoors[0]\open = False
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x-2.0*RoomScale,r\y-6.0*RoomScale,r\z-1202.0*RoomScale,0,r,False,DOOR_WINDOWED)
	r\RoomDoors[1]\open = False
	
	CreateDarkSprite(r, 10)
	
	Local ElevatorOBJ = LoadRMesh("GFX\map\Elevators\elevator_cabin_4.rmesh",Null)
	HideEntity ElevatorOBJ
	r\Objects[11] = CopyEntity(ElevatorOBJ,r\obj)
	
	PositionEntity(r\Objects[11],2881,2560,1562)
	EntityType r\Objects[11],HIT_MAP
	EntityPickMode r\Objects[11],2
	
	r\RoomDoors[2] = CreateDoor(r\zone,r\x +2561.0 * RoomScale, r\y+2560*RoomScale, r\z+1562*RoomScale, 90, r, True, 5, False, "", 2)
	r\RoomDoors[2]\DisableWaypoint = True
	
	ne = CreateNewElevator(r\Objects[11],3,r\RoomDoors[2],2,r,-10.0,0.0,2560.0)
	ne\floorlocked[0] = True
	
	r\Objects[12] = CopyEntity(ElevatorOBJ,r\obj)
	
	PositionEntity(r\Objects[12],735,2560,7698)
	RotateEntity r\Objects[12],0,90,0
	EntityType r\Objects[12],HIT_MAP
	EntityPickMode r\Objects[12],2
	
	r\RoomDoors[3] = CreateDoor(r\zone,r\x +735.0 * RoomScale, r\y+2560*RoomScale, r\z+7378*RoomScale, 0, r, True, 5, False, "", 3)
	r\RoomDoors[3]\DisableWaypoint = True
	
	ne = CreateNewElevator(r\Objects[12],3,r\RoomDoors[3],3,r,-10.0,0.0,2560.0)
	Local i
	For i = 0 To 2
		ne\floorlocked[i] = True
	Next
	
	d = CreateDoor(r\zone,r\x + 735.0 * RoomScale, r\y+2560*RoomScale, r\z+4628*RoomScale, 0, r, False, False)
	
	d = CreateDoor(r\zone,r\x + 735.0 * RoomScale, r\y+2560*RoomScale, r\z+6693*RoomScale, 0, r, False, False)
	
	d = CreateDoor(r\zone,r\x - 296.0 * RoomScale, r\y+2560*RoomScale, r\z+5661*RoomScale, -90, r, False, False)
	
	d = CreateDoor(r\zone,r\x + 735.0 * RoomScale, r\y+2560*RoomScale, r\z+2588*RoomScale, 0, r, False, False)
	
	d = CreateDoor(r\zone,r\x + 796.0 * RoomScale, r\y+2560*RoomScale, r\z+1564*RoomScale, 90, r, True, False)
	
	d = CreateDoor(r\zone,r\x + 1880.0 * RoomScale, r\y+2560*RoomScale, r\z+1564*RoomScale, 90, r, False, False, KEY_CARD_2)
	
	d = CreateDoor(r\zone,r\x +1847.0 * RoomScale, r\y, r\z+1562*RoomScale, 90, r, False, False)
	
	d = CreateDoor(r\zone,r\x - 4.0 * RoomScale, r\y, r\z+794*RoomScale, 0, r, False, False)
	
	it = CreateItem("Level 2 Key Card", "key2", r\x+199.0*RoomScale, r\y+2663.0*RoomScale, r\z+1417.0*RoomScale)
	EntityParent(it\collider, r\Objects[7])
	
	it = CreateItem("Ballistic Helmet", "helmet", r\x+199.0*RoomScale, r\y+2760.0*RoomScale, r\z+1417.0*RoomScale)
	EntityParent(it\collider, r\Objects[7])
	
	it = CreateItem("H&K USP", "usp", r\x+196.0*RoomScale, r\y+2663.0*RoomScale, r\z+1329.0*RoomScale)
	EntityParent(it\Collider, r\Objects[7])
	
End Function

Function UpdateEvent_Room1_Intro(e.Events)
	Local de.Decals,ne.NewElevator,b
	
	If PlayerRoom = e\room
		
		Curr106\Idle = True
		Curr173\Idle = True
		PositionEntity Curr173\Collider,0,-2000,0
		
		If e\EventState[2] = 0 Then
			CreateSplashText("SCP - SECURITY STORIES",opt\GraphicWidth/2,opt\GraphicHeight/2,100,20,Font_Menu_Medium,True,False)
			psp\IsShowingHUD = False
			wbi\Vest = True
			psp\Kevlar = 100
			EntityAlpha e\room\Objects[10],1
			e\EventState[2] = 1
		EndIf
		
		If e\EventState[2] <> 0 Then
			e\EventState[2] = e\EventState[2] + fps\Factor[0]
		EndIf
		
		If e\EventState[2] >= 70*6 Then
			e\EventState[3] = e\EventState[3] - (0.01*fps\Factor[0])
			EntityAlpha e\room\Objects[10],Min(1,e\EventState[3]+3)
		EndIf
		
		If e\EventState[2] >= 70*10.0 And e\EventState[2] < 70*10.1 Then
			If HUDenabled Then
				psp\IsShowingHUD = True
			EndIf
			If (Not TaskExists(TASK_GO_TO_TRAM)) Then
				If HUDenabled And psp\IsShowingHUD Then
					CreateSplashText(GetLocalString("Singleplayer","chapter_0"),opt\GraphicWidth/2,opt\GraphicHeight/2,100,5,Font_Default_Large,True,False)
				EndIf
				BeginTask(TASK_GO_TO_TRAM)
			EndIf
			ElseIf e\EventState[2] < 70*10.0 Then
				psp\IsShowingHUD = False
			EndIf
			
			CanPlayerUseGuns% = False
			
			mpl\HasNTFGasmask = False
			
			If EntityY(Collider)<3000.0*RoomScale ; ~ 3rd floor
				
				If (Not PlayerInNewElevator)
					PositionEntity e\room\RoomDoors[b]\frameobj,EntityX(e\room\RoomDoors[b]\frameobj),2560.0*RoomScale,EntityZ(e\room\RoomDoors[b]\frameobj)
					PositionEntity e\room\RoomDoors[b]\obj,EntityX(e\room\RoomDoors[b]\obj),2560.0*RoomScale,EntityZ(e\room\RoomDoors[b]\obj)
					PositionEntity e\room\RoomDoors[b]\obj2,EntityX(e\room\RoomDoors[b]\obj2),2560.0*RoomScale,EntityZ(e\room\RoomDoors[b]\obj2)
					PositionEntity e\room\RoomDoors[b]\buttons[0],EntityX(e\room\RoomDoors[b]\buttons[0]),2560.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[b]\buttons[0])
					PositionEntity e\room\RoomDoors[b]\buttons[1],EntityX(e\room\RoomDoors[b]\buttons[1]),2560.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[b]\buttons[1])
					For ne = Each NewElevator
						If ne\door = e\room\RoomDoors[b]
							If ne\currfloor = 2 And ne\state = 0.0
								e\room\RoomDoors[b]\open = True
							Else
								e\room\RoomDoors[b]\open = False
							EndIf
						EndIf
					Next
				EndIf
				
			Else ; ~ Exit
				
				If (Not PlayerInNewElevator)
					PositionEntity e\room\RoomDoors[b]\frameobj,EntityX(e\room\RoomDoors[b]\frameobj),0.0,EntityZ(e\room\RoomDoors[b]\frameobj)
					PositionEntity e\room\RoomDoors[b]\obj,EntityX(e\room\RoomDoors[b]\obj),0.0,EntityZ(e\room\RoomDoors[b]\obj)
					PositionEntity e\room\RoomDoors[b]\obj2,EntityX(e\room\RoomDoors[b]\obj2),0.0,EntityZ(e\room\RoomDoors[b]\obj2)
					PositionEntity e\room\RoomDoors[b]\buttons[0],EntityX(e\room\RoomDoors[b]\buttons[0]),0.0+0.6,EntityZ(e\room\RoomDoors[b]\buttons[0])
					PositionEntity e\room\RoomDoors[b]\buttons[1],EntityX(e\room\RoomDoors[b]\buttons[1]),0.0+0.7,EntityZ(e\room\RoomDoors[b]\buttons[1])
					For ne = Each NewElevator
						If ne\door = e\room\RoomDoors[b]
							If ne\currfloor = 3 And ne\state = 0.0
								e\room\RoomDoors[b]\open = True
							Else
								e\room\RoomDoors[b]\open = False
							EndIf
						EndIf
					Next
				EndIf
			EndIf
			
			If e\room\NPC[0] = Null Then
				e\room\NPC[0]=CreateNPC(NPCtypeD, EntityX(e\room\Objects[8],True), 0.5, EntityZ(e\room\Objects[8],True))
				e\room\NPC[0]\texture = "GFX\npcs\Humans\Personnel\scientist.jpg"
				Local tex = LoadTexture_Strict(e\room\NPC[0]\texture, 1, 2)
				TextureBlend(tex,5)
				EntityTexture(e\room\NPC[0]\obj, tex)
				DeleteSingleTextureEntryFromCache tex
				RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\obj)+90,0, True
				e\room\NPC[0]\State = 0
			EndIf
			
			CameraFogRange Camera, 5,30
			CameraFogColor (Camera,200,200,200)
			CameraClsColor (Camera,200,200,200)
			CameraRange(Camera, 0.005, 100)
			HideEntity Overlay[0]
			ShowEntity e\room\Objects[6]
			ShowEntity e\room\Objects[9]
			
			Animate2(e\room\Objects[9], AnimTime(e\room\Objects[9]), 1.0, 251.0, 0.2,False)
			
			If e\EventState[1] <> 1.0 Then
				If e\EventState[2] >= 70*8 Then
					ShouldPlay = MUS_INTRODUCTION; ~ Intro Music
				Else
					ShouldPlay = MUS_NULL
					If e\Sound = 0 Then
						e\Sound = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Introduction.ogg"))
					EndIf
				EndIf
			ElseIf e\EventState[1] = 1.0
				ShouldPlay = MUS_CHASE_106; ~ SCP-106 Music
			EndIf
			
			If e\EventState[0] = 0 Then
				If EntityDistanceSquared(e\room\Objects[0],Collider) < PowTwo(0.5) Then
					PointEntity Collider, e\room\Objects[1]
					IsZombie = True
					BlurTimer = 0
					ForceMove = 1.0
					psp\NoMove = False
					psp\NoRotation = True
					e\EventState[0] = 0.5
					
					If TaskExists(TASK_GO_TO_TRAM) Then
						EndTask(TASK_GO_TO_TRAM)
					EndIf
					
				EndIf
			ElseIf e\EventState[0] = 0.5 Then
				User_Camera_Pitch = CurveAngle(DeltaPitch(Camera, e\room\Objects[4]), User_Camera_Pitch, 15.0)
				e\EventState[1] = 1.0
				If EntityDistanceSquared(e\room\Objects[1],Collider) < PowTwo(0.2) Then
					
					ForceMove = 0.0
					BlurTimer = 8
					IsZombie = True
					psp\NoMove = True
					psp\NoRotation = True
					
					User_Camera_Pitch = CurveAngle(DeltaPitch(Camera, e\room\Objects[2]), User_Camera_Pitch, 15.0)
					
					Local PrevAnim# = AnimTime(e\room\Objects[6])
					
					Animate2(e\room\Objects[6], AnimTime(e\room\Objects[6]), 1.0, 140.0, 0.31,False)
					
					If AnimTime(e\room\Objects[6]) >= 1.0 And PrevAnim < 1.0 Then
						PlayNewDialogue(0,%01)
					EndIf
					
					PlaySound_Strict(LoadTempSound("SFX\SCP\106\Laugh.ogg"))
					
					de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[5], True), EntityY(e\room\Objects[5], True) - 0.01, EntityZ(e\room\Objects[5], True), -90, Rand(360), 0)
					de\Size = 0.1 : de\SizeChange = 0.01 : de\MaxSize = 1.75 : EntityAlpha(de\obj, 1.0)
					
					e\EventState[0] = 1
				EndIf
			ElseIf e\EventState[0] >= 1 And e\EventState[0] < 2 Then
				
				e\EventState[0] = e\EventState[0] + (fps\Factor[0] / 70.0)/5
				
				User_Camera_Pitch = CurveAngle(DeltaPitch(Camera, e\room\Objects[3]), User_Camera_Pitch, 15.0)
				Animate2(e\room\Objects[6], AnimTime(e\room\Objects[6]), 1.0, 140.0, 0.31,False)
				
				If e\EventState[0] >=2 Then
					e\EventState[0] = 3
				EndIf
			Else
				
				psp\NoMove = True
				psp\NoRotation = True
				User_Camera_Pitch = CurveAngle(DeltaPitch(Camera, e\room\Objects[3]), User_Camera_Pitch, 15.0)
				
				If KillTimer >= 0 And FallTimer >= 0 Then
					FallTimer = Min(-1, FallTimer)
					PositionEntity(Head, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True), True)
					ResetEntity (Head)
					RotateEntity(Head, 0, EntityYaw(Camera) + Rand(-45, 45), 0)
					mpl\DamageTimer = 70.0*1.0
					If wbi\Helmet Then
						wbi\Helmet = False
						psp\Helmet = 0
					EndIf
					psp\Health = 70
				EndIf
				
				e\EventState[0] = e\EventState[0] + (0.01*fps\Factor[0])
				
				BlurTimer = 10
				LightFlash = 0.7
				
				EntityAlpha e\room\Objects[10],Min(e\EventState[0]-3,1)
				PointEntity Camera,e\room\Objects[3],EntityRoll(Camera)
				Animate2(e\room\Objects[6], AnimTime(e\room\Objects[6]), 1.0, 140.0, 0.31,False)
				
				If e\EventState[0] > 4.05 Then
					psp\IsShowingHUD = False
					Local r.Rooms
					For r = Each Rooms
						If r\RoomTemplate\Name = "room1_start" Then
							PlayerRoom = r
							TeleportEntity(Collider, r\x,r\y,r\z)
							Exit
						EndIf
					Next
					DropSpeed = 0
					FallTimer = 0
					psp\NoMove = False
					psp\NoRotation = False
					IsZombie = False
					FlushKeys()
					FlushMouse()
					FlushJoy()
					FreeEntity r\Objects[7]
					SaveGame(SavePath + CurrSave\Name + "\")
					Return
				EndIf
				
			EndIf
		Else
			HideEntity(e\room\Objects[10])
			
			CameraRange(Camera, 0.01, CameraFogFar)
			CameraFogRange (Camera, CameraFogNear, CameraFogFar)
			CameraFogColor (Camera,200,200,200)
			CameraRange(Camera, 0.005, 100)
			ShowEntity Overlay[0]
		EndIf
		
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D