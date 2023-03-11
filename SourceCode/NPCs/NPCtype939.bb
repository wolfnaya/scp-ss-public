
; ~ Multiplayer SCP-939 constants
;[Block]
Const MP939_STATE_FREEZE = -1
Const MP939_STATE_WANDER = 0
Const MP939_STATE_DETECTED = 1
Const MP939_STATE_ATTACK = 2
Const MP939_STATE_VOCAL = 3
;[End Block]
		
Function CreateNPCtype939(n.NPCs)
	Local n2.NPCs
	Local temp#
	Local i
	
	Local amount939% = 0
	For n2.NPCs = Each NPCs
		If (n\NPCtype = n2\NPCtype) And (n<>n2)
			amount939% = amount939% + 1
		EndIf
	Next
	If amount939% = 0 Then i = 53
	If amount939% = 1 Then i = 89
	If amount939% = 2 Then i = 96
	n\NVName = "SCP-939-"+i
	
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.3
	EntityType n\Collider, HIT_PLAYER
	For n2.NPCs = Each NPCs
		If n\NPCtype = n2\NPCtype And n<>n2 Then
			n\obj = CopyEntity (n2\obj)
			Exit
		EndIf
	Next
	
	n\HP = 500
	
	If n\obj = 0 Then 
		n\obj = LoadAnimMesh_Strict("GFX\NPCs\scps\939\939.b3d")
		
		temp# = GetINIFloat("DATA\NPCs.ini", "SCP-939", "scale")/2.5
		ScaleEntity n\obj, temp, temp, temp		
	EndIf
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-939", "speed") / 100.0)
	
	n\CollRadius = 0.3
	
	CopyHitBoxes(n)
	
End Function

