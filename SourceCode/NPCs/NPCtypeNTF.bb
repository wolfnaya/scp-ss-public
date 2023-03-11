; ~ NTF Unit constants
;[Block]
Const MTF_FOLLOWPLAYER = 0
Const MTF_INTRO = 1
Const MTF_CONTAIN173 = 2
Const MTF_CONTACT = 3
Const MTF_FLEE = 4
Const MTF_SPLIT_UP = 5
Const MTF_SEARCH = 6
Const MTF_HEAL = 7
Const MTF_TOTARGET = 8
Const MTF_TESLA = 9
Const MTF_WONDERING = 10
; ~ Containment constants
Const MTF_GOTOSCP = 1
; ~ NTF Designation constants
Const MTF_UNIT_DEFAULT = 0
Const MTF_UNIT_REGULAR = 1
Const MTF_UNIT_MEDIC = 2
Const MTF_UNIT_COMMANDER = 3
;[End Block]

Function CreateNPCtypeNTF(n.NPCs)
	Local temp#
	
	n\NPCName = "MTF"
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.175, 0.2
	EntityType n\Collider, HIT_PLAYER
	n\obj = LoadAnimMesh_Strict("GFX\npcs\Humans\MTF\NTF\NineTailedFox.b3d")
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "MTF", "speed") / 100.0)
	temp# = (GetINIFloat("DATA\NPCs.ini", "MTF", "scale") / 2.5)
	ScaleEntity n\obj, temp, temp, temp
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2) 
	
	Local i% = Rand(1, 75)
	If i <= 13 Then
		SwitchNPCGun%(n, "mp7")
	ElseIf i > 13 And i <= 36 Then	
		SwitchNPCGun%(n, "mp5k")
	ElseIf i > 36 Then	
		SwitchNPCGun%(n, "p90")
	EndIf
	
	n\Gun\Ammo = n\Gun\MaxAmmo
	
	n\HP = 350-(75*SelectedDifficulty\OtherFactors)
	
	n\BlinkTimer = 70.0*Rnd(5,8)
	
	n\Clearance = 3
End Function

