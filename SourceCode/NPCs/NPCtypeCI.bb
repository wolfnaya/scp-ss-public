; ~ CI Constants
;[Block]
Const CI_WONDERING = 8
Const CI_TAKE_COVER = 7
Const CI_GO_TO_ATTACK = 6
Const CI_THROW_GRENADE = 5
Const CI_RELOAD = 4
Const CI_GO_AFTER = 3
Const CI_GO_TO_RELOAD = 2
Const CI_ATTACK = 1
Const CI_IDLE = 0
;[End Block]

Function CreateNPCtypeCI(n.NPCs)
	Local temp#, tex%,random%
	
	n\NPCName = "Chaos Insurgency"
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.25, 0.32
	EntityType n\Collider, HIT_PLAYER
	
	n\obj = LoadAnimMesh_Strict("GFX\npcs\Humans\Enemies\CI\ChaosInsurgency.b3d")
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "CI", "speed") / 100.0)
	temp# = (GetINIFloat("DATA\NPCs.ini", "CI", "scale") / 2.5)
	ScaleEntity n\obj, temp, temp, temp
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj)*5, MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*10)
	
	random% = Rand(1, 105)
	If random <= 42 Then
		SwitchNPCGun%(n, "p90")
	ElseIf random > 42 And random <= 84 Then	
		SwitchNPCGun%(n, "mp7")	
	ElseIf random > 84 And random <= 90 Then
		SwitchNPCGun%(n, "mp5k")
	ElseIf random > 90 And random <= 96 Then
		SwitchNPCGun%(n, "hk416")
	ElseIf random > 96 And random <= 100 Then
		SwitchNPCGun%(n, "ak12")
	ElseIf random > 100 
		SwitchNPCGun%(n, "spas12")
	EndIf
	
	n\Gun\Ammo = n\Gun\MaxAmmo
	
	n\CollRadius = 0.16
	
	n\HP = 180+(20*SelectedDifficulty\OtherFactors)
	
	n\BlinkTimer = 70.0*Rnd(5,8)
	
	n\Clearance = 3
	
	CopyHitBoxes(n)
End Function

