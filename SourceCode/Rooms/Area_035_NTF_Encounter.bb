
Function FillRoom_Area_035_NTF_Encounter(r.Rooms)
	Local de.Decals,n.NPCs
	
	de.Decals=CreateDecal(DECAL_BLOODPOOL,r\x-896*RoomScale,r\y+5*RoomScale,r\z,90,180,0)
	EntityParent de\obj,r\obj
	
	r\Objects[0] = CreatePivot()
	PositionEntity r\Objects[0],r\x-896*RoomScale,r\y+266*RoomScale,r\z
	EntityParent r\Objects[0],r\obj
	
	r\Objects[3] =	CreatePivot()
	PositionEntity(r\Objects[3], r\x,r\y,r\z, True)
	EntityParent r\Objects[3],r\obj
	
	r\Objects[4] =	CreatePivot()
	PositionEntity(r\Objects[4], r\x-134*RoomScale,r\y,r\z+206*RoomScale, True)
	EntityParent r\Objects[4],r\obj
	
	r\Objects[5] =	CreatePivot()
	PositionEntity(r\Objects[5], r\x-134*RoomScale,r\y,r\z-206*RoomScale, True)
	EntityParent r\Objects[5],r\obj
	
	r\Objects[6] =	CreatePivot()
	PositionEntity(r\Objects[6], r\x+224*RoomScale,r\y+240*RoomScale,r\z, True)
	EntityParent r\Objects[6],r\obj
	
	r\Objects[7] =	LoadAnimMesh_Strict("GFX\Npcs\scps\035\035_trailer.b3d")
	ScaleEntity r\Objects[7], RoomScale*0.375*10,RoomScale*0.375*10,RoomScale*0.375*10
	RotateEntity r\Objects[7],0,r\angle+180,0
	PositionEntity(r\Objects[7],r\x-896*RoomScale,r\y+1*RoomScale,r\z+80*RoomScale,True)
	HideEntity r\Objects[7]
	
End Function