Function UpdateNPCtypeNTF(n.NPCs)
	Local n2.NPCs,w.WayPoints,e.Events,r.Rooms
	Local prevFrame# = n\Frame
	Local SeePlayer% = False
	Local SeeNPC%, VoiceLine$, SmallestNPCDist#, CurrentNPCDist#
	Local PlayerDistSquared# = EntityDistanceSquared(n\Collider,Collider)
	Local WayPDist#
	Local WaypointDist# = 0.0
	Local temp% = False
	Local sfxstep% = 0
	Local IsPushable% = False
	Local i%,it.Items,g.Guns
	Local prevX#, prevZ#
	Local pvt%
	Local v3d_1.Vector3D, v3d_2.Vector3D
	
	n\BlinkTimer = Max(n\BlinkTimer-fps\Factor[0],0.0)
	If n\BlinkTimer = 0.0 Then
		If n\State = MTF_CONTAIN173 Then
			If n\PrevState = MTF_UNIT_MEDIC Then
				PlayMTFSound(LoadTempSound("SFX\Character\MTF\173\Medic_Blinking"+Rand(1,3)+".ogg"),n,True)
			Else
				PlayMTFSound(LoadTempSound("SFX\Character\MTF\173\Regular_Blinking"+Rand(1,3)+".ogg"),n,True)
			EndIf
		EndIf
		n\BlinkTimer = 70.0*Rnd(5,8)
	EndIf
	
	If IsNPCStuck(n, 70.0*5) Then
		n\PathStatus = 0
		If n\State = MTF_FOLLOWPLAYER Then
			v3d_1 = CreateVector3D(962, 1259, 0.3)
			v3d_2 = CreateVector3D(1496, 1524, 0.3)
			NPC_GoTo(n, v3d_1, v3d_2, Collider, 0.7)
			Delete v3d_1
			Delete v3d_2
		ElseIf n\State = MTF_SPLIT_UP Then
			v3d_1 = CreateVector3D(962, 1259, 0.3)
			v3d_2 = CreateVector3D(1496, 1524, 0.3)
			NPC_GoToRoom(n, v3d_1, v3d_2, 0.7)
			Delete v3d_1
			Delete v3d_2
		EndIf
	EndIf
	
	n\Reload = Max(n\Reload-fps\Factor[0],0.0)
	n\PathTimer = Max(n\PathTimer-fps\Factor[0],0.0)
	
	If n\HP > 0 Then
		If n\State = MTF_FOLLOWPLAYER Lor n\State = MTF_HEAL Lor n\State = MTF_SEARCH Then
			;Spotting other NPCs
			SmallestNPCDist# = 0.0 : CurrentNPCDist# = 0.0
			n\Target = Null
			For n2 = Each NPCs
				If n2\NPCtype <> n\NPCtype And n2\HP > 0 And IsTarget(n, n2) Then
					SeeNPC = OtherNPCSeesMeNPC(n2, n)
					If SeeNPC And EntityVisible(n2\Collider, n\Collider) Then
						CurrentNPCDist = EntityDistanceSquared(n\Collider, n2\Collider)
						If SmallestNPCDist = 0.0 Lor SmallestNPCDist > CurrentNPCDist Then
							temp = True
							
							If n2\NPCtype = NPCtype173 And Curr173\Idle > SCP173_STATIONARY Then
								temp = False
							EndIf
							
							If temp Then
								SmallestNPCDist = CurrentNPCDist
								n\Target = n2
							EndIf
						EndIf
					EndIf
				EndIf
			Next
			
			If n\Target <> Null Then
				If n\State = MTF_SEARCH Then
					SetNPCFrame(n, 1383)
				EndIf
				n\State = MTF_CONTACT
				VoiceLine = ""
				Select n\Target\NPCtype
					Case NPCtypeD2
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "D-Class\Medic_Spotted" + Rand(1,6)
						Else
							VoiceLine = "D-Class\Regular_Spotted" + Rand(1,6)
						EndIf
					Case NPCtype049_2,NPCtype049_3,NPCtype049_4,NPCtype049_5,NPCType049_6
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "Zombie\Medic_049-2"
						Else
							VoiceLine = "Zombie\Regular_049-2"
						EndIf	
					Case NPCtype008,NPCtype008_2,NPCtype008_3,NPCtype008_4
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "Zombie\Medic_008-1"
						Else
							VoiceLine = "Zombie\Regular_008-1"
						EndIf	
					Case NPCtype939
						;VoiceLine = "939\Spotted" ;Todo: Voice line required
					Case NPCtype106
						If n\PrevState = MTF_UNIT_MEDIC Then
							If Rand(1,10) = 1 Then
								VoiceLine = "106\Medic_Extra1"
							Else	
								VoiceLine = "106\Medic_Spotted" + Rand(1,4)
							EndIf	
						Else
							VoiceLine = "106\Regular_Spotted" + Rand(1,4)
						EndIf
						n\State = MTF_FLEE
					Case NPCtype173
						If Curr173\Idle = SCP173_ACTIVE And Curr173\IdleTimer <= 0.0 Then
							If n\PrevState = MTF_UNIT_MEDIC Then
								VoiceLine = "173\Medic_Spotted" + Rand(1,4)
							Else
								VoiceLine = "173\Regular_Spotted" + Rand(1,4)
							EndIf
							Curr173\Idle = SCP173_STATIONARY
							Curr173\IdleTimer = 70*30
						EndIf
						n\State = MTF_CONTAIN173
					Case NPCtype049
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "049\Medic_Spotted" + Rand(1,4)
						Else
							VoiceLine = "049\Regular_Spotted" + Rand(1,4)
						EndIf
						n\State = MTF_FLEE
					Case NPCtype096
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "096\Medic_Spotted" + Rand(1,4)
						Else
							VoiceLine = "096\Regular_Spotted" + Rand(1,4)
						EndIf
						n\State = MTF_FLEE
				End Select
				If VoiceLine <> "" Then
					PlayMTFSound(LoadTempSound("SFX\Character\MTF\" + VoiceLine + ".ogg"), n, True)
				EndIf
			EndIf
		EndIf
		
		Select n\State
			Case MTF_FOLLOWPLAYER, MTF_HEAL
				;[Block]
				If (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
					If n\State = MTF_HEAL And (psp\Health = 100 Lor n\PrevState <> MTF_UNIT_MEDIC) Then
						n\State = MTF_FOLLOWPLAYER
					EndIf
					
					;Player can push MTF unit in this state
					IsPushable = True
					
					;Following the player
					If PlayerDistSquared <= PowTwo(8.0) Then
						If EntityVisible(n\Collider, Collider) Then
							SeePlayer = True
						EndIf
					EndIf
					If SeePlayer Then
						n\PathStatus = 0
						n\PathLocation = 0
						n\PathTimer = 0.0
						
						n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj, Collider)
						RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
						If EntityDistanceSquared(n\Collider, Collider) < PowTwo(1.0 - (0.5 * n\State = MTF_HEAL)) Then
							If n\CurrSpeed <= 0.01 Then
								n\CurrSpeed = 0.0
								AnimateNPC(n, 962, 1259, 0.3)
								psp\Health = 100
								n\State = MTF_FOLLOWPLAYER
							Else
								n\CurrSpeed = CurveValue(0.0,n\CurrSpeed,20.0)
								AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
								MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
							EndIf
						Else
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
							AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
						EndIf
					Else
						v3d_1 = CreateVector3D(962, 1259, 0.3)
						v3d_2 = CreateVector3D(1496, 1524, 0.3)
						NPC_GoTo(n, v3d_1, v3d_2, Collider, 0.7)
						Delete v3d_1
						Delete v3d_2
					EndIf
					
	;				If Curr173\Idle = SCP173_STATIONARY Then
	;					v3d_1 = CreateVector3D(79, 309, 0.3)
	;					v3d_2 = CreateVector3D(488, 522, 0.3)
	;					NPC_GoTo(n, v3d_1, v3d_2, Collider, 0.7)
	;					Delete v3d_1
	;					Delete v3d_2
	;				EndIf
					
					If PlayerDistSquared > PowTwo(16) Then
						Local shortestDist# = 10000.0
						Local shortestDistRoom% = -1
						For i = 0 To 3
							If PlayerRoom\Adjacent[i] <> Null Then
								Local currDist# = EntityDistanceSquared(n\Collider, PlayerRoom\Adjacent[i]\obj)
								If currDist < shortestDist And (Not EntityInView(PlayerRoom\Adjacent[i]\obj, Camera)) Then
									shortestDist = currDist
									shortestDistRoom = i
								EndIf
							EndIf
						Next
						
						If shortestDistRoom >= 0 Then
							TeleportEntity(n\Collider, PlayerRoom\Adjacent[shortestDistRoom]\x, PlayerRoom\Adjacent[shortestDistRoom]\y + 0.1, PlayerRoom\Adjacent[shortestDistRoom]\z, n\CollRadius)
							n\PathStatus = 0
							n\PathLocation = 0
							n\PathTimer = 0.0
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case MTF_INTRO
				;[Block]
				If n\State2 = 0 ;sitting
					AnimateNPC(n, 1525, 1623, 0.5)
				ElseIf n\State2 = 1 ;aiming
					AnimateNPC(n, 346, 351, 0.2)
				ElseIf n\State2 = 2 ;idle
					If Rand(400) = 1 Then
						n\Angle = n\Angle + Rnd(-30, 30)
					EndIf
					AnimateNPC(n, 962, 1259, 0.3)
					RotateEntity(n\Collider, 0, CurveAngle(n\Angle,EntityYaw(n\Collider),10.0), 0, True)
				EndIf
				;[End Block]
			Case MTF_CONTAIN173
				;[Block]
				AnimateNPC(n, 962, 1259, 0.3)
				
				n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj, Curr173\obj)
				RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
				
				mtfd\Enabled = False
				
				If Curr173\Idle <> SCP173_STATIONARY Then
					n\State = MTF_FOLLOWPLAYER
				EndIf
				
				If Curr173\IdleTimer > 0.0 Then
					Curr173\Idle = SCP173_STATIONARY
					;Yes, the player's Collider, LOL
					If EntityDistance(Curr173\Collider, Collider) < 15.0 And (BlinkTimer < - 16 Lor BlinkTimer > - 6) And (EntityInView(Curr173\obj, Camera) Lor EntityInView(Curr173\obj2, Camera)) Then
						Curr173\IdleTimer = Max(Curr173\IdleTimer - fps\Factor[0], 0)
						If Curr173\IdleTimer <= 0.0 Then
							mtfd\Enabled = True
							Curr173\Idle = SCP173_BOXED
							EndTask(TASK_CLASSIC_CONTAIN_173)
							BeginTask(TASK_CLASSIC_173_TO_CHAMBER)
							;PlayPlayerSPVoiceLine("scp173containmentbox" + Rand(1, 2))
						EndIf
					Else
						Local isLooking% = False
						For n2 = Each NPCs
							If n2\NPCtype = n\NPCtype And n2\State = MTF_CONTAIN173 And n2\BlinkTimer > 10.0 Then
								isLooking = True
								Exit
							EndIf
						Next
						If (Not isLooking) Then
							Curr173\Idle = SCP173_ACTIVE
							mtfd\Enabled = True
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case MTF_ATTACK_PLAYER
				;[Block]
				Local dist = EntityDistanceSquared(n\Collider, Collider)
				If (I_268\Using = 0 Lor I_268\Timer =< 0.0) And psp\Health > 0 Then
					If n\Target <> Null Then
						If n\Target\HP > 0 Then
							n\CurrSpeed = 0.0
							n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,n\Target\obj)
							RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
							If n\Reload = 0 Then
								If Abs(DeltaYaw(n\Collider,n\Target\Collider))<45.0 Then
									If NPCSeesEntity(n, n\Target\Collider)
										n\Gun\Ammo = n\Gun\Ammo - 1
										;SetNPCFrame(n,575)
										ShootTarget(0, 0, 0, n, Clamp(2 / dist, 0.0, 0.65), True, n\Gun\Damage * Rand(1, n\Gun\BulletsPerShot))
										If n\Gun\MaxGunshotSounds = 1 Then
											;n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 35)
											PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"))
										Else
											;n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 35)
											PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"))
										EndIf
										n\Reload = n\Gun\ShootFrequency
									EndIf
								EndIf
							EndIf
						Else
							n\IdleTimer = 70*10
							n\State = MTF_SEARCH
							n\Target = Null
						EndIf
					Else
						If psp\Health <= 0 Then
							;PlayNPCSound(n, LoadTempSound("SFX\Character\D-Class\Player_Kill"+Rand(1, 3)+".ogg"))
							;m_msg\DeathTxt = "SCP-035 was terminated by Nine-Tailed Fox." 
							n\IdleTimer = 70*10
							n\State = MTF_SEARCH
							n\Target = Null
						Else
							n\CurrSpeed = 0.0
							n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,Collider)
							RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
							If n\Reload = 0 Then
								If Abs(DeltaYaw(n\Collider,Collider))<45.0 Then
									If NPCSeesEntity(n, Camera)
										;SetNPCFrame(n,575)
										If n\Gun\MaxGunshotSounds = 1 Then
											;n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 35)
											PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"))
										Else
											;n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 35)
											PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"))
										EndIf
										ShootPlayer(0, 0, 0, Clamp(2 / dist, 0.0, 0.65), True, n\Gun\Damage * Rand(1, n\Gun\BulletsPerShot))
										n\Gun\Ammo = n\Gun\Ammo - 1
										n\Reload = n\Gun\ShootFrequency
									Else
										n\LastSeen = Collider
										n\IdleTimer = 70*8
										n\State = MTF_FOLLOW_ENEMY_PLAYER
									EndIf
								EndIf
							EndIf
						EndIf
						
						If n\Gun\Ammo <= 0 Then
							PlayNPCSound(n, LoadTempSound("SFX\Character\D-Class\Reload"+Rand(1, 4)+".ogg"))
							n\State = MTF_RELOAD
						EndIf
					EndIf
				EndIf
				
				n\IdleTimer = 70*10
				n\State = MTF_SEARCH
				n\Target = Null
				;[End Block]
			Case MTF_FIND_COVER
				;[Block]
				;If n\Gun\Ammo <= 0 Then
				;	If n\Target<>Null Then
				;		temp2 = NPC_GoToCover(n, FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), n\Target\Collider, 0.8)
				;	Else
				;		temp2 = NPC_GoToCover(n, FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), Collider, 0.8)
				;	EndIf
				;EndIf
				;If temp2 Then
				;	n\State = D2_RELOAD
				;EndIf
				;[End Block]
			Case MTF_FOLLOW_ENEMY_PLAYER
				;[Block]
				;NPC_GoTo(n,n\Collider, Collider, 0.8)
				If n\IdleTimer = 0.0 Then
					n\State = MTF_SEARCH
					n\Target = Null
				EndIf
				;[End Block]
			Case MTF_RELOAD
				;[Block]
				AnimateNPC(n,1624,1675,0.3,False)
				If n\Frame >= 1670 Then
					n\Gun\Ammo = n\Gun\MaxAmmo
					n\State = MTF_FOLLOW_ENEMY_PLAYER
				EndIf
				;[End Block]
			Case MTF_CONTACT
				;[Block]
				If n\Target <> Null And n\Target\HP > 0 Then
					CurrentNPCDist = EntityDistanceSquared(n\Collider, n\Target\Collider)
					If CurrentNPCDist < PowTwo(6) ;And EntityVisible(n\Collider, n\Target\Collider) Then
						n\CurrSpeed = 0.0
						n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,n\Target\obj)
						RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
						If n\Frame = 1383 Then
							If n\Reload = 0 Then
								n\SoundChn2 = PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"))
								pvt% = CreatePivot()
								
								RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
								PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
								MoveEntity (pvt,0.8*0.079, 10.0*0.079, 6.0*0.079)
								
								ShootTarget(EntityX(pvt), EntityY(pvt), EntityZ(pvt), n, Rnd(0.4,0.8))
								n\Reload = n\Gun\ShootFrequency
							EndIf
						Else
							If n\Frame > 1383 Then
								SetNPCFrame(n, 1375)
							EndIf
							AnimateNPC(n,1375,1383,0.2,False)
						EndIf
					Else
						v3d_1 = CreateVector3D(79, 309, 0.3)
						v3d_2 = CreateVector3D(488, 522, 0.3)
						NPC_GoTo(n, v3d_1, v3d_2, n\Target\Collider, 0.7)
						Delete v3d_1
						Delete v3d_2
					EndIf
				ElseIf IsZombie And psp\Health > 0 Then
					n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,Collider)
					RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
					If n\Frame = 1383 Then
						If n\Reload = 0 Then
							If n\Gun\MaxGunshotSounds = 1 Then
								n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 25)
							Else
								n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 25)
							EndIf
							pvt% = CreatePivot()
							
							RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
							PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
							MoveEntity (pvt,0.8*0.079, 10.0*0.079, 6.0*0.079)
							
							ShootPlayer(EntityX(pvt), EntityY(pvt), EntityZ(pvt), Rnd(0.4,0.8))
							n\Reload = n\Gun\ShootFrequency
						EndIf
					Else
						If n\Frame > 1383 Then
							SetNPCFrame(n, 1375)
						EndIf
						AnimateNPC(n,1375,1383,0.2,False)
					EndIf
				Else
					pvt = FreeEntity_Strict(pvt)
					n\IdleTimer = 70*10
					n\State = MTF_SEARCH
					n\Target = Null
				EndIf
				;[End block]
			Case MTF_FLEE
				;[Block]
				If n\Target <> Null Then
					CurrentNPCDist = EntityDistanceSquared(n\Collider, n\Target\Collider)
				EndIf
				
				If n\Target <> Null And CurrentNPCDist < 35.0 Then
					If CurrentNPCDist < PowTwo(2) Then
						n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, -n\CurrSpeed * fps\Factor[0]
						n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,n\Target\obj)
						RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
						AnimateNPC(n,488,522,-0.5)
						n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,n\Target\obj)
						RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