Function UpdateNPCtypeCI(n.NPCs)
	Local n2.NPCs, w.WayPoints, g.Guns, it.Items, r.Rooms, v3d.Vector3D
	Local prevFrame#, temp2%, deathFrame#, bone%, dist#, gr.Grenades
	
	prevFrame = n\Frame
	
	If n\IsDead = False Then
	
	If n\State = CI_IDLE Lor n\State = CI_GO_AFTER Then
		For n2.NPCs = Each NPCs
			If n2\HP > 0 Then
				If n2\NPCtype = NPCtypeNTF Then
					If NPCSeesEntity(n, n2\Collider) Then
						n\Target = n2
						If n\State = CI_IDLE Then 
							If (Not n\IsDead) Then
								If (Not ChannelPlaying(CIChn)) Then CIChn = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\Spotted_"+Rand(1, 4)+".ogg"))
							Else
								StopChannel(CIChn)
							EndIf
						EndIf	
						n\State = CI_GO_TO_ATTACK
						Exit
					EndIf
				EndIf
				If n2\NPCtype = NPCTypePlotNTF Then
					If NPCSeesEntity(n, n2\Collider) Then
						n\Target = n2
						If n\State = CI_IDLE Then 
							If (Not n\IsDead) Then
								If (Not ChannelPlaying(CIChn)) Then CIChn = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\Spotted_"+Rand(1, 4)+".ogg"))
							Else
								StopChannel(CIChn)
							EndIf
						EndIf	
						n\State = CI_GO_TO_ATTACK
						Exit
					EndIf
				EndIf
				If n2\NPCtype = NPCTypeNTFEnemy Then
					If NPCSeesEntity(n, n2\Collider) Then
						n\Target = n2
						If n\State = CI_IDLE Then 
							If (Not n\IsDead) Then
								If (Not ChannelPlaying(CIChn)) Then CIChn = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\Spotted_"+Rand(1, 4)+".ogg"))
							Else
								StopChannel(CIChn)
							EndIf
						EndIf	
						n\State = CI_GO_TO_ATTACK
						Exit
					EndIf
				EndIf
			EndIf
		Next
		If (I_268\Using = 0 Lor I_268\Timer =< 0.0) And psp\Health > 0 Then
			If IsPlayerOutside() Then
				If NPCSeesEntityFar(n, Camera) And (I_268\Using = 0 Lor I_268\Timer =< 0.0) And psp\Health > 0 Then
					If n\State = CI_IDLE Then 
						If (Not n\IsDead) Then
							If (Not ChannelPlaying(CIChn)) Then CIChn = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\Spotted_"+Rand(1, 7)+".ogg"))
						Else
							StopChannel(CIChn)
						EndIf
					EndIf
					n\State = CI_GO_TO_ATTACK
				EndIf
			Else
				If NPCSeesEntity(n, Camera) And (I_268\Using = 0 Lor I_268\Timer =< 0.0) And psp\Health > 0 Then
					If n\State = CI_IDLE Then 
						If (Not n\IsDead) Then
							If (Not ChannelPlaying(CIChn)) Then CIChn = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\Spotted_"+Rand(1, 7)+".ogg"))
						Else
							StopChannel(CIChn)
						EndIf
					EndIf
					n\State = CI_GO_TO_ATTACK
				EndIf
			EndIf
		EndIf
	EndIf
	
	;If n\IsDead = False Then
		Select n\State
			Case CI_IDLE
				;[Block]
				
				n\State3 = n\State3 + fps\Factor[0]
				
				Local roomfound% = False
				While roomfound = False
					If n\NPCRoom=Null Then
						GetNPCRoom(n)
					EndIf
					For r.Rooms = Each Rooms
						;If Rand(5)=1 Then
							roomfound = True
							Exit
						;EndIf
					Next
				Wend
				
				NPC_GoTo(n, FindNPCAnimation(n\NPCtype, "idle"), FindNPCAnimation(n\NPCtype, "walk"), r\obj, 0.3)
				;NPC_GoToRoom(n, FindNPCAnimation(n\NPCtype, "idle"), FindNPCAnimation(n\NPCtype, "walk"), 0.5)
				
				If n\State3 >= 70*5 And n\State3 < 70*5.001 Then
					n\State = CI_WONDERING
				EndIf
				;[End Block]
			Case CI_GO_TO_ATTACK
				;[Block]
				
				Select Rand(0,11)
					Case 0,1,2,3,4,5,6,7,8,10,11
						n\State = CI_ATTACK
					Case 9
						n\State = CI_THROW_GRENADE
				End Select
				
				;[End Block]
			Case CI_ATTACK
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
									If IsPlayerOutside() Then
										If NPCSeesEntityFar(n, n\Target\Collider)
											n\Gun\Ammo = n\Gun\Ammo - 1
											
											Local flashpvt% = CreatePivot()
											
											RotateEntity(flashpvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
											PositionEntity(flashpvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
											MoveEntity (flashpvt,0.8*0.079, 10.0*0.079, 6.0*0.079)
											
											ShootTarget(0, 0, 0, n, Clamp(2 / dist, 0.0, 0.65), True, n\Gun\Damage * Rand(1, n\Gun\BulletsPerShot))
											If n\Gun\MaxGunshotSounds = 1 Then
												n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 35)
											Else
												n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 35)
											EndIf
											If GetNPCWeaponAnim(n\Gun\AnimType) = "pistol" Then
												n\Reload = Rand(25,50)
											Else
												n\Reload = n\Gun\ShootFrequency
											EndIf
										EndIf
									Else
										If NPCSeesEntity(n, n\Target\Collider)
											n\Gun\Ammo = n\Gun\Ammo - 1
											
											flashpvt% = CreatePivot()
											
											RotateEntity(flashpvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
											PositionEntity(flashpvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
											MoveEntity (flashpvt,0.8*0.079, 10.0*0.079, 6.0*0.079)
											
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
							EndIf
						Else
							n\Target = Null
							n\State = CI_IDLE
						EndIf
					Else
						If psp\Health <= 0 Then
							If (Not n\IsDead) Then
								If (Not ChannelPlaying(CIChn)) Then CIChn = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\Terminated_"+Rand(1, 11)+".ogg"))
							Else
								StopChannel(CIChn)
							EndIf
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","death_ci",Designation)
							n\State = CI_IDLE
						Else
							n\CurrSpeed = 0.0
							n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,Collider)
							RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
							If n\Reload = 0 Then
								If Abs(DeltaYaw(n\Collider,Collider))<45.0 Then
									If IsPlayerOutside() Then
										If NPCSeesEntityFar(n, Camera)
											If n\Gun\MaxGunshotSounds = 1 Then
												n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 25)
											Else
												n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 25)
											EndIf
											ShootPlayer(0, 0, 0, Clamp(2 / dist, 0.0, 0.65), True, n\Gun\Damage * Rand(1, n\Gun\BulletsPerShot))
											n\Gun\Ammo = n\Gun\Ammo - 1
											
											flashpvt% = CreatePivot()
											
											RotateEntity(flashpvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
											PositionEntity(flashpvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
											MoveEntity (flashpvt,0.8*0.079, 10.0*0.079, 6.0*0.079)
											
											If GetNPCWeaponAnim(n\Gun\AnimType) = "pistol" Then
												n\Reload = Rand(25,50)
											Else
												n\Reload = n\Gun\ShootFrequency
											EndIf
										Else
											n\LastSeen = Collider
											n\IdleTimer = 70*8
											n\State = CI_GO_AFTER
										EndIf
									Else
										If NPCSeesEntity(n, Camera)
											If n\Gun\MaxGunshotSounds = 1 Then
												n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 25)
											Else
												n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 25)
											EndIf
											ShootPlayer(0, 0, 0, Clamp(2 / dist, 0.0, 0.65), True, n\Gun\Damage * Rand(1, n\Gun\BulletsPerShot))
											n\Gun\Ammo = n\Gun\Ammo - 1
											
											flashpvt% = CreatePivot()
											
											RotateEntity(flashpvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
											PositionEntity(flashpvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
											MoveEntity (flashpvt,0.8*0.079, 10.0*0.079, 6.0*0.079)
											
											If GetNPCWeaponAnim(n\Gun\AnimType) = "pistol" Then
												n\Reload = Rand(25,50)
											Else
												n\Reload = n\Gun\ShootFrequency
											EndIf
										Else
											n\LastSeen = Collider
											n\IdleTimer = 70*8
											n\State = CI_GO_AFTER
										EndIf
									EndIf
								EndIf
							EndIf
						EndIf
						
						If n\Gun\Ammo <= 0 Then
							If (Not n\IsDead) Then
								If (Not ChannelPlaying(CIChn)) Then CIChn = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\CoverMe_"+Rand(1, 4)+".ogg"))
							Else
								StopChannel(CIChn)
							EndIf
							n\State = CI_GO_TO_RELOAD
						EndIf
					EndIf
				EndIf
					
				v3d = FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_idle")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z)
				;[End Block]
			Case CI_GO_TO_RELOAD
				;[Block]
				If n\Gun\Ammo <= 0 Then
					n\State = CI_RELOAD
				EndIf
				;[End Block]
			Case CI_GO_AFTER
				;[Block]
				NPC_GoTo(n, FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_idle"), FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), Collider, 0.8)
				If n\IdleTimer = 0.0 Then
					n\State = CI_IDLE
				EndIf
				;[End Block]
			Case CI_RELOAD
				;[Block]
				v3d = FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_reload")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False)
				If n\Frame >= v3d\y Then
					n\Gun\Ammo = n\Gun\MaxAmmo
					n\State = CI_GO_AFTER
;				ElseIf n\Frame >= v3d\x Then
;					If n\Gun\MaxReloadSounds = 1 Then
;						n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\reload_empty.ogg"), Camera, n\Collider, 25)
;					Else
;						n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\reload_empty.ogg"), Camera, n\Collider, 25)
;					EndIf
				EndIf
				;[End Block]
			Case CI_THROW_GRENADE
				;[Block]
				Local Throwframe# = GetINIInt("Data\NPCAnims.ini", n\NPCName, "throw_grenade_frame")
				
;				If (Not n\IsDead) Then
;					If (Not ChannelPlaying(CIChn)) Then CIChn = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\ThrowGrenade_"+Rand(1, 3)+".ogg"))
;				Else
;					StopChannel(CIChn)
;				EndIf
				
				PointEntity(n\Collider, Collider)
				v3d = FindNPCAnimation(n\NPCtype,"throw_grenade")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False)
				If n\Frame >= Throwframe Then
					CreateGrenade(EntityX(n\Collider),EntityY(n\Collider),EntityZ(n\Collider), EntityPitch(n\Collider), EntityYaw(n\Collider))
					n\IsNPCThrownGrenade% = True
					For gr = Each Grenades
						gr\Speed = gr\Speed*1.6
					Next
					n\State = CI_GO_AFTER
				EndIf
				;[End Block]
			Case CI_TAKE_COVER
				;[Block]
				
;				If (Not n\IsDead) Then
;					If (Not ChannelPlaying(CIChn)) Then CIChn = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\TakeCover_"+Rand(1, 2)+".ogg"))
;				Else
;					StopChannel(CIChn)
;				EndIf
				
				If n\Target<>Null Then
					temp2 = NPC_GoToCover(n, FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), n\Target\Collider, 0.8)
				Else
					temp2 = NPC_GoToCover(n, FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), Collider, 0.8)
				EndIf
				If temp2 Then
					n\State = CI_GO_AFTER
				EndIf
				;[End Block]
			Case CI_WONDERING
				;[Block]
				n\State3 = 0
				NPC_GoToRoom(n, FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_idle"), FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), 0.8)
				;[End Block]
			Case STATE_SCRIPT
				;[Block]
				
				;[End Block]
		End Select
		n\IdleTimer = Max(0.0, n\IdleTimer - fps\Factor[0])
		n\Reload = Max(0, n\Reload - fps\Factor[0])
		
		Local sfxstep%
		
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
		
	Else
		Select n\State2
			Case 0.0
				v3d = FindNPCAnimation(n\NPCtype, "death_2")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from front
			Case 1.0
				v3d = FindNPCAnimation(n\NPCtype, "death_2")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from left
			Case 2.0
				v3d = FindNPCAnimation(n\NPCtype, "death_3")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from back
			Case 3.0
				v3d = FindNPCAnimation(n\NPCtype, "death_1")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from right
		End Select
		n\LastSeen = 0.0
		n\Reload = 0.0
		
		If gc\CurrZone <> GATE_A_ROAD And gc\CurrZone <> GATE_C_TOPSIDE Then
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
		EndIf
		
		If n\Gun <> Null And n\Frame >= v3d\y-0.5 Then
			bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
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

; ~ For test

Function createnpctypecitest(n.NPCs)
	Local random%,temp#
	
	n\NPCName = "MTF_Enemy"
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
		SwitchNPCGun%(n, 10)
	ElseIf i > 13 And i <= 36 Then	
		SwitchNPCGun%(n, 12)
	ElseIf i > 36 Then	
		SwitchNPCGun%(n, 11)
	EndIf
	
	n\Gun\Ammo = n\Gun\MaxAmmo
	
	n\HP = 350+(75*SelectedDifficulty\OtherFactors)
	
	n\BlinkTimer = 70.0*Rnd(5,8)
	
	n\Clearance = 3
	
	CopyHitBoxes(n)
End Function

Function updatenpctypecitest(n.NPCs)
	Local r.Rooms, p.Particles, n2.NPCs, wp.WayPoints, wayPointCloseToPlayer.WayPoints
	Local x#, y#, z#
	Local PrevDist#, NewDist#
	Local Target%, Dist#, SearchPlayer%
	Local terminated% = False
	Local i%,it.Items,g.Guns
	
	If n\HP < 1 Then
		n\IsDead = True
	EndIf
	
	If n\IsDead Then
		n\BlinkTimer = -1.0
		SetNPCFrame(n, 532.0)
		If ChannelPlaying(n\SoundChn) Then StopChannel(n\SoundChn)
		If ChannelPlaying(n\SoundChn2) Then StopChannel(n\SoundChn2)
		
		;If n\HP = -1 Then
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
		
;		EntityType n\Collider, HIT_DEAD
;		If n\Frame = 1407 Then
;			SetNPCFrame(n, 1407)
;		Else
;			AnimateNPC(n, 1383, 1407, 0.05, False)
;		EndIf
;		If n\SoundChn <> 0
;			StopChannel n\SoundChn
;			n\SoundChn = 0
;			FreeSound_Strict n\Sound
;			n\Sound = 0
;		EndIf
;		n\Reload = 0.0
;		If n\Gun <> Null And n\Frame >= 1390-0.5 Then
;			bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
;			For g = Each Guns
;				If g\ID = n\Gun\ID Then
;					it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
;					EntityType it\Collider, HIT_ITEM
;					it\state = n\Gun\Ammo
;					it\state2 = Rand(0,2)
;					it\Dropped = 1
;					Exit
;				EndIf
;			Next
;			RemoveNPCGun(n)
;		EndIf
			
			
		n\MaxGravity = 0.03
		
		;UpdateNPCBlinking(n)
		; ~ Only play the "blinking" sound clip if searching / containing SCP-173
		If n\State = 2.0 Then
			If n\BlinkTimer <= 0.0 Then PlayMTFSound(LoadTempSound("SFX\Character\MTF\173\BLINKING.ogg"), n)
		EndIf
		
		n\Reload = n\Reload - fps\Factor[0]
		
		Local PrevFrame# = n\Frame
		
		If Int(n\State) <> 1.0 Then n\PrevState = 0
		
		n\SoundChn2 = LoopSound2(n\Sound, n\SoundChn2, Camera, n\Collider)
		
		If n\Idle > 0.0 Then
			FinishWalking(n, 488.0, 522.0, 0.015 * 26.0)
			n\Idle = n\Idle - fps\Factor[0]
			If n\Idle <= 0.0 Then n\Idle = 0.0
		Else
			Dist = EntityDistanceSquared(Collider, n\Collider)
			Select Int(n\State) ; ~ What is this MTF doing
				Case 0.0 ; ~ Wandering around
					;[Block]
					n\Speed = 0.015
					If n\PathTimer <= 0.0 Then ; ~ Update path
						If n\MTFLeader <> Null Then ; ~ I'll follow the leader
							n\PathStatus = FindPath(n, EntityX(n\MTFLeader\Collider, True), EntityY(n\MTFLeader\Collider, True) + 0.1, EntityZ(n\MTFLeader\Collider, True)) ; ~ Whatever you say boss
						Else ; ~ I am the leader
							If Curr173\Idle <> 2 Then
								If (Not Curr173\IsDead) And Curr173\Idle = 3 Then
									For r.Rooms = Each Rooms
										If r\RoomTemplate\Name = "cont_173" Then
											If EntityX(n\Collider, True) - r\x < 15.0 Then
												If r\RoomDoors[1]\open Then OpenCloseDoor(r\RoomDoors[1])
												Curr173\IsDead = True
												Exit
											EndIf
										EndIf
									Next
								EndIf
								For r.Rooms = Each Rooms
									If ((Abs(r\x - EntityX(n\Collider, True)) > 12.0) Lor (Abs(r\z - EntityZ(n\Collider, True)) > 12.0)) And (Rand(Max(4 - Int(Abs(r\z - EntityZ(n\Collider, True) / 8.0)), 2)) = 1) Then
										x = r\x
										y = 0.1
										z = r\z
										Exit
									EndIf
								Next
							Else
								Local Tmp% = False
								
								If (Not EntityVisible(n\Collider, Curr173\Collider)) Then
									If EntityDistanceSquared(n\Collider, Curr173\Collider) > 16.0 Then Tmp = True
								EndIf
								
								If (Not Tmp) Then
									For r.Rooms = Each Rooms
										If r\RoomTemplate\Name = "cont_173" Then
											Local FoundChamber% = False
											Local Pvt% = CreatePivot()
											
											PositionEntity(Pvt, EntityX(r\obj, True) + 4736.0 * RoomScale, 0.5, EntityZ(r\obj, True) + 1692.0 * RoomScale)
											
											If DistanceSquared(EntityX(Pvt), EntityX(n\Collider), EntityZ(Pvt), EntityZ(n\Collider)) < 12.25 Then
												FoundChamber = True
											EndIf
											
											If Curr173\Idle = 3 And DistanceSquared(EntityX(Pvt), EntityX(n\Collider), EntityZ(Pvt), EntityZ(n\Collider)) > 16.0 Then
												If r\RoomDoors[1]\open Then OpenCloseDoor(r\RoomDoors[1])
											EndIf
											
											FreeEntity(Pvt)
											
											If DistanceSquared(EntityX(n\Collider), EntityX(r\obj, True) + 4736.0 * RoomScale, EntityZ(n\Collider), EntityZ(r\obj, True) + 1692.0 * RoomScale) > 2.56 And (Not FoundChamber) Then
												x = EntityX(r\obj, True) + 4736.0 * RoomScale
												y = 0.1
												z = EntityZ(r\obj, True) + 1692.0 * RoomScale
												Exit
											ElseIf DistanceSquared(EntityX(n\Collider), EntityX(r\obj, True) + 4736.0 * RoomScale, EntityZ(n\Collider), EntityZ(r\obj, True) + 1692.0 * RoomScale) > 2.56 And FoundChamber
												n\PathX = EntityX(r\obj, True) + 4736.0 * RoomScale
												n\PathZ = EntityZ(r\obj, True) + 1692.0 * RoomScale
												Exit
											Else
												Curr173\Idle = 3
												Curr173\Target = Null
												;LoadNPCSound(n, "SFX\Character\MTF\173\Cont" + Rand(4) + ".ogg")
												PlayMTFSound(n\Sound, n)
												If PlayerInReachableRoom() Then PlayAnnouncement("SFX\Character\MTF\Announc173Contain.ogg")
												Exit
											EndIf
										EndIf
									Next
								Else
									x = EntityX(Curr173\Collider)
									y = 0.1
									z = EntityZ(Curr173\Collider)
								EndIf
							EndIf
							If n\PathX = 0 Then n\PathStatus = FindPath(n, x, y, z) ; ~ We're going to this room for no particular reason
						EndIf
						If n\PathStatus = 1 Then
							While n\Path[n\PathLocation] = Null
								If n\PathLocation > 19 Then Exit
								n\PathLocation = n\PathLocation + 1
							Wend
							If n\PathLocation < 19 Then
								If n\Path[n\PathLocation] <> Null And n\Path[n\PathLocation + 1] <> Null Then
									If n\Path[n\PathLocation]\door = Null Then
										If Abs(DeltaYaw(n\Collider, n\Path[n\PathLocation]\obj)) > Abs(DeltaYaw(n\Collider, n\Path[n\PathLocation + 1]\obj)) Then n\PathLocation = n\PathLocation + 1
									EndIf
								EndIf
							EndIf
						EndIf
						n\PathTimer = 70.0 * Rnd(6.0, 10.0) ; ~ Search again after 6-10 seconds
					ElseIf n\PathTimer <= 70.0 * 2.5 And n\MTFLeader = Null
						n\PathTimer = n\PathTimer - fps\Factor[0]
						n\CurrSpeed = 0.0
						If Rand(35) = 1 Then RotateEntity(n\Collider, 0.0, Rnd(360.0), 0.0, True)
						FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
						n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
						RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
					Else
						If n\PathStatus = 2 Then
							n\PathTimer = n\PathTimer - (fps\Factor[0] * 2.0) ; ~ Timer goes down fast
							n\CurrSpeed = 0.0
							If Rand(35) = 1 Then RotateEntity(n\Collider, 0.0, Rnd(360.0), 0.0, True)
							FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
							n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
							RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
						ElseIf n\PathStatus = 1
							If n\Path[n\PathLocation] = Null Then
								If n\PathLocation > 19 Then
									n\PathLocation = 0 : n\PathStatus = 0
								Else
									n\PathLocation = n\PathLocation + 1
								EndIf
							Else
								PrevDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
								
								PointEntity(n\Collider, n\Path[n\PathLocation]\obj)
								RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
								
								n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
								
								RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
								
								n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
								
								TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
								AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 26.0)
								
								NewDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
								
								;If NewDist < 1.0 Then UseDoorNPC(d,n)
								
								If (NewDist < 0.04) Lor ((PrevDist < NewDist) And (PrevDist < 1.0)) Then n\PathLocation = n\PathLocation + 1
							EndIf
							n\PathTimer = n\PathTimer - fps\Factor[0] ; ~ Timer goes down slow
						ElseIf n\PathX <> 0.0
							Pvt = CreatePivot()
							PositionEntity(Pvt, n\PathX, 0.5, n\PathZ)
							
							PointEntity(n\Collider, Pvt)
							RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
							n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
							RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
							
							n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
							TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
							AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 26.0)
							
							If DistanceSquared(EntityX(n\Collider), n\PathX, EntityZ(n\Collider), n\PathZ) < 0.04 Then
								n\PathX = 0.0
								n\PathZ = 0.0
								n\PathTimer = 70.0 * Rnd(6.0, 10.0)
							EndIf
							
							FreeEntity(Pvt)
						Else
							n\PathTimer = n\PathTimer - (fps\Factor[0] * 2.0) ; ~ Timer goes down fast
							If n\MTFLeader = Null Then
								If Rand(35) = 1 Then RotateEntity(n\Collider, 0.0, Rnd(360.0), 0.0, True)
								FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
								n\CurrSpeed = 0.0
							ElseIf EntityDistanceSquared(n\Collider, n\MTFLeader\Collider) > 1.0
								PointEntity(n\Collider, n\MTFLeader\Collider)
								RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
								
								n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
								TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
								AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 26.0)
							Else
								If Rand(35) = 1 Then RotateEntity(n\Collider, 0.0, Rnd(360.0), 0.0, True)
								FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
								n\CurrSpeed = 0.0
							EndIf
							n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
							RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
						EndIf
					EndIf
					
					Local Temp% = NPCSeesEntity(n,Camera)
					
					If (I_268\Using > 0 And I_268\Timer > 0.0) Then Temp = False
					
					If Temp > 0 Then
						If n\LastSeen > 0 And n\LastSeen < 70.0 * 15.0 Then
							If Temp < 2 Then
								;LoadNPCSound(n, "SFX\Character\MTF\ThereHeIs" + Rand(6) + ".ogg")
								PlayMTFSound("SFX\Character\MTF\ThereHeIs" + Rand(6) + ".ogg", n)
							EndIf
						Else
							If Temp = 1 Then
								;LoadNPCSound(n, "SFX\Character\MTF\Stop" + Rand(6) + ".ogg")
								PlayMTFSound("SFX\Character\MTF\Stop" + Rand(6) + ".ogg", n)
							ElseIf Temp = 2
								;LoadNPCSound(n, "SFX\Character\MTF\ClassD" + Rand(4) + ".ogg")
								PlayMTFSound("SFX\Character\MTF\ClassD" + Rand(4) + ".ogg", n)
							EndIf
						EndIf
						
						n\LastSeen = 70.0 * Rnd(30.0, 40.0)
						n\LastDist = 1.0
						
						n\State = 1.0
						n\EnemyX = EntityX(Collider, True)
						n\EnemyY = EntityY(Collider, True)
						n\EnemyZ = EntityZ(Collider, True)
						n\State2 = 70.0 * (15.0 * Temp) ; ~ Give up after 15 seconds (30 seconds if detected by loud noise, over camera: 45)
						n\PathTimer = 0.0
						n\PathStatus = 0
						n\Reload = 200.0 - (100.0 * SelectedDifficulty\AggressiveNPCs)
					EndIf
					
					; ~ B3D doesn't do short-circuit evaluation, so this retarded nesting is an optimization
