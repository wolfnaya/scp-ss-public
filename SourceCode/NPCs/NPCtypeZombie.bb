
;Zombie constants
;[Block]
Const Z_STATE_LYING = 0
Const Z_STATE_STANDUP = 1
Const Z_STATE_WANDER = 2
Const Z_STATE_ATTACK = 3
;[End Block]

Function CreateNPCType049_2(n.NPCs)
	Local temp#
	
	n\NVName = "Human"
	n\NPCName = "Zombie"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	
	If n\obj = 0 Then
		
		If Rand(2) = 1 Then
			n\obj = LoadAnimMesh_Strict("GFX\Npcs\Multiplayer\Zombies\guardzombie2.b3d")
		Else
			n\obj = LoadAnimMesh_Strict("GFX\Npcs\Multiplayer\Zombies\guardzombie.b3d")
		EndIf
		
		temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-049-2", "scale") / 2.5)
		ScaleEntity n\obj, temp, temp, temp
		
		MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	EndIf
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-049-2", "speed") / 100.0)
	
	SetAnimTime(n\obj, 107)
	
	n\Sound = LoadSound_Strict("SFX\SCP\049\0492Breath.ogg")
	
	n\HP = 150
	
	Local random% = Rand(1, 150)
	
	If random <= 15 Then
		n\State4 = 0
	Else
		n\State4 = 1
	EndIf
	
	If n\State4 = 1 Then
		If random <= 42 Then
			SwitchNPCGun%(n, "usp")
		ElseIf random > 42 And random <= 84 Then	
			SwitchNPCGun%(n, "beretta")
		ElseIf random > 84 And random <= 90 Then
			SwitchNPCGun%(n, "p99")
		ElseIf random > 90 Then
			SwitchNPCGun%(n, "fiveseven")
		EndIf
		
		n\Gun\Ammo = n\Gun\MaxAmmo
		n\Gun\ReloadAmmo = 2
	EndIf
	
	CopyHitBoxes(n)
	
End Function

Function CreateNPCType049_3(n.NPCs)
	Local temp#
	
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	
	If n\obj = 0 Then 
		n\obj = LoadAnimMesh_Strict("GFX\Npcs\Multiplayer\Zombies\Zombieclassd.b3d")
		
		temp# = 0.5 / MeshWidth(n\obj)
		ScaleEntity n\obj, temp, temp, temp
		
		MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	EndIf
	
	Local Rndt% = Rand(0,6)
	
;	Select Rndt%
;		Case 0
;			n\texture = "GFX\npcs\Multiplayer\Zombies\ZombieDINF.jpg"
;		Case 1
;			n\texture = "GFX\npcs\Multiplayer\Zombies\dclass_zombie2.jpg"
;		Case 2
;			n\texture = "GFX\npcs\Multiplayer\Zombies\janitor_zombie.jpg"
;		Case 3
;			n\texture = "GFX\npcs\Multiplayer\Zombies\janitor_zombie2.png"
;		Case 4
;			n\texture = "GFX\npcs\Multiplayer\Zombies\scientist_zombie_1.jpg"
;		Case 5
;			n\texture = "GFX\npcs\Multiplayer\Zombies\scientist_zombie_2.png"
;		Case 6
;			n\texture = "GFX\npcs\Multiplayer\Zombies\maintenance_zombie.png"
;	End Select
;	
;	n\texture = LoadTexture_Strict(n\texture, 0, 2)
;	TextureBlend(n\texture,5)
;	EntityTexture(n\obj, n\texture)
	
	n\Speed = 2.0 / 100
	
	n\Sound = LoadSound_Strict("SFX\SCP\049\0492Breath.ogg")
	
	n\HP = 120
	
	CopyHitBoxes(n)
	
End Function

Function CreateNPCType049_4(n.NPCs)
	Local temp#
	
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	
	If n\obj = 0 Then 
		n\obj = LoadAnimMesh_Strict("GFX\Npcs\Multiplayer\Zombies\Zombieworker.b3d")
		
		temp# = 0.52 / MeshWidth(n\obj)
		ScaleEntity n\obj, temp, temp, temp
		
		MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	EndIf
	
	n\Speed = 2.0 / 100
	
	n\Sound = LoadSound_Strict("SFX\SCP\049\0492Breath.ogg")
	
	n\HP = 130
	
	CopyHitBoxes(n)
	
End Function

Function CreateNPCType049_5(n.NPCs)
	Local temp#
	
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	
	If n\obj = 0 Then 
		n\obj = LoadAnimMesh_Strict("GFX\Npcs\Multiplayer\Zombies\Zombieclerk.b3d")
		
		temp# = 0.5 / MeshWidth(n\obj)
		ScaleEntity n\obj, temp, temp, temp
		
		MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	EndIf
	
	n\Speed = 2.0 / 100
	
	n\Sound = LoadSound_Strict("SFX\SCP\049\0492Breath.ogg")
	
	n\HP = 140
	
	CopyHitBoxes(n)
	
End Function

Function CreateNPCType049_6(n.NPCs)
	Local temp#
	
	n\NVName = "Human"
	n\NPCName = "Zombie"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	
;	If Rand(2)=1 Then
;		n\texture = LoadTexture_Strict("GFX\npcs\Multiplayer\Zombies\NineTailedFox_Zombie.png", 0, 2)
;	Else
;		n\texture = LoadTexture_Strict("GFX\npcs\Multiplayer\Zombies\NineTailedFox_Zombie_Medic.png", 0, 2)
;	EndIf
	
	If n\obj = 0 Then 
		n\obj = LoadAnimMesh_Strict("GFX\Npcs\Multiplayer\Zombies\MTFZombie.b3d")
		
;		TextureBlend(n\texture,5)
;		EntityTexture(n\obj, n\texture)
		
		temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-049-2", "scale") / 2.5)
		ScaleEntity n\obj, temp, temp, temp
		
		MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	EndIf
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-049-2", "speed") / 100.0)
	
	n\Sound = LoadSound_Strict("SFX\SCP\049\0492MTFBreath.ogg")
	
	n\HP = 250
	
	Local random% = Rand(1, 150)
	
	If random <= 15 Then
		n\State4 = 0
	Else
		n\State4 = 1
	EndIf
	
	If n\State4 = 1 Then	
		If random <= 42 Then
			SwitchNPCGun%(n, "p90")
		ElseIf random > 42 And random <= 84 Then	
			SwitchNPCGun%(n, "beretta")
		ElseIf random > 84 And random <= 90 Then
			SwitchNPCGun%(n, "mp5k")
		ElseIf random > 90 Then
			SwitchNPCGun%(n, "usp")
		EndIf
		
		n\Gun\Ammo = n\Gun\MaxAmmo
		n\Gun\ReloadAmmo = 3
	EndIf
	
	CopyHitBoxes(n)
	
End Function

Function CreateNPCtype008(n.NPCs)
	Local temp#
	
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	
	If n\obj = 0 Then
		n\obj = LoadAnimMesh_Strict("GFX\Npcs\Multiplayer\Zombies\zombiesurgeon.b3d")
		
		temp# = 0.5 / MeshWidth(n\obj)
		ScaleEntity n\obj, temp, temp, temp
		
		MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
		
	EndIf
	
	n\Speed = 2.0 / 100
	
	SetNPCFrame n,11
	
	n\Sound = LoadSound_Strict("SFX\SCP\049\0492Breath.ogg")
	
	CopyHitBoxes(n)
	
	n\HP = 120
	
End Function

Function CreateNPCtype008_2(n.NPCs)
	Local temp#
	
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	
	If n\obj = 0 Then
		n\obj = LoadAnimMesh_Strict("GFX\Npcs\Multiplayer\Zombies\zombie1.b3d")
		
		temp# = 0.5 / MeshWidth(n\obj)
		ScaleEntity n\obj, temp, temp, temp
		
		MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
		
	EndIf
	
	n\Speed = 2.0 / 100
	
	SetNPCFrame n,11
	
	n\Sound = LoadSound_Strict("SFX\SCP\049\0492Breath.ogg")
	
	CopyHitBoxes(n)
	
	n\HP = 120
	
End Function

Function CreateNPCtype008_3(n.NPCs)
	Local temp#
	
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	
	If n\obj = 0 Then
		n\obj = LoadAnimMesh_Strict("GFX\Npcs\Multiplayer\Zombies\zombie2.b3d")
		
		temp# = 0.6 / MeshWidth(n\obj)
		ScaleEntity n\obj, temp, temp, temp
		
		MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
		
	EndIf
	
	n\Speed = 2.0 / 100
	
	SetNPCFrame n,11
	
	n\Sound = LoadSound_Strict("SFX\SCP\049\0492Breath.ogg")
	
	CopyHitBoxes(n)
	
	n\HP = 120
	
End Function

Function CreateNPCtype008_4(n.NPCs)
	Local temp#
	
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	
	If n\obj = 0 Then
		
		If Rand(2) = 1 Then
			n\State4 = 1
		Else
			n\State4 = 0
		EndIf
		
		If n\State4 = 1 Then
			n\obj = LoadAnimMesh_Strict("GFX\Npcs\SCPs\Cave_Zombies\Cave_Zombie.b3d")
		Else
			n\obj = LoadAnimMesh_Strict("GFX\Npcs\SCPs\Cave_Zombies\Cave_Zombie_2.b3d")
		EndIf
		
		temp# = 0.6 / MeshWidth(n\obj)
		ScaleEntity n\obj, temp, temp, temp
		
		n\Speed = 4.0 / 100
		
		MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	EndIf
	
	n\Sound = LoadSound_Strict("SFX\SCP\049\0492Breath.ogg")
	
	n\HP = 200
	
	CopyHitBoxes(n)
	
End Function