Function UpdateEvent_Area_035_NTF_Encounter(e.Events)
	Local n.NPCs,i%,pvt%,tex
	
	If PlayerRoom = e\room Then
		
		ShouldPlay = MUS_NULL
		
		If e\room\NPC[1] = Null Then
			e\room\NPC[1] = CreateNPC(NPCtypeD,e\room\x-1204*RoomScale,e\room\y+1*RoomScale,e\room\z+206.0*RoomScale)
			e\room\NPC[1]\texture = "GFX\npcs\Multiplayer\Zombies\janitor_zombie.jpg"
			tex = LoadTexture_Strict(e\room\NPC[1]\texture, 1, 2)
			TextureBlend(tex,5)
			EntityTexture(e\room\NPC[1]\obj, tex)
			DeleteSingleTextureEntryFromCache tex
		Else
			SetNPCFrame(e\room\NPC[1],566)
			e\room\NPC[1]\IsDead = True
			e\room\NPC[1]\State = STATE_SCRIPT
		EndIf
		If e\room\NPC[2] = Null Then
			e\room\NPC[2] = CreateNPC(NPCtypeD,EntityX(e\room\Objects[0],True),EntityY(e\room\Objects[0],True),EntityZ(e\room\Objects[0],True))
			e\room\NPC[2]\texture = "GFX\npcs\Humans\personnel\body3.jpg"
			tex = LoadTexture_Strict(e\room\NPC[2]\texture, 1, 2)
			TextureBlend(tex,5)
			EntityTexture(e\room\NPC[2]\obj, tex)
			DeleteSingleTextureEntryFromCache tex
		Else
			SetNPCFrame(e\room\NPC[2],567)
			e\room\NPC[2]\IsDead = True
			e\room\NPC[2]\State = STATE_SCRIPT
		EndIf
		
		If I_035\Possessed Then
			
			CanPlayerUseGuns% = False
			InvOpen = False
			CanSave = False
			
			If e\EventState[0] < 7 Then
				If e\EventState[0] < 3 Then
					HideEntity Collider
				EndIf
				Crouch = True
				psp\NoMove = True
				psp\NoRotation = True
			EndIf
			
			Select e\EventState[0]
				Case 0
					BlurTimer = 0
					PositionEntity Collider, EntityX(e\room\Objects[0], True), EntityY(e\room\Objects[0], True), EntityZ(e\room\Objects[0], True)
					RotateEntity Collider, 0,270,0
					RotateEntity Camera,60,0,0
					User_Camera_Pitch = 60
					CrouchState = 1.0
					ResetEntity Collider
					DropSpeed = 0
					psp\Health = 999
					psp\Kevlar = 0
					hds\Wearing = False
					e\EventState[0] = 1
					If (Not ChannelPlaying(e\SoundCHN)) Then
						e\SoundCHN = PlaySound_Strict(LoadTempSound("SFX\SCP\035\Impatient.ogg"))
					EndIf
					
					n = CreateNPC(NPCtypeNTF,EntityX(e\room\Objects[3],True),EntityY(e\room\Objects[3],True)+0.2,EntityZ(e\room\Objects[3],True))
					n\State = STATE_SCRIPT
					AnimateNPC(n,79,195,0.3,True)
					PointEntity n\Collider,Collider
					
					n\texture = "GFX\npcs\Humans\MTF\NTF\NineTailedFox_commander.png"
					tex = LoadTexture_Strict(n\texture, 1, 2)
					TextureBlend(tex,5)
					EntityTexture(n\obj, tex)
					DeleteSingleTextureEntryFromCache tex
					
					n = CreateNPC(NPCtypeNTF,EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True)+0.2,EntityZ(e\room\Objects[4],True))
					n\State = STATE_SCRIPT
					AnimateNPC(n,79,195,0.3,True)
					PointEntity n\Collider,Collider
					
					n\texture = "GFX\npcs\Humans\MTF\NTF\NineTailedFox_medic.png"
					tex = LoadTexture_Strict(n\texture, 1, 2)
					TextureBlend(tex,5)
					EntityTexture(n\obj, tex)
					DeleteSingleTextureEntryFromCache tex
					
					n = CreateNPC(NPCtypeNTF,EntityX(e\room\Objects[5],True),EntityY(e\room\Objects[5],True)+0.2,EntityZ(e\room\Objects[5],True))
					n\State = STATE_SCRIPT
					AnimateNPC(n,79,195,0.3,True)
					PointEntity n\Collider,Collider
					
				Case 1
					If EntityY(Camera) < EntityY(e\room\Objects[0], True) Then
						mpl\HasNTFGasmask = False
						BlurTimer = 1200
						e\EventState[0] = 2
					EndIf
				Case 3
					If EntityY(Collider) < EntityY(e\room\Objects[0], True) Then
						e\EventState[0] = 4
					EndIf
				Case 7
					e\EventState[0] = 8
			End Select
			
			If e\EventState[0] >= 2.0 And e\EventState[0] < 3.0 Then
				PositionEntity Collider, EntityX(e\room\Objects[0], True), EntityY(e\room\Objects[0], True), EntityZ(e\room\Objects[0], True)
				ResetEntity Collider
				e\EventState[0] = e\EventState[0] + (fps\Factor[0] / 70.0)
				User_Camera_Pitch = 60
				If e\EventState[0] >= 3.0 Then
					e\EventState[0] = 3
					ShowEntity Collider
				EndIf
				DropSpeed = 0
			ElseIf e\EventState[0] >= 4.0 And e\EventState[0] < 6.0 Then
				e\EventState[0] = e\EventState[0] + (fps\Factor[0] / 210.0)
				If e\EventState[0] >= 5.0 Then
					User_Camera_Pitch = -60.0 * (e\EventState[0] - 6.0)
					CrouchState = (6.0 - e\EventState[0])
					RotateEntity Camera, EntityPitch(Camera), EntityYaw(Camera), 20.0 - Abs(Min((5.5 - e\EventState[0]) * 40.0, 20.0))
					If e\EventState[0] >= 6.0 Then
						ResetInput()
						Crouch = False
						
						psp\NoRotation = False
						
						e\EventState[0] = 7
					EndIf
				EndIf
			EndIf
			
			If e\EventState[0] >= 7 Then
				
				pvt% = CreatePivot()
				PositionEntity pvt, EntityX(Camera), EntityY(e\room\Objects[6],True)-0.05, EntityZ(Camera)
				PointEntity(pvt, e\room\Objects[6])
				RotateEntity(Collider, EntityPitch(Collider), CurveAngle(EntityYaw(pvt), EntityYaw(Collider), 80-(e\EventState[2]/200.0)), 0)
				
				TurnEntity(pvt, 70, 0, 0)
				User_Camera_Pitch = CurveAngle(EntityPitch(pvt)+25, User_Camera_Pitch + 90.0, 80-(e\EventState[2]/200.0))
				User_Camera_Pitch=User_Camera_Pitch-90
				
				e\EventState[1] = e\EventState[1] + fps\Factor[0]
			EndIf
			
			If e\EventState[1] > 13.5*70 Then
				e\EventState[0] = 9
				BlinkTimer = -10
				psp\IsShowingHUD = False
				If pvt <> 0 Then
					FreeEntity pvt
				EndIf
			EndIf
			
			If e\EventState[1] > 14*70 Then
				SelectedEnding = "scp-035"
				RemoveEvent(e)
				Return
			EndIf
			
		Else
			
			If clm\NTFMode Then
				
				SecondaryLightOn = True
				CameraFogRange Camera, 5,60
				CameraRange(Camera, 0.01, 60)
				
				Select e\EventState[0]
					Case 0
						BlurTimer = 0
						PositionEntity Collider, EntityX(e\room\Objects[3], True), EntityY(e\room\Objects[3], True), EntityZ(e\room\Objects[3], True)
						e\EventState[0] = 1
						If (Not ChannelPlaying(e\SoundCHN)) Then
							e\SoundCHN = PlaySound_Strict(LoadTempSound("SFX\SCP\035\Impatient.ogg"))
						EndIf
					Case 1
						psp\NoMove = True
						
						pvt% = CreatePivot()
						PositionEntity pvt, EntityX(Camera), EntityY(e\room\Objects[0],True)-0.05, EntityZ(Camera)
						PointEntity(pvt, e\room\Objects[0])
						RotateEntity(Collider, EntityPitch(Collider), CurveAngle(EntityYaw(pvt), EntityYaw(Collider), 80-(e\EventState[2]/200.0)), 0)
						
						TurnEntity(pvt, 70, 0, 0)
						User_Camera_Pitch = CurveAngle(EntityPitch(pvt)+25, User_Camera_Pitch + 90.0, 80-(e\EventState[2]/200.0))
						User_Camera_Pitch=User_Camera_Pitch-90
						
						e\EventState[1] = e\EventState[1] + fps\Factor[0]
				End Select
				
				If e\EventState[1] > 22.5*70 Then
					e\EventState[0] = 2
					If pvt <> 0 Then
						FreeEntity pvt
						BlinkTimer = -10
						e\room\NPC[0] = CreateNPC(NPCtype035,EntityX(e\room\Objects[0],True),EntityY(e\room\Objects[0],True)+1*RoomScale,EntityZ(e\room\Objects[0],True))
						e\room\NPC[0]\State = MP035_STATE_WANDER
					EndIf
					If e\room\Objects[7] <> 0 Then
						e\room\Objects[7] = FreeEntity_Strict(e\room\Objects[7])
					EndIf
				Else
					If e\room\Objects[7] <> 0 Then
						ShowEntity e\room\Objects[7]
						Animate2(e\room\Objects[7],AnimTime(e\room\Objects[7]),787,1158,0.3,False)
					EndIf
				EndIf
				
				If e\EventState[0] = 2
					psp\NoMove = False
					
				EndIf
				
			EndIf
			
		EndIf
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D