;					If Curr173\Idle < 2 Then
;						Local SoundVol173# = Max(Min((Distance(EntityX(Curr173\Collider), Curr173\PrevX, EntityZ(Curr173\Collider), Curr173\PrevZ) * 2.5), 1.0), 0.0)
;						
;						If NPCSeesEntity(Curr173, n) Lor (SoundVol173 > 0.0 And EntityDistanceSquared(n\Collider, Curr173\Collider) < 36.0) Then
;							If EntityVisible(n\Collider, Curr173\Collider) Lor SoundVol173 > 0.0 Then							
;								n\State = 2.0
;								n\EnemyX = EntityX(Curr173\Collider, True)
;								n\EnemyY = EntityY(Curr173\Collider, True)
;								n\EnemyZ = EntityZ(Curr173\Collider, True)
;								n\State2 = 70.0 * 15.0 ; ~ Give up after 15 seconds
;								n\State3 = 0.0
;								n\PathTimer = 0.0
;								n\PathStatus = 0
;								;LoadNPCSound(n, "SFX\Character\MTF\173\Spotted" + Rand(2) + ".ogg")
;								PlayMTFSound(n\Sound, n)
;							EndIf
;						EndIf
;					EndIf
;					
;					If Curr106\State <= 0.0 Then
;						If NPCSeesEntity(Curr106, n) Lor EntityDistanceSquared(n\Collider, Curr106\Collider) < 9.0 Then
;							If EntityVisible(n\Collider, Curr106\Collider) Then
;								n\State = 4.0
;								n\EnemyX = EntityX(Curr106\Collider, True)
;								n\EnemyY = EntityY(Curr106\Collider, True)
;								n\EnemyZ = EntityZ(Curr106\Collider, True)
;								n\State2 = 70.0 * 15.0
;								n\State3 = 0.0
;								n\PathTimer = 0.0
;								n\PathStatus = 0
;								n\Target = Curr106
;								;LoadNPCSound(n, "SFX\Character\MTF\106\Spotted" + Rand(3) + ".ogg")
;								PlayMTFSound(n\Sound, n)
;							EndIf
;						EndIf
;					EndIf
;					
;					If Curr096 <> Null Then
;						If NPCSeesEntity(Curr096, n) Then
;							If EntityVisible(n\Collider, Curr096\Collider) Then
;								n\State = 8.0
;								n\EnemyX = EntityX(Curr096\Collider, True)
;								n\EnemyY = EntityY(Curr096\Collider, True)
;								n\EnemyZ = EntityZ(Curr096\Collider, True)
;								n\State2 = 70.0 * 15.0
;								n\State3 = 0.0
;								n\PathTimer = 0.0
;								n\PathStatus = 0
;								;LoadNPCSound(n, "SFX\Character\MTF\096\Spotted" + Rand(2) + ".ogg")
;								PlayMTFSound(n\Sound, n)
;							EndIf
;						EndIf
;					EndIf
;					
;;					If n_I\Curr049 <> Null Then
;;						If NPCSeesNPC(n_I\Curr049, n) Then
;;							If EntityVisible(n\Collider, n_I\Curr049\Collider)
;;								n\State = 4.0
;;								n\EnemyX = EntityX(n_I\Curr049\Collider, True)
;;								n\EnemyY = EntityY(n_I\Curr049\Collider, True)
;;								n\EnemyZ = EntityZ(n_I\Curr049\Collider, True)
;;								n\State2 = 70.0 * 15.0
;;								n\State3 = 0.0
;;								n\PathTimer = 0.0
;;								n\PathStatus = 0
;;								n\Target = n_I\Curr049
;;								;LoadNPCSound(n, "SFX\Character\MTF\049\Spotted" + Rand(5) + ".ogg")
;;								PlayMTFSound(n\Sound, n)
;;							EndIf
;;						EndIf
;;					EndIf
;					
;					For n2.NPCs = Each NPCs
;						If n2\NPCtype = NPCtype049_2 And (Not n2\IsDead)
;							If NPCSeesEntity(n2, n) Then
;								If EntityVisible(n\Collider, n2\Collider)
;									n\State = 9.0
;									n\EnemyX = EntityX(n2\Collider, True)
;									n\EnemyY = EntityY(n2\Collider, True)
;									n\EnemyZ = EntityZ(n2\Collider, True)
;									n\State2 = 70.0 * 15.0
;									n\State3 = 0.0
;									n\PathTimer = 0.0
;									n\PathStatus = 0
;									n\Target = n2
;									n\Reload = 70.0 * 5.0
;									;LoadNPCSound(n, "SFX\Character\MTF\049_2\Spotted.ogg")
;									PlayMTFSound(n\Sound, n)
;									Exit
;								EndIf
;							EndIf
;						ElseIf n2\NPCtype = NPCtype008 And (Not n2\IsDead)
;							If NPCSeesEntity(n2, n) Then
;								If EntityVisible(n\Collider, n2\Collider)
;									n\State = 9.0
;									n\EnemyX = EntityX(n2\Collider, True)
;									n\EnemyY = EntityY(n2\Collider, True)
;									n\EnemyZ = EntityZ(n2\Collider, True)
;									n\State2 = 70.0 * 15.0
;									n\State3 = 0.0
;									n\PathTimer = 0.0
;									n\PathStatus = 0
;									n\Target = n2
;									n\Reload = 70.0 * 5.0
;									Exit
;								EndIf
;							EndIf
;						ElseIf n2\NPCtype = NPCtypeTentacle And (Not n2\IsDead)
;							If NPCSeesEntity(n2, n) Then
;								If EntityVisible(n\Collider, n2\Collider)
;									n\State = 9.0
;									n\EnemyX = EntityX(n2\Collider, True)
;									n\EnemyY = EntityY(n2\Collider, True)
;									n\EnemyZ = EntityZ(n2\Collider, True)
;									n\State2 = 70.0 * 15.0
;									n\State3 = 0.0
;									n\PathTimer = 0.0
;									n\PathStatus = 0
;									n\Target = n2
;									n\Reload = 70.0 * 5.0
;									Exit
;								EndIf
;							EndIf
;						EndIf
					;Next
					;[End Block]
				Case 1.0 ; ~ Searching for player
					;[Block]
					n\Speed = 0.015
					n\State2 = n\State2 - fps\Factor[0]
					If NPCSeesEntity(n,Camera) = 1 Then
						; ~ If close enough, start shooting at the player
						If Dist < 25.0 Then
							Local Angle# = VectorYaw(EntityX(Collider) - EntityX(n\Collider), 0.0, EntityZ(Collider) - EntityZ(n\Collider))
							
							RotateEntity(n\Collider, 0.0, CurveAngle(Angle, EntityYaw(n\Collider), 10.0), 0.0, True)
							n\Angle = EntityYaw(n\Collider)
							
							If n\Reload <= 0.0 And (Not terminated) Then
								If EntityVisible(n\Collider, Camera) Then
									Angle = WrapAngle(Angle - EntityYaw(n\Collider))
									If Angle < 5.0 Lor Angle > 355.0 Then 
										Local PrevTerminated# = terminated
										
										PlaySound2(GunshotSFX, Camera, n\Collider, 15.0)
										
										Pvt = CreatePivot()
										
										RotateEntity(Pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0.0, True)
										PositionEntity(Pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
										MoveEntity(Pvt, 0.0632, 0.84925, 0.5451)
										
										;ShootPlayer(EntityX(Pvt), EntityY(Pvt), EntityZ(Pvt), ((25.0 / Sqr(Dist)) * (1.0 / Sqr(Dist))), True)
										;n\Reload = 7.0
										If n\Gun\MaxGunshotSounds = 1 Then
											n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 35)
										Else
											n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 35)
										EndIf
										ShootPlayer(0, 0, 0, Clamp(2 / Dist, 0.0, 0.65), True, n\Gun\Damage * Rand(1, 1.12))
										n\Gun\Ammo = n\Gun\Ammo - 1
										n\Reload = n\Gun\ShootFrequency
										
										FreeEntity(Pvt)
										
										m_msg\DeathTxt = GetLocalStringR("Singleplayer","death_ci",Designation)
										
										If (Not PrevTerminated) And terminated Then
											m_msg\DeathTxt = GetLocalStringR("Singleplayer","death_ci",Designation)
											PlayMTFSound(LoadTempSound("SFX\Character\MTF\TargetTerminated" + Rand(4) + ".ogg"), n)
										EndIf
									EndIf	
								EndIf
								
								If n\Gun\Ammo <= 0 Then
									PlayNPCSound(n, LoadTempSound("SFX\Character\D-Class\Reload"+Rand(1, 4)+".ogg"))
									n\State = 11
								EndIf
								
							EndIf
							
							For n2.NPCs = Each NPCs
								If n2\NPCtype = NPCTypeCITest And n2 <> n Then
									If n2\State = 0.0 Then
										If EntityDistanceSquared(n\Collider, n2\Collider) < 36.0 Then
											n\PrevState = 1
											n2\LastSeen = (70.0 * Rnd(30.0, 40.0))
											n2\LastDist = 1.0
											
											n2\State = 1.0
											n2\EnemyX = EntityX(Collider, True)
											n2\EnemyY = EntityY(Collider, True)
											n2\EnemyZ = EntityZ(Collider, True)
											n2\State2 = n\State2
											n2\PathTimer = 0.0
											n2\PathStatus = 0
											n2\Reload = 200.0 - (100.0 * SelectedDifficulty\AggressiveNPCs)
											n2\PrevState = 0
										EndIf
									EndIf
								EndIf
							Next
							
							If n\PrevState = 1 Then
								SetNPCFrame(n, 423.0)
								n\PrevState = 2
							ElseIf n\PrevState = 2
								If n\Frame > 200.0 Then
									n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 20.0)
									AnimateNPC(n, 423.0, 463.0, 0.4, False)
									If n\Frame > 462.9 Then n\Frame = 78.0
								Else
									AnimateNPC(n, 78.0, 193.0, 0.2, False)
									n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 20.0)
								EndIf
							Else
								If n\Frame > 958.0 Then
									AnimateNPC(n, 1374.0, 1383.0, 0.3, False)
									n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 20.0)
									If n\Frame > 1382.9 Then n\Frame = 78.0
								Else
									AnimateNPC(n, 78.0, 193.0, 0.2, False)
									n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 20.0)
								EndIf
							EndIf
						Else
							PositionEntity(n\obj, n\EnemyX, n\EnemyY, n\EnemyZ, True)
							PointEntity(n\Collider, n\obj)
							RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
							n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
							RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
							
							n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
							TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
							AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 26.0)
						EndIf
					Else
						n\LastSeen = n\LastSeen - fps\Factor[0]
						
						If n\Reload <= 7.0 Then n\Reload = 7.0
						
						If n\PathTimer <= 0.0 Then ; ~ Update path
							n\PathStatus = FindPath(n, n\EnemyX, n\EnemyY + 0.1, n\EnemyZ)
							n\PathTimer = 70.0 * Rnd(6.0, 10.0) ; ~ Search again after 6 seconds
						ElseIf n\PathTimer <= 70.0 * 2.5
							n\PathTimer = n\PathTimer - fps\Factor[0]
							n\CurrSpeed = 0.0
							If Rand(35) = 1 Then RotateEntity(n\Collider, 0.0, Rnd(360.0), 0.0, True)
							FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
							n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
							RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
						Else
							If n\PathStatus = 2 Then
								n\PathTimer = n\PathTimer - (fps\Factor[0] * 2.0) ; ~ Timer goes down fast
								n\CurrSpeed = 0.0
								If Rand(35) = 1 Then RotateEntity(n\Collider, 0.0, Rnd(360.0), 0.0, True)
								FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
								n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
								RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
							ElseIf n\PathStatus = 1
								If n\Path[n\PathLocation] = Null Then
									If n\PathLocation > 19 Then
										n\PathLocation = 0 : n\PathStatus = 0
									Else
										n\PathLocation = n\PathLocation + 1
									EndIf
								Else
									PrevDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
									
									PointEntity(n\Collider, n\Path[n\PathLocation]\obj)
									RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
									n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
									RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
									
									n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
									
									TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
									AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 26.0)
									
									NewDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
									
									;If NewDist < 1.0 Then UseDoorNPC(n)
									
									If (NewDist < 0.04) Lor ((PrevDist < NewDist) And (PrevDist < 1.0)) Then n\PathLocation = n\PathLocation + 1
								EndIf
								n\PathTimer = n\PathTimer - fps\Factor[0] ; ~ Timer goes down slow
							Else
								PositionEntity(n\obj, n\EnemyX, n\EnemyY, n\EnemyZ, True)
								If DistanceSquared(EntityX(n\Collider, True), n\EnemyX, EntityZ(n\Collider, True), n\EnemyZ) < 0.04 Lor (Not EntityVisible(n\obj, n\Collider)) Then
									If Rand(35) = 1 Then RotateEntity(n\Collider, 0.0, Rnd(360.0), 0.0, True)
									FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
									If Rand(35) = 1 Then
										For wp.WayPoints = Each WayPoints
											If Rand(3) = 1 Then
												If EntityDistanceSquared(wp\obj, n\Collider) < 36.0 Then
													n\EnemyX = EntityX(wp\obj, True)
													n\EnemyY = EntityY(wp\obj, True)
													n\EnemyZ = EntityZ(wp\obj, True)
													n\PathTimer = 0.0
													Exit
												EndIf
											EndIf
										Next
									EndIf
									n\PathTimer = n\PathTimer - fps\Factor[0] ; ~ Timer goes down slow
								Else
									PointEntity(n\Collider, n\obj)
									RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
									n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
									RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
									
									n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
									TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
									AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 26.0)
								EndIf
							EndIf
						EndIf
						
						If n\MTFLeader = Null And n\LastSeen < 70.0 * 30.0 And n\LastSeen + fps\Factor[0] >= 70.0 * 30.0 Then
							If Rand(2) = 1 Then PlayMTFSound(LoadTempSound("SFX\Character\MTF\Searching" + Rand(6) + ".ogg"), n)
						EndIf
					EndIf
					
					If n\State2 <= 0.0 And n\State2 + fps\Factor[0] > 0.0 Then
						If n\MTFLeader = Null Then
							PlayMTFSound(LoadTempSound("SFX\Character\MTF\Targetlost" + Rand(3) + ".ogg"), n)
							;If MTFCameraCheckTimer = 0.0 Then
							;	If Rand(15 - (7 * SelectedDifficulty\AggressiveNPCs)) = 1 ; ~ Maybe change this to another chance -- ENDSHN
							;		If PlayerInReachableRoom() Then PlayAnnouncement("SFX\Character\MTF\AnnouncCameraCheck.ogg")
							;		MTFCameraCheckTimer = fps\Factor[0]
							;	EndIf
							;EndIf
						EndIf
						n\State = 0.0
					EndIf
					
					; ~ B3D doesn't do short-circuit evaluation, so this retarded nesting is an optimization