Function CreateNPCtype008_5(n.NPCs)
	Local temp#
	
	n\NVName = "Human"
	n\NPCName = "008"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	
	If n\obj = 0 Then 
		n\obj = LoadAnimMesh_Strict("GFX\Npcs\Multiplayer\Zombies\zombie1.b3d")
		
		temp# = 0.5 / MeshWidth(n\obj)
		ScaleEntity n\obj, temp, temp, temp
		
		MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	EndIf
	
	n\Speed = 2.0 / 100
	
	n\Sound = LoadSound_Strict("SFX\SCP\049\0492Normal.ogg")
	
	n\HP = 400
	
	Local random% = Rand(1, 150)
	
	If random <= 20 Then
		n\State4 = 0
	Else
		n\State4 = 1
	EndIf
	
	If n\State4 = 1 Then
		If random <= 42 Then
			SwitchNPCGun%(n, "usp")
		ElseIf random > 42 And random <= 84 Then	
			SwitchNPCGun%(n, "beretta")
		ElseIf random > 84 And random <= 90 Then
			SwitchNPCGun%(n, "p99")
		ElseIf random > 90 Then
			SwitchNPCGun%(n, "fiveseven")
		EndIf
		
		n\Gun\Ammo = n\Gun\MaxAmmo
	Else
		SwitchNPCGun%(n, "knife")
	EndIf
	
	CopyHitBoxes(n)
	
End Function

Function CreateNPCTypeElias(n.NPCs)
	Local temp#
	
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	
	If n\obj = 0 Then 
		n\obj = LoadAnimMesh_Strict("GFX\NPCs\Humans\Personnel\Elias.b3d")
		
		temp# = 0.52 / MeshWidth(n\obj)
		ScaleEntity n\obj, temp, temp, temp
		
		MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	EndIf
	
	n\Speed = 2.0 / 100
	
	n\Sound = LoadSound_Strict("SFX\SCP\409\409ZombieBreath.ogg")
	
	n\HP = 690.420 										   ; ~ ;)
	
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCType049_2(n.NPCs)
	Local prevFrame#
	Local g.Guns,it.Items
	
	If Abs(EntityY(Collider)-EntityY(n\Collider))<4.0 Then
		
		prevFrame# = n\Frame
		
		n\Reload = Max(0, n\Reload - fps\Factor[0])
		
		Local bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
		Local dist# = EntityDistanceSquared(n\Collider, Collider)
		
		If (Not n\IsDead)
			Select n\State
				Case Z_STATE_LYING
					;[Block]
					AnimateNPC(n, 1, 3, 0.2, False)
					
					If n\Frame=3 Then
						If Rand(700)=1 Then
							If EntityDistanceSquared(Collider, n\Collider)<PowTwo(5.0) Then
								n\Frame = 4
							EndIf
						EndIf
					EndIf
					;[End Block]
				Case Z_STATE_STANDUP ;stands up
					;[Block]
					If n\Frame=>4 Then 
						AnimateNPC(n, 4, 69, 0.3, False)
						If n\Frame = 69 Then n\State = Z_STATE_WANDER
					Else
						AnimateNPC(n, 4, 69, 1.5, False)
					EndIf
					;[End Block]
				Case Z_STATE_WANDER ;following the player
					;[Block]
					If n\State3 < 0 Then ;check if the player is visible every three seconds
						If EntityDistanceSquared(Collider, n\Collider) < PowTwo(5.0) Then 
							If EntityVisible(Collider, n\Collider) Then n\State2 = 70*5
						EndIf
						n\State3=70*3
					Else
						n\State3=n\State3-fps\Factor[0]
					EndIf
					
					If n\State2 > 0 And (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then ;player is visible -> attack
						n\SoundChn = LoopSound2(n\Sound, n\SoundChn, Camera, n\Collider, 6.0, 0.6)
						
						n\PathStatus = 0
						
						PointEntity n\obj, Collider
						RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 30.0), 0
						
						; ~ Shooting
						If n\Gun <> Null Then
							If n\State4 = 2 Then
								For g = Each Guns
									If g\ID = n\Gun\ID Then
										it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
										EntityType it\Collider, HIT_ITEM
										it\state = n\Gun\Ammo
										Select Rand(2)
											Case 0
												it\state2 = 0
											Case 1
												it\state2 = n\Gun\MaxAmmo
											Case 2
												it\state2 = n\Gun\MaxAmmo+n\Gun\MaxAmmo
										End Select
										it\Dropped = 1
										Exit
									EndIf
								Next
								RemoveNPCGun(n)
							EndIf
							If n\State4 = 1 Then
								If n\Gun\Ammo > 0 Then
									If n\Reload = 0 Then
										If EntityVisible(Collider, n\Collider) Then
											n\Gun\Ammo = n\Gun\Ammo - 1
											Local flashpvt% = CreatePivot()
											
											RotateEntity(flashpvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
											PositionEntity(flashpvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
											MoveEntity (flashpvt,2*0.079, 8.8*0.079, 6.0*0.079)
											ShootPlayer(EntityX(flashpvt), EntityY(flashpvt), EntityZ(flashpvt), Clamp(2 / dist, 0.0, 0.65), True, n\Gun\Damage * Rand(1, 1.25))
											If n\Gun\MaxGunshotSounds = 1 Then
												n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 25)
											Else
												n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 25)
											EndIf
											If n\Gun\GunType = GUNTYPE_HANDGUN Then
												n\Reload = Rand(25,50)
											Else
												n\Reload = Rand(10,15)
											EndIf
										EndIf
									EndIf
								EndIf
							EndIf
						EndIf
						
						If n\Gun <> Null Then
							If n\Gun\ReloadAmmo = 0 And n\Gun\Ammo = 0 Then
								n\State4 = 2
							EndIf
						EndIf
						
						If EntityDistanceSquared(Collider, n\Collider) < PowTwo(0.7) Then 
							n\State = Z_STATE_ATTACK
							If n\Gun <> Null Then
								If Rand(4)=1 Then
									n\Frame = 255
								Else
									n\Frame = 306
								EndIf
							Else
								If Rand(2)=1 Then
									n\Frame = 255
								Else
									n\Frame = 306
								EndIf
							EndIf
						Else
							n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
							
							If n\State4 = 1 Then
								If n\Gun\ReloadAmmo > 0 Then
									If n\Gun\Ammo = 0 Then
										n\Reload = 210
										AnimateNPC(n,486,607,n\CurrSpeed*60,False)
										If n\Frame >= 487 And n\Frame < 487.5 Then
											If n\Gun\MaxReloadSounds = 1 Then
												n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\reload_empty.ogg"), Camera, n\Collider, 25)
											Else
												n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\reload_empty.ogg"), Camera, n\Collider, 25)
											EndIf
										EndIf
										If n\Frame >=606.5 Then 
											n\Gun\Ammo = n\Gun\MaxAmmo
											n\Gun\ReloadAmmo = n\Gun\ReloadAmmo - 1
										EndIf
									Else
										AnimateNPC(n,425,485,n\CurrSpeed*60)
									EndIf
								Else
									AnimateNPC(n,425,485,n\CurrSpeed*60)
								EndIf
							Else
								AnimateNPC(n,194,254,n\CurrSpeed*60)
							EndIf
						EndIf
						
						n\State2=n\State2-fps\Factor[0]
					Else
						If n\PathStatus = 1 Then ;path found
							If n\Path[n\PathLocation]=Null Then 
								If n\PathLocation > 19 Then 
									n\PathLocation = 0 : n\PathStatus = 0
								Else
									n\PathLocation = n\PathLocation + 1
								EndIf
							Else
								PointEntity n\obj, n\Path[n\PathLocation]\obj
								
								RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 30.0), 0
								n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
								MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
								
								AnimateNPC(n, 195, 255, n\CurrSpeed*60)
								
								If EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj) < PowTwo(0.2) Then
									n\PathLocation = n\PathLocation + 1
								EndIf 
								
								;opens doors in front of him
								
								Local dist2# = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
								If dist2<PowTwo(0.6) Then
									Local temp = True
									If n\Path[n\PathLocation]\door <> Null Then
										If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
											If n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\KeyCard>0 Lor n\Path[n\PathLocation]\door\Code<>"" Then
												temp = False
											Else
												If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
											EndIf
										EndIf
									EndIf
									If dist2<PowTwo(0.2) And temp
										n\PathLocation = n\PathLocation + 1
									ElseIf dist2<PowTwo(0.5) And (Not temp)
										n\PathStatus = 0
										n\PathTimer# = 0.0
									EndIf
								EndIf
							EndIf
						Else ;no path to the player, stands still
							n\CurrSpeed = 0
							AnimateNPC(n, 70, 194, 0.7)
							
							n\PathTimer = n\PathTimer-fps\Factor[0]
							If n\PathTimer =< 0 Then
								n\PathStatus = FindPath(n, EntityX(Collider),EntityY(Collider)+0.1,EntityZ(Collider))
								n\PathTimer = n\PathTimer+70*5
							EndIf
						EndIf
					EndIf
					
					;65, 80, 93, 109, 123
					If n\CurrSpeed > 0.005 Then
						If (prevFrame < 231 And n\Frame=>231) Lor (prevFrame > 255 And n\Frame<240) Then
							PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
						EndIf
					EndIf
					;[End Block]
				Case Z_STATE_ATTACK
					;[Block]
					If (I_268\Using > 0 And I_268\Timer > 0) Then n\State = Z_STATE_WANDER
					If n\Frame < 306 Then
						AnimateNPC(n, 255, 305, 0.7, False)
						If prevFrame < 277 And n\Frame=>277 Then
							If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
								If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
									PlaySound_Strict DamageSFX[Rand(5,8)]
									If hds\Wearing And hds\Health < 35 Lor (Not hds\Wearing) Then
										PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Collider)
									EndIf
									DamageSPPlayer(Rnd(10.0,20.0))
									m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp049_2_death",Designation)
								EndIf
							EndIf
						ElseIf n\Frame=305 Then
							n\State = Z_STATE_WANDER
						EndIf							
					Else
						AnimateNPC(n, 306, 356, 0.7, False)
						If prevFrame < 332 And n\Frame=>332 Then
							If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
								If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
									PlaySound_Strict DamageSFX[Rand(5,8)]
									PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Collider)
									If I_1033RU\HP = 0
										DamageSPPlayer(Rnd(10.0,20.0))
									Else
										Damage1033RU(30 + 5 * SelectedDifficulty\AggressiveNPCs)
										n\HP = n\HP - Rnd(50)
										If n\HP =< 0 Then
											n\IsDead = True
										EndIf
									EndIf
									m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp049_2_death",Designation)
								EndIf
							EndIf
						ElseIf n\Frame=356 Then
							n\State = Z_STATE_WANDER
						EndIf		
					EndIf
					;[End Block]
			End Select
		Else
			If n\SoundChn <> 0
				StopChannel n\SoundChn
				n\SoundChn = 0
				FreeSound_Strict n\Sound
				n\Sound = 0
			EndIf
			AnimateNPC(n, 357, 393, 0.5, False)
			
			If n\Frame >= 392.5
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
			
			If n\Gun <> Null And n\Frame >= 393-0.5 Then
				For g = Each Guns
					If g\ID = n\Gun\ID Then
						it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
						EntityType it\Collider, HIT_ITEM
						it\state = n\Gun\Ammo
						Select Rand(2)
							Case 0
								it\state2 = 0
							Case 1
								it\state2 = n\Gun\MaxAmmo
							Case 2
								it\state2 = n\Gun\MaxAmmo+n\Gun\MaxAmmo
						End Select
						it\Dropped = 1
						Exit
					EndIf
				Next
				RemoveNPCGun(n)
			EndIf
		EndIf
		
		If n\HP <= 0 Then
			n\IsDead = True
			If n\Frame > 393 Then
				SetNPCFrame(n, 357)
			EndIf
		EndIf
		
		PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.2, EntityZ(n\Collider))
		
		RotateEntity n\obj, -90, EntityYaw(n\Collider), 0
	EndIf
	