;						If n\Reload = 0 Then
;							n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\P90\shoot"+Rand(1,4)+".ogg"), Camera, n\Collider, 35)
;							ShootTarget(0, 0, 0, n, Rnd(0.4,0.8))
;							n\Reload = 4.67 ;900 RPM
;						EndIf
					Else
						v3d_1 = CreateVector3D(79, 309, 0.3)
						v3d_2 = CreateVector3D(488, 522, 0.3)
						NPC_GoToRoom(n, v3d_1, v3d_2, 0.7)
						Delete v3d_1
						Delete v3d_2
					EndIf
				Else
					n\State = MTF_FOLLOWPLAYER
				EndIf
				;[End block]
			Case MTF_SEARCH	
				;[Block]
				v3d_1 = CreateVector3D(79, 309, 0.3)
				v3d_2 = CreateVector3D(488, 522, 0.3)
				NPC_GoToRoom(n, v3d_1, v3d_2, 0.5)
				Delete v3d_1
				Delete v3d_2
				
				If n\IdleTimer = 0.0 Then
					If n\PrevState = MTF_UNIT_MEDIC Then
						PlayMTFSound(LoadTempSound("SFX\Character\MTF\Medic_SearchComplete"+Rand(1,4)+".ogg"),n,True)
					Else
						PlayMTFSound(LoadTempSound("SFX\Character\MTF\Regular_SearchComplete"+Rand(1,4)+".ogg"),n,True)
					EndIf
					n\State = MTF_FOLLOWPLAYER
				EndIf
				;[End Block]
			Case MTF_SPLIT_UP
				;[Block]
				v3d_1 = CreateVector3D(962, 1259, 0.3)
				v3d_2 = CreateVector3D(1496, 1524, 0.3)
				NPC_GoToRoom(n, v3d_1, v3d_2, 0.7)
				Delete v3d_1
				Delete v3d_2
				;[End block]
			Case MTF_TOTARGET
				;[Block]
				pvt = CreatePivot()
				PositionEntity pvt, n\EnemyX, n\EnemyY, n\EnemyZ
				If EntityDistanceSquared(n\Collider, pvt) < PowTwo(0.3) Then
					If n\CurrSpeed <= 0.01 Then
						n\CurrSpeed = 0.0
						AnimateNPC(n, 962, 1259, 0.3)
					Else
						n\CurrSpeed = CurveValue(0.0,n\CurrSpeed,20.0)
						AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
					EndIf
				Else
					n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,pvt)
					RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
					n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
					AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
					MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
				EndIf
				pvt = FreeEntity_Strict(pvt)
				;[End Block]
			Case MTF_TESLA
				;[Block]
				AnimateNPC(n, 1260, 1375, 0.25, False)
				If n\Frame = 1375 Then
					n\State = MTF_FOLLOWPLAYER
				EndIf
				;[End Block]
			Case STATE_SCRIPT
				;[Block]
				
				;[End Block]
		End Select
		n\IdleTimer = Max(0.0, n\IdleTimer-fps\Factor[0])
	ElseIf n\HP = -1 Then
		EntityType n\Collider, HIT_DEAD
		If n\Frame > 21 Then
			SetNPCFrame(n, 27)
		Else
			AnimateNPC(n, 2, 27, 0.3)
		EndIf
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		n\Reload = 0.0
		
		If n\Frame >= 26.5
			If n\State5 < 70*25 Then
				n\State5 = n\State5 + fps\Factor[0]
			Else
				If n\State5 >= 70*25 And n\State5 < 1000 Then
					n\State5 = 1000
				ElseIf n\State5 >= 1000 And n\State5 < 2000 Then
					EntityAlpha n\obj,Inverse((n\State5-1000.0)/1000.0)
					n\State5 = n\State5 + 2*fps\Factor[0]
				Else
					RemoveNPC(n)
					Return
				EndIf
			EndIf
		EndIf
		
		If n\Gun <> Null And n\Frame >= 15-0.5 Then
			Local bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
			For g = Each Guns
				If g\ID = n\Gun\ID Then
					it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
					EntityType it\Collider, HIT_ITEM
					it\state = n\Gun\Ammo
					Select Rand(2)
						Case 0
							it\state2 = n\Gun\MaxAmmo
						Case 1
							it\state2 = n\Gun\MaxAmmo+n\Gun\MaxAmmo
						Case 2
							it\state2 = n\Gun\MaxAmmo+n\Gun\MaxAmmo+n\Gun\MaxAmmo
					End Select
					it\Dropped = 1
					Exit
				EndIf
			Next
			RemoveNPCGun(n)
		EndIf
	Else
		EntityType n\Collider, HIT_DEAD
		If n\Frame = 1407 Then
			SetNPCFrame(n, 1407)
		Else
			AnimateNPC(n, 1383, 1407, 0.05, False)
		EndIf
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		n\Reload = 0.0
		
		If n\Frame >= 1406.5
			If n\State5 < 70*25 Then
				n\State5 = n\State5 + fps\Factor[0]
			Else
				If n\State5 >= 70*25 And n\State5 < 1000 Then
					n\State5 = 1000
				ElseIf n\State5 >= 1000 And n\State5 < 2000 Then
					EntityAlpha n\obj,Inverse((n\State5-1000.0)/1000.0)
					n\State5 = n\State5 + 2*fps\Factor[0]
				Else
					RemoveNPC(n)
					Return
				EndIf
			EndIf
		EndIf
		
		If n\Gun <> Null And n\Frame >= 1390-0.5 Then
			bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
			For g = Each Guns
				If g\ID = n\Gun\ID Then
					it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
					EntityType it\Collider, HIT_ITEM
					it\state = n\Gun\Ammo
					it\state2 = Rand(0,2)
					it\Dropped = 1
					Exit
				EndIf
			Next
			RemoveNPCGun(n)
		EndIf
	EndIf
	
	If n\HP <= 0 Then
		mtfd\Enabled = False
	EndIf
	
	;Play step sounds
	If n\CurrSpeed > 0.01 Then
		If prevFrame > 500 And n\Frame<495 Then
			sfxstep = GetStepSound(n\Collider,n\CollRadius)
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(sfxstep*MaxStepSounds)], Camera, n\Collider, 8.0, Rnd(0.5,0.7))
		ElseIf prevFrame < 505 And n\Frame=>505
			sfxstep = GetStepSound(n\Collider,n\CollRadius)
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(sfxstep*MaxStepSounds)], Camera, n\Collider, 8.0, Rnd(0.5,0.7))
		ElseIf prevFrame < 1509 And n\Frame=>1509
			sfxstep = GetStepSound(n\Collider,n\CollRadius)
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(sfxstep*MaxStepSounds)], Camera, n\Collider, 8.0, Rnd(0.5,0.7))
		ElseIf prevFrame < 1522 And n\Frame=>1522
			sfxstep = GetStepSound(n\Collider,n\CollRadius)
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(sfxstep*MaxStepSounds)], Camera, n\Collider, 8.0, Rnd(0.5,0.7))
		EndIf
	EndIf
	
	If n\State <> STATE_SCRIPT Then
		If n\State <> MTF_TOTARGET Then
			For n2.NPCs = Each NPCs
				If n2\NPCtype = NPCtypeNTF Then
					If n2\ID > n\ID Then
						If EntityDistanceSquared(n2\Collider,n\Collider)<PowTwo(0.5) Then
							TranslateEntity n2\Collider, Cos(EntityYaw(n2\Collider,True)+45)* n\Speed*0.7 * fps\Factor[0], 0, Sin(EntityYaw(n2\Collider,True)+45)* 0.005 * fps\Factor[0], True
						EndIf
					EndIf
				EndIf
			Next
		EndIf
		
		;Push MTF unit aside when player is close (only works in certain states)
		If IsPushable Then
			If PlayerDistSquared<PowTwo(0.7) Then
				n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,Collider)
				RotateEntity n\Collider,0.0,CurveAngle(n\Angle,EntityYaw(n\Collider,True),20.0),0.0,True
				TranslateEntity n\Collider, Cos(EntityYaw(n\Collider,True)-45)* 0.005 * fps\Factor[0], 0, Sin(EntityYaw(n\Collider,True)-45)* 0.005 * fps\Factor[0], True
			EndIf
			If Abs(DeltaYaw(Collider,n\Collider))<80.0 Then
				If PlayerDistSquared<PowTwo(0.7) Then
					TranslateEntity n\Collider, Cos(EntityYaw(Collider,True)+90)* 0.005 * fps\Factor[0], 0, Sin(EntityYaw(Collider,True)+90)* 0.005 * fps\Factor[0], True
				EndIf
			EndIf
		EndIf
	EndIf
	
	;Position the NPC's model (although not if the MTF unit is sitting in the helicopter)
	If Int(n\State)<>MTF_INTRO Lor Int(n\State2)<>0 Then
		RotateEntity n\obj,0.0,EntityYaw(n\Collider,True),0.0,True
		PositionEntity n\obj,EntityX(n\Collider,True),EntityY(n\Collider,True)-n\CollRadius,EntityZ(n\Collider,True),True
	EndIf
	