;					If Curr173\Idle < 2 Then
;						SoundVol173 = Max(Min((Distance(EntityX(Curr173\Collider), Curr173\PrevX, EntityZ(Curr173\Collider), Curr173\PrevZ) * 2.5), 1.0), 0.0)
;						If NPCSeesEntity(Curr173, n) Lor (SoundVol173 > 0.0 And EntityDistanceSquared(n\Collider, Curr173\Collider) < 36.0) Then
;							If EntityVisible(n\Collider, Curr173\Collider) Lor SoundVol173 > 0.0 Then	
;								n\State = 2.0
;								n\EnemyX = EntityX(Curr173\Collider, True)
;								n\EnemyY = EntityY(Curr173\Collider, True)
;								n\EnemyZ = EntityZ(Curr173\Collider, True)
;								n\State2 = 70.0 * 15.0 ; ~ Give up after 15 seconds
;								;LoadNPCSound(n, "SFX\Character\MTF\173\Spotted3.ogg")
;								PlayMTFSound(n\Sound, n)
;								n\State3 = 0.0
;								n\PathTimer = 0.0
;								n\PathStatus = 0
;							EndIf
;						EndIf
;					EndIf
;					
;					If Curr106\State <= 0.0 Then
;						If NPCSeesEntity(Curr106, n) Lor EntityDistanceSquared(n\Collider, Curr106\Collider) < 9.0 Then
;							If EntityVisible(n\Collider, Curr106\Collider) Then
;								n\State = 4.0
;								n\EnemyX = EntityX(Curr106\Collider, True)
;								n\EnemyY = EntityY(Curr106\Collider, True)
;								n\EnemyZ = EntityZ(Curr106\Collider, True)
;								n\State2 = 70.0 * 15.0
;								n\State3 = 0.0
;								n\PathTimer = 0.0
;								n\PathStatus = 0
;								n\Target = Curr106
;								If n\MTFLeader = Null Then
;									;LoadNPCSound(n, "SFX\Character\MTF\106\Spotted4.ogg")
;									PlayMTFSound(n\Sound, n)
;								EndIf
;							EndIf
;						EndIf
;					EndIf
;					
;					If Curr096 <> Null Then
;						If NPCSeesEntity(Curr096, n) Then
;							If EntityVisible(n\Collider, Curr096\Collider) Then
;								n\State = 8.0
;								n\EnemyX = EntityX(Curr096\Collider, True)
;								n\EnemyY = EntityY(Curr096\Collider, True)
;								n\EnemyZ = EntityZ(Curr096\Collider, True)
;								n\State2 = 70.0 * 15.0
;								n\State3 = 0.0
;								n\PathTimer = 0.0
;								n\PathStatus = 0
;								If n\MTFLeader = Null Then
;									;LoadNPCSound(n, "SFX\Character\MTF\096\Spotted" + Rand(2) + ".ogg")
;									PlayMTFSound(n\Sound, n)
;								EndIf
;							EndIf
;						EndIf
;					EndIf
;					
;;					If n_I\Curr049 <> Null Then
;;						If NPCSeesNPC(n_I\Curr049, n) Then
;;							If EntityVisible(n\Collider, n_I\Curr049\Collider)
;;								n\State = 4.0
;;								n\EnemyX = EntityX(n_I\Curr049\Collider, True)
;;								n\EnemyY = EntityY(n_I\Curr049\Collider, True)
;;								n\EnemyZ = EntityZ(n_I\Curr049\Collider, True)
;;								n\State2 = 70.0 * 15.0
;;								n\State3 = 0.0
;;								n\PathTimer = 0.0
;;								n\PathStatus = 0
;;								n\Target = NPCtype049
;;								;LoadNPCSound(n, "SFX\Character\MTF\049\Spotted" + Rand(5) + ".ogg")
;;								PlayMTFSound(n\Sound, n)
;;							EndIf
;;						EndIf
;;					EndIf
;					
;					For n2.NPCs = Each NPCs
;						If n2\NPCtype = NPCtype049_2 And (Not n2\IsDead)
;							If NPCSeesEntity(n2, n) Then
;								If EntityVisible(n\Collider, n2\Collider)
;									n\State = 9.0
;									n\EnemyX = EntityX(n2\Collider, True)
;									n\EnemyY = EntityY(n2\Collider, True)
;									n\EnemyZ = EntityZ(n2\Collider, True)
;									n\State2 = 70.0 * 15.0
;									n\State3 = 0.0
;									n\PathTimer = 0.0
;									n\PathStatus = 0
;									n\Target = n2
;									n\Reload = 70.0 * 5.0
;									;LoadNPCSound(n, "SFX\Character\MTF\049_2\Spotted.ogg")
;									PlayMTFSound(n\Sound, n)
;									Exit
;								EndIf
;							EndIf
;						ElseIf n2\NPCtype = NPCtype008 And (Not n2\IsDead)
;							If NPCSeesEntity(n2, n) Then
;								If EntityVisible(n\Collider, n2\Collider)
;									n\State = 9.0
;									n\EnemyX = EntityX(n2\Collider, True)
;									n\EnemyY = EntityY(n2\Collider, True)
;									n\EnemyZ = EntityZ(n2\Collider, True)
;									n\State2 = 70.0 * 15.0
;									n\State3 = 0.0
;									n\PathTimer = 0.0
;									n\PathStatus = 0
;									n\Target = n2
;									n\Reload = 70.0 * 5.0
;									Exit
;								EndIf
;							EndIf
;						ElseIf n2\NPCtype = NPCtypeTentacle And (Not n2\IsDead)
;							If NPCSeesEntity(n2, n) Then
;								If EntityVisible(n\Collider, n2\Collider)
;									n\State = 9.0
;									n\EnemyX = EntityX(n2\Collider, True)
;									n\EnemyY = EntityY(n2\Collider, True)
;									n\EnemyZ = EntityZ(n2\Collider, True)
;									n\State2 = 70.0 * 15.0
;									n\State3 = 0.0
;									n\PathTimer = 0.0
;									n\PathStatus = 0
;									n\Target = n2
;									n\Reload = 70.0 * 5.0
;									Exit
;								EndIf
;							EndIf
;						EndIf
;					Next
					;[End Block]
				Case 2.0 ; ~ Searching for / Looking at SCP-173
					;[Block]
					If Curr173\Idle = 2 Then
						n\State = 0.0
					Else
						For n2.NPCs = Each NPCs
							If n2 <> n Then
								If n2\NPCtype = NPCTypeCITest Then n2\State = 2.0
							EndIf
						Next
						
						Local Curr173Dist# = DistanceSquared(EntityX(n\Collider, True), EntityX(Curr173\Collider, True), EntityZ(n\Collider, True), EntityZ(Curr173\Collider, True))
						
						If Curr173Dist < 25.0 Then
							If Curr173\Idle <> 2 Then Curr173\Idle = 1
							n\State2 = 70.0 * 15.0
							n\PathTimer = 0.0
							
							Local TempDist# = 1.0
							
							If n\MTFLeader <> Null Then TempDist = 4.0
							If Curr173Dist < TempDist Then
								If n\MTFLeader = Null Then
									n\State3 = n\State3 + fps\Factor[0]
									If n\State3 >= 70.0 * 15.0 Then
										Curr173\Idle = 2
										If n\MTFLeader = Null Then Curr173\Target = n
										;LoadNPCSound(n, "SFX\Character\MTF\173\Box" + Rand(3) + ".ogg")
										PlayMTFSound(n\Sound, n)
									EndIf
								EndIf
								PositionEntity(n\obj, EntityX(Curr173\Collider, True), EntityY(Curr173\Collider, True), EntityZ(Curr173\Collider, True), True)
								PointEntity(n\Collider, n\obj)
								RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
								n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
								FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
								RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
							Else
								PositionEntity(n\obj, EntityX(Curr173\Collider, True), EntityY(Curr173\Collider, True), EntityZ(Curr173\Collider, True), True)
								PointEntity(n\Collider, n\obj)
								RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
								n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
								RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
								
								n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
								TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
								AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 26.0)
							EndIf
						Else
							If Curr173\Idle <> 2 Then Curr173\Idle = 0
							If n\PathTimer <= 0.0 Then ; ~ Update path
								n\PathStatus = FindPath(n, EntityX(Curr173\Collider, True), EntityY(Curr173\Collider, True) + 0.1, EntityZ(Curr173\Collider, True))
								n\PathTimer = 70.0 * Rnd(6.0, 10.0) ; ~ Search again after 6 seconds
							ElseIf n\PathTimer <= 70.0 * 2.5
								n\PathTimer = n\PathTimer - fps\Factor[0]
								n\CurrSpeed = 0.0
								If Rand(35) = 1 Then RotateEntity(n\Collider, 0.0, Rnd(360.0), 0.0, True)
								FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
								n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
								RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
							Else
								If n\PathStatus = 2 Then
									n\PathTimer = n\PathTimer - (fps\Factor[0] * 2.0) ; ~ Timer goes down fast
									n\CurrSpeed = 0.0
									If Rand(35) = 1 Then RotateEntity(n\Collider, 0.0, Rnd(360.0), 0.0, True)
									FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
									n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
									RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
								ElseIf n\PathStatus = 1
									If n\Path[n\PathLocation] = Null Then
										If n\PathLocation > 19 Then
											n\PathLocation = 0 : n\PathStatus = 0
										Else
											n\PathLocation = n\PathLocation + 1
										EndIf
									Else
										PrevDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
										
										PointEntity(n\Collider, n\Path[n\PathLocation]\obj)
										RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
										n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
										RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
										
										n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
										
										TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
										AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 26.0)
										
										NewDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
										
										;If NewDist < 1.0 Then UseDoorNPC(n)
										
										If (NewDist < 0.04) Lor ((PrevDist < NewDist) And (PrevDist < 1.0)) Then n\PathLocation = n\PathLocation + 1
									EndIf
									n\PathTimer = n\PathTimer - fps\Factor[0] ; ~ Timer goes down slow
								Else
									n\PathTimer = n\PathTimer - (fps\Factor[0] * 2.0) ; ~ Timer goes down fast
									n\CurrSpeed = 0.0
									If Rand(35) = 1 Then RotateEntity(n\Collider, 0.0, Rnd(360.0), 0.0, True)
									FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
									n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
									RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
								EndIf
							EndIf
						EndIf
					EndIf
					;[End Block]
				Case 3.0 ; ~ Following a path
					;[Block]
					n\Angle = CurveValue(0.0, n\Angle, 40.0)
					
					If n\PathStatus = 2 Then
						n\State = 5.0
						n\CurrSpeed = 0.0
					ElseIf n\PathStatus = 1
						If n\Path[n\PathLocation] = Null Then 
							If n\PathLocation > 19 Then 
								n\PathLocation = 0
								n\PathStatus = 0
								If n\LastSeen > 0 Then n\State = 5.0
							Else
								n\PathLocation = n\PathLocation + 1
							EndIf
						Else
							;UseDoorNPC(n)
							
							If Dist < PowTwo(HideDistance) Then 
								PointEntity(n\obj, n\Path[n\PathLocation]\obj)
								
								RotateEntity(n\Collider, 0.0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0.0)
								If n\Idle = 0 Then
									n\CurrSpeed = CurveValue(n\Speed * Max(Min(EntityDistance(n\Collider, n\Path[n\PathLocation]\obj), 1.0), 0.1), n\CurrSpeed, 20.0)
									MoveEntity(n\Collider, 0.0, 0.0, n\CurrSpeed * fps\Factor[0])
									
									If EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj) < 0.25 Then n\PathLocation = n\PathLocation + 1
								EndIf
							Else
								If Rand(20) = 1 Then 
									PositionEntity(n\Collider, EntityX(n\Path[n\PathLocation]\obj, True), EntityY(n\Path[n\PathLocation]\obj, True) + 0.25, EntityZ(n\Path[n\PathLocation]\obj, True), True)
									n\PathLocation = n\PathLocation + 1
									ResetEntity(n\Collider)
								EndIf
							EndIf
						EndIf
					Else
						n\CurrSpeed = 0.0
						n\State = 5.0
					EndIf
					
					If n\Idle = 0 And n\PathStatus = 1 Then
						If Dist < PowTwo(HideDistance) Then
							If n\Frame > 959.0 Then
								AnimateNPC(n, 1376.0, 1383.0, 0.2, False)
								If n\Frame > 1382.9 Then n\Frame = 488.0
							Else
								AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 30.0)
							EndIf
						EndIf
					Else
						If Dist < PowTwo(HideDistance) Then
							If n\LastSeen > 0 Then 
								AnimateNPC(n, 78.0, 312.0, 0.2)
							Else
								If n\Frame < 962.0 Then
									If n\Frame > 487.0 Then n\Frame = 463.0
									AnimateNPC(n, 463.0, 487.0, 0.3, False)
									If n\Frame > 486.9 Then n\Frame = 962.0
								Else
									AnimateNPC(n, 962.0, 1259.0, 0.3)
								EndIf
							EndIf
						EndIf
						n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 20.0)
					EndIf
					n\Angle = EntityYaw(n\Collider)
					;[End Block]
				Case 4.0 ; ~ SCP-106 or SCP-049 detected
					;[Block]
					n\Speed = 0.03
					n\State2 = n\State2 - fps\Factor[0]
					If n\State2 > 0.0 Then
						;If NPCSeesEntity(n, n\Target) n\State2 = 70.0 * 15.0