End Function

Function UpdateNPCType049_3(n.NPCs)
	Local w.WayPoints
	Local i%, dist#, dist2#, angle#, x#, y#, z#, PlayerSeeAble%, RN$
	
	Local target
	
	Local pick%
	
	Local sfxstep%
	
	Local prevFrame#
	
	;[Block]
	;n\State: Main State
	;n\State2: A timer used for the player detection
	;n\State3: A timer for making the NPC idle (if the player escapes during that time)
	
	If (Not n\IsDead)
		If n\State = 0
			EntityType n\Collider,HIT_DEAD
		Else
			EntityType n\Collider,HIT_PLAYER
		EndIf
		
		prevFrame = n\Frame
		
		n\BlinkTimer = 1
		
		Select n\State
			Case 0 ;Lying next to the wall
				SetNPCFrame(n,11)
			Case 1 ;Standing up
				AnimateNPC(n,11,32,0.1,False)
				If n\Frame => 29
					n\State = 2
				EndIf
			Case 2 ;Being active
				PlayerSeeAble = MeNPCSeesPlayer(n)
				If PlayerSeeAble=1 Lor n\State2 > 0.0 And (I_268\Using = 0 Lor I_268\Timer =< 0.0)
					If PlayerSeeAble=1
						n\State2 = 70*2
					Else
						n\State2 = Max(n\State2-fps\Factor[0],0)
					EndIf
					PointEntity n\obj, Collider
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
					
					AnimateNPC(n, 64, 93, n\CurrSpeed*30)
					n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
					MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
					
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.0)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							n\State = 3
						EndIf
					EndIf
					
					n\PathTimer = 0
					n\PathStatus = 0
					n\PathLocation = 0
					n\State3 = 0
				Else
					If n\PathStatus = 1
						If n\Path[n\PathLocation]=Null Then 
							If n\PathLocation > 19 Then 
								n\PathLocation = 0 : n\PathStatus = 0
							Else
								n\PathLocation = n\PathLocation + 1
							EndIf
						Else
							PointEntity n\obj, n\Path[n\PathLocation]\obj
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
							
							AnimateNPC(n, 64, 93, n\CurrSpeed*30)
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
							
							;opens doors in front of him
							
							dist2# = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
							If dist2<PowTwo(0.6) Then
								temp = True
								If n\Path[n\PathLocation]\door <> Null Then
									If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
										If n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\KeyCard>0 Lor n\Path[n\PathLocation]\door\Code<>"" Then
											temp = False
										Else
											If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
										EndIf
									EndIf
								EndIf
								If dist2<PowTwo(0.2) And temp
									n\PathLocation = n\PathLocation + 1
								ElseIf dist2<PowTwo(0.5) And (Not temp)
									n\PathStatus = 0
									n\PathTimer# = 0.0
								EndIf
							EndIf
						EndIf
					Else
						AnimateNPC(n, 167, 202, 0.2, True)
						n\CurrSpeed = 0
						If n\PathTimer < 70*5
							n\PathTimer = n\PathTimer + Rnd(1,2+(2*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0]
						Else
							n\PathStatus = FindPath(n,EntityX(Collider),EntityY(Collider),EntityZ(Collider))
							n\PathTimer = 0
						EndIf
					EndIf
					
					If EntityDistanceSquared(n\Collider,Collider)>PowTwo(HideDistance)
						If n\State3 < 70*(15+(10*SelectedDifficulty\AggressiveNPCs))
							n\State3 = n\State3+fps\Factor[0]
						Else
							DebugLog "SCP-049-3 IDLE"
							n\State3 = 70*(6*60)
							n\State = 4
						EndIf
					EndIf
				EndIf
				
				If n\CurrSpeed > 0.005 Then
					If (prevFrame < 80 And n\Frame=>80) Lor (prevFrame > 92 And n\Frame<65)
						PlaySound2(StepSFX(0,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
					EndIf
				EndIf
				
				n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider)
			Case 3 ;Attacking
				AnimateNPC(n, 126, 165, 0.4, False)
				If (n\Frame => 146 And prevFrame < 146)
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							PlaySound_Strict DamageSFX[Rand(5,8)]
							If hds\Wearing And hds\Health < 35 Lor (Not hds\Wearing) Then
								PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Collider)
							EndIf
							If I_1033RU\HP = 0
								DamageSPPlayer(Rnd(10.0,20.0))
							Else
								Damage1033RU(30 + 5 * SelectedDifficulty\AggressiveNPCs)
								n\HP = n\HP - Rnd(50)
								If n\HP =< 0 Then
									n\IsDead = True
								EndIf
							EndIf
							;I_008\Timer = I_008\Timer + (1+(1*SelectedDifficulty\aggressiveNPCs))
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp049_2_death",Designation)
						EndIf
					EndIf
				ElseIf n\Frame => 164
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							SetNPCFrame(n,126)
						Else
							n\State = 2
						EndIf
					Else
						n\State = 2
					EndIf
				EndIf
			Case 4 ;Idling
				HideEntity n\obj
				HideEntity n\Collider
				n\DropSpeed = 0
				PositionEntity n\Collider,0,500,0
				ResetEntity n\Collider
				If n\Idle > 0
					n\Idle = Max(n\Idle-(1+(1*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0],0)
				Else
					If PlayerInReachableRoom() ;Player is in a room where SCP-008-1 can teleport to
						If Rand(50-(20*SelectedDifficulty\AggressiveNPCs))=1
							ShowEntity n\Collider
							ShowEntity n\obj
							For w.WayPoints = Each WayPoints
								If w\door=Null And w\room\dist < HideDistance And Rand(3)=1 Then
									If EntityDistanceSquared(w\room\obj,n\Collider)<EntityDistanceSquared(Collider,n\Collider)
										x = Abs(EntityX(n\Collider)-EntityX(w\obj,True))
										If x < 12.0 And x > 4.0 Then
											z = Abs(EntityZ(n\Collider)-EntityZ(w\obj,True))
											If z < 12 And z > 4.0 Then
												If w\room\dist > 4
													DebugLog "MOVING 049-3 TO "+w\room\RoomTemplate\Name
													PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True)
													ResetEntity n\Collider
													n\PathStatus = 0
													n\PathTimer# = 0.0
													n\PathLocation = 0
													Exit
												EndIf
											EndIf
										EndIf
									EndIf
								EndIf
							Next
							n\State = 2
							n\State3 = 0
						EndIf
					EndIf
				EndIf
		End Select
	Else
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		AnimateNPC(n, 590, 639, 0.5, False)
		
		If n\Frame >= 638.5
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
		
	EndIf
	
	If n\HP <= 0 Then
		n\IsDead = True
	EndIf
	
	RotateEntity n\obj,0,EntityYaw(n\Collider)-180,0
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	;[End Block]
	
End Function

Function UpdateNPCType049_4(n.NPCs)
	Local w.WayPoints
	Local i%, dist#, dist2#, angle#, x#, y#, z#, PlayerSeeAble%, RN$
	
	Local target
	
	Local pick%
	
	Local sfxstep%
	
	Local prevFrame#
	
	;[Block]
	;n\State: Main State
	;n\State2: A timer used for the player detection
	;n\State3: A timer for making the NPC idle (if the player escapes during that time)
	
	If (Not n\IsDead)
		If n\State = 0
			EntityType n\Collider,HIT_DEAD
		Else
			EntityType n\Collider,HIT_PLAYER
		EndIf
		
		prevFrame = n\Frame
		
		n\BlinkTimer = 1
		
		Select n\State
			Case 0 ;Lying next to the wall
				SetNPCFrame(n,11)
			Case 1 ;Standing up
				AnimateNPC(n,11,32,0.1,False)
				If n\Frame => 29
					n\State = 2
				EndIf
			Case 2 ;Being active
				PlayerSeeAble = MeNPCSeesPlayer(n)
				If PlayerSeeAble=1 Lor n\State2 > 0.0 And (I_268\Using = 0 Lor I_268\Timer =< 0.0)
					If PlayerSeeAble=1
						n\State2 = 70*2
					Else
						n\State2 = Max(n\State2-fps\Factor[0],0)
					EndIf
					PointEntity n\obj, Collider
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
					
					AnimateNPC(n, 64, 93, n\CurrSpeed*30)
					n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
					MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
					
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.0)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							n\State = 3
						EndIf
					EndIf
					
					n\PathTimer = 0
					n\PathStatus = 0
					n\PathLocation = 0
					n\State3 = 0
				Else
					If n\PathStatus = 1
						If n\Path[n\PathLocation]=Null Then 
							If n\PathLocation > 19 Then 
								n\PathLocation = 0 : n\PathStatus = 0
							Else
								n\PathLocation = n\PathLocation + 1
							EndIf
						Else
							PointEntity n\obj, n\Path[n\PathLocation]\obj
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
							
							AnimateNPC(n, 64, 93, n\CurrSpeed*30)
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
							
							;opens doors in front of him
							
							dist2# = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
							If dist2<PowTwo(0.6) Then
								temp = True
								If n\Path[n\PathLocation]\door <> Null Then
									If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
										If n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\KeyCard>0 Lor n\Path[n\PathLocation]\door\Code<>"" Then
											temp = False
										Else
											If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
										EndIf
									EndIf
								EndIf
								If dist2<PowTwo(0.2) And temp
									n\PathLocation = n\PathLocation + 1
								ElseIf dist2<PowTwo(0.5) And (Not temp)
									n\PathStatus = 0
									n\PathTimer# = 0.0
								EndIf
							EndIf
						EndIf
					Else
						AnimateNPC(n, 167, 202, 0.2, True)
						n\CurrSpeed = 0
						If n\PathTimer < 70*5
							n\PathTimer = n\PathTimer + Rnd(1,2+(2*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0]
						Else
							n\PathStatus = FindPath(n,EntityX(Collider),EntityY(Collider),EntityZ(Collider))
							n\PathTimer = 0
						EndIf
					EndIf
					
					If EntityDistanceSquared(n\Collider,Collider)>PowTwo(HideDistance)
						If n\State3 < 70*(15+(10*SelectedDifficulty\AggressiveNPCs))
							n\State3 = n\State3+fps\Factor[0]
						Else
							DebugLog "SCP-049-4 IDLE"
							n\State3 = 70*(6*60)
							n\State = 4
						EndIf
					EndIf
				EndIf
				
				If n\CurrSpeed > 0.005 Then
					If (prevFrame < 80 And n\Frame=>80) Lor (prevFrame > 92 And n\Frame<65)
						PlaySound2(StepSFX(0,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
					EndIf
				EndIf
				
				n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider)
			Case 3 ;Attacking
				AnimateNPC(n, 126, 165, 0.4, False)
				If (n\Frame => 146 And prevFrame < 146)
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							PlaySound_Strict DamageSFX[Rand(5,8)]
							If hds\Wearing And hds\Health < 35 Lor (Not hds\Wearing) Then
								PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Collider)
							EndIf
							If I_1033RU\HP = 0
								DamageSPPlayer(Rnd(10.0,20.0))
							Else
								Damage1033RU(30 + 5 * SelectedDifficulty\AggressiveNPCs)
								n\HP = n\HP - Rnd(50)
								If n\HP =< 0 Then
									n\IsDead = True
								EndIf
							EndIf
							;I_008\Timer = I_008\Timer + (1+(1*SelectedDifficulty\aggressiveNPCs))
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp049_2_death",Designation)
						EndIf
					EndIf
				ElseIf n\Frame => 164
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							SetNPCFrame(n,126)
						Else
							n\State = 2
						EndIf
					Else
						n\State = 2
					EndIf
				EndIf
			Case 4 ;Idling
				HideEntity n\obj
				HideEntity n\Collider
				n\DropSpeed = 0
				PositionEntity n\Collider,0,500,0
				ResetEntity n\Collider
				If n\Idle > 0
					n\Idle = Max(n\Idle-(1+(1*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0],0)
				Else
					If PlayerInReachableRoom() ;Player is in a room where SCP-008-1 can teleport to
						If Rand(50-(20*SelectedDifficulty\AggressiveNPCs))=1
							ShowEntity n\Collider
							ShowEntity n\obj
							For w.WayPoints = Each WayPoints
								If w\door=Null And w\room\dist < HideDistance And Rand(3)=1 Then
									If EntityDistanceSquared(w\room\obj,n\Collider)<EntityDistanceSquared(Collider,n\Collider)
										x = Abs(EntityX(n\Collider)-EntityX(w\obj,True))
										If x < 12.0 And x > 4.0 Then
											z = Abs(EntityZ(n\Collider)-EntityZ(w\obj,True))
											If z < 12 And z > 4.0 Then
												If w\room\dist > 4
													DebugLog "MOVING 049-4 TO "+w\room\RoomTemplate\Name
													PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True)
													ResetEntity n\Collider
													n\PathStatus = 0
													n\PathTimer# = 0.0
													n\PathLocation = 0
													Exit
												EndIf
											EndIf
										EndIf
									EndIf
								EndIf
							Next
							n\State = 2
							n\State3 = 0
						EndIf
					EndIf
				EndIf
		End Select
	Else
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		AnimateNPC(n, 642, 699, 0.5, False)
		
		If n\Frame >= 698.5
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
		
	EndIf
	
	If n\HP <= 0 Then
		n\IsDead = True
	EndIf
	
	RotateEntity n\obj,0,EntityYaw(n\Collider)-180,0
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	;[End Block]
	
End Function

Function UpdateNPCtype008(n.NPCs)
	Local w.WayPoints
	Local i%, dist#, dist2#, angle#, x#, y#, z#, PlayerSeeAble%, RN$
	
	Local target
	
	Local pick%
	
	Local sfxstep%
	
	Local prevFrame#
	
	;[Block]
	;n\State: Main State
	;n\State2: A timer used for the player detection
	;n\State3: A timer for making the NPC idle (if the player escapes during that time)
	
	If (Not n\IsDead)
		If n\State = 0
			EntityType n\Collider,HIT_DEAD
		Else
			EntityType n\Collider,HIT_PLAYER
		EndIf
		
		prevFrame = n\Frame
		
		n\BlinkTimer = 1
		
		Select n\State
			Case 0 ;Lying next to the wall
				SetNPCFrame(n,11)
			Case 1 ;Standing up
				AnimateNPC(n,11,32,0.1,False)
				If n\Frame => 29
					n\State = 2
				EndIf
			Case 2 ;Being active
				PlayerSeeAble = MeNPCSeesPlayer(n)
				If PlayerSeeAble=1 Lor n\State2 > 0.0 And (I_268\Using = 0 Lor I_268\Timer =< 0.0)
					If PlayerSeeAble=1
						n\State2 = 70*2
					Else
						n\State2 = Max(n\State2-fps\Factor[0],0)
					EndIf
					PointEntity n\obj, Collider
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
					
					AnimateNPC(n, 64, 93, n\CurrSpeed*30)
					n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
					MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
					
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.0)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							n\State = 3
						EndIf
					EndIf
					
					n\PathTimer = 0
					n\PathStatus = 0
					n\PathLocation = 0
					n\State3 = 0
				Else
					If n\PathStatus = 1
						If n\Path[n\PathLocation]=Null Then 
							If n\PathLocation > 19 Then 
								n\PathLocation = 0 : n\PathStatus = 0
							Else
								n\PathLocation = n\PathLocation + 1
							EndIf
						Else
							PointEntity n\obj, n\Path[n\PathLocation]\obj
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
							
							AnimateNPC(n, 64, 93, n\CurrSpeed*30)
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
							
							;opens doors in front of him
							
							dist2# = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
							If dist2<PowTwo(0.6) Then
								temp = True
								If n\Path[n\PathLocation]\door <> Null Then
									If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
										If n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\KeyCard>0 Lor n\Path[n\PathLocation]\door\Code<>"" Then
											temp = False
										Else
											If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
										EndIf
									EndIf
								EndIf
								If dist2<PowTwo(0.2) And temp
									n\PathLocation = n\PathLocation + 1
								ElseIf dist2<PowTwo(0.5) And (Not temp)
									n\PathStatus = 0
									n\PathTimer# = 0.0
								EndIf
							EndIf
						EndIf
					Else
						AnimateNPC(n, 167, 202, 0.2, True)
						n\CurrSpeed = 0
						If n\PathTimer < 70*5
							n\PathTimer = n\PathTimer + Rnd(1,2+(2*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0]
						Else
							n\PathStatus = FindPath(n,EntityX(Collider),EntityY(Collider),EntityZ(Collider))
							n\PathTimer = 0
						EndIf
					EndIf
					
					If EntityDistanceSquared(n\Collider,Collider)>PowTwo(HideDistance)
						If n\State3 < 70*(15+(10*SelectedDifficulty\AggressiveNPCs))
							n\State3 = n\State3+fps\Factor[0]
						Else
							DebugLog "SCP-008 IDLE"
							n\State3 = 70*(6*60)
							n\State = 4
						EndIf
					EndIf
				EndIf
				
				If n\CurrSpeed > 0.005 Then
					If (prevFrame < 80 And n\Frame=>80) Lor (prevFrame > 92 And n\Frame<65)
						PlaySound2(StepSFX(0,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
					EndIf
				EndIf
				
				n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider)
			Case 3 ;Attacking
				AnimateNPC(n, 126, 165, 0.4, False)
				If (n\Frame => 146 And prevFrame < 146)
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							PlaySound_Strict DamageSFX[Rand(5,8)]
							If hds\Wearing And hds\Health < 35 Lor (Not hds\Wearing) Then
								PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Collider)
							EndIf
							If I_1033RU\HP = 0
								DamageSPPlayer(Rnd(10.0,20.0))
							Else
								Damage1033RU(30 + 5 * SelectedDifficulty\AggressiveNPCs)
								n\HP = n\HP - Rnd(50)
								If n\HP =< 0 Then
									n\IsDead = True
								EndIf
							EndIf
							;I_008\Timer = I_008\Timer + (1+(1*SelectedDifficulty\aggressiveNPCs))
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp049_2_death",Designation)
						EndIf
					EndIf
				ElseIf n\Frame => 164
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							SetNPCFrame(n,126)
						Else
							n\State = 2
						EndIf
					Else
						n\State = 2
					EndIf
				EndIf
			Case 4 ;Idling
				HideEntity n\obj
				HideEntity n\Collider
				n\DropSpeed = 0
				PositionEntity n\Collider,0,500,0
				ResetEntity n\Collider
				If n\Idle > 0
					n\Idle = Max(n\Idle-(1+(1*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0],0)
				Else
					If PlayerInReachableRoom() ;Player is in a room where SCP-008-1 can teleport to
						If Rand(50-(20*SelectedDifficulty\AggressiveNPCs))=1
							ShowEntity n\Collider
							ShowEntity n\obj
							For w.WayPoints = Each WayPoints
								If w\door=Null And w\room\dist < HideDistance And Rand(3)=1 Then
									If EntityDistanceSquared(w\room\obj,n\Collider)<EntityDistanceSquared(Collider,n\Collider)
										x = Abs(EntityX(n\Collider)-EntityX(w\obj,True))
										If x < 12.0 And x > 4.0 Then
											z = Abs(EntityZ(n\Collider)-EntityZ(w\obj,True))
											If z < 12 And z > 4.0 Then
												If w\room\dist > 4
													DebugLog "MOVING 008 TO "+w\room\RoomTemplate\Name
													PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True)
													ResetEntity n\Collider
													n\PathStatus = 0
													n\PathTimer# = 0.0
													n\PathLocation = 0
													Exit
												EndIf
											EndIf
										EndIf
									EndIf
								EndIf
							Next
							n\State = 2
							n\State3 = 0
						EndIf
					EndIf
				EndIf
		End Select
	Else
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		AnimateNPC(n, 590, 639, 0.5, False)
		
		If n\Frame >= 638.5
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
		
	EndIf
	
	If n\HP <= 0 Then
		n\IsDead = True
	EndIf
	
	RotateEntity n\obj,0,EntityYaw(n\Collider)-180,0
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	;[End Block]
	
End Function

Function UpdateNPCtype008_2(n.NPCs)
	Local w.WayPoints
	Local i%, dist#, dist2#, angle#, x#, y#, z#, PlayerSeeAble%, RN$
	
	Local target
	
	Local pick%
	
	Local sfxstep%
	
	Local prevFrame#
	
	;[Block]
				;n\State: Main State
				;n\State2: A timer used for the player detection
				;n\State3: A timer for making the NPC idle (if the player escapes during that time)
	
	If (Not n\IsDead)
		If n\State = 0
			EntityType n\Collider,HIT_DEAD
		Else
			EntityType n\Collider,HIT_PLAYER
		EndIf
		
		prevFrame = n\Frame
		
		n\BlinkTimer = 1
		
		Select n\State
			Case 0 ;Lying next to the wall
				SetNPCFrame(n,11)
			Case 1 ;Standing up
				AnimateNPC(n,11,32,0.1,False)
				If n\Frame => 29
					n\State = 2
				EndIf
			Case 2 ;Being active
				PlayerSeeAble = MeNPCSeesPlayer(n)
				If PlayerSeeAble=1 Lor n\State2 > 0.0 And (I_268\Using = 0 Lor I_268\Timer =< 0.0)
					If PlayerSeeAble=1
						n\State2 = 70*2
					Else
						n\State2 = Max(n\State2-fps\Factor[0],0)
					EndIf
					PointEntity n\obj, Collider
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
					
					AnimateNPC(n, 64, 93, n\CurrSpeed*30)
					n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
					MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
					
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.0)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							n\State = 3
						EndIf
					EndIf
					
					n\PathTimer = 0
					n\PathStatus = 0
					n\PathLocation = 0
					n\State3 = 0
				Else
					If n\PathStatus = 1
						If n\Path[n\PathLocation]=Null Then 
							If n\PathLocation > 19 Then 
								n\PathLocation = 0 : n\PathStatus = 0
							Else
								n\PathLocation = n\PathLocation + 1
							EndIf
						Else
							PointEntity n\obj, n\Path[n\PathLocation]\obj
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
							
							AnimateNPC(n, 64, 93, n\CurrSpeed*30)
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
							
							;opens doors in front of him
							
							dist2# = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
							If dist2<PowTwo(0.6) Then
								temp = True
								If n\Path[n\PathLocation]\door <> Null Then
									If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
										If n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\KeyCard>0 Lor n\Path[n\PathLocation]\door\Code<>"" Then
											temp = False
										Else
											If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
										EndIf
									EndIf
								EndIf
								If dist2<PowTwo(0.2) And temp
									n\PathLocation = n\PathLocation + 1
								ElseIf dist2<PowTwo(0.5) And (Not temp)
									n\PathStatus = 0
									n\PathTimer# = 0.0
								EndIf
							EndIf
						EndIf
					Else
						AnimateNPC(n, 167, 202, 0.2, True)
						n\CurrSpeed = 0
						If n\PathTimer < 70*5
							n\PathTimer = n\PathTimer + Rnd(1,2+(2*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0]
						Else
							n\PathStatus = FindPath(n,EntityX(Collider),EntityY(Collider),EntityZ(Collider))
							n\PathTimer = 0
						EndIf
					EndIf
					
					If EntityDistanceSquared(n\Collider,Collider)>PowTwo(HideDistance)
						If n\State3 < 70*(15+(10*SelectedDifficulty\AggressiveNPCs))
							n\State3 = n\State3+fps\Factor[0]
						Else
							DebugLog "SCP-008-2 IDLE"
							n\State3 = 70*(6*60)
							n\State = 4
						EndIf
					EndIf
				EndIf
				
				If n\CurrSpeed > 0.005 Then
					If (prevFrame < 80 And n\Frame=>80) Lor (prevFrame > 92 And n\Frame<65)
						PlaySound2(StepSFX(0,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
					EndIf
				EndIf
				
				n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider)
			Case 3 ;Attacking
				AnimateNPC(n, 126, 165, 0.4, False)
				If (n\Frame => 146 And prevFrame < 146)
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							PlaySound_Strict DamageSFX[Rand(5,8)]
							If hds\Wearing And hds\Health < 35 Lor (Not hds\Wearing) Then
								PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Collider)
							EndIf
							If I_1033RU\HP = 0
								DamageSPPlayer(Rnd(10.0,20.0))
							Else
								Damage1033RU(30 + 5 * SelectedDifficulty\AggressiveNPCs)
								n\HP = n\HP - Rnd(50)
								If n\HP =< 0 Then
									n\IsDead = True
								EndIf
							EndIf
										;I_008\Timer = I_008\Timer + (1+(1*SelectedDifficulty\aggressiveNPCs))
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp008_1_death",Designation)
						EndIf
					EndIf
				ElseIf n\Frame => 164
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							SetNPCFrame(n,126)
						Else
							n\State = 2
						EndIf
					Else
						n\State = 2
					EndIf
				EndIf
			Case 4 ;Idling
				HideEntity n\obj
				HideEntity n\Collider
				n\DropSpeed = 0
				PositionEntity n\Collider,0,500,0
				ResetEntity n\Collider
				If n\Idle > 0
					n\Idle = Max(n\Idle-(1+(1*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0],0)
				Else
					If PlayerInReachableRoom() ;Player is in a room where SCP-008-1 can teleport to
						If Rand(50-(20*SelectedDifficulty\AggressiveNPCs))=1
							ShowEntity n\Collider
							ShowEntity n\obj
							For w.WayPoints = Each WayPoints
								If w\door=Null And w\room\dist < HideDistance And Rand(3)=1 Then
									If EntityDistanceSquared(w\room\obj,n\Collider)<EntityDistanceSquared(Collider,n\Collider)
										x = Abs(EntityX(n\Collider)-EntityX(w\obj,True))
										If x < 12.0 And x > 4.0 Then
											z = Abs(EntityZ(n\Collider)-EntityZ(w\obj,True))
											If z < 12 And z > 4.0 Then
												If w\room\dist > 4
													DebugLog "MOVING 008-2 TO "+w\room\RoomTemplate\Name
													PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True)
													ResetEntity n\Collider
													n\PathStatus = 0
													n\PathTimer# = 0.0
													n\PathLocation = 0
													Exit
												EndIf
											EndIf
										EndIf
									EndIf
								EndIf
							Next
							n\State = 2
							n\State3 = 0
						EndIf
					EndIf
				EndIf
		End Select
	Else
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		AnimateNPC(n, 590, 639, 0.5, False)
		
		If n\Frame >= 638.5
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
	EndIf
	
	If n\HP <= 0 Then
		n\IsDead = True
	EndIf
	
	RotateEntity n\obj,0,EntityYaw(n\Collider)-180,0
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	;[End Block]
	
End Function

Function UpdateNPCtype008_3(n.NPCs)
	Local w.WayPoints
	Local i%, dist#, dist2#, angle#, x#, y#, z#, PlayerSeeAble%, RN$
	
	Local target
	
	Local pick%
	
	Local sfxstep%
	
	Local prevFrame#
	
	;[Block]
	
	If (Not n\IsDead)
		If n\State = 0
			EntityType n\Collider,HIT_DEAD
		Else
			EntityType n\Collider,HIT_PLAYER
		EndIf
		
		prevFrame = n\Frame
		
		n\BlinkTimer = 1
		
		Select n\State
			Case 0 ;Lying next to the wall
				SetNPCFrame(n,11)
			Case 1 ;Standing up
				AnimateNPC(n,11,32,0.1,False)
				If n\Frame => 29
					n\State = 2
				EndIf
			Case 2 ;Being active
				PlayerSeeAble = MeNPCSeesPlayer(n)
				If PlayerSeeAble=1 Lor n\State2 > 0.0 And (I_268\Using = 0 Lor I_268\Timer =< 0.0)
					If PlayerSeeAble=1
						n\State2 = 70*2
					Else
						n\State2 = Max(n\State2-fps\Factor[0],0)
					EndIf
					PointEntity n\obj, Collider
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
					
					AnimateNPC(n, 64, 93, n\CurrSpeed*30)
					n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
					MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
					
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.0)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							n\State = 3
						EndIf
					EndIf
					
					n\PathTimer = 0
					n\PathStatus = 0
					n\PathLocation = 0
					n\State3 = 0
				Else
					If n\PathStatus = 1
						If n\Path[n\PathLocation]=Null Then 
							If n\PathLocation > 19 Then 
								n\PathLocation = 0 : n\PathStatus = 0
							Else
								n\PathLocation = n\PathLocation + 1
							EndIf
						Else
							PointEntity n\obj, n\Path[n\PathLocation]\obj
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
							
							AnimateNPC(n, 64, 93, n\CurrSpeed*30)
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
							
							;opens doors in front of him
							
							dist2# = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
							If dist2<PowTwo(0.6) Then
								temp = True
								If n\Path[n\PathLocation]\door <> Null Then
									If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
										If n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\KeyCard>0 Lor n\Path[n\PathLocation]\door\Code<>"" Then
											temp = False
										Else
											If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
										EndIf
									EndIf
								EndIf
								If dist2<PowTwo(0.2) And temp
									n\PathLocation = n\PathLocation + 1
								ElseIf dist2<PowTwo(0.5) And (Not temp)
									n\PathStatus = 0
									n\PathTimer# = 0.0
								EndIf
							EndIf
						EndIf
					Else
						AnimateNPC(n, 167, 202, 0.2, True)
						n\CurrSpeed = 0
						If n\PathTimer < 70*5
							n\PathTimer = n\PathTimer + Rnd(1,2+(2*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0]
						Else
							n\PathStatus = FindPath(n,EntityX(Collider),EntityY(Collider),EntityZ(Collider))
							n\PathTimer = 0
						EndIf
					EndIf
					
					If EntityDistanceSquared(n\Collider,Collider)>PowTwo(HideDistance)
						If n\State3 < 70*(15+(10*SelectedDifficulty\AggressiveNPCs))
							n\State3 = n\State3+fps\Factor[0]
						Else
							DebugLog "SCP-008-3 IDLE"
							n\State3 = 70*(6*60)
							n\State = 4
						EndIf
					EndIf
				EndIf
				
				If n\CurrSpeed > 0.005 Then
					If (prevFrame < 80 And n\Frame=>80) Lor (prevFrame > 92 And n\Frame<65)
						PlaySound2(StepSFX(0,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
					EndIf
				EndIf
				
				n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider)
			Case 3 ;Attacking
				AnimateNPC(n, 126, 165, 0.4, False)
				If (n\Frame => 146 And prevFrame < 146)
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							PlaySound_Strict DamageSFX[Rand(5,8)]
							If hds\Wearing And hds\Health < 35 Lor (Not hds\Wearing) Then
								PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Collider)
							EndIf
							If I_1033RU\HP = 0
								DamageSPPlayer(Rnd(10.0,20.0))
							Else
								Damage1033RU(30 + 5 * SelectedDifficulty\AggressiveNPCs)
								n\HP = n\HP - Rnd(50)
								If n\HP =< 0 Then
									n\IsDead = True
								EndIf
							EndIf
										;I_008\Timer = I_008\Timer + (1+(1*SelectedDifficulty\aggressiveNPCs))
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp008_1_death",Designation)
						EndIf
					EndIf
				ElseIf n\Frame => 164
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							SetNPCFrame(n,126)
						Else
							n\State = 2
						EndIf
					Else
						n\State = 2
					EndIf
				EndIf
			Case 4 ;Idling
				HideEntity n\obj
				HideEntity n\Collider
				n\DropSpeed = 0
				PositionEntity n\Collider,0,500,0
				ResetEntity n\Collider
				If n\Idle > 0
					n\Idle = Max(n\Idle-(1+(1*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0],0)
				Else
					If PlayerInReachableRoom() ;Player is in a room where SCP-008-1 can teleport to
						If Rand(50-(20*SelectedDifficulty\AggressiveNPCs))=1
							ShowEntity n\Collider
							ShowEntity n\obj
							For w.WayPoints = Each WayPoints
								If w\door=Null And w\room\dist < HideDistance And Rand(3)=1 Then
									If EntityDistanceSquared(w\room\obj,n\Collider)<EntityDistanceSquared(Collider,n\Collider)
										x = Abs(EntityX(n\Collider)-EntityX(w\obj,True))
										If x < 12.0 And x > 4.0 Then
											z = Abs(EntityZ(n\Collider)-EntityZ(w\obj,True))
											If z < 12 And z > 4.0 Then
												If w\room\dist > 4
													DebugLog "MOVING 008-3 TO "+w\room\RoomTemplate\Name
													PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True)
													ResetEntity n\Collider
													n\PathStatus = 0
													n\PathTimer# = 0.0
													n\PathLocation = 0
													Exit
												EndIf
											EndIf
										EndIf
									EndIf
								EndIf
							Next
							n\State = 2
							n\State3 = 0
						EndIf
					EndIf
				EndIf
		End Select
	Else
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		AnimateNPC(n, 642, 699, 0.5, False)
		
		If n\Frame >= 698.5
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
	EndIf
	
	If n\HP <= 0 Then
		n\IsDead = True
	EndIf
	
	RotateEntity n\obj,0,EntityYaw(n\Collider)-180,0
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	;[End Block]
	
End Function

Function UpdateNPCtype008_4(n.NPCs)
	Local w.WayPoints
	Local i%, dist#, dist2#, angle#, x#, y#, z#, PlayerSeeAble%, RN$
	
	Local target
	
	Local pick%
	
	Local sfxstep%
	
	Local prevFrame#
	
	;[Block]
	
	;n\State: Main State
	;n\State2: A timer used for the player detection
	;n\State3: A timer for making the NPC idle (if the player escapes during that time)
	
	If (Not n\IsDead)
		If n\State = 0
			EntityType n\Collider,HIT_DEAD
		Else
			EntityType n\Collider,HIT_PLAYER
		EndIf
		
		prevFrame = n\Frame
		
		n\BlinkTimer = 1
		
		Select n\State
			Case 0 ;Lying next to the wall
				SetNPCFrame(n,1)
			Case 1 ;Standing up
				AnimateNPC(n,1,2,0.1,False)
				If n\Frame => 1
					n\State = 2
				EndIf
			Case 2 ;Being active
				PlayerSeeAble = MeNPCSeesPlayer(n)
				If PlayerSeeAble=1 Lor n\State2 > 0.0 And (I_268\Using = 0 Lor I_268\Timer =< 0.0)
					If PlayerSeeAble=1
						n\State2 = 70*2
					Else
						n\State2 = Max(n\State2-fps\Factor[0],0)
					EndIf
					PointEntity n\obj, Collider
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
					
					AnimateNPC(n, 10, 49, n\CurrSpeed*18)
					n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
					MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
					
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.0)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							n\State = 3
						EndIf
					EndIf
					
					n\PathTimer = 0
					n\PathStatus = 0
					n\PathLocation = 0
					n\State3 = 0
				Else
					If n\PathStatus = 1
						If n\Path[n\PathLocation]=Null Then 
							If n\PathLocation > 19 Then 
								n\PathLocation = 0 : n\PathStatus = 0
							Else
								n\PathLocation = n\PathLocation + 1
							EndIf
						Else
							PointEntity n\obj, n\Path[n\PathLocation]\obj
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
							
							AnimateNPC(n, 10, 48, n\CurrSpeed*18)
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
							
							;opens doors in front of him
							
							dist2# = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
							If dist2<PowTwo(0.6) Then
								temp = True
								If n\Path[n\PathLocation]\door <> Null Then
									If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
										If n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\KeyCard>0 Lor n\Path[n\PathLocation]\door\Code<>"" Then
											temp = False
										Else
											If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
										EndIf
									EndIf
								EndIf
								If dist2<PowTwo(0.2) And temp
									n\PathLocation = n\PathLocation + 1
								ElseIf dist2<PowTwo(0.5) And (Not temp)
									n\PathStatus = 0
									n\PathTimer# = 0.0
								EndIf
							EndIf
						EndIf
					Else
						AnimateNPC(n, 54, 104, 0.2, True)
						n\CurrSpeed = 0
						If n\PathTimer < 70*5
							n\PathTimer = n\PathTimer + Rnd(1,2+(2*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0]
						Else
							n\PathStatus = FindPath(n,EntityX(Collider),EntityY(Collider),EntityZ(Collider))
							n\PathTimer = 0
						EndIf
					EndIf
					
					If EntityDistanceSquared(n\Collider,Collider)>PowTwo(HideDistance)
						If n\State3 < 70*(15+(10*SelectedDifficulty\AggressiveNPCs))
							n\State3 = n\State3+fps\Factor[0]
						Else
							DebugLog "SCP-008-4 IDLE"
							n\State3 = 70*(6*60)
							n\State = 4
						EndIf
					EndIf
				EndIf
				
				If n\CurrSpeed > 0.005 Then
					If (prevFrame < 80 And n\Frame=>80) Lor (prevFrame > 92 And n\Frame<65)
						PlaySound2(StepSFX(0,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
					EndIf
				EndIf
				
				n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider)
			Case 3 ;Attacking
				AnimateNPC(n, 417, 471, 0.4, False)
				If (n\Frame => 443 And prevFrame < 443)
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							PlaySound_Strict DamageSFX[Rand(5,8)]
							If hds\Wearing And hds\Health < 35 Lor (Not hds\Wearing) Then
								PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Collider)
							EndIf
							If I_1033RU\HP = 0
								DamageSPPlayer(Rnd(10.0,20.0))
							Else
								Damage1033RU(30 + 5 * SelectedDifficulty\AggressiveNPCs)
								n\HP = n\HP - Rnd(50)
								If n\HP =< 0 Then
									n\IsDead = True
								EndIf
							EndIf
										;I_008\Timer = I_008\Timer + (1+(1*SelectedDifficulty\aggressiveNPCs))
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp008_1_death",Designation)
						EndIf
					EndIf
				ElseIf n\Frame => 470
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							SetNPCFrame(n,417)
						Else
							n\State = 2
						EndIf
					Else
						n\State = 2
					EndIf
				EndIf
			Case 4 ;Idling
				HideEntity n\obj
				HideEntity n\Collider
				n\DropSpeed = 0
				PositionEntity n\Collider,0,500,0
				ResetEntity n\Collider
				If n\Idle > 0
					n\Idle = Max(n\Idle-(1+(1*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0],0)
				Else
					If PlayerInReachableRoom() ;Player is in a room where SCP-008-1 can teleport to
						If Rand(50-(20*SelectedDifficulty\AggressiveNPCs))=1
							ShowEntity n\Collider
							ShowEntity n\obj
							For w.WayPoints = Each WayPoints
								If w\door=Null And w\room\dist < HideDistance And Rand(3)=1 Then
									If EntityDistanceSquared(w\room\obj,n\Collider)<EntityDistanceSquared(Collider,n\Collider)
										x = Abs(EntityX(n\Collider)-EntityX(w\obj,True))
										If x < 12.0 And x > 4.0 Then
											z = Abs(EntityZ(n\Collider)-EntityZ(w\obj,True))
											If z < 12 And z > 4.0 Then
												If w\room\dist > 4
													DebugLog "MOVING 008-4 TO "+w\room\RoomTemplate\Name
													PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True)
													ResetEntity n\Collider
													n\PathStatus = 0
													n\PathTimer# = 0.0
													n\PathLocation = 0
													Exit
												EndIf
											EndIf
										EndIf
									EndIf
								EndIf
							Next
							n\State = 2
							n\State3 = 0
						EndIf
					EndIf
				EndIf
		End Select
	Else
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		AnimateNPC(n, 812, 871, 0.5, False)
		
		If n\Frame >= 870.5
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
		
	EndIf
	
	If n\HP <= 0 Then
		n\IsDead = True
	EndIf
	
	RotateEntity n\obj,0,EntityYaw(n\Collider)-180,0
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	;[End Block]
End Function

Function UpdateNPCtype008_5(n.NPCs)
	Local w.WayPoints
	Local i%, dist#, dist2#, angle#, x#, y#, z#, PlayerSeeAble%, RN$
	Local target,pick%,sfxstep%,prevFrame#
	Local g.Guns,it.Items
	Local bone%
	If n\State4 = 0 Then
		bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename_2"))
	Else
		bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
	EndIf
	
	;[Block]
	;n\State: Main State
	;n\State2: A timer used for the player detection
	;n\State3: A timer for making the NPC idle (if the player escapes during that time)
	
	If (Not n\IsDead)
		If n\State = 0
			EntityType n\Collider,HIT_DEAD
		Else
			EntityType n\Collider,HIT_PLAYER
		EndIf
		
		prevFrame = n\Frame
		
		n\BlinkTimer = 1
		
		n\Reload = Max(0, n\Reload - fps\Factor[0])
		
		Select n\State
			Case 0 ;Lying next to the wall
				SetNPCFrame(n,11)
			Case 1 ;Standing up
				AnimateNPC(n,11,32,0.1,False)
				If n\Frame => 29
					n\State = 2
				EndIf
			Case 2 ;Being active
				PlayerSeeAble = MeNPCSeesPlayer(n)
				If PlayerSeeAble=1 Lor n\State2 > 0.0 And (I_268\Using = 0 Lor I_268\Timer =< 0.0)
					If PlayerSeeAble=1
						n\State2 = 70*2
					Else
						n\State2 = Max(n\State2-fps\Factor[0],0)
					EndIf
					PointEntity n\obj, Collider
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
					
					; ~ Walking towards player
					AnimateNPC(n, 95, 125, n\CurrSpeed*30)
					n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
					MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
					; ~ Shooting
					If n\Gun <> Null Then
						If n\State4 = 2 Then
							For g = Each Guns
								If g\ID = n\Gun\ID Then
									it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
									EntityType it\Collider, HIT_ITEM
									it\state = n\Gun\Ammo
									Select Rand(2)
										Case 0
											it\state2 = 0
										Case 1
											it\state2 = n\Gun\MaxAmmo
										Case 2
											it\state2 = n\Gun\MaxAmmo+n\Gun\MaxAmmo
									End Select
									it\Dropped = 1
									Exit
								EndIf
							Next
							RemoveNPCGun(n)
						EndIf
						If n\State4 = 1 Then
							If n\Gun\Ammo > 0 Then
								If n\Reload = 0 Then
									If EntityVisible(Collider, n\Collider) Then
										n\Gun\Ammo = n\Gun\Ammo - 1
										Local flashpvt% = CreatePivot()
										
										RotateEntity(flashpvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
										PositionEntity(flashpvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
										MoveEntity (flashpvt,0.8*0.079, 7.0*0.079, 6.0*0.079)
										ShootPlayer(EntityX(flashpvt), EntityY(flashpvt), EntityZ(flashpvt), 0.7, True, n\Gun\Damage * Rand(1,2))
										If n\Gun\MaxGunshotSounds = 1 Then
											n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 25)
										Else
											n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 25)
										EndIf
										n\Reload = Rand(50,70)
									EndIf
								EndIf
							Else
								n\State4 = 2
							EndIf
						EndIf
					EndIf
					; ~ Melee Attack
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.0)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							n\State = 3
						EndIf
					EndIf
					
					n\PathTimer = 0
					n\PathStatus = 0
					n\PathLocation = 0
					n\State3 = 0
				Else
					If n\PathStatus = 1
						If n\Path[n\PathLocation]=Null Then 
							If n\PathLocation > 19 Then 
								n\PathLocation = 0 : n\PathStatus = 0
							Else
								n\PathLocation = n\PathLocation + 1
							EndIf
						Else
							PointEntity n\obj, n\Path[n\PathLocation]\obj
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
							
							AnimateNPC(n, 95, 125, n\CurrSpeed*30)
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
							
							;opens doors in front of him
							
							dist2# = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
							If dist2<PowTwo(0.6) Then
								temp = True
								If n\Path[n\PathLocation]\door <> Null Then
									If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
										If n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\KeyCard>0 Lor n\Path[n\PathLocation]\door\Code<>"" Then
											temp = False
										Else
											If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
										EndIf
									EndIf
								EndIf
								If dist2<PowTwo(0.2) And temp
									n\PathLocation = n\PathLocation + 1
								ElseIf dist2<PowTwo(0.5) And (Not temp)
									n\PathStatus = 0
									n\PathTimer# = 0.0
								EndIf
							EndIf
						EndIf
					Else
						AnimateNPC(n, 167, 202, 0.2, True)
						n\CurrSpeed = 0
						If n\PathTimer < 70*5
							n\PathTimer = n\PathTimer + Rnd(1,2+(2*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0]
						Else
							n\PathStatus = FindPath(n,EntityX(Collider),EntityY(Collider),EntityZ(Collider))
							n\PathTimer = 0
						EndIf
					EndIf
					
					If EntityDistanceSquared(n\Collider,Collider)>PowTwo(HideDistance)
						If n\State3 < 70*(15+(10*SelectedDifficulty\AggressiveNPCs))
							n\State3 = n\State3+fps\Factor[0]
						Else
							DebugLog "SCP-049-3 IDLE"
							n\State3 = 70*(6*60)
							n\State = 4
						EndIf
					EndIf
				EndIf
				
				If n\CurrSpeed > 0.005 Then
					If (prevFrame < 80 And n\Frame=>80) Lor (prevFrame > 92 And n\Frame<65)
						PlaySound2(StepSFX(0,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
					EndIf
				EndIf
				
				n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider)
			Case 3 ;Attacking
				AnimateNPC(n, 126, 165, 0.4, False)
				If (n\Frame => 146 And prevFrame < 146)
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							If n\State4 = 0 Then
								If (Not hds\Wearing) Then
									PlaySound_Strict(LoadTempSound("SFX\Guns\Knife\hitbody"+Rand(1,4)+".ogg"))
								Else
									PlaySound_Strict(LoadTempSound("SFX\Guns\Knife\hitwall.ogg"))
								EndIf
							Else
								PlaySound_Strict DamageSFX[Rand(5,8)]
							EndIf
							If hds\Wearing And hds\Health < 35 Lor (Not hds\Wearing) Then
								PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Collider)
							EndIf
							If I_1033RU\HP = 0
								If n\State4 = 0 Then
									DamageSPPlayer(Rnd(20.0,45.0))
								Else
									DamageSPPlayer(Rnd(10.0,20.0))
								EndIf
							Else
								If n\State4 = 0 Then
									Damage1033RU(40 + 5 * SelectedDifficulty\AggressiveNPCs)
								Else
									Damage1033RU(20 + 5 * SelectedDifficulty\AggressiveNPCs)
								EndIf
								n\HP = n\HP - Rnd(50)
								If n\HP =< 0 Then
									n\IsDead = True
								EndIf
							EndIf
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp049_2_death",Designation)
						EndIf
					EndIf
				ElseIf n\Frame => 164
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							SetNPCFrame(n,126)
						Else
							n\State = 2
						EndIf
					Else
						n\State = 2
					EndIf
				EndIf
			Case 4 ;Idling
				HideEntity n\obj
				HideEntity n\Collider
				n\DropSpeed = 0
				PositionEntity n\Collider,0,500,0
				ResetEntity n\Collider
				If n\Idle > 0
					n\Idle = Max(n\Idle-(1+(1*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0],0)
				Else
					If PlayerInReachableRoom() ;Player is in a room where SCP-008-1 can teleport to
						If Rand(50-(20*SelectedDifficulty\AggressiveNPCs))=1
							ShowEntity n\Collider
							ShowEntity n\obj
							For w.WayPoints = Each WayPoints
								If w\door=Null And w\room\dist < HideDistance And Rand(3)=1 Then
									If EntityDistanceSquared(w\room\obj,n\Collider)<EntityDistanceSquared(Collider,n\Collider)
										x = Abs(EntityX(n\Collider)-EntityX(w\obj,True))
										If x < 12.0 And x > 4.0 Then
											z = Abs(EntityZ(n\Collider)-EntityZ(w\obj,True))
											If z < 12 And z > 4.0 Then
												If w\room\dist > 4
													DebugLog "MOVING 049-3 TO "+w\room\RoomTemplate\Name
													PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True)
													ResetEntity n\Collider
													n\PathStatus = 0
													n\PathTimer# = 0.0
													n\PathLocation = 0
													Exit
												EndIf
											EndIf
										EndIf
									EndIf
								EndIf
							Next
							n\State = 2
							n\State3 = 0
						EndIf
					EndIf
				EndIf
		End Select
	Else
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		AnimateNPC(n, 590, 639, 0.5, False)
		
		If n\Frame >= 638.5
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
		
		If n\Gun <> Null And n\Frame >= 639-0.5 Then
			For g = Each Guns
				If g\ID = n\Gun\ID Then
					it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
					EntityType it\Collider, HIT_ITEM
					If n\State4 <> 0 Then
						it\state = n\Gun\Ammo
						Select Rand(2)
							Case 0
								it\state2 = 0
							Case 1
								it\state2 = n\Gun\MaxAmmo
							Case 2
								it\state2 = n\Gun\MaxAmmo+n\Gun\MaxAmmo
						End Select
					EndIf
					it\Dropped = 1
					Exit
				EndIf
			Next
			RemoveNPCGun(n)
		EndIf
		
	EndIf
	
	If n\HP <= 0 Then
		n\IsDead = True
	EndIf
	
	RotateEntity n\obj,0,EntityYaw(n\Collider)-180,0
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	;[End Block]
End Function

Function UpdateNPCTypeElias(n.NPCs)
	Local w.WayPoints
	Local i%, dist#, dist2#, angle#, x#, y#, z#, PlayerSeeAble%, RN$
	
	Local target
	
	Local pick%
	
	Local sfxstep%
	
	Local prevFrame#
	
	;[Block]
	;n\State: Main State
	;n\State2: A timer used for the player detection
	;n\State3: A timer for making the NPC idle (if the player escapes during that time)
	
	If (Not n\IsDead)
		If n\State = 0
			EntityType n\Collider,HIT_DEAD
		Else
			EntityType n\Collider,HIT_PLAYER
		EndIf
		
		prevFrame = n\Frame
		
		n\BlinkTimer = 1
		
		Select n\State
			Case 0 ;Lying next to the wall
				SetNPCFrame(n,11)
			Case 1 ;Standing up
				AnimateNPC(n,11,32,0.1,False)
				If n\Frame => 29
					n\State = 2
				EndIf
			Case 2 ;Being active
				PlayerSeeAble = MeNPCSeesPlayer(n)
				If PlayerSeeAble=1 Lor n\State2 > 0.0 And (I_268\Using = 0 Lor I_268\Timer =< 0.0)
					If PlayerSeeAble=1
						n\State2 = 70*2
					Else
						n\State2 = Max(n\State2-fps\Factor[0],0)
					EndIf
					PointEntity n\obj, Collider
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
					
					AnimateNPC(n, 64, 93, n\CurrSpeed*30)
					n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
					MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
					
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.0)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							n\State = 3
						EndIf
					EndIf
					
					n\PathTimer = 0
					n\PathStatus = 0
					n\PathLocation = 0
					n\State3 = 0
				Else
					If n\PathStatus = 1
						If n\Path[n\PathLocation]=Null Then 
							If n\PathLocation > 19 Then 
								n\PathLocation = 0 : n\PathStatus = 0
							Else
								n\PathLocation = n\PathLocation + 1
							EndIf
						Else
							PointEntity n\obj, n\Path[n\PathLocation]\obj
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
							
							AnimateNPC(n, 64, 93, n\CurrSpeed*30)
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * fps\Factor[0]
							
							;opens doors in front of him
							
							dist2# = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
							If dist2<PowTwo(0.6) Then
								temp = True
								If n\Path[n\PathLocation]\door <> Null Then
									If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
										If n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\KeyCard>0 Lor n\Path[n\PathLocation]\door\Code<>"" Then
											temp = False
										Else
											If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
										EndIf
									EndIf
								EndIf
								If dist2<PowTwo(0.2) And temp
									n\PathLocation = n\PathLocation + 1
								ElseIf dist2<PowTwo(0.5) And (Not temp)
									n\PathStatus = 0
									n\PathTimer# = 0.0
								EndIf
							EndIf
						EndIf
					Else
						AnimateNPC(n, 167, 202, 0.2, True)
						n\CurrSpeed = 0
						If n\PathTimer < 70*5
							n\PathTimer = n\PathTimer + Rnd(1,2+(2*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0]
						Else
							n\PathStatus = FindPath(n,EntityX(Collider),EntityY(Collider),EntityZ(Collider))
							n\PathTimer = 0
						EndIf
					EndIf
					
					If EntityDistanceSquared(n\Collider,Collider)>PowTwo(HideDistance)
						If n\State3 < 70*(15+(10*SelectedDifficulty\AggressiveNPCs))
							n\State3 = n\State3+fps\Factor[0]
						Else
							DebugLog "SCP-049-3 IDLE"
							n\State3 = 70*(6*60)
							n\State = 4
						EndIf
					EndIf
				EndIf
				
				If n\CurrSpeed > 0.005 Then
					If (prevFrame < 80 And n\Frame=>80) Lor (prevFrame > 92 And n\Frame<65)
						PlaySound2(StepSFX(0,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
					EndIf
				EndIf
				
				n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider)
			Case 3 ;Attacking
				AnimateNPC(n, 126, 165, 0.4, False)
				If (n\Frame => 146 And prevFrame < 146)
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							PlaySound_Strict DamageSFX[Rand(5,8)]
							If hds\Wearing And hds\Health < 35 Lor (Not hds\Wearing) Then
								PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Collider)
							EndIf
							If I_1033RU\HP = 0
								DamageSPPlayer(Rnd(10.0,20.0))
							Else
								Damage1033RU(30 + 5 * SelectedDifficulty\AggressiveNPCs)
								n\HP = n\HP - Rnd(50)
								If n\HP =< 0 Then
									n\IsDead = True
								EndIf
							EndIf
							If hds\Wearing And hds\Health < 35 Lor (Not hds\Wearing) Then
								I_008\Timer = I_008\Timer + (1+(1*SelectedDifficulty\AggressiveNPCs))
								I_409\Timer = I_409\Timer + (1+(1*SelectedDifficulty\AggressiveNPCs))
							EndIf
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp049_3_death",Designation)
						EndIf
					EndIf
				ElseIf n\Frame => 164
					If EntityDistanceSquared(n\Collider,Collider)<PowTwo(1.1)
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0)
							SetNPCFrame(n,126)
						Else
							n\State = 2
						EndIf
					Else
						n\State = 2
					EndIf
				EndIf
			Case 4 ;Idling
				HideEntity n\obj
				HideEntity n\Collider
				n\DropSpeed = 0
				PositionEntity n\Collider,0,500,0
				ResetEntity n\Collider
				If n\Idle > 0
					n\Idle = Max(n\Idle-(1+(1*SelectedDifficulty\AggressiveNPCs))*fps\Factor[0],0)
				Else
					If PlayerInReachableRoom() ;Player is in a room where SCP-008-1 can teleport to
						If Rand(50-(20*SelectedDifficulty\AggressiveNPCs))=1
							ShowEntity n\Collider
							ShowEntity n\obj
							For w.WayPoints = Each WayPoints
								If w\door=Null And w\room\dist < HideDistance And Rand(3)=1 Then
									If EntityDistanceSquared(w\room\obj,n\Collider)<EntityDistanceSquared(Collider,n\Collider)
										x = Abs(EntityX(n\Collider)-EntityX(w\obj,True))
										If x < 12.0 And x > 4.0 Then
											z = Abs(EntityZ(n\Collider)-EntityZ(w\obj,True))
											If z < 12 And z > 4.0 Then
												If w\room\dist > 4
													DebugLog "MOVING 049-3 TO "+w\room\RoomTemplate\Name
													PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True)
													ResetEntity n\Collider
													n\PathStatus = 0
													n\PathTimer# = 0.0
													n\PathLocation = 0
													Exit
												EndIf
											EndIf
										EndIf
									EndIf
								EndIf
							Next
							n\State = 2
							n\State3 = 0
						EndIf
					EndIf
				EndIf
		End Select
	Else
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		AnimateNPC(n, 590, 639, 0.5, False)
		
		If n\Frame >= 638.5
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
		
	EndIf
	
	If n\HP <= 0 Then
		n\IsDead = True
	EndIf
	
	RotateEntity n\obj,0,EntityYaw(n\Collider)-180,0
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	;[End Block]
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D