End Function

Function UpdateNPCtypePlotNTF(n.NPCs)
	Local n2.NPCs,w.WayPoints,e.Events,r.Rooms
	Local prevFrame# = n\Frame
	Local SeePlayer% = False
	Local SeeNPC%, VoiceLine$, SmallestNPCDist#, CurrentNPCDist#
	Local PlayerDistSquared# = EntityDistanceSquared(n\Collider,Collider)
	Local WayPDist#
	Local WaypointDist# = 0.0
	Local temp% = False
	Local sfxstep% = 0
	Local IsPushable% = False
	Local i%,it.Items,g.Guns
	Local prevX#, prevZ#
	Local pvt%
	Local v3d_1.Vector3D, v3d_2.Vector3D
	
	n\BlinkTimer = Max(n\BlinkTimer-fps\Factor[0],0.0)
	If n\BlinkTimer = 0.0 Then
		If n\State = MTF_CONTAIN173 Then
			If n\PrevState = MTF_UNIT_MEDIC Then
				PlayMTFSound(LoadTempSound("SFX\Character\MTF\173\Medic_Blinking"+Rand(1,3)+".ogg"),n,True)
			Else
				PlayMTFSound(LoadTempSound("SFX\Character\MTF\173\Regular_Blinking"+Rand(1,3)+".ogg"),n,True)
			EndIf
		EndIf
		n\BlinkTimer = 70.0*Rnd(5,8)
	EndIf
	
	If IsNPCStuck(n, 70.0*5) Then
		n\PathStatus = 0
		If n\State = MTF_FOLLOWPLAYER Then
			v3d_1 = CreateVector3D(962, 1259, 0.3)
			v3d_2 = CreateVector3D(1496, 1524, 0.3)
			NPC_GoTo(n, v3d_1, v3d_2, Collider, 0.7)
			Delete v3d_1
			Delete v3d_2
		ElseIf n\State = MTF_SPLIT_UP Lor n\State = MTF_WONDERING Then
			v3d_1 = CreateVector3D(962, 1259, 0.3)
			v3d_2 = CreateVector3D(1496, 1524, 0.3)
			NPC_GoToRoom(n, v3d_1, v3d_2, 0.7)
			Delete v3d_1
			Delete v3d_2
		EndIf
	EndIf
	
	n\Reload = Max(n\Reload-fps\Factor[0],0.0)
	n\PathTimer = Max(n\PathTimer-fps\Factor[0],0.0)
	
	If EntityDistanceSquared(Collider, n\Collider)<PowTwo(1.5) Then
		If KeyHitUse Then
			n\IsCalledNPC = (Not n\IsCalledNPC)
		EndIf
	EndIf
	
	IsPushable = True
	
	If n\HP > 0 Then
		If n\State = MTF_FOLLOWPLAYER Lor n\State = MTF_HEAL Lor n\State = MTF_SEARCH Then
			;Spotting other NPCs
			SmallestNPCDist# = 0.0 : CurrentNPCDist# = 0.0
			n\Target = Null
			For n2 = Each NPCs
				If n2\NPCtype <> n\NPCtype And n2\HP > 0 And IsTarget(n, n2) Then
					SeeNPC = OtherNPCSeesMeNPC(n2, n)
					If SeeNPC And EntityVisible(n2\Collider, n\Collider) Then
						CurrentNPCDist = EntityDistanceSquared(n\Collider, n2\Collider)
						If SmallestNPCDist = 0.0 Lor SmallestNPCDist > CurrentNPCDist Then
							temp = True
							
							If n2\NPCtype = NPCtype173 And Curr173\Idle > SCP173_STATIONARY Then
								temp = False
							EndIf
							
							If temp Then
								SmallestNPCDist = CurrentNPCDist
								n\Target = n2
							EndIf
						EndIf
					EndIf
				EndIf
			Next
			
			If n\Target <> Null Then
				If n\State = MTF_SEARCH Then
					SetNPCFrame(n, 1383)
				EndIf
				n\State = MTF_CONTACT
				VoiceLine = ""
				Select n\Target\NPCtype
					Case NPCtypeCI
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "D-Class\Medic_Spotted" + Rand(1,6)
						ElseIf n\PrevState = MTF_UNIT_REGULAR Then
							VoiceLine = "D-Class\Regular_Spotted" + Rand(1,6)
						Else
							VoiceLine = "Stop" + Rand(1,6)
						EndIf
					Case NPCtypeD2
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "D-Class\Medic_Spotted" + Rand(1,6)
						ElseIf n\PrevState = MTF_UNIT_REGULAR Then
							VoiceLine = "D-Class\Regular_Spotted" + Rand(1,6)
						Else
							VoiceLine = "ClassD" + Rand(1,4)
						EndIf
					Case NPCtype049_2,NPCtype049_3,NPCtype049_4,NPCtype049_5,NPCType049_6
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "Zombie\Medic_049-2"
						Else
							VoiceLine = "Zombie\Regular_049-2"
						EndIf	
					Case NPCtype008,NPCtype008_2,NPCtype008_3,NPCtype008_4
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "Zombie\Medic_008-1"
						Else
							VoiceLine = "Zombie\Regular_008-1"
						EndIf	
					Case NPCtype939
						;VoiceLine = "939\Spotted" ;Todo: Voice line required
					Case NPCtype106
						If n\PrevState = MTF_UNIT_MEDIC Then
							If Rand(1,10) = 1 Then
								VoiceLine = "106\Medic_Extra1"
							Else	
								VoiceLine = "106\Medic_Spotted" + Rand(1,4)
							EndIf	
						Else
							VoiceLine = "106\Regular_Spotted" + Rand(1,4)
						EndIf
						n\State = MTF_FLEE
					Case NPCtype173
						If Curr173\Idle = SCP173_ACTIVE And Curr173\IdleTimer <= 0.0 Then
							If n\PrevState = MTF_UNIT_MEDIC Then
								VoiceLine = "173\Medic_Spotted" + Rand(1,4)
							Else
								VoiceLine = "173\Regular_Spotted" + Rand(1,4)
							EndIf
							Curr173\Idle = SCP173_STATIONARY
							Curr173\IdleTimer = 70*30
						EndIf
						n\State = MTF_CONTAIN173
					Case NPCtype049
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "049\Medic_Spotted" + Rand(1,4)
						Else
							VoiceLine = "049\Regular_Spotted" + Rand(1,4)
						EndIf
						n\State = MTF_FLEE
					Case NPCtype096
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "096\Medic_Spotted" + Rand(1,4)
						Else
							VoiceLine = "096\Regular_Spotted" + Rand(1,4)
						EndIf
						n\State = MTF_FLEE
				End Select
				If VoiceLine <> "" Then
					PlayMTFSound(LoadTempSound("SFX\Character\MTF\" + VoiceLine + ".ogg"), n, True)
				EndIf
			EndIf
		EndIf
		
		Select n\State
			Case MTF_FOLLOWPLAYER, MTF_HEAL
				;[Block]
				If (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
					If n\State = MTF_HEAL And (psp\Health = 100 Lor n\PrevState <> MTF_UNIT_MEDIC) Then
						n\State = MTF_FOLLOWPLAYER
					EndIf
					
					;Player can push MTF unit in this state
					IsPushable = True
					
					;Following the player
					If PlayerDistSquared <= PowTwo(8.0) Then
						If EntityVisible(n\Collider, Collider) Then
							SeePlayer = True
						EndIf
					EndIf
					If SeePlayer Then
						n\PathStatus = 0
						n\PathLocation = 0
						n\PathTimer = 0.0
						
						n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj, Collider)
						RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
						If EntityDistanceSquared(n\Collider, Collider) < PowTwo(1.0 - (0.5 * n\State = MTF_HEAL)) Then
							If n\CurrSpeed <= 0.01 Then
								n\CurrSpeed = 0.0
								AnimateNPC(n, 962, 1259, 0.3)
								psp\Health = 100
								n\State = MTF_FOLLOWPLAYER
							Else
								n\CurrSpeed = CurveValue(0.0,n\CurrSpeed,20.0)
								AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
								MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
							EndIf
						Else
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
							AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
						EndIf
					Else
						v3d_1 = CreateVector3D(962, 1259, 0.3)
						v3d_2 = CreateVector3D(1496, 1524, 0.3)
						NPC_GoTo(n, v3d_1, v3d_2, Collider, 0.7)
						Delete v3d_1
						Delete v3d_2
					EndIf
					
					If PlayerDistSquared > PowTwo(16) Then
						Local shortestDist# = 10000.0
						Local shortestDistRoom% = -1
						For i = 0 To 3
							If PlayerRoom\Adjacent[i] <> Null Then
								Local currDist# = EntityDistanceSquared(n\Collider, PlayerRoom\Adjacent[i]\obj)
								If currDist < shortestDist And (Not EntityInView(PlayerRoom\Adjacent[i]\obj, Camera)) Then
									shortestDist = currDist
									shortestDistRoom = i
								EndIf
							EndIf
						Next
						
						If shortestDistRoom >= 0 Then
							TeleportEntity(n\Collider, PlayerRoom\Adjacent[shortestDistRoom]\x, PlayerRoom\Adjacent[shortestDistRoom]\y + 0.1, PlayerRoom\Adjacent[shortestDistRoom]\z, n\CollRadius)
							n\PathStatus = 0
							n\PathLocation = 0
							n\PathTimer = 0.0
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case MTF_INTRO
				;[Block]
				If n\State2 = 0 ;sitting
					AnimateNPC(n, 1525, 1623, 0.5)
				ElseIf n\State2 = 1 ;aiming
					AnimateNPC(n, 346, 351, 0.2)
				ElseIf n\State2 = 2 ;idle
					If Rand(400) = 1 Then
						n\Angle = n\Angle + Rnd(-30, 30)
					EndIf
					AnimateNPC(n, 962, 1259, 0.3)
					RotateEntity(n\Collider, 0, CurveAngle(n\Angle,EntityYaw(n\Collider),10.0), 0, True)
				EndIf
				;[End Block]
			Case MTF_CONTAIN173
				;[Block]
				AnimateNPC(n, 962, 1259, 0.3)
				
				n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj, Curr173\obj)
				RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
				
				mtfd\Enabled = False
				
				If Curr173\Idle <> SCP173_STATIONARY Then
					If (Not n\IsCalledNPC) Then
						n\State = MTF_WONDERING
					Else
						n\State = MTF_FOLLOWPLAYER
					EndIf
				EndIf
				
				If Curr173\IdleTimer > 0.0 Then
					Curr173\Idle = SCP173_STATIONARY
					If n\PrevState = MTF_UNIT_COMMANDER Then
						If EntityDistance(Curr173\Collider, n\Collider) < 15.0 And (EntityInView(Curr173\obj, n\Collider) Lor EntityInView(Curr173\obj2, n\Collider)) Then
							Curr173\IdleTimer = Max(Curr173\IdleTimer - fps\Factor[0], 0)
							If Curr173\IdleTimer <= 0.0 Then
								mtfd\Enabled = True
								Curr173\Idle = SCP173_BOXED
								;PlayPlayerSPVoiceLine("scp173containmentbox" + Rand(1, 2))
							EndIf
						Else
							Local isLooking% = False
							For n2 = Each NPCs
								If n2\NPCtype = n\NPCtype And n2\State = MTF_CONTAIN173 And n2\BlinkTimer > 10.0 Then
									isLooking = True
									Exit
								EndIf
							Next
							If (Not isLooking) Then
								Curr173\Idle = SCP173_ACTIVE
								mtfd\Enabled = True
							EndIf
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case MTF_RELOAD
				;[Block]
				AnimateNPC(n,1624,1675,0.3,False)
				If n\Frame >= 1670 Then
					n\Gun\Ammo = n\Gun\MaxAmmo
					If (Not n\IsCalledNPC) Then
						n\State = MTF_WONDERING
					Else
						n\State = MTF_FOLLOWPLAYER
					EndIf
				EndIf
				;[End Block]
			Case MTF_CONTACT
				;[Block]
				If n\Target <> Null And n\Target\HP > 0 Then
					CurrentNPCDist = EntityDistanceSquared(n\Collider, n\Target\Collider)
					If CurrentNPCDist < PowTwo(6) ;And EntityVisible(n\Collider, n\Target\Collider) Then
						n\CurrSpeed = 0.0
						n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,n\Target\obj)
						RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
						If n\Frame = 1383 Then
							If n\Reload = 0 Then
								If n\Gun\MaxGunshotSounds = 1 Then
											;n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 35)
									PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"))
								Else
											;n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 35)
									PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"))
								EndIf
								pvt% = CreatePivot()
								
								RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
								PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
								MoveEntity (pvt,0.8*0.079, 10.0*0.079, 6.0*0.079)
								
								ShootTarget(EntityX(pvt), EntityY(pvt), EntityZ(pvt), n, Rnd(0.4,0.8))
								n\Reload = n\Gun\ShootFrequency
							EndIf
						Else
							If n\Frame > 1383 Then
								SetNPCFrame(n, 1375)
							EndIf
							AnimateNPC(n,1375,1383,0.2,False)
						EndIf
					Else
						v3d_1 = CreateVector3D(79, 309, 0.3)
						v3d_2 = CreateVector3D(488, 522, 0.3)
						NPC_GoTo(n, v3d_1, v3d_2, n\Target\Collider, 0.7)
						Delete v3d_1
						Delete v3d_2
					EndIf
				ElseIf IsZombie And psp\Health > 0 Then
					n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,Collider)
					RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
					If n\Frame = 1383 Then
						If n\Reload = 0 Then
							If n\Gun\MaxGunshotSounds = 1 Then
											;n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 35)
								PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"))
							Else
											;n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 35)
								PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"))
							EndIf
							pvt% = CreatePivot()
							
							RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
							PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
							MoveEntity (pvt,0.8*0.079, 10.0*0.079, 6.0*0.079)
							
							ShootPlayer(EntityX(pvt), EntityY(pvt), EntityZ(pvt), Rnd(0.4,0.8))
							n\Reload = n\Gun\ShootFrequency
						EndIf
					Else
						If n\Frame > 1383 Then
							SetNPCFrame(n, 1375)
						EndIf
						AnimateNPC(n,1375,1383,0.2,False)
					EndIf
				Else
					pvt = FreeEntity_Strict(pvt)
					n\IdleTimer = 70*10
					n\State = MTF_SEARCH
					n\Target = Null
				EndIf
				;[End block]
			Case MTF_FLEE
				;[Block]
				If n\Target <> Null Then
					CurrentNPCDist = EntityDistanceSquared(n\Collider, n\Target\Collider)
				EndIf
				
				If n\Target <> Null And CurrentNPCDist < 35.0 Then
					If CurrentNPCDist < PowTwo(2) Then
						n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, -n\CurrSpeed * fps\Factor[0]
						n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,n\Target\obj)
						RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
						AnimateNPC(n,488,522,-0.5)
						n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,n\Target\obj)
						RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
