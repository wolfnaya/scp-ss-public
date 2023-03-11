; ~ Class-D Constants
;[Block]
Const D9341_FOLLOW = 5
Const D9341_RELOAD = 4
Const D9341_GO_AFTER = 3
Const D9341_FIND_COVER = 2
Const D9341_ATTACK = 1
Const D9341_IDLE = 0
;[End Block]

Function CreateNPCtypeD9341(n.NPCs)
	Local temp#, tex%
	
	n\BossName = "Subject D-9341"
	n\NPCName = "Class-D"
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.25, 0.32
	EntityType n\Collider, HIT_PLAYER
	
	n\obj = LoadAnimMesh_Strict("GFX\npcs\Humans\Personnel\Base_Human_Armed.b3d")
	temp# = 0.5 / MeshWidth(n\obj)
	ScaleEntity n\obj, temp, temp, temp
	
	n\texture = "GFX\npcs\Humans\Personnel\D9341.jpg"
	tex = LoadTexture_Strict(n\texture, 0, 2)
	TextureBlend(tex,5)
	EntityTexture(n\obj, tex)
	
	n\Speed = 4.0 / 100
	
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj)*5, MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*10)
	
	Local random% = Rand(1, 150)
	If random <= 42 Then
		SwitchNPCGun%(n, "p90")
		If Rand(1,10) = 1 Then
			n\State3 = n\HP
		EndIf	
	ElseIf random > 42 And random <= 84 Then	
		SwitchNPCGun%(n, "mp5k")
		If Rand(1,10) = 1 Then
			n\State3 = n\HP
		EndIf	
	ElseIf random > 84 And random <= 90 Then
		SwitchNPCGun%(n, "ak12")
	ElseIf random > 90 And random <= 96 Then
		SwitchNPCGun%(n, "mp7")
	ElseIf random > 96 And random <= 100 Then
		SwitchNPCGun%(n, "ak12")
	ElseIf random > 100 And random <= 112 Then
		SwitchNPCGun%(n, "hk416")
	ElseIf random > 112 Then
		SwitchNPCGun%(n, "spas12")
	EndIf
	
	n\Gun\Ammo = n\Gun\MaxAmmo
	
	n\CollRadius = 0.16
	
	n\HP = 1024+(20*SelectedDifficulty\OtherFactors)
	n\MaxBossHealth = n\HP
	n\Boss = n
	
	CopyHitBoxes(n)
End Function