;							If n\State2 > 70.0 And EntityDistanceSquared(n\Target\Collider, n\Collider) > PowTwo(HideDistance) Then n\State2 = 70.0
;							If n\State3 >= 0.0 And EntityDistanceSquared(n\Target\Collider, n\Collider) < 9.0 Then n\State3 = 70.0 * 5.0
;							
;							If n\State3 > 0.0 Then
;								n\PathStatus = 0
;								n\PathLocation = 0
;								n\Speed = 0.02
;								PointEntity(n\Collider, n\Target\Collider)
;								RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
;								n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
;								RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
;								n\CurrSpeed = CurveValue(-n\Speed, n\CurrSpeed, 20.0)
;								TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
;								AnimateNPC(n, 522.0, 488.0, n\CurrSpeed * 26.0)
;								
;								n\PathTimer = 1.0
;								
;								n\State3 = Max(n\State3 - fps\Factor[0], 0.0)
;								
;								If (Not EntityHidden(n\Collider)) Then HideEntity(n\Collider)
;								TurnEntity(n\Collider, 0.0, 180.0, 0.0)
;								EntityPick(n\Collider, 1.0)
;								If PickedEntity() <> 0 Then n\State3 = (-70.0) * 2.0
;								If EntityHidden(n\Collider) Then ShowEntity(n\Collider)
;								TurnEntity(n\Collider, 0.0, 180.0, 0.0)
;							ElseIf n\State3 < 0.0
;								n\State3 = Min(n\State3 + fps\Factor[0], 0.0)
;							EndIf
;							
;							If n\PathTimer <= 0.0 Then
;								If n\MTFLeader <> Null Then
;									n\PathStatus = FindPath(n, EntityX(n\MTFLeader\Collider, True), EntityY(n\MTFLeader\Collider, True) + 0.1, EntityZ(n\MTFLeader\Collider, True))
;								Else
;									For r.Rooms = Each Rooms
;										If ((Abs(r\x - EntityX(n\Collider, True)) > 12.0) Lor (Abs(r\z - EntityZ(n\Collider, True)) > 12.0)) And (Rand(Max(4 - Int(Abs(r\z - EntityZ(n\Collider, True) / 8.0)), 2)) = 1) Then
;											If EntityDistanceSquared(r\obj, n\Target\Collider) > 36.0 Then
;												x = r\x
;												y = 0.1
;												z = r\z
;												Exit
;											EndIf
;										EndIf
;									Next
;									n\PathStatus = FindPath(n, x, y, z)
;								EndIf
;								If n\PathStatus = 1 Then
;									While n\Path[n\PathLocation] = Null
;										If n\PathLocation > 19 Then Exit
;										n\PathLocation = n\PathLocation + 1
;									Wend
;									If n\PathLocation < 19 Then
;										If n\Path[n\PathLocation] <> Null And n\Path[n\PathLocation + 1] <> Null Then
;											If n\Path[n\PathLocation]\door = Null Then
;												If Abs(DeltaYaw(n\Collider, n\Path[n\PathLocation]\obj)) > Abs(DeltaYaw(n\Collider, n\Path[n\PathLocation + 1]\obj)) Then n\PathLocation = n\PathLocation + 1
;											EndIf
;										EndIf
;									EndIf
;								EndIf
;								n\PathTimer = 70.0 * 10.0
;							Else
;								If n\PathStatus = 1 Then
;									If n\Path[n\PathLocation] = Null Then
;										If n\PathLocation > 19 Then
;											n\PathLocation = 0 : n\PathStatus = 0
;										Else
;											n\PathLocation = n\PathLocation + 1
;										EndIf
;									Else
;										PrevDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
;										
;										PointEntity(n\Collider, n\Path[n\PathLocation]\obj)
;										RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
;										n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
;										RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
;										
;										n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
;										TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
;										AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 26.0) ; ~ Placeholder (until running animation has been implemented)
;										
;										NewDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
;										
;										If NewDist < 4.0 Then UseDoorNPC(n)
;										
;										If (NewDist < 0.04) Lor ((PrevDist < NewDist) And (PrevDist < 1.0)) Then n\PathLocation = n\PathLocation + 1
;									EndIf
;									n\PathTimer = n\PathTimer - fps\Factor[0]
;								Else
;									n\PathTimer = 0.0
;								EndIf
;							EndIf
						;Else
							n\State = 0.0
						EndIf
						;[End Block]
					Case 5.0 ; ~ Looking at some other target than the player
						;[Block]
						Target = CreatePivot()
						PositionEntity(Target, n\EnemyX, n\EnemyY, n\EnemyZ, True)
						
						If Dist < PowTwo(HideDistance) Then AnimateNPC(n, 79.0, 194.0, 0.2)
						
						If Abs(EntityX(Target) - EntityX(n\Collider)) < 55.0 And Abs(EntityZ(Target) - EntityZ(n\Collider)) < 55.0 And Abs(EntityY(Target) - EntityY(n\Collider)) < 20.0 Then
							PointEntity(n\obj, Target)
							RotateEntity(n\Collider, 0.0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 30.0), 0.0, True)
							
							If n\PathTimer = 0 Then
								n\PathStatus = EntityVisible(n\Collider, Target)
								n\PathTimer = Rnd(100.0, 200.0)
							Else
								n\PathTimer = Min(n\PathTimer - fps\Factor[0], 0.0)
							EndIf
							
							If n\PathStatus = 1 And n\Reload <= 0.0 Then Dist = DistanceSquared(EntityX(Target), EntityX(n\Collider), EntityZ(Target), EntityZ(n\Collider))
						EndIf		
						
						FreeEntity(Target)
						
						n\Angle = EntityYaw(n\Collider)
						;[End Block]
					Case 6.0 ; ~ Seeing the player as SCP-049-2 instance / Shooting at player
						;[Block]
						PointEntity(n\obj, Collider)
						RotateEntity(n\Collider, 0.0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0.0)
						n\Angle = EntityYaw(n\Collider)
						
						AnimateNPC(n, 346.0, 351.0, 0.2, False)
						
						If n\Reload <= 0.0 And (Not terminated) Then
							If EntityVisible(n\Collider, Collider) Then
								If Abs(DeltaYaw(n\Collider, Collider)) < 50.0 Then
									PrevTerminated = terminated
									
									PlaySound2(GunshotSFX, Camera, n\Collider, 15.0)
									
									Pvt = CreatePivot()
									
									RotateEntity(Pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0.0, True)
									PositionEntity(Pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
									MoveEntity(Pvt, 0.0632, 0.84925, 0.5451)
									
									ShootPlayer(EntityX(Pvt), EntityY(Pvt), EntityZ(Pvt), 0.9)
									n\Reload = 7.0
									
									FreeEntity(Pvt)
									
									m_msg\DeathTxt = GetLocalStringR("Singleplayer","death_ci",Designation)
									
									If (Not PrevTerminated) And terminated Then
										m_msg\DeathTxt = GetLocalStringR("Singleplayer","death_ci",Designation)
										PlayMTFSound(LoadTempSound("SFX\Character\MTF\Targetterminated" + Rand(4) + ".ogg"), n)
									EndIf
								EndIf
							EndIf
						EndIf
						;[End Block]
					Case 7.0 ; ~ Just shooting
						;[Block]
						AnimateNPC(n, 346.0, 351.0, 0.2, False)
						
						RotateEntity(n\Collider, 0.0, CurveAngle(n\State2, EntityYaw(n\Collider), 20.0), 0.0)
						n\Angle = EntityYaw(n\Collider)
						
						If n\Reload <= 0.0 Then
							LightVolume = TempLightVolume * 1.2
							PlaySound2(GunshotSFX, Camera, n\Collider, 20.0)
							
							Pvt = CreatePivot()
							
							RotateEntity(Pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0.0, True)
							PositionEntity(Pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
							MoveEntity(Pvt,0.0632, 0.84925, 0.5451)
							
							p.Particles = CreateParticle(EntityX(Pvt), EntityY(Pvt), EntityZ(Pvt),1, Rnd(0.08, 0.1), 0.0, 5.0)
							p\Achange = -0.15
							TurnEntity(p\obj, 0.0, 0.0, Rnd(360.0))
							
							FreeEntity(Pvt)
							n\Reload = 7.0
						EndIf
						;[End Block]
					Case 8.0 ; ~ SCP-096 spotted
						;[Block]
						n\Speed = 0.015
						If n\PathTimer <= 0.0 Then ; ~ Update path
							If n\MTFLeader <> Null Then ; ~ I'll follow the leader
								n\PathStatus = FindPath(n, EntityX(n\MTFLeader\Collider, True), EntityY(n\MTFLeader\Collider, True) + 0.1, EntityZ(n\MTFLeader\Collider, True)) ; ~ Whatever you say boss
							Else ; ~ I am the leader
								For r.Rooms = Each Rooms
									If ((Abs(r\x - EntityX(n\Collider, True)) > 12.0) Lor (Abs(r\z - EntityZ(n\Collider, True)) > 12.0)) And (Rand(Max(4 - Int(Abs(r\z - EntityZ(n\Collider, True) / 8.0)), 2)) = 1) Then
										x = r\x
										y = 0.1
										z = r\z
										Exit
									EndIf
								Next
								n\PathStatus = FindPath(n, x, y, z) ; ~ We're going to this room for no particular reason
							EndIf
							If n\PathStatus = 1 Then
								While n\Path[n\PathLocation] = Null
									If n\PathLocation > 19 Then Exit
									n\PathLocation = n\PathLocation + 1
								Wend
								If n\PathLocation < 19 Then
									If n\Path[n\PathLocation] <> Null And n\Path[n\PathLocation + 1] <> Null Then
										If n\Path[n\PathLocation]\door = Null Then
											If Abs(DeltaYaw(n\Collider, n\Path[n\PathLocation]\obj)) > Abs(DeltaYaw(n\Collider, n\Path[n\PathLocation + 1]\obj)) Then n\PathLocation = n\PathLocation + 1
										EndIf
									EndIf
								EndIf
							EndIf
							n\PathTimer = 70.0 * Rnd(6.0, 10.0) ; ~ Search again after 6-10 seconds
						ElseIf n\PathTimer <= 70.0 * 2.5 And n\MTFLeader = Null
							n\PathTimer = n\PathTimer - fps\Factor[0]
							n\CurrSpeed = 0.0
							FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
							n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
							RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
						Else
							If n\PathStatus = 2 Then
								n\PathTimer = n\PathTimer - (fps\Factor[0] * 2.0) ; ~ Timer goes down fast
								n\CurrSpeed = 0.0
								FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
								n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
								RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
							ElseIf n\PathStatus = 1
								If n\Path[n\PathLocation] = Null Then
									If n\PathLocation > 19 Then
										n\PathLocation = 0 : n\PathStatus = 0
									Else
										n\PathLocation = n\PathLocation + 1
									EndIf
								Else
									PrevDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
									
									PointEntity(n\Collider, n\Path[n\PathLocation]\obj)
									RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
									n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
									RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
									
									n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
									TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
									AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 26.0)
									
									NewDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
									
									;If NewDist < 1.0 Then UseDoorNPC(n)
									
									If (NewDist < 0.04) Lor ((PrevDist < NewDist) And (PrevDist < 1.0)) Then n\PathLocation = n\PathLocation + 1
								EndIf
								n\PathTimer = n\PathTimer - fps\Factor[0] ; ~ Timer goes down slow
							Else
								n\PathTimer = n\PathTimer - (fps\Factor[0] * 2.0) ; ~ Timer goes down fast
								If n\MTFLeader = Null Then
									FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
									n\CurrSpeed = 0.0
								ElseIf EntityDistanceSquared(n\Collider, n\MTFLeader\Collider) > 1.0
									PointEntity(n\Collider, n\MTFLeader\Collider)
									RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
									
									n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
									TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
									AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 26.0)
								Else
									FinishWalking(n, 488.0, 522.0, n\Speed * 26.0)
									n\CurrSpeed = 0.0
								EndIf
								n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
								RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
							EndIf
						EndIf
						
						If (Not EntityVisible(n\Collider, Curr096\Collider)) Lor EntityDistanceSquared(n\Collider, Curr096\Collider) > 36.0 Then n\State = 0.0
						;[End Block]
					Case 9.0 ; ~ SCP-049-2 or SCP-008-1 or SCP-035's tentacle spotted
						;[Block]
						If EntityVisible(n\Collider, n\Target\Collider) Then
							PointEntity(n\obj, n\Target\Collider)
							RotateEntity(n\Collider, 0.0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0.0)
							n\Angle = EntityYaw(n\Collider)
							
							If EntityDistanceSquared(n\Target\Collider, n\Collider) < 1.69 Then n\State3 = 70.0 * 2.0
							
							If n\State3 > 0.0 Then
								n\PathStatus = 0
								n\PathLocation = 0
								n\Speed = 0.02
								n\CurrSpeed = CurveValue(-n\Speed, n\CurrSpeed, 20.0)
								TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
								AnimateNPC(n, 522.0, 488.0, n\CurrSpeed * 26.0)
								
								n\PathTimer = 1.0
								
								n\State3 = Max(n\State3 - fps\Factor[0], 0.0)
							Else
								n\State3 = 0.0
								AnimateNPC(n, 346.0, 351.0, 0.2, False)
							EndIf
							If n\Reload <= 0.0 And (Not n\Target\IsDead) Then
								If Abs(DeltaYaw(n\Collider, n\Target\Collider)) < 50.0 Then
									PlaySound2(GunshotSFX, Camera, n\Collider, 15.0)
									
									Pvt = CreatePivot()
									
									RotateEntity(Pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0.0, True)
									PositionEntity(Pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
									MoveEntity(Pvt, 0.0632, 0.84925, 0.5451)
									
									p.Particles = CreateParticle(EntityX(Pvt), EntityY(Pvt), EntityZ(Pvt),1, Rnd(0.08, 0.1), 0.0, 5.0)
									p\Achange = -0.15
									TurnEntity(p\obj, 0.0, 0.0, Rnd(360.0))
									If n\Target\HP > 0 Then
										n\Target\HP = Max(n\Target\HP - Rand(5, 10), 0.0)
									Else
										If (Not n\Target\IsDead) Then
											If n\Target\NPCtype = NPCtype049_2
												;LoadNPCSound(n, "SFX\Character\MTF\049_2\TargetTerminated.ogg")
												PlayMTFSound(n\Sound, n)
											EndIf
										EndIf
										SetNPCFrame(n\Target, 133.0)
										n\Target\IsDead = True
										n\Target = Null
										n\State = 0.0
										Return
									EndIf
									n\Reload = 7.0
									
									FreeEntity(Pvt)
								EndIf	
							EndIf
							n\PathStatus = 0
						Else
							If n\PathTimer <= 0.0 Then
								n\PathStatus = FindPath(n, EntityX(n\Target\Collider), EntityY(n\Target\Collider), EntityZ(n\Target\Collider))
								If n\PathStatus = 1 Then
									While n\Path[n\PathLocation] = Null
										If n\PathLocation > 19 Then Exit
										n\PathLocation = n\PathLocation + 1
									Wend
									If n\PathLocation < 19 Then
										If n\Path[n\PathLocation] <> Null And n\Path[n\PathLocation + 1] <> Null Then
											If n\Path[n\PathLocation]\door = Null Then
												If Abs(DeltaYaw(n\Collider, n\Path[n\PathLocation]\obj)) > Abs(DeltaYaw(n\Collider, n\Path[n\PathLocation + 1]\obj)) Then n\PathLocation = n\PathLocation + 1
											EndIf
										EndIf
									EndIf
								EndIf
								n\PathTimer = 70.0 * 10.0
							Else
								If n\PathStatus = 1 Then
									If n\Path[n\PathLocation] = Null Then
										If n\PathLocation > 19 Then
											n\PathLocation = 0 : n\PathStatus = 0
										Else
											n\PathLocation = n\PathLocation + 1
										EndIf
									Else
										PrevDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
										
										PointEntity(n\Collider, n\Path[n\PathLocation]\obj)
										RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
										n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
										RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
										
										n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
										TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
										AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 26.0)
										
										NewDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
										
										;If NewDist < 1.0 Then UseDoorNPC(n)
										;
										If (NewDist < 0.04) Lor ((PrevDist < NewDist) And (PrevDist < 1.0)) Then n\PathLocation = n\PathLocation + 1
									EndIf
									n\PathTimer = n\PathTimer - fps\Factor[0]
								Else
									n\PathTimer = 0.0
								EndIf
							EndIf
						EndIf
						
						If n\Target\IsDead Then
							n\Target = Null
							n\State = 0.0
						EndIf
						;[End Block]
					Case 10.0 ; ~ Trying to find player on the Gate A or Gate B
						;[Block]
						n\Angle = CurveValue(0.0, n\Angle, 40.0)
						
						If (Not terminated) Then
							SearchPlayer = False
							If Dist < 36.0 And EntityVisible(n\Collider, Collider) And (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then SearchPlayer = True
							If SearchPlayer Then
								Target = CreatePivot()
								PositionEntity(Target, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
								PointEntity(Target, Collider)
								RotateEntity(Target, Min(EntityPitch(Target), 20.0), EntityYaw(Target), 0.0)
								
								RotateEntity(n\Collider, CurveAngle(EntityPitch(Target), EntityPitch(n\Collider), 10.0), CurveAngle(EntityYaw(Target), EntityYaw(n\Collider), 10.0), 0.0, True)
								
								PositionEntity(Target, EntityX(n\Collider), EntityY(n\Collider) + 0.2, EntityZ(n\Collider))
								PointEntity(Target, Collider)
								RotateEntity(Target, Min(EntityPitch(Target), 40.0), EntityYaw(n\Collider), 0.0)
								
								If PlayerRoom\RoomTemplate\Name = "gate_b_topside" Then
									n\State3 = Min(n\State3 + fps\Factor[0], 70.0 * 4.0)
								Else
									If n\Reload <= 0.0 Then
										PrevTerminated = terminated
										
										PlaySound2(GunshotSFX, Camera, n\Collider, 15.0)
										
										RotateEntity(Target, EntityPitch(n\Collider), EntityYaw(n\Collider), 0.0, True)
										PositionEntity(Target, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
										MoveEntity(Target, 0.0632, 0.84925, 0.5451)
										
										ShootPlayer(EntityX(Target), EntityY(Target), EntityZ(Target), ((25.0 / Sqr(Dist)) * (1.0 / Sqr(Dist))), True)
										n\Reload = 7.0
										
										m_msg\DeathTxt = GetLocalStringR("Singleplayer","death_ci",Designation)
										
										If (Not PrevTerminated) And terminated Then
											m_msg\DeathTxt = GetLocalStringR("Singleplayer","death_ci",Designation)
											PlayMTFSound(LoadTempSound("SFX\Character\MTF\Targetterminated" + Rand(4) + ".ogg"), n)
										EndIf
									EndIf
								EndIf
								
								If n\Reload > 0.0 And n\Reload <= 7.0 Then
									AnimateNPC(n, 347.0, 351.0, 0.35)
								Else
									AnimateNPC(n, 79.0, 310.0, 0.35)
								EndIf
								
								FreeEntity(Target)
							Else
								If PlayerRoom\RoomTemplate\Name = "gate_b_topside" Then n\State3 = Max(0.0, n\State3 - fps\Factor[0])
								
								If Dist < 4.0 And (I_268\Using > 0 And I_268\Timer > 0.0) Then
									AnimateNPC(n, 79.0, 310.0, 0.35)
								Else
									If n\PathStatus = 1 Then
										If n\Path[n\PathLocation] = Null Then 
											If n\PathLocation > 19 Then 
												n\PathLocation = 0 : n\PathStatus = 0
											Else
												n\PathLocation = n\PathLocation + 1
											EndIf
										Else
											PrevDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
											
											PointEntity(n\obj, n\Path[n\PathLocation]\obj)
											RotateEntity(n\Collider, 0.0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0.0)
											
											n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
											TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * n\CurrSpeed * fps\Factor[0], True)
											AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 26.0)
											
											NewDist = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
											
											;If NewDist < 1.0 Then UseDoorNPC(n)
											
											If (NewDist < 0.04) Lor ((PrevDist < NewDist) And (PrevDist < 1.0)) Then n\PathLocation = n\PathLocation + 1
										EndIf
									Else
										If n\PathTimer = 0.0 Then n\PathStatus = FindPath(n, EntityX(Collider), EntityY(Collider) + 0.5, EntityZ(Collider))
										
										wayPointCloseToPlayer = Null
										
										For wp.WayPoints = Each WayPoints
											If EntityDistanceSquared(wp\obj, Collider) < 4.0 Then
												wayPointCloseToPlayer = wp
												Exit
											EndIf
										Next
										
										If wayPointCloseToPlayer <> Null Then
											n\PathTimer = 1.0
											If EntityVisible(wayPointCloseToPlayer\obj, n\Collider) Then
												If Abs(DeltaYaw(n\Collider, wayPointCloseToPlayer\obj)) > 0.0 Then
													PointEntity(n\obj, wayPointCloseToPlayer\obj)
													RotateEntity(n\Collider, 0.0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0.0)
												EndIf
											EndIf
										Else
											n\PathTimer = 0.0
										EndIf
										
										If n\PathTimer = 1.0 Then
											n\CurrSpeed = CurveValue(n\Speed * 0.7, n\CurrSpeed, 20.0)
											AnimateNPC(n, 488.0, 522.0, n\CurrSpeed * 40.0)
											MoveEntity(n\Collider, 0.0, 0.0, n\CurrSpeed * fps\Factor[0])
										EndIf
									EndIf
								EndIf
							EndIf
						EndIf
						n\Angle = EntityYaw(n\Collider)
						;[End Block]
					Case 11
						AnimateNPC(n,1624,1675,0.3,False)
						If n\Frame >= 1670 Then
							n\Gun\Ammo = n\Gun\MaxAmmo
							n\State = 1
						EndIf
						RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
				End Select
				
				If n\CurrSpeed > 0.01 Then
					If (PrevFrame > 500.0 And n\Frame < 495.0) And (PrevFrame < 505.0 And n\Frame >= 505.0) Then PlaySound2(Step2SFX[Rand(3, 5)], Camera, n\Collider, 8.0, Rnd(0.5, 0.7))
				EndIf
				
				If (I_268\Using > 0 And I_268\Timer > 0.0) And n\State = 1.0 Then n\State = 0.0
				
				If n\State <> 3.0 And n\State <> 5.0 And n\State <> 6.0 And n\State <> 7.0 Then
					If n\MTFLeader <> Null Then
						If EntityDistanceSquared(n\Collider, n\MTFLeader\Collider) < 0.49 Then
							PointEntity(n\Collider, n\MTFLeader\Collider)
							RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider, True), 0.0, True)
							n\Angle = CurveAngle(EntityYaw(n\Collider, True), n\Angle, 20.0)
							
							TranslateEntity(n\Collider, Cos(EntityYaw(n\Collider, True) - 45.0) * 0.01 * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) - 45.0) * 0.01 * fps\Factor[0], True)
						EndIf
					Else
						For n2.NPCs = Each NPCs
							If n2 <> n And (Not n2\IsDead) Then
								If Abs(DeltaYaw(n\Collider, n2\Collider)) < 80.0 Then
									If EntityDistanceSquared(n\Collider, n2\Collider) < 0.49 Then TranslateEntity(n2\Collider, Cos(EntityYaw(n\Collider, True) + 90.0) * 0.01 * fps\Factor[0], 0.0, Sin(EntityYaw(n\Collider, True) + 90.0) * 0.01 * fps\Factor[0], True)
								EndIf
							EndIf
						Next
					EndIf
				EndIf
				
			; ~ Teleport back to the facility if fell through the floor
				If n\State <> 6.0 And n\State <> 7.0 Then
					If EntityY(n\Collider) < -10.0 Then TeleportCloser(n)
				EndIf
			EndIf
		EndIf
		RotateEntity(n\obj, 0.0, n\Angle, 0.0, True)
		PositionEntity(n\obj, EntityX(n\Collider, True), EntityY(n\Collider, True) - 0.15, EntityZ(n\Collider, True), True)
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D