;						If n\Reload = 0 Then
;							n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\P90\shoot"+Rand(1,4)+".ogg"), Camera, n\Collider, 35)
;							ShootTarget(0, 0, 0, n, Rnd(0.4,0.8))
;							n\Reload = 4.67 ;900 RPM
;						EndIf
					Else
						v3d_1 = CreateVector3D(79, 309, 0.3)
						v3d_2 = CreateVector3D(488, 522, 0.3)
						NPC_GoToRoom(n, v3d_1, v3d_2, 0.7)
						Delete v3d_1
						Delete v3d_2
					EndIf
				Else
					If (Not n\IsCalledNPC) Then
						n\State = MTF_WONDERING
					Else
						n\State = MTF_FOLLOWPLAYER
					EndIf
				EndIf
				;[End Block]
			Case MTF_WONDERING
				;[Block]
				If n\PrevState = MTF_UNIT_COMMANDER Then
					v3d_1 = CreateVector3D(962, 1259, 0.3)
					v3d_2 = CreateVector3D(1496, 1524, 0.3)
					NPC_GoToRoom(n, v3d_1, v3d_2, 0.5)
					Delete v3d_1
					Delete v3d_2
				Else
					For n2 = Each NPCs
						If n2\NPCtype = n\NPCtype And n2\PrevState = MTF_UNIT_COMMANDER Then
							v3d_1 = CreateVector3D(962, 1259, 0.3)
							v3d_2 = CreateVector3D(1496, 1524, 0.3)
							NPC_GoTo(n, v3d_1, v3d_2, n2\Collider, 0.7)
							Delete v3d_1
							Delete v3d_2
							Exit
						EndIf
					Next
				EndIf
				;[End Block]
			Case MTF_SEARCH	
				;[Block]
				v3d_1 = CreateVector3D(79, 309, 0.3)
				v3d_2 = CreateVector3D(488, 522, 0.3)
				NPC_GoToRoom(n, v3d_1, v3d_2, 0.5)
				Delete v3d_1
				Delete v3d_2
				
				If n\IdleTimer = 0.0 Then
					If n\PrevState = MTF_UNIT_MEDIC Then
						PlayMTFSound(LoadTempSound("SFX\Character\MTF\Medic_SearchComplete"+Rand(1,4)+".ogg"),n,True)
					Else
						PlayMTFSound(LoadTempSound("SFX\Character\MTF\Regular_SearchComplete"+Rand(1,4)+".ogg"),n,True)
					EndIf
					If (Not n\IsCalledNPC) Then
						n\State = MTF_WONDERING
					Else
						n\State = MTF_FOLLOWPLAYER
					EndIf
				EndIf
				;[End Block]
			Case MTF_SPLIT_UP
				;[Block]
				v3d_1 = CreateVector3D(962, 1259, 0.3)
				v3d_2 = CreateVector3D(1496, 1524, 0.3)
				NPC_GoToRoom(n, v3d_1, v3d_2, 0.7)
				Delete v3d_1
				Delete v3d_2
				;[End block]
			Case MTF_TOTARGET
				;[Block]
				pvt = CreatePivot()
				PositionEntity pvt, n\EnemyX, n\EnemyY, n\EnemyZ
				If EntityDistanceSquared(n\Collider, pvt) < PowTwo(0.3) Then
					If n\CurrSpeed <= 0.01 Then
						n\CurrSpeed = 0.0
						AnimateNPC(n, 962, 1259, 0.3)
					Else
						n\CurrSpeed = CurveValue(0.0,n\CurrSpeed,20.0)
						AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
					EndIf
				Else
					n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,pvt)
					RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
					n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
					AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
					MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
				EndIf
				pvt = FreeEntity_Strict(pvt)
				;[End Block]
			Case MTF_TESLA
				;[Block]
				AnimateNPC(n, 1260, 1375, 0.25, False)
				If n\Frame = 1375 Then
					If (Not n\IsCalledNPC) Then
						n\State = MTF_WONDERING
					Else
						n\State = MTF_FOLLOWPLAYER
					EndIf
				EndIf
				;[End Block]
			Case STATE_SCRIPT
				;[Block]
				
				;[End Block]
		End Select
		n\IdleTimer = Max(0.0, n\IdleTimer-fps\Factor[0])
	ElseIf n\HP = -1 Then
		EntityType n\Collider, HIT_DEAD
		If n\Frame > 21 Then
			SetNPCFrame(n, 27)
		Else
			AnimateNPC(n, 2, 27, 0.3)
		EndIf
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		n\Reload = 0.0
		
		If n\Frame >= 26.5
			If n\State5 < 70*25 Then
				n\State5 = n\State5 + fps\Factor[0]
			Else
				If n\State5 >= 70*25 And n\State5 < 1000 Then
					n\State5 = 1000
				ElseIf n\State5 >= 1000 And n\State5 < 2000 Then
					EntityAlpha n\obj,Inverse((n\State5-1000.0)/1000.0)
					n\State5 = n\State5 + 2*fps\Factor[0]
				Else
					RemoveNPC(n)
					Return
				EndIf
			EndIf
		EndIf
		
		If n\Gun <> Null And n\Frame >= 15-0.5 Then
			Local bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
			For g = Each Guns
				If g\ID = n\Gun\ID Then
					it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
					EntityType it\Collider, HIT_ITEM
					it\state = n\Gun\Ammo
					it\state2 = n\Gun\MaxAmmo*Rand(1,4)
					it\Dropped = 1
					Exit
				EndIf
			Next
			RemoveNPCGun(n)
		EndIf
	Else
		EntityType n\Collider, HIT_DEAD
		If n\Frame = 1407 Then
			SetNPCFrame(n, 1407)
		Else
			AnimateNPC(n, 1383, 1407, 0.05, False)
		EndIf
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		n\Reload = 0.0
		
		If n\Frame >= 1406.5
			If n\State5 < 70*25 Then
				n\State5 = n\State5 + fps\Factor[0]
			Else
				If n\State5 >= 70*25 And n\State5 < 1000 Then
					n\State5 = 1000
				ElseIf n\State5 >= 1000 And n\State5 < 2000 Then
					EntityAlpha n\obj,Inverse((n\State5-1000.0)/1000.0)
					n\State5 = n\State5 + 2*fps\Factor[0]
				Else
					RemoveNPC(n)
					Return
				EndIf
			EndIf
		EndIf
		
		If n\Gun <> Null And n\Frame >= 1390-0.5 Then
			bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
			For g = Each Guns
				If g\ID = n\Gun\ID Then
					it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
					EntityType it\Collider, HIT_ITEM
					it\state = n\Gun\Ammo
					it\state2 = Rand(0,2)
					it\Dropped = 1
					Exit
				EndIf
			Next
			RemoveNPCGun(n)
		EndIf
	EndIf
	
	If n\HP <= 0 Then
		mtfd\Enabled = False
	EndIf
	
	;Play step sounds
	If n\CurrSpeed > 0.01 Then
		If prevFrame > 500 And n\Frame<495 Then
			sfxstep = GetStepSound(n\Collider,n\CollRadius)
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(sfxstep*MaxStepSounds)], Camera, n\Collider, 8.0, Rnd(0.5,0.7))
		ElseIf prevFrame < 505 And n\Frame=>505
			sfxstep = GetStepSound(n\Collider,n\CollRadius)
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(sfxstep*MaxStepSounds)], Camera, n\Collider, 8.0, Rnd(0.5,0.7))
		ElseIf prevFrame < 1509 And n\Frame=>1509
			sfxstep = GetStepSound(n\Collider,n\CollRadius)
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(sfxstep*MaxStepSounds)], Camera, n\Collider, 8.0, Rnd(0.5,0.7))
		ElseIf prevFrame < 1522 And n\Frame=>1522
			sfxstep = GetStepSound(n\Collider,n\CollRadius)
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(sfxstep*MaxStepSounds)], Camera, n\Collider, 8.0, Rnd(0.5,0.7))
		EndIf
	EndIf
	
	If n\State <> STATE_SCRIPT Then
		If n\State <> MTF_TOTARGET Then
			For n2.NPCs = Each NPCs
				If n2\NPCtype = NPCTypePlotNTF Then
					If n2\ID > n\ID Then
						If EntityDistanceSquared(n2\Collider,n\Collider)<PowTwo(0.5) Then
							TranslateEntity n2\Collider, Cos(EntityYaw(n2\Collider,True)+45)* n\Speed*0.7 * fps\Factor[0], 0, Sin(EntityYaw(n2\Collider,True)+45)* 0.005 * fps\Factor[0], True
						EndIf
					EndIf
				EndIf
			Next
		EndIf
		
		;Push MTF unit aside when player is close (only works in certain states)
		If IsPushable Then
			If PlayerDistSquared<PowTwo(0.7) Then
				n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,Collider)
				RotateEntity n\Collider,0.0,CurveAngle(n\Angle,EntityYaw(n\Collider,True),20.0),0.0,True
				TranslateEntity n\Collider, Cos(EntityYaw(n\Collider,True)-45)* 0.005 * fps\Factor[0], 0, Sin(EntityYaw(n\Collider,True)-45)* 0.005 * fps\Factor[0], True
			EndIf
			If Abs(DeltaYaw(Collider,n\Collider))<80.0 Then
				If PlayerDistSquared<PowTwo(0.7) Then
					TranslateEntity n\Collider, Cos(EntityYaw(Collider,True)+90)* 0.005 * fps\Factor[0], 0, Sin(EntityYaw(Collider,True)+90)* 0.005 * fps\Factor[0], True
				EndIf
			EndIf
		EndIf
	EndIf
	
	;Position the NPC's model (although not if the MTF unit is sitting in the helicopter)
	If Int(n\State)<>MTF_INTRO Lor Int(n\State2)<>0 Then
		RotateEntity n\obj,0.0,EntityYaw(n\Collider,True),0.0,True
		PositionEntity n\obj,EntityX(n\Collider,True),EntityY(n\Collider,True)-n\CollRadius,EntityZ(n\Collider,True),True
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D