Function UpdateNPCtypeD9341(n.NPCs)
	Local n2.NPCs, w.WayPoints, g.Guns, it.Items, r.Rooms, v3d.Vector3D
	Local prevFrame#, temp2%, deathFrame#, bone%, dist#
	
	prevFrame = n\Frame
	
	Local agitateHealth% = 512+(10*SelectedDifficulty\OtherFactors)
	
	If n\State = D9341_IDLE Lor n\State = D9341_GO_AFTER Then
		For n2.NPCs = Each NPCs
			If n2\HP > 0 Then
				If n2\NPCtype = NPCtypeNTF Then
					If NPCSeesEntity(n, n2\Collider) Then
						n\Target = n2
						n\State = D9341_ATTACK
						Exit
					EndIf
				EndIf
			EndIf
		Next
		If (I_268\Using = 0 Lor I_268\Timer =< 0.0) And psp\Health > 0 Then
			If NPCSeesEntity(n, Camera) And (I_268\Using = 0 Lor I_268\Timer =< 0.0) And psp\Health > 0 Then
				n\State = D9341_ATTACK
			EndIf
		EndIf
	EndIf
	
	If n\IsDead = False Then
		
		Select n\State
			Case D9341_IDLE
				;[Block]
				Local roomfound% = False
				While roomfound = False
					If n\NPCRoom=Null Then
						GetNPCRoom(n)
					EndIf
					For r.Rooms = Each Rooms
						If Rand(5)=1 Then
							roomfound = True
							Exit
						EndIf
					Next
				Wend
				
				NPC_GoTo(n, FindNPCAnimation(n\NPCtype, "idle"), FindNPCAnimation(n\NPCtype, "walk"), r\obj, 0.3)
				;AnimateNPC(n, 212, 235, 0.1)
				;[End Block]
			Case D9341_ATTACK
				;[Block]
				dist = EntityDistanceSquared(n\Collider, Collider)
				
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
										Local pvt% = CreatePivot()
										
										RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
										PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
										If GetNPCWeaponAnim(n\Gun\AnimType) <> "shotgun" Then
											MoveEntity (pvt,0.8*0.079, 10.0*0.079, 6.0*0.079)
										Else
											MoveEntity (pvt,0.8*0.079, 7.0*0.079, 6.0*0.079)
										EndIf
										ShootTarget(0, 0, 0, n, Clamp(2 / dist, 0.0, 0.65), True, n\Gun\Damage * Rand(1, n\Gun\BulletsPerShot))
										If n\Gun\MaxGunshotSounds = 1 Then
											n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 25)
										Else
											n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 25)
										EndIf
										If GetNPCWeaponAnim(n\Gun\AnimType) = "pistol" Then
											n\Reload = Rand(25,50)
										Else
											n\Reload = n\Gun\ShootFrequency
										EndIf
									EndIf
								EndIf
							EndIf
						Else
							n\Target = Null
							n\State = D9341_IDLE
						EndIf
					Else
						If psp\Health <= 0 Then
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","death_d",Designation)
							n\State = D9341_IDLE
						Else
							n\CurrSpeed = 0.0
							n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,Collider)
							RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
							If n\Reload = 0 Then
								If Abs(DeltaYaw(n\Collider,Collider))<45.0 Then
									If NPCSeesEntity(n, Camera)
										pvt% = CreatePivot()
										
										RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
										PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
										If GetNPCWeaponAnim(n\Gun\AnimType) <> "shotgun" Then
											MoveEntity (pvt,0.8*0.079, 10.0*0.079, 6.0*0.079)
										Else
											MoveEntity (pvt,0.8*0.079, 7.0*0.079, 6.0*0.079)
										EndIf
										If n\Gun\MaxGunshotSounds = 1 Then
											n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 25)
										Else
											n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 25)
										EndIf
										ShootPlayer(0, 0, 0, Clamp(2 / dist, 0.0, 0.65), True, n\Gun\Damage * Rand(1, n\Gun\BulletsPerShot))
										n\Gun\Ammo = n\Gun\Ammo - 1
										If GetNPCWeaponAnim(n\Gun\AnimType) = "pistol" Then
											n\Reload = Rand(25,50)
										Else
											n\Reload = n\Gun\ShootFrequency
										EndIf
									Else
										n\LastSeen = Collider
										n\IdleTimer = 70*8
										n\State = D9341_GO_AFTER
									EndIf
								EndIf
							EndIf
						EndIf
						
						If n\Gun\Ammo <= 0 Then
						;	Until we fix the AnimateNPC Function breaking the reloading. -Meow
							n\State = D9341_FIND_COVER
						EndIf
					EndIf
				EndIf
				
				v3d = FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_idle")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z)
				;[End Block]
			Case D9341_FIND_COVER
				;[Block]
				If n\Gun\Ammo <= 0 Then
					If n\Target<>Null Then
						temp2 = NPC_GoToCover(n, FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), n\Target\Collider, 0.8)
					Else
						temp2 = NPC_GoToCover(n, FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), Collider, 0.8)
					EndIf
				EndIf
				If temp2 Then
					n\State = D9341_RELOAD
				EndIf
				;[End Block]
			Case D9341_GO_AFTER
				;[Block]
				NPC_GoTo(n, FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_idle"), FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), Collider, 0.8)
				If n\IdleTimer = 0.0 Then
					n\State = D9341_IDLE
				EndIf
				;[End Block]
			Case D9341_RELOAD
				;[Block]
				v3d = FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_reload")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False)
				If n\Frame >= v3d\y Then
					n\Gun\Ammo = n\Gun\MaxAmmo
					n\State = D9341_GO_AFTER
				EndIf
				;[End Block]
			Case STATE_SCRIPT
				;[Block]
				
				;[End Block]
		End Select
		n\IdleTimer = Max(0.0, n\IdleTimer - fps\Factor[0])
		n\Reload = Max(0, n\Reload - fps\Factor[0])
		
		;Play step sounds
		If n\CurrSpeed > 0.01 Then
			If prevFrame > 500 And n\Frame<495 Then
				Local sfxstep = GetStepSound(n\Collider,n\CollRadius)
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
		
	Else
		Select n\State2
			Case 0.0
				v3d = FindNPCAnimation(n\NPCtype, "death_front")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from front
			Case 1.0
				v3d = FindNPCAnimation(n\NPCtype, "death_left")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from left
			Case 2.0
				v3d = FindNPCAnimation(n\NPCtype, "death_back")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from back
			Case 3.0
				v3d = FindNPCAnimation(n\NPCtype, "death_right")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from right
		End Select
		n\LastSeen = 0.0
		n\Reload = 0.0
		
		If n\Frame >= v3d\y-0.5
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
		
		If n\Gun <> Null And n\Frame >= v3d\y-0.5 Then
			bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
			Local bone2% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "grenade_hand_bonename"))
			For g = Each Guns
				If g\ID = n\Gun\ID Then
					it = CreateItem("SCRAMBLE Gear","scramble", EntityX(bone2%, True)+0.05, EntityY(bone2%, True) + 0.04, EntityZ(bone2%, True)+0.05)
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
	
	;Is the NPC dead?
	If n\HP <= 0 And (Not n\IsDead) Then
		n\IsDead = True
		;This needs to be rewritten!
		Local temp% = (EntityYaw(Camera) - EntityYaw(n\obj) + 45 + 180) Mod 360
		n\State2 = 0.0
		If temp > 90 Then
			n\State2 = 1.0
			If temp > 180 Then
				n\State2 = 2.0
				If temp > 270 Then
					n\State2 = 3.0
				EndIf
			EndIf
		EndIf
		SetNPCFrame(n, 0)
	EndIf
	
	PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.32, EntityZ(n\Collider))
	
	RotateEntity n\obj, EntityPitch(n\Collider), EntityYaw(n\Collider), 0
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D