Function CreateNPCtype939MP(n.NPCs)
	
	n\CollRadius = 0.3
	n\Collider = CreatePivot()
	EntityRadius n\Collider, n\CollRadius
	EntityType n\Collider, HIT_NPC_MP
	
	n\obj = CopyEntity(mp_I\SCP939Model)
	
	Local temp# = GetINIFloat("DATA\NPCs.ini", "SCP-939", "scale")/2.5
	ScaleEntity n\obj, temp, temp, temp	
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-939", "speed") / 150.0)
	n\HP = 250
	n\PathTimer = 70*5
	n\NVName = "SCP-939"
	
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCtype939(n.NPCs)
	Local dist#,prevFrame#,temp%,angle#
	
	If PlayerRoom\RoomTemplate\Name <> "room3_storage" Then
		n\State = 66.0
	EndIf
	
	If n\HP <= 0
		n\IsDead = True
	EndIf
	
	; ~ State is set to 66 in the room3_storage-event if player isn't inside the room
	If n\State < 66.0 Then
		If (Not n\IsDead) Then
			Select n\State
				Case 0.0 ; ~ Idles
					;[Block]
					AnimateNPC(n, 290.0, 405.0, 0.1)
					;[End Block]
				Case 1.0
					;[Block]
					If n\Frame >= 644.0 And n\Frame < 683.0 Then
						n\CurrSpeed = CurveValue(n\Speed * 0.05, n\CurrSpeed, 10.0)
						AnimateNPC(n, 644.0, 683.0, 28.0 * n\CurrSpeed * 4.0, False)
						If n\Frame >= 682 Then SetNPCFrame(n, 175.0)
					Else
						n\CurrSpeed = CurveValue(0, n\CurrSpeed, 5.0)
						AnimateNPC(n, 175.0, 297.0, 0.22, False)
						If n\Frame >= 296.0 Then n\State = 2.0
					EndIf
					
					n\LastSeen = 0
					
					MoveEntity(n\Collider, 0.0, 0.0, n\CurrSpeed * fps\Factor[0])						
					;[End Block]
				Case 2.0 ; ~ Walking
					;[Block]
					n\State2 = Max(n\State2, n\PrevState - 3)
					
					dist = EntityDistanceSquared(n\Collider, PlayerRoom\Objects[n\State2])
					
					n\CurrSpeed = CurveValue(n\Speed * 0.3 * Min(Sqr(dist), 1.0), n\CurrSpeed, 10.0)
					MoveEntity(n\Collider, 0.0, 0.0, n\CurrSpeed * fps\Factor[0]) 
					
					prevFrame = n\Frame
					AnimateNPC(n, 644.0, 683.0, 28.0 * n\CurrSpeed)
					
					If (prevFrame < 664.0 And n\Frame >= 664.0) Lor (prevFrame > 673.0 And n\Frame < 654.0) Then
						PlaySound2(Step2SFX[Rand(6, 9)], Camera, n\Collider, 12.0)
						If Rand(10) = 1 Then
							temp = False
							If (Not n\SoundChn) Then 
								temp = True
							ElseIf (Not ChannelPlaying(n\SoundChn))
								temp = True
							EndIf
							If temp Then
								If n\Sound <> 0 Then 
									FreeSound_Strict(n\Sound) : n\Sound = 0
								EndIf
								n\Sound = LoadSound_Strict("SFX\SCP\939\" + (n\ID Mod 3) + "Lure" + Rand(1, 10) + ".ogg")
								n\SoundChn = PlaySound2(n\Sound, Camera, n\Collider)
							EndIf
						EndIf
					EndIf
					
					PointEntity(n\obj, PlayerRoom\Objects[n\State2])
					RotateEntity(n\Collider, 0.0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0.0)
					
					If dist < 0.16 Then
						n\State2 = n\State2 + 1.0
						If n\State2 > n\PrevState Then n\State2 = (n\PrevState - 3)
						n\State = 1.0
					EndIf
					;[End Block]
				Case 3.0 ; ~ Attack
					;[Block]
					If EntityVisible(Collider, n\Collider) Then
						If n\Sound2 = 0 Then n\Sound2 = LoadSound_Strict("SFX\General\Slash1.ogg")
						n\EnemyX = EntityX(Collider)
						n\EnemyZ = EntityZ(Collider)
						n\LastSeen = 70.0 * 1.0
					EndIf
					
					If n\LastSeen > 0 And (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
						prevFrame = n\Frame
						
						If n\Frame >= 18.0 And n\Frame < 68.0 Then
							n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
							AnimateNPC(n, 18.0, 68.0, 0.5, True)
							
							temp = False
							
							If prevFrame < 24.0 And n\Frame >= 24.0 Then
								temp = True
							ElseIf prevFrame < 57.0 And n\Frame >= 57.0
								temp = True
							EndIf
							
							If temp Then
								If DistanceSquared(n\EnemyX, EntityX(n\Collider), n\EnemyZ, EntityZ(n\Collider)) < 2.25 Then
									PlayNPCSound(n, n\Sound2)
									If (Not GodMode) Then
										If I_1033RU\HP = 0
											DamageSPPlayer(Rand(20,30))
										Else
											Damage1033RU(35 + 5 * SelectedDifficulty\AggressiveNPCs)
										EndIf
										BlurTimer = 500
									EndIf
								Else
									SetNPCFrame(n, 449.0)
								EndIf
							EndIf
							
							If (Not IsSPPlayerAlive()) Then 
								m_msg\DeathTxt = Chr(34) + "All four (4) escaped SCP-939 specimens have been captured and recontained successfully. "
								m_msg\DeathTxt = m_msg\DeathTxt + "They made quite a mess at Storage Area 6. A cleaning team has been dispatched." + Chr(34)
								Kill()
								If (Not GodMode) Then n\State = 5.0
							EndIf								
						Else
							If n\LastSeen = 70.0 * 1.0 Then 
								n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
								
								AnimateNPC(n, 449.0, 464.0, n\CurrSpeed * 6.0)
								
								If (prevFrame < 452.0 And n\Frame >= 452.0) Lor (prevFrame < 459.0 And n\Frame >= 459.0) Then
									PlaySound2(StepSFX(1, 1, Rand(0, 7)), Camera, n\Collider, 12.0)
								EndIf										
								
								If DistanceSquared(n\EnemyX, EntityX(n\Collider), n\EnemyZ, EntityZ(n\Collider)) < 1.0 Then ; ~ Player is visible
									SetNPCFrame(n, 18.0)
								EndIf
							Else
								n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
								AnimateNPC(n, 175.0, 297.0, n\CurrSpeed * 5.0)	
							EndIf
						EndIf
						
						angle = VectorYaw(n\EnemyX - EntityX(n\Collider), 0.0, n\EnemyZ - EntityZ(n\Collider))
						RotateEntity(n\Collider, 0.0, CurveAngle(angle, EntityYaw(n\Collider), 15.0), 0.0)									
						
						MoveEntity(n\Collider, 0.0, 0.0, n\CurrSpeed * fps\Factor[0])							
						
						n\LastSeen = n\LastSeen - fps\Factor[0]
					Else
						n\State = 2.0
					EndIf
					;[End Block]
				Case 5.0 ; ~ Finishes attack and goes to idle
					;[Block]
					If n\Frame < 68.0 Then
						AnimateNPC(n, 18.0, 68.0, 0.5, False)
					Else
						AnimateNPC(n, 464.0, 473.0, 0.5, False)
					EndIf
					;[End Block]
			End Select
			
		ElseIf n\IsDead Then
			;If Rand(1,2)=1
				AnimateNPC(n, 165, 174, 0.5, False)
				If n\Frame => 173
					SetNPCFrame(n, 174)
				Else
					AnimateNPC(n, 165, 174, 0.5, False)
				EndIf
			;Else
			;	AnimateNPC(n, 73, 164, 0.5, False)
			;	If n\Frame => 163
			;		SetNPCFrame(n, 164)
			;	Else
			;		AnimateNPC(n, 73, 164, 0.5, False)
			;	EndIf
			;EndIf
		EndIf
		
		If n\State < 3.0 And (I_268\Using = 0 Lor I_268\Timer =< 0.0) And (Not n\IgnorePlayer) Then
			dist = EntityDistanceSquared(n\Collider, Collider)
			
			If dist < 16.0 Then dist = dist - PowTwo((EntityVisible(Collider, n\Collider) + (EntityVisible(Collider, n\Collider) * 0.21)))
			If PowTwo(opt\SFXVolume * 1.2) > dist Lor dist < 2.25 Then
				If n\State3 = 0.0 Then
					If n\Sound <> 0 Then 
						FreeSound_Strict(n\Sound) : n\Sound = 0
					EndIf
					n\Sound = LoadSound_Strict("SFX\SCP\939\" + (n\ID Mod 3) + "Attack" + Rand(1, 3) + ".ogg")
					n\SoundChn = PlaySound2(n\Sound, Camera, n\Collider)										
					
					PlaySound_Strict(LoadTempSound("SFX\SCP\939\Attack.ogg"))
					n\State3 = 1.0
				EndIf
				
				n\State = 3.0
			ElseIf PowTwo(opt\SFXVolume * 1.6) > dist
				If n\State <> 1 And n\Reload <= 0.0 Then
					If n\Sound <> 0 Then 
						FreeSound_Strict(n\Sound) : n\Sound = 0
					EndIf
					n\Sound = LoadSound_Strict("SFX\SCP\939\" + (n\ID Mod 3) + "Alert" + Rand(1, 3) + ".ogg")
					n\SoundChn = PlaySound2(n\Sound, Camera, n\Collider)	
					
					SetNPCFrame(n, 175.0)
					
					n\Reload = 70.0 * 3.0
				EndIf
				n\State = 1.0
			EndIf
			n\Reload = n\Reload - fps\Factor[0]
		EndIf				
		
		RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider), 0.0, True)	
		
		PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.28, EntityZ(n\Collider))
		RotateEntity(n\obj, EntityPitch(n\Collider) - 90.0, EntityYaw(n\Collider), EntityRoll(n\Collider), True)
	EndIf
	;[End Block]
End Function

Function UpdateNPCtype939MP(n.NPCs)
	
	Local w.WayPoints,n2.NPCs, cmsg.ChatMSG
	Local dist#,prevFrame#,yaw#,shouldTime%
	Local i,j
	
	prevFrame = n\Frame
	
	If (Not n\IsDead)
		Select n\State
			Case MP939_STATE_FREEZE
				;[Block]
				;do nothing
				;[End Block]
			Case MP939_STATE_WANDER ;Wandering around
				;[Block]
				If n\PathStatus=1 Then
					While n\Path[n\PathLocation]=Null
						If n\PathLocation >= 19
							n\PathLocation = 0 : n\PathStatus = 0 : Exit
						Else
							n\PathLocation = n\PathLocation + 1
						EndIf
					Wend
					If n\PathStatus=1 Then
						PointEntity n\obj, n\Path[n\PathLocation]\obj
						If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
						
						AnimateNPC(n,1,41,n\CurrSpeed*30)
						n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
						
						dist = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
						If dist < 0.49 Then
							n\PathLocation = n\PathLocation + 1
						EndIf
					EndIf
				Else
					If n\PathTimer <= 0.0 Then
						n\EnemyX = EntityX(Players[n\ClosestPlayer]\Collider)
						n\EnemyY = EntityY(Players[n\ClosestPlayer]\Collider)
						n\EnemyZ = EntityZ(Players[n\ClosestPlayer]\Collider)
						n\PathStatus = FindPath(n,n\EnemyX,n\EnemyY,n\EnemyZ)
						
						If n\PathStatus = 1 Then
							If n\Path[1]<>Null Then
								If n\Path[2]=Null And EntityDistanceSquared(n\Path[1]\obj,n\Collider)<0.16 Then
									n\PathLocation = 0
									n\PathStatus = 0
								EndIf
							EndIf
							If n\Path[0]<>Null And n\Path[1]=Null Then
								n\PathLocation = 0
								n\PathStatus = 0
							EndIf
						EndIf
						
						If n\PathStatus<>1 Then
							n\PathTimer = 70*5
						EndIf
					Else
						n\PathTimer = n\PathTimer - fps\Factor[0]
						
						PointEntity n\obj, Players[n\ClosestPlayer]\Collider
						If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
						
						AnimateNPC(n,1,41,n\CurrSpeed*30)
						n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
					EndIf
				EndIf
				
				If (prevFrame<21 And n\Frame=>21) Lor (prevFrame>n\Frame And n\Frame<2) Then
					PlaySound2(StepSFX(4, 0, Rand(0,3)), Camera, n\Collider, 10.0)
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)<(25.0/(1+Players[n\ClosestPlayer]\CrouchState)) Then
						If EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider) Then
							n\State = MP939_STATE_DETECTED
						EndIf
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - fps\Factor[0]
				EndIf
				
				If mp_I\Gamemode\EnemyCount <= MinEnemyLeft Then
					If n\BlinkTimer <= 0.0 Then
						CreateOverHereParticle(EntityX(n\Collider),EntityY(n\Collider)+0.5,EntityZ(n\Collider))
						n\BlinkTimer = 70*5
					Else
						n\BlinkTimer = n\BlinkTimer - fps\Factor[0]
					EndIf
				EndIf
				;[End Block]
			Case MP939_STATE_DETECTED ;Player detected
				;[Block]
				PointEntity n\obj, Players[n\ClosestPlayer]\Collider
				If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
				
				AnimateNPC(n, 42, 56, n\CurrSpeed*10)
				n\CurrSpeed = CurveValue(n\Speed*1.4, n\CurrSpeed, 20.0)
				MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
				
				If (prevFrame<45 And n\Frame=>45) Lor (prevFrame<52 And n\Frame=>52) Then
					PlaySound2(StepSFX(4, 0, Rand(0,3)), Camera, n\Collider, 10.0)
				EndIf
				
				dist = EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)
				If dist<1.69 Then
					If (Abs(DeltaYaw(n\Collider,Players[n\ClosestPlayer]\Collider))<=60.0) Then
						n\State = MP939_STATE_ATTACK
						If Rand(2)=1 Then
							SetNPCFrame(n,57)
						Else
							SetNPCFrame(n,86)
						EndIf
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0
					If dist>56.25 Lor (Not EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider)) Then
						n\State = MP939_STATE_WANDER
						n\PathTimer = 0.0
						n\PathStatus = 0
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - fps\Factor[0]
				EndIf
				
				If mp_I\Gamemode\Timer939 < TIMER_939_MAX Then
					If n\ClosestPlayer = mp_I\PlayerID Then
						shouldTime = True
						For n2 = Each NPCs
							If n2 <> n And n2\NPCtype = n\NPCtype And n2\State = n\State And n2\ClosestPlayer = n\ClosestPlayer Then
								If n2\ID < n\ID Then
									shouldTime = False
								EndIf
								Exit
							EndIf
						Next
						If shouldTime Then
							mp_I\Gamemode\Timer939 = mp_I\Gamemode\Timer939 + fps\Factor[0]
							If mp_I\Gamemode\Timer939 >= TIMER_939_MAX Then
								Steam_Achieve(ACHV_939_5MIN)
							EndIf
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case MP939_STATE_ATTACK ;Attacking
				;[Block]
				Local shouldFrame#
				Local restartFrame#
				Local finalFrame#
				If n\Frame < 86 Then
					AnimateNPC(n, 57, 85, 0.6, False)
					shouldFrame = 62
					finalFrame = 84.5
				Else
					AnimateNPC(n, 86, 108, 0.6, False)
					shouldFrame = 92
					finalFrame = 107.5
				EndIf
				dist = EntityDistanceSquared(n\Collider,Players[n\ClosestPlayer]\Collider)
				yaw = Abs(DeltaYaw(n\Collider,Players[n\ClosestPlayer]\Collider))
				If (dist<2.56) Then
					If (yaw<=60.0) Then
						If prevFrame < shouldFrame And n\Frame => shouldFrame Then
							PlaySound2(DamageSFX[Rand(5,8)],Camera,n\Collider)
							If n\ClosestPlayer = mp_I\PlayerID Then
								If Players[n\ClosestPlayer]\CurrKevlar>0 Then
									PlaySound_Strict(NTF_PainSFX[Rand(0,7)])
								Else
									PlaySound_Strict(NTF_PainWeakSFX[Rand(0,1)])
								EndIf
							Else
								If Players[n\ClosestPlayer]\CurrKevlar>0 Then
									PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Players[n\ClosestPlayer]\Collider)
								Else
									PlaySound2(NTF_PainWeakSFX[Rand(0,1)],Camera,Players[n\ClosestPlayer]\Collider)
								EndIf
							EndIf
							If Players[n\ClosestPlayer]\CurrHP > 0 Then
								DamagePlayer(n\ClosestPlayer,Rand(20+5*mp_I\Gamemode\Difficulty,30+5*mp_I\Gamemode\Difficulty),Rand(25+5*mp_I\Gamemode\Difficulty,35+5*mp_I\Gamemode\Difficulty),5)
								If Players[n\ClosestPlayer]\CurrHP <= 0 Then
									cmsg = AddChatMSG("death_killedby", 0, SERVER_MSG_IS, CHATMSG_TYPE_TWOPARAM_TRANSLATE)
									cmsg\Msg[1] = Players[n\ClosestPlayer]\Name
									cmsg\Msg[2] = n\NVName
									Players[n\ClosestPlayer]\Deaths = Players[n\ClosestPlayer]\Deaths + 1
								EndIf
							EndIf	
						EndIf
					EndIf
				EndIf
				If n\Frame => finalFrame Then
					If Players[n\ClosestPlayer]\Collider=0 Lor Players[n\ClosestPlayer]\CurrHP<=0 Then
						n\State = MP939_STATE_VOCAL
					Else
						If dist>2.25 Then
							n\State = MP939_STATE_VOCAL
						EndIf
						If (yaw>60.0) Then
							n\State = MP939_STATE_VOCAL
						EndIf
					EndIf
					If n\State = MP939_STATE_ATTACK Then
						If Rand(2)=1 Then
							restartFrame = 57
						Else
							restartFrame = 86
						EndIf
						SetNPCFrame(n,restartFrame)
					EndIf
				EndIf
				;[End Block]
			Case MP939_STATE_VOCAL ;Vocalize screams
				;[Block]
				AnimateNPC(n, 109, 279, 0.5, False)
				If n\State3 = 0 Then
					n\State3 = Rand(1,9)
				EndIf
				If prevFrame < 136 And n\Frame >= 136 Then
					If ChannelPlaying(n\SoundChn) Then
						StopChannel(n\SoundChn)
					EndIf
					If n\Sound<>0 Then
						FreeSound_Strict n\Sound
						n\Sound = 0
					EndIf
					n\Sound = LoadSound_Strict("SFX\SCP\939\"+((Int(n\State3)-1) Mod 3)+"Attack"+(Int(Ceil(Int(n\State3+2)/3)))+".ogg")
					n\SoundChn = PlaySound2(n\Sound, Camera, n\Collider, 20)
				EndIf
				If n\Frame >= 278.5 Then
					n\State = MP939_STATE_DETECTED
					n\State3 = 0
				EndIf
				;[End Block]
		End Select
		
		UpdateSoundOrigin(n\SoundChn, Camera, n\Collider, 20)
		
		If n\HP<=0 Then
			n\IsDead=True
			EntityType n\Collider,HIT_DEAD
			If Rand(2)=1 Then
				SetNPCFrame(n,279)
			Else
				SetNPCFrame(n,373)
			EndIf
			MoveEntity n\Collider,0,0.01,0
		EndIf
	Else
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		Local dieFrame#
		If n\Frame < 373 Then
			AnimateNPC(n, 279, 372, 0.5, False)
			dieFrame = 288.5
		Else
			AnimateNPC(n, 373, 382, 0.5, False)
			dieFrame = 381.5
		EndIf
		If n\Frame >= dieFrame
			If n\State2 < 70*5
				n\State2 = n\State2 + fps\Factor[0]
			Else
				If n\State2 >= 70*5 And n\State2 < 1000
					n\State2 = 1000
				ElseIf n\State2 >= 1000 And n\State2 < 2000
					EntityAlpha n\obj,Inverse((n\State2-1000.0)/1000.0)
					n\State2 = n\State2 + 2*fps\Factor[0]
				Else
					RemoveNPC(n)
					Return
				EndIf
			EndIf
		EndIf
	EndIf
	
	PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.28, EntityZ(n\Collider))
	RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), EntityRoll(n\Collider), True
	
	EntityAutoFade(n\obj,GetCameraFogRangeFar(Camera)-0.5,GetCameraFogRangeFar(Camera)+0.5)
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D