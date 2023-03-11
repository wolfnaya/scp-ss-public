
; ~ General Ambience

Const NTF_MaxAmbienceSFX = 105
Global NTF_AmbienceStrings$[NTF_MaxAmbienceSFX]

;[Block]
NTF_AmbienceStrings[0] = "brief_encounter"
NTF_AmbienceStrings[1] = "friendly_fire"
NTF_AmbienceStrings[2] = "int_bursts"
NTF_AmbienceStrings[3] = "panic"
NTF_AmbienceStrings[4] = "indoor_camera_generic_underground_jolt1"
NTF_AmbienceStrings[5] = "indoor_camera_generic_underground_tremor_high1"
NTF_AmbienceStrings[6] = "indoor_dist_generic_door_kick1"
NTF_AmbienceStrings[7] = "indoor_dist_generic_gunfire_chaotic_pistol1"
NTF_AmbienceStrings[8] = "indoor_dist_generic_gunfire_sustained_pistol1"
NTF_AmbienceStrings[9] = "indoor_dist_generic_gunfire_sustained_smg1"
NTF_AmbienceStrings[10] = "indoor_dist_generic_metal_break1"
NTF_AmbienceStrings[11] = "indoor_dist_generic_metal_scrape1"
NTF_AmbienceStrings[12] = "indoor_dist_generic_metal_scrape2"
NTF_AmbienceStrings[13] = "indoor_dist_generic_metal_stress1"
NTF_AmbienceStrings[14] = "indoor_dist_generic_metal_stress2"
NTF_AmbienceStrings[15] = "indoor_dist_generic_metaldoor_kick1"
NTF_AmbienceStrings[16] = "indoor_far_generic_groan1"
NTF_AmbienceStrings[17] = "indoor_far_generic_groan2"
NTF_AmbienceStrings[18] = "indoor_dist_generic_door_kick1"
NTF_AmbienceStrings[19] = "indoor_dist_generic_explosion_bassy1"
NTF_AmbienceStrings[20] = "indoor_dist_generic_explosion_bassy2"
NTF_AmbienceStrings[21] = "indoor_dist_generic_gunfire_chaotic_ar1"
NTF_AmbienceStrings[22] = "indoor_dist_generic_gunfire_chaotic_pistol2"
NTF_AmbienceStrings[23] = "indoor_dist_generic_gunfire_sustained_ar1"
NTF_AmbienceStrings[24] = "indoor_dist_generic_gunfire_sustained_lmg1"
NTF_AmbienceStrings[25] = "indoor_dist_generic_gunfire_sustained_pistol2"
NTF_AmbienceStrings[26] = "indoor_dist_generic_gunfire_sustained_shotgun1"
NTF_AmbienceStrings[27] = "indoor_dist_generic_gunfire_sustained_smg2"
NTF_AmbienceStrings[28] = "indoor_dist_generic_humanscream_long1"
NTF_AmbienceStrings[29] = "indoor_dist_generic_humanscream_long2"
NTF_AmbienceStrings[30] = "indoor_far_generic_explosion_small1"
NTF_AmbienceStrings[31] = "indoor_far_generic_gunfire_chaotic_pistol1"
NTF_AmbienceStrings[32] = "indoor_far_generic_gunfire_chaotic_smg1"
NTF_AmbienceStrings[33] = "indoor_far_generic_gunfire_chaotic_smg2"
NTF_AmbienceStrings[34] = "indoor_far_generic_gunfire_sustained_ar1"
NTF_AmbienceStrings[35] = "indoor_far_generic_gunfire_sustained_ar2"
NTF_AmbienceStrings[36] = "indoor_far_generic_gunfire_sustained_smg1"
NTF_AmbienceStrings[37] = "indoor_far_generic_gunfire_sustained_smg2"
NTF_AmbienceStrings[38] = "alley_lmg"

For i = 0 To 4
	NTF_AmbienceStrings[39+i] = "int_amb"+(i+1)
Next
For i = 0 To 2
	NTF_AmbienceStrings[44+i] = "scream"+(i+1)
Next
For i = 0 To 2
	NTF_AmbienceStrings[47+i] = "indoor_camera_generic_underground_tremor"+(i+1)
Next
For i = 0 To 6
	NTF_AmbienceStrings[50+i] = "indoor_dist_generic_metal_strike"+(i+1)
Next
For i = 0 To 3
	NTF_AmbienceStrings[57+i] = "indoor_dist_generic_metal_squeak"+(i+1)
Next
For i = 0 To 3
	NTF_AmbienceStrings[61+i] = "indoor_dist_generic_metal_strikeshort"+(i+1)
Next
For i = 0 To 2
	NTF_AmbienceStrings[65+i] = "indoor_far_generic_gunfire_sustained_pistol"+(i+1)
Next
For i = 0 To 4
	NTF_AmbienceStrings[68+i] = "indoor_far_generic_moan"+(i+1)
Next
For i = 0 To 4
	NTF_AmbienceStrings[73+i] = "indoor_far_generic_snarl"+(i+1)
Next
For i = 0 To 3
	NTF_AmbienceStrings[78+i] = "indoor_camera_generic_lights_flicker"+(i+1)
Next
For i = 0 To 2
	NTF_AmbienceStrings[81+i] = "indoor_dist_generic_howl"+(i+1)
Next
For i = 0 To 2
	NTF_AmbienceStrings[84+i] = "indoor_dist_generic_humanscream_short"+(i+1)
Next
For i = 0 To 3
	NTF_AmbienceStrings[87+i] = "indoor_dist_generic_roar"+(i+1)
Next
For i = 0 To 4
	NTF_AmbienceStrings[91+i] = "indoor_far_generic_explosion_med"+(i+1)
Next
For i = 0 To 2
	NTF_AmbienceStrings[96+i] = "indoor_far_generic_gunfire_sustained_pistol"+(i+1)
Next

NTF_AmbienceStrings[99] = "2scream"
NTF_AmbienceStrings[100] = "2scream2"

For i = 0 To 2
	NTF_AmbienceStrings[101+i] = "Boom"+(i+1)
Next

NTF_AmbienceStrings[104] = "LowMoan"
;[End Block]

; ~ Entrance Zone Ambience

Const NTF_MaxEZAmbience% = 3

Global NTF_EZAmbienceStrings$[NTF_MaxEZAmbience]

;[Block]
NTF_EZAmbienceStrings[0] = "Chatter4"
NTF_EZAmbienceStrings[1] = "containmentbreachreaction1"
NTF_EZAmbienceStrings[2] = "OhGod"
;[End Block]

; ~ Heavy Containment Zone Ambience

Const NTF_MaxHCZAmbience% = 5

Global NTF_HCZAmbienceStrings$[NTF_MaxHCZAmbience]

;[Block]
NTF_HCZAmbienceStrings[0] = "008death1"
NTF_HCZAmbienceStrings[1] = "008death2"
NTF_HCZAmbienceStrings[2] = "Cough1"
NTF_HCZAmbienceStrings[3] = "Damage2"
NTF_HCZAmbienceStrings[4] = "Damage5"
;[End Block]

; ~ Light Containment Zone Ambience

Const NTF_MaxLCZAmbience% = 2

Global NTF_LCZAmbienceStrings$[NTF_MaxLCZAmbience]

;[Block]
NTF_LCZAmbienceStrings[0] = "muffle"
NTF_LCZAmbienceStrings[1]= "troublewithdoors"
;[End Block]

; ~ Random Sound Event Ambience

Const NTF_RES_Max = 11

Global NTF_RandomEventSound$[NTF_RES_Max]

;[Block]
NTF_RandomEventSound[0] = "7_classD_finished_off.ogg"
NTF_RandomEventSound[1] = "8_guard_is_compromised_by_loud_radio.ogg"
NTF_RandomEventSound[2] = "classD_pretends_tosurrender_but_doesnt.ogg"
NTF_RandomEventSound[3] = "grenadelauncher_panicked_discharge.ogg"
NTF_RandomEventSound[4] = "guard_squads_meet_each_other.ogg"
NTF_RandomEventSound[5] = "guards_accidental_friendly_fire_scientist.ogg"
NTF_RandomEventSound[6] = "guards_squad_attacked_bySCP_one_survivor.ogg"
NTF_RandomEventSound[7] = "guards_use_a_grenade.ogg"
NTF_RandomEventSound[8] = "mtf_breach_door.ogg"
NTF_RandomEventSound[9] = "mtf_finds_bodies.ogg"
NTF_RandomEventSound[10] = "mtf_prepares_to_apprehend_SCP.ogg"
;[End Block]

; ~ Surface ambience

Const NTF_MaxSurfaceAmbienceSFX = 2

Global NTF_SurfaceAmbienceStrings$[NTF_MaxSurfaceAmbienceSFX]

For i = 0 To 1
	NTF_SurfaceAmbienceStrings[i] = "Howl ("+(i-1)+")"
Next

; ~ Intro Ambience

Const NTF_MaxIntroAmbienceSFX = 41

Global NTF_IntroAmbienceStrings$[NTF_MaxIntroAmbienceSFX]

;[Block]
For i = 0 To 6
	NTF_IntroAmbienceStrings[i] = "outdoor_helo_generic_pass_dist_single"+(i+1)
Next
For i = 0 To 2
	NTF_IntroAmbienceStrings[7+i] = "outdoor_helo_generic_pass_dist_squadron"+(i+1)
Next
For i = 0 To 4
	NTF_IntroAmbienceStrings[10+i] = "outdoor_helo_generic_pass_far_double"+(i+1)
Next
For i = 0 To 8
	NTF_IntroAmbienceStrings[15+i] = "outdoor_helo_generic_pass_far_single"+(i+1)
Next
For i = 0 To 4
	NTF_IntroAmbienceStrings[24+i] = "outdoor_helo_generic_pass_xfar_double"+(i+1)
Next
For i = 0 To 6
	NTF_IntroAmbienceStrings[29+i] = "outdoor_helo_generic_pass_xfar_single"+(i+1)
Next
For i = 0 To 4
	NTF_IntroAmbienceStrings[36+i] = "outdoor_helo_generic_patrol_dist_long"+(i+3)
Next
;[End Block]

; ~ Sewer Ambience

Const NTF_MaxSewerAmbienceSFX = 9
Global NTF_SewerAmbienceStrings$[NTF_MaxSewerAmbienceSFX]

;[Block]
NTF_SewerAmbienceStrings[0] = "BangDoorMetalSCP"
NTF_SewerAmbienceStrings[1] = "Breathe"
NTF_SewerAmbienceStrings[2] = "explosiveprojectilefallin"
NTF_SewerAmbienceStrings[3] = "ImHereSewerScp"
NTF_SewerAmbienceStrings[4] = "NearYou"
NTF_SewerAmbienceStrings[5] = "Sewers1"
NTF_SewerAmbienceStrings[6] = "Sewers2"
NTF_SewerAmbienceStrings[7] = "Tunnels1"
NTF_SewerAmbienceStrings[8] = "Tunnels2"
;[End Block]

Type CubeMap
	Field Name$
	Field Texture%
	Field Cam%
	Field CamOverlay%
	Field RenderTimer%
	Field RenderY#
	Field Position#[3]
	Field FollowsCamera%
End Type

Global MapCubeMap.CubeMap

Function DeleteGameEntities()
	Local i
	
	DestroyMainPlayer()
	
	For i = 0 To 7
		FreeSound_Strict NTF_PainSFX[i] : NTF_PainSFX[i] = 0
	Next
	For i = 0 To 1
		FreeSound_Strict NTF_PainWeakSFX[i] : NTF_PainWeakSFX[i] = 0
	Next
	
	Contain173State% = 0
	Contain173Timer# = 0.0
	
	PlayCustomMusic% = False
	If CustomMusic <> 0 Then FreeSound_Strict CustomMusic : CustomMusic = 0
	
	If NewElevatorMoveSFX1 <> 0 Then FreeSound_Strict NewElevatorMoveSFX1 : NewElevatorMoveSFX1 = 0
	If NewElevatorMoveSFX2 <> 0 Then FreeSound_Strict NewElevatorMoveSFX2 : NewElevatorMoveSFX2 = 0
	
	Delete Each NewElevator
	Delete Each HitBox
	Delete Each CubeMap
	MapCubeMap = Null
	
	Delete NEI
	
	DeleteGuns()
	
	Delete CurrGrid
	
End Function

Const MaxFuseAmount% = 3

Type FuseBox
	Field model$
	Field obj%
	Field fuses%
	Field OverHereSprite%
End Type

Type FuseBox2
	Field model$
	Field obj%
	Field fuses%
	Field OverHereSprite%
End Type

Function CreateFuseBox.FuseBox(model$, Position.Vector3D, Rotation.Vector3D, Scale.Vector3D)
	Local fb.FuseBox = New FuseBox
	Local fb2.FuseBox
	
	fb\model = model
	For fb2 = Each FuseBox
		If fb2 <> fb And fb2\model = fb\model Then
			fb\obj = CopyEntity(fb2\obj)
			Exit
		EndIf
	Next
	If fb\obj = 0 Then
		fb\obj = LoadAnimMesh_Strict("GFX\map\" + model)
	EndIf
	PositionEntity fb\obj,Position\x,Position\y,Position\z
	RotateEntity fb\obj,Rotation\x,Rotation\y,Rotation\z
	ScaleEntity fb\obj,Scale\x,Scale\y,Scale\z
	EntityPickMode fb\obj,2
	EntityType fb\obj,HIT_MAP
	SetAnimTime fb\obj,1
	
	If gc\CurrGamemode = 3 Then
		fb\OverHereSprite = CreateSprite()
		Local tex% = LoadTexture_Strict("GFX\HUD\communication_wheel\look_fuse.png",1+2)
		ScaleSprite fb\OverHereSprite,0.125,0.125
		EntityOrder fb\OverHereSprite,-2
		EntityTexture fb\OverHereSprite,tex
		EntityFX fb\OverHereSprite,1+8
		PositionEntity fb\OverHereSprite,Position\x,Position\y,Position\z
		HideEntity fb\OverHereSprite
	EndIf	
	
	Return fb
End Function

Function UpdateFuseBoxes()
	CatchErrors("UpdateFuseBoxes")
	Local fb.FuseBox
	Local dist#
	
	If gc\CurrGamemode = 3 Then
		For fb = Each FuseBox
			If fb\fuses < MaxFuseAmount And Players[mp_I\PlayerID]\Item <> Null And Players[mp_I\PlayerID]\Item\itemtemplate\tempname = "fuse" And (Not Players[mp_I\PlayerID]\Item\isDeleted) Then
				dist = EntityDistanceSquared(Camera, fb\obj)
				If dist < PowTwo(0.9) Then
					If EntityInView(fb\obj, Camera) Then
						EntityPick(Camera, 0.8)
						
						If PickedEntity() = fb\obj Then
							DrawHandIcon = True
							If KeyHitUse Then
								If fb\fuses = 2 Then
									PlaySound_Strict(LoadTempSound("SFX\Interact\FuseInsert_Final.ogg"))
								Else
									PlaySound_Strict(LoadTempSound("SFX\Interact\FuseInsert.ogg"))
								EndIf
								If mp_I\PlayState = GAME_SERVER Then
									Delete Players[mp_I\PlayerID]\Item
								Else
									Players[mp_I\PlayerID]\Item\isDeleted = True
								EndIf
								fb\fuses = fb\fuses + 1
								Steam_Achieve(STAT_FUSE)
							EndIf
						EndIf
					EndIf
				EndIf
				ShowEntity fb\OverHereSprite
			Else
				HideEntity fb\OverHereSprite
			EndIf
			
			SetAnimTime fb\obj, fb\fuses+1
		Next
	Else
		For fb = Each FuseBox
			If fb\fuses < MaxFuseAmount Then
				dist = EntityDistanceSquared(Camera, fb\obj)
				If dist < PowTwo(0.9) Then
					If EntityInView(fb\obj, Camera) Then
						EntityPick(Camera, 0.8)
						
						If PickedEntity() = fb\obj Then
							DrawHandIcon = True
							If KeyHitUse Then
								If SelectedItem <> Null Then
									If fb\fuses = 2 Then
										PlaySound_Strict(LoadTempSound("SFX\Interact\FuseInsert_Final.ogg"))
									Else
										PlaySound_Strict(LoadTempSound("SFX\Interact\FuseInsert.ogg"))
									EndIf
									If SelectedItem\itemtemplate\tempname = "fuse" Then
										fb\fuses = fb\fuses + 1
										RemoveItem(SelectedItem)
										CreateMsg(GetLocalString("Singleplayer", "fuse_placed"))
									Else
										CreateMsg(GetLocalString("Singleplayer", "fuse_cantplace"))
									EndIf
								Else
									CreateMsg(GetLocalString("Singleplayer", "fuse_find"))
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			SetAnimTime fb\obj, fb\fuses+1
		Next
	EndIf
	
	CatchErrors("Uncaught (UpdateFuseBoxes)")
End Function

Function CreateFuseBox2.FuseBox2(model$, Position.Vector3D, Rotation.Vector3D, Scale.Vector3D)
	Local fbx2.FuseBox2 = New FuseBox2
	Local fb2.FuseBox2
	
	fbx2\model = model
	For fb2 = Each FuseBox2
		If fb2 <> fbx2 And fb2\model = fbx2\model Then
			fbx2\obj = CopyEntity(fb2\obj)
			Exit
		EndIf
	Next
	If fbx2\obj = 0 Then
		fbx2\obj = LoadAnimMesh_Strict("GFX\map\" + model)
	EndIf
	PositionEntity fbx2\obj,Position\x,Position\y,Position\z
	RotateEntity fbx2\obj,Rotation\x,Rotation\y,Rotation\z
	ScaleEntity fbx2\obj,Scale\x,Scale\y,Scale\z
	EntityPickMode fbx2\obj,2
	EntityType fbx2\obj,HIT_MAP
	SetAnimTime fbx2\obj,1
	
	If gc\CurrGamemode = 3 Then
		fbx2\OverHereSprite = CreateSprite()
		Local tex% = LoadTexture_Strict("GFX\HUD\communication_wheel\look_fuse.png",1+2)
		ScaleSprite fbx2\OverHereSprite,0.125,0.125
		EntityOrder fbx2\OverHereSprite,-2
		EntityTexture fbx2\OverHereSprite,tex
		EntityFX fbx2\OverHereSprite,1+8
		PositionEntity fbx2\OverHereSprite,Position\x,Position\y,Position\z
		HideEntity fbx2\OverHereSprite
	EndIf	
	
	Return fbx2
End Function

Function UpdateFuseBoxes2()
	CatchErrors("UpdateFuseBoxes")
	Local fbx2.FuseBox2
	Local dist#
	
	If gc\CurrGamemode = 3 Then
		For fbx2 = Each FuseBox2
			If fbx2\fuses < MaxFuseAmount And Players[mp_I\PlayerID]\Item <> Null And Players[mp_I\PlayerID]\Item\itemtemplate\tempname = "fuse_purple" And (Not Players[mp_I\PlayerID]\Item\isDeleted) Then
				dist = EntityDistanceSquared(Camera, fbx2\obj)
				If dist < PowTwo(0.9) Then
					If EntityInView(fbx2\obj, Camera) Then
						EntityPick(Camera, 0.8)
						
						If PickedEntity() = fbx2\obj Then
							DrawHandIcon = True
							If KeyHitUse Then
								If fbx2\fuses = 2 Then
									PlaySound_Strict(LoadTempSound("SFX\Interact\FuseInsert_Final.ogg"))
								Else
									PlaySound_Strict(LoadTempSound("SFX\Interact\FuseInsert.ogg"))
								EndIf
								If mp_I\PlayState = GAME_SERVER Then
									Delete Players[mp_I\PlayerID]\Item
								Else
									Players[mp_I\PlayerID]\Item\isDeleted = True
								EndIf
								fbx2\fuses = fbx2\fuses + 1
								Steam_Achieve(STAT_FUSE)
							EndIf
						EndIf
					EndIf
				EndIf
				ShowEntity fbx2\OverHereSprite
			Else
				HideEntity fbx2\OverHereSprite
			EndIf
			
			SetAnimTime fbx2\obj, fbx2\fuses+1
		Next
	Else
		For fbx2 = Each FuseBox2
			If fbx2\fuses < MaxFuseAmount Then
				dist = EntityDistanceSquared(Camera, fbx2\obj)
				If dist < PowTwo(0.9) Then
					If EntityInView(fbx2\obj, Camera) Then
						EntityPick(Camera, 0.8)
						
						If PickedEntity() = fbx2\obj Then
							DrawHandIcon = True
							If KeyHitUse Then
								If SelectedItem <> Null Then
									If fbx2\fuses = 2 Then
										PlaySound_Strict(LoadTempSound("SFX\Interact\FuseInsert_Final.ogg"))
									Else
										PlaySound_Strict(LoadTempSound("SFX\Interact\FuseInsert.ogg"))
									EndIf
									If SelectedItem\itemtemplate\tempname = "fuse_purple" Then
										fbx2\fuses = fbx2\fuses + 1
										RemoveItem(SelectedItem)
										CreateMsg(GetLocalString("Singleplayer", "fuse_2_placed"))
									Else
										CreateMsg(GetLocalString("Singleplayer", "fuse_cantplace"))
									EndIf
								Else
									CreateMsg(GetLocalString("Singleplayer", "fuse_2_find"))
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			SetAnimTime fbx2\obj, fbx2\fuses+1
		Next
	EndIf
	
	CatchErrors("Uncaught (UpdateFuseBoxes)")
End Function

Type MapProps
	Field obj%
End Type

Function UpdateMapProps()
	Local a.MapProps
	
	For a.MapProps = Each MapProps
		If a\obj<>0
			If EntityDistanceSquared(a\obj,Camera)<PowTwo(10.0)
				If EntityVisible(a\obj,Camera)
					ShowEntity a\obj
				Else
					HideEntity a\obj
				EndIf
			Else
				HideEntity a\obj
			EndIf
		EndIf
	Next
	
End Function

Type SplashText
	Field Timer#
	Field Speed#
	Field ShowTime#
	Field CurrentLength%
	Field DisplayAmount#
	Field X#, Y#
	Field Txt$
	Field Font%
	Field Centered%
	Field isSound%
	Field R#,G#,B#
End Type

Function CreateSplashText.SplashText(txt$,x#,y#,displayamount#,speed#=1,font%=Font_Digital_Large,centered%=False,issound%=True,r#=255,g#=255,b#=255)
	Local st.SplashText = New SplashText
	
	st\Txt = txt
	st\X = x
	st\Y = y
	st\DisplayAmount = displayamount
	st\Font = font
	st\Centered = centered
	st\isSound = issound
	st\Speed = speed
	st\R# = r
	st\G# = g
	st\B# = b
	
	Return st
End Function

Function UpdateSplashTexts()
	Local st.SplashText
	
	For st = Each SplashText
		If st\CurrentLength < Len(st\Txt) Then
			If st\Timer < 10.0
				st\Timer = st\Timer + fps\Factor[0] * st\Speed
			Else
				st\CurrentLength = st\CurrentLength + 1
				If st\isSound Then
					PlaySound_Strict SplashTextSFX[Rand(0,2)]
				EndIf
				st\Timer = 0.0
			EndIf
		Else
			If st\Timer < st\DisplayAmount+255.0 Then
				st\Timer = Min(st\Timer+fps\Factor[0],st\DisplayAmount+255.0)
			Else
				Delete st
			EndIf
		EndIf
	Next
	
End Function

Function DrawSplashTexts()
	Local st.SplashText
	Local amount% = 0
	
	For st = Each SplashText
		SetFont fo\Font[st\Font]
		
		If st\CurrentLength < Len(st\Txt)
			st\R = st\R
			st\G = st\G
			st\B = st\B
		Else
			st\R = Min((st\DisplayAmount+255.0)-st\Timer,st\R)
			st\G = Min((st\DisplayAmount+255.0)-st\Timer,st\G)
			st\B = Min((st\DisplayAmount+255.0)-st\Timer,st\B)
		EndIf
		
		Color st\R,st\G,st\B
		
		If (Not st\Centered) Then
			Text st\X,st\Y+(32*amount),Left(st\Txt,st\CurrentLength)
		Else
			Text st\X-StringWidth(st\Txt)/2,st\Y+(32*amount)-StringHeight(st\Txt)/2,Left(st\Txt,st\CurrentLength)
		EndIf
		
		amount = amount + 1
	Next
	
End Function

Type CoordPoints
	Field name$
	Field obj%
End Type

Function CreateCoordPoint.CoordPoints(name$,x#,y#,z#,parent%=0,pos%=False)
	Local cdp.CoordPoints = New CoordPoints
	
	cdp\name$ = name$
	cdp\obj% = CreatePivot(parent%)
	PositionEntity cdp\obj%,x#,y#,z#,pos%
	
	Return cdp
End Function

Function DeleteCoordPoint(cdp.CoordPoints)
	
	If cdp\obj% <> 0 Then FreeEntity cdp\obj% : cdp\obj% = 0
	Delete cdp
	
End Function

Function Between(a#,value#,b#)
	
	If a# > value#
		Return a#
	ElseIf b# < value#
		Return b#
	Else
		Return value#
	EndIf
	
End Function

Global NTF_SprintPitch# = 0.0
Global NTF_SprintPitchSide% = 0

Function UpdateSprint()
	
	If IsPlayerSprinting%
		If NTF_SprintPitchSide% = 0
			If NTF_SprintPitch# < 0.5
				NTF_SprintPitch# = NTF_SprintPitch + (0.1*fps\Factor[0])
			Else
				NTF_SprintPitchSide% = 1
			EndIf
		Else
			If NTF_SprintPitch# > -0.5
				NTF_SprintPitch# = NTF_SprintPitch - (0.1*fps\Factor[0])
			Else
				NTF_SprintPitchSide% = 0
			EndIf
		EndIf
	Else
		If NTF_SprintPitchSide% = 0
			NTF_SprintPitch# = Min(0,NTF_SprintPitch#+(0.1*fps\Factor[0]))
		Else
			NTF_SprintPitch# = Max(0,NTF_SprintPitch#-(0.1*fps\Factor[0]))
		EndIf
	EndIf
	
End Function

Global Credits_LineAmount% = 0

Type Credits_Lines
	Field txt$
End Type

Function UpdateAlarmRotor(OBJ,rotation#)
	
	TurnEntity OBJ,0,-rotation*fps\Factor[0],0
	
End Function

Function UpdateDoubleAlarmRotor(OBJ,OBJ2,rotation#)
	
	TurnEntity OBJ,0,-rotation*fps\Factor[0],0
	TurnEntity OBJ2,0,-rotation*fps\Factor[0],0
	
End Function

Function RelightRoom(r.Rooms,tex%,oldtex%)
	
	Local mesh=GetChild(r\obj,2)
	Local surf%,brush%,tex2%,texname$,temp%,temp2%
	Local comparison$ = StripPath(TextureName(oldtex))
	
	temp=(BumpEnabled+1)
	For i=1 To CountSurfaces(mesh)
		temp2=temp
		surf=GetSurface(mesh,i)
		brush=GetSurfaceBrush(surf)
		If brush<>0 Then
			tex2=GetBrushTexture(brush,temp2)
			If tex2=0 And temp2>1 Then tex2=GetBrushTexture(brush,0) : temp2=1
			If tex2<>0 Then
				texname=TextureName(tex2)
				If StripPath(texname)=comparison Then
					BrushTexture brush,tex,0,temp
					PaintSurface surf,brush
				EndIf
				DeleteSingleTextureEntryFromCache tex2
			EndIf
			FreeBrush brush
		EndIf
	Next
	
End Function

Function CreateCubeMap.CubeMap(Name$,CubeMapMode%=1,FollowsCamera%=True,PosX#=0.0,PosY#=0.0,PosZ#=0.0,RenderY#=0.0,TexSize%=256)
	Local cm.CubeMap = New CubeMap
	
	cm\Name = Name$
	cm\RenderY = RenderY#
	
	cm\Texture=CreateTextureUsingCacheSystem(TexSize,TexSize,128+256)
	TextureBlend cm\Texture,3
	cm\Cam=CreateCamera()
	CameraFogMode cm\Cam,1
	CameraFogRange cm\Cam,5,10
	CameraRange cm\Cam,0.01,20
	CameraClsMode cm\Cam,False,True
	CameraProjMode cm\Cam,0
	CameraViewport cm\Cam,0,0,TexSize,TexSize
	cm\CamOverlay=CreateSprite(cm\Cam)
	EntityFX cm\CamOverlay,1
	EntityColor cm\CamOverlay,0,0,0
	EntityOrder cm\CamOverlay,-1000
	ScaleSprite cm\CamOverlay,5,5
	MoveEntity cm\CamOverlay,0,0,0.1
	EntityAlpha cm\CamOverlay,0.0
	HideEntity cm\CamOverlay
	
	SetCubeMode cm\Texture,CubeMapMode
	
	cm\FollowsCamera = FollowsCamera
	If cm\FollowsCamera = False Then
		cm\Position[Vector_X] = PosX#
		cm\Position[Vector_Y] = PosY#
		cm\Position[Vector_Z] = PosZ#
	EndIf
	
	Return cm
End Function

Function RenderCubeMap(entity%,name$)
	Local cm.CubeMap
	Local tex_sz%
	
	If opt\RenderCubeMapMode=0 Then Return
	
	For cm = Each CubeMap
		If cm\Name = name$ Then
			tex_sz=TextureWidth(cm\Texture)
			CameraProjMode Camera,0
			CameraProjMode cm\Cam,1
			If entity<>0 Then
				HideEntity entity
			EndIf
			HideEntity g_I\GunPivot
			ShowEntity cm\CamOverlay
			EntityAlpha cm\CamOverlay,0.7
			CameraFogRange cm\Cam,0.1,3
			CameraFogColor cm\Cam,5,20,3
			If opt\RenderCubeMapMode = 2 Then
				; ~ do left view
				SetCubeFace cm\Texture,0
				RotateEntity cm\Cam,0,90,0
				RenderWorld
				CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
				; ~ do forward view
				SetCubeFace cm\Texture,1
				RotateEntity cm\Cam,0,0,0
				RenderWorld
				CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
				; ~ do right view	
				SetCubeFace cm\Texture,2
				RotateEntity cm\Cam,0,-90,0
				RenderWorld
				CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
				; ~ do backward view
				SetCubeFace cm\Texture,3
				RotateEntity cm\Cam,0,180,0
				RenderWorld
				CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
				; ~ do up view
				SetCubeFace cm\Texture,4
				RotateEntity cm\Cam,-90,0,0
				RenderWorld
				CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
				; ~ do down view
				SetCubeFace cm\Texture,5
				RotateEntity cm\Cam,90,0,0
				RenderWorld
				CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
			ElseIf opt\RenderCubeMapMode = 1 Then
				If cm\RenderTimer <= 0 Then
					; ~ do left view	
					SetCubeFace cm\Texture,0
					RotateEntity cm\Cam,0,90,0
					RenderWorld
					CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
					; ~ do forward view
					SetCubeFace cm\Texture,1
					RotateEntity cm\Cam,0,0,0
					RenderWorld
					CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
					cm\RenderTimer = 1
				ElseIf cm\RenderTimer = 1 Then
					; ~ do right view	
					SetCubeFace cm\Texture,2
					RotateEntity cm\Cam,0,-90,0
					RenderWorld
					CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
					; ~ do backward view
					SetCubeFace cm\Texture,3
					RotateEntity cm\Cam,0,180,0
					RenderWorld
					CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
					cm\RenderTimer = 2
				ElseIf cm\RenderTimer = 2 Then
					; ~ do up view
					SetCubeFace cm\Texture,4
					RotateEntity cm\Cam,-90,0,0
					RenderWorld
					CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
					; ~ do down view
					SetCubeFace cm\Texture,5
					RotateEntity cm\Cam,90,0,0
					RenderWorld
					CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
					cm\RenderTimer = 0
				EndIf
			EndIf	
			If entity<>0 Then
				ShowEntity entity
			EndIf
			ShowEntity g_I\GunPivot
			EntityAlpha cm\CamOverlay,0.0
			CameraFogRange cm\Cam,5,10
			CameraFogColor cm\Cam,0,0,0
			HideEntity cm\CamOverlay
			CameraProjMode Camera,1
			CameraProjMode cm\Cam,0
			Exit
		EndIf
	Next
	
End Function

Function UpdateCubeMap(entity%,name$)
	Local cm.CubeMap
	Local camoffsety#
	
	If opt\RenderCubeMapMode=0 Then Return
	
	For cm = Each CubeMap
		If cm\Name = name$
			If cm\FollowsCamera Then
				If entity<>0 Then
					camoffsety#=(EntityY(Camera,True)-(EntityY(entity,True)+cm\RenderY))
					PositionEntity cm\Cam,EntityX(Camera,True),(EntityY#(entity,True)+cm\RenderY)-camoffsety,EntityZ(Camera,True)
				Else
					camoffsety#=(EntityY(Camera,True)+cm\RenderY)
					PositionEntity cm\Cam,EntityX(Camera,True),cm\RenderY-camoffsety,EntityZ(Camera,True)
				EndIf
			Else
				PositionEntity cm\Cam,cm\Position[Vector_X],cm\Position[Vector_Y],cm\Position[Vector_Z]
			EndIf
			If entity<>0 Then
				ShowEntity entity
			EndIf
			Exit
		EndIf
	Next
	
End Function

Global PlayerInNewElevator% = False
Global PlayerNewElevator% = 0

Type NewElevatorInstance
	Field button_number_tex[3]
End Type

Global NEI.NewElevatorInstance

Type NewElevator
	Field obj%
	Field state#
	Field currfloor%
	Field tofloor%
	Field door.Doors
	Field floory#[3]
	Field ID#
	Field speed#=6
	Field CurrSpeed#
	Field sound%
	Field soundchn%
	Field currsound%
	Field room.Rooms
	Field button_arrow[2]
	Field button_numbers
	Field floorlocked[3]
	Field isStorage%
End Type

Function CreateNewElevator.NewElevator(obj%,currfloor%,door.Doors,id#,r.Rooms,floor1y#,floor2y#,floor3y#=0.0,speed#=7.0,isStorage%=False)
	Local tex%
	Local ne.NewElevator = New NewElevator
	Local i
	
	ne\room = r
	ne\obj = obj
	ne\currfloor = currfloor
	ne\tofloor = currfloor
	ne\state = 0.0
	ne\door = door
	ne\floory[0] = floor1y
	ne\floory[1] = floor2y
	ne\floory[2] = floor3y
	ne\speed = speed
	ne\ID = id
	ne\isStorage = isStorage
	tex = LoadTextureCheckingIfInCache("GFX\HUD\elevator_arrow_HUD.png")
	ne\button_arrow[0] = CreateSprite(ne\door\buttons[0])
	ne\button_arrow[1] = CopyEntity(ne\button_arrow[0],ne\door\buttons[0])
	For i = 0 To 1
		ScaleSprite ne\button_arrow[i],10,10
		SpriteViewMode ne\button_arrow[i],2
		EntityTexture ne\button_arrow[i],tex
		EntityFX ne\button_arrow[i],1
	Next
	PositionEntity ne\button_arrow[0],-10,285,0.8
	PositionEntity ne\button_arrow[1],14,285,0
	TurnEntity ne\button_arrow[1],0,0,180
	DeleteSingleTextureEntryFromCache tex
	ne\button_numbers = CreateSprite(ne\door\buttons[0])
	ScaleSprite ne\button_numbers,15,15
	SpriteViewMode ne\button_numbers,2
	PositionEntity ne\button_numbers,2,250,0
	EntityFX ne\button_numbers,1
	
	Return ne
End Function

Function StartNewElevator(door.Doors,newfloor%)
	Local ne.NewElevator, ne_found.NewElevator
	Local playerinside% = False
	
	For ne = Each NewElevator
		If ne\door = door
			ne_found = ne
			Exit
		EndIf
	Next
	
	If ne\isStorage Then
		If ne\door\locked Then
			CreateMsg(GetLocalString("Doors", "door_locked"))
		ElseIf ne\floorlocked[newfloor - 1] Then
			CreateMsg(GetLocalString("Doors", "door_nothing"))
		Else
			If newfloor <> ne\currfloor
				ne\tofloor = newfloor
				If Abs(EntityX(Collider)-EntityX(ne\obj,True))<=280.0*RoomScale+(0.015*fps\Factor[0])
					If Abs(EntityZ(Collider)-EntityZ(ne\obj,True))<=280.0*RoomScale+(0.015*fps\Factor[0])
						DebugLog "In Elevator"
						PlayerInNewElevator = True
						PlayerNewElevator = ne\ID
						playerinside = True
					EndIf
				EndIf
				If (Not playerinside)
					CreateMsg(GetLocalString("Doors", "elevator_called"))
				EndIf
			Else
				CreateMsg(GetLocalString("Doors", "elevator_on_floor"))
			EndIf
		EndIf
	Else
		If ne\door\locked Then
			CreateMsg(GetLocalString("Doors", "door_locked"))
		ElseIf ne\floorlocked[newfloor - 1] Then
			CreateMsg(GetLocalString("Doors", "door_nothing"))
		Else
			If newfloor <> ne\currfloor
				ne\tofloor = newfloor
				If Abs(EntityX(Collider)-EntityX(ne\obj,True))<=280.0*RoomScale+(0.015*fps\Factor[0])
					If Abs(EntityZ(Collider)-EntityZ(ne\obj,True))<=280.0*RoomScale+(0.015*fps\Factor[0])
						DebugLog "In Elevator"
						PlayerInNewElevator = True
						PlayerNewElevator = ne\ID
						playerinside = True
					EndIf
				EndIf
				If (Not playerinside)
					CreateMsg(GetLocalString("Doors", "elevator_called"))
				Else
					UseDoor(ne\door)
				EndIf
			Else
				CreateMsg(GetLocalString("Doors", "elevator_on_floor"))
			EndIf
		EndIf
	EndIf
	
End Function

Function UpdateNewElevators()
	Local ne.NewElevator
	Local n.NPCs,it.Items
	Local i%
	
	For ne = Each NewElevator
		
		If PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Lor PlayerRoom\RoomTemplate\Name = "core_lcz" Lor PlayerRoom\RoomTemplate\Name = "core_hcz" Lor PlayerRoom\RoomTemplate\Name = "core_ez" Then
			If PlayerInNewElevator Then
				If opt\ElevatorMusicEnabled Then
					ShouldPlay = Rand(MUS_ELEVATOR,MUS_ELEVATOR_3)
				Else
					ShouldPlay = MUS_NULL
				EndIf
			EndIf
		EndIf
		
		If ne\tofloor <> ne\currfloor Then
			If ne\state < 200.0 Then
				ne\state = ne\state + fps\Factor[0]
			Else
				If ne\currsound=0 Then
					If ne\soundchn <> 0 Then StopStream_Strict(ne\soundchn)
					
					If (Not PlayerInNewElevator) Then
						
					Else
						If PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Then
							ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Checkpoint\StartAndLoop.ogg",opt\SFXVolume*0.5,0)
						Else
							ne\soundchn = StreamSound_Strict("SFX\General\Elevator\StartAndLoop"+Rand(1,3)+".ogg",opt\SFXVolume*0.5,0)
						EndIf
					EndIf
					ne\currsound = 1
				Else
					If ne\currsound = 1 Lor (ne\currsound = 2 And (Not IsStreamPlaying_Strict(ne\soundchn))) Then
						If (Not IsStreamPlaying_Strict(ne\soundchn))
							StopStream_Strict(ne\soundchn)
							ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Loop"+Rand(1,3)+".ogg",opt\SFXVolume*0.5,Mode)
							ne\currsound = 2
						EndIf
					EndIf
				EndIf
				If ne\tofloor < ne\currfloor Then
					ShowEntity ne\button_arrow[1]
					HideEntity ne\button_arrow[0]
					If PlayerRoom\RoomTemplate\Name = "core_lcz" Lor PlayerRoom\RoomTemplate\Name = "core_hcz" Lor PlayerRoom\RoomTemplate\Name = "core_ez" Then
						If ne\currfloor = 3 And ne\tofloor = 1 Then
							If EntityY(ne\obj) < ne\floory[1] Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],1
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],2
							EndIf
						Else
							If ne\currfloor = 3 Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],2
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],1
							EndIf
						EndIf
					ElseIf PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Then
						If ne\currfloor = 3 And ne\tofloor = 1 Then
							If EntityY(ne\obj) < ne\floory[1] Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],1
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],2
							EndIf
						Else
							If ne\currfloor = 3 Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],2
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],1
							EndIf
						EndIf
					Else
						If ne\currfloor = 3 And ne\tofloor = 1 Then
							If EntityY(ne\obj) < ne\floory[1] Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],1
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],2
							EndIf
						Else
							If ne\currfloor = 3 Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],2
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],1
							EndIf
						EndIf
					EndIf
					If EntityY(ne\obj) > ne\floory[ne\tofloor-1] Then
						If EntityY(ne\obj) > ne\floory[ne\tofloor-1] + (0.6/RoomScale) Then
							ne\CurrSpeed = CurveValue(ne\speed,ne\CurrSpeed,75.0)
						Else
							If ne\isStorage Then
								ne\CurrSpeed = CurveValue(0.05,1.5,25.0)
							Else
								ne\CurrSpeed = CurveValue(0.05,ne\CurrSpeed,25.0)
							EndIf
							If ne\currsound = 1 Lor ne\currsound = 2 Then
								StopStream_Strict(ne\soundchn)
								If PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Then
									ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Checkpoint\Stop.ogg",opt\SFXVolume*0.5,0)
								Else
									ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Stop"+Rand(1,3)+".ogg",opt\SFXVolume*0.5,0)
								EndIf
								ne\currsound = 3
							EndIf
						EndIf
						MoveEntity ne\obj,0,-ne\CurrSpeed*fps\Factor[0],0
						If ne\room = PlayerRoom And PlayerInNewElevator And PlayerNewElevator = ne\ID Then
							TeleportEntity Collider,EntityX(Collider),EntityY(ne\obj,True)+0.3,EntityZ(Collider)
						EndIf
					Else
						PositionEntity ne\obj,EntityX(ne\obj),ne\floory[ne\tofloor-1],EntityZ(ne\obj)
						ne\currfloor = ne\tofloor
						ne\state = fps\Factor[0]
					EndIf
				Else
					ShowEntity ne\button_arrow[0]
					HideEntity ne\button_arrow[1]
					If PlayerRoom\RoomTemplate\Name = "core_lcz" Lor PlayerRoom\RoomTemplate\Name = "core_hcz" Lor PlayerRoom\RoomTemplate\Name = "core_ez" Then
						If ne\currfloor = 1 And ne\tofloor = 3 Then
							If EntityY(ne\obj)>ne\floory[1]
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],1
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],0
							EndIf
						Else
							If ne\currfloor = 1 Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],0
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],1
							EndIf
						EndIf
					ElseIf PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Then
						If ne\currfloor = 3 And ne\tofloor = 1 Then
							If EntityY(ne\obj) < ne\floory[1] Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],1
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],0
							EndIf
						Else
							If ne\currfloor = 3 Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],0
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],1
							EndIf
						EndIf
					Else
						If ne\currfloor = 1 And ne\tofloor = 3 Then
							If EntityY(ne\obj)>ne\floory[1]
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],1
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],0
							EndIf
						Else
							If ne\currfloor = 1 Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],0
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],1
							EndIf
						EndIf
					EndIf
					If EntityY(ne\obj) < ne\floory[ne\tofloor-1] Then
						If EntityY(ne\obj) < ne\floory[ne\tofloor-1] - (0.6/RoomScale) Then
							ne\CurrSpeed = CurveValue(ne\speed,ne\CurrSpeed,75.0)
						Else
							If ne\isStorage Then
								ne\CurrSpeed = CurveValue(0.05,1.5,25.0)
							Else
								ne\CurrSpeed = CurveValue(0.05,ne\CurrSpeed,25.0)
							EndIf
							If ne\currsound = 1 Lor ne\currsound = 2 Then
								StopStream_Strict(ne\soundchn)
								If PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Then
									ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Checkpoint\Stop.ogg",opt\SFXVolume*0.5,0)
								Else
									ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Stop"+Rand(1,3)+".ogg",opt\SFXVolume*0.5,0)
								EndIf
								ne\currsound = 3
							EndIf
						EndIf
						MoveEntity ne\obj,0,ne\CurrSpeed*fps\Factor[0],0
						If ne\room = PlayerRoom And PlayerInNewElevator And PlayerNewElevator = ne\ID Then
							TeleportEntity Collider,EntityX(Collider),EntityY(ne\obj,True)+0.3,EntityZ(Collider)
						EndIf
					Else
						PositionEntity ne\obj,EntityX(ne\obj),ne\floory[ne\tofloor-1],EntityZ(ne\obj)
						ne\currfloor = ne\tofloor
						ne\state = fps\Factor[0]
					EndIf
				EndIf
				If ne\room = PlayerRoom And PlayerInNewElevator And PlayerNewElevator = ne\ID Then
					If (Not ne\isStorage) Then
						PositionEntity ne\door\obj,EntityX(ne\door\obj),EntityY(ne\obj,True),EntityZ(ne\door\obj)
						PositionEntity ne\door\obj2,EntityX(ne\door\obj2),EntityY(ne\obj,True),EntityZ(ne\door\obj2)
						PositionEntity ne\door\frameobj,EntityX(ne\door\frameobj),EntityY(ne\obj,True),EntityZ(ne\door\frameobj)
						PositionEntity ne\door\buttons[0],EntityX(ne\door\buttons[0]),EntityY(ne\obj,True)+0.6,EntityZ(ne\door\buttons[0])
						PositionEntity ne\door\buttons[1],EntityX(ne\door\buttons[1]),EntityY(ne\obj,True)+0.7,EntityZ(ne\door\buttons[1])
					Else
						PositionEntity ne\door\frameobj,EntityX(ne\door\frameobj),EntityY(ne\obj,True),EntityZ(ne\door\frameobj)
						PositionEntity ne\door\buttons[0],EntityX(ne\door\buttons[0]),EntityY(ne\obj,True)+0.6,EntityZ(ne\door\buttons[0])
						PositionEntity ne\door\buttons[1],EntityX(ne\door\buttons[1]),EntityY(ne\obj,True)+0.7,EntityZ(ne\door\buttons[1])
					EndIf
					PositionEntity Collider,EntityX(Collider),EntityY(ne\obj,True)+0.3,EntityZ(Collider)
					DropSpeed = 0
					CameraShake = Sin(Abs(ne\CurrSpeed*15)/3.0)*0.5
				EndIf
				For n.NPCs = Each NPCs
					If Abs(EntityX(n\Collider)-EntityX(ne\obj,True)) < 280.0*RoomScale+(0.015*fps\Factor[0]) Then
						If Abs(EntityZ(n\Collider)-EntityZ(ne\obj,True)) < 280.0*RoomScale+(0.015*fps\Factor[0]) Then
							PositionEntity n\Collider,EntityX(n\Collider),EntityY(ne\obj,True)+n\CollRadius,EntityZ(n\Collider)
							n\DropSpeed = 0
						EndIf
					EndIf
				Next
				For it.Items = Each Items
					If Abs(EntityX(it\Collider)-EntityX(ne\obj,True)) < 280.0*RoomScale+(0.015*fps\Factor[0]) Then
						If Abs(EntityZ(it\Collider)-EntityZ(ne\obj,True)) < 280.0*RoomScale+(0.015*fps\Factor[0]) Then
							PositionEntity it\Collider,EntityX(it\Collider),EntityY(ne\obj,True)+0.01,EntityZ(it\Collider)
							it\DropSpeed = 0
						EndIf
					EndIf
				Next
			EndIf
		Else
			ne\CurrSpeed = 0.0
			ne\currsound = 0
			For i = 0 To 1
				HideEntity ne\button_arrow[i]
			Next
			If PlayerRoom\RoomTemplate\Name = "core_lcz" Lor PlayerRoom\RoomTemplate\Name = "core_hcz" Lor PlayerRoom\RoomTemplate\Name = "core_ez" Then
				Select ne\currfloor
					Case 1
						EntityTexture ne\button_numbers,NEI\button_number_tex[1],0
					Case 2
						EntityTexture ne\button_numbers,NEI\button_number_tex[1],1
					Case 3
						EntityTexture ne\button_numbers,NEI\button_number_tex[1],2
				End Select
			ElseIf PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Then
				Select ne\currfloor
					Case 1
						EntityTexture ne\button_numbers,NEI\button_number_tex[2],0
					Case 2
						EntityTexture ne\button_numbers,NEI\button_number_tex[2],1
					Case 3
						EntityTexture ne\button_numbers,NEI\button_number_tex[2],2
				End Select
			Else
				Select ne\currfloor
					Case 1
						EntityTexture ne\button_numbers,NEI\button_number_tex[0],0
					Case 2
						EntityTexture ne\button_numbers,NEI\button_number_tex[0],1
					Case 3
						EntityTexture ne\button_numbers,NEI\button_number_tex[0],2
				End Select
			EndIf
			If ne\state > 0.0 And ne\state < 100.0 Then
				ne\state = ne\state + fps\Factor[0]
			ElseIf ne\state >= 100.0 Then
				If (Not ne\isStorage) Then
					UseDoor(ne\door)
				EndIf
				PlayerInNewElevator = False
				ne\state = 0.0
			EndIf
		EndIf
		If PlayerInNewElevator Then
			SetStreamVolume_Strict(ne\soundchn,opt\SFXVolume*0.5)
		Else
			UpdateStreamSoundOrigin(ne\soundchn,Camera,ne\obj)
		EndIf
		
		;EndIf
	Next
	
End Function

Function DeleteNewElevators()
	Local ne.NewElevator
	
	For ne = Each NewElevator
		FreeEntity ne\obj : ne\obj=0
		StopStream_Strict(ne\soundchn)
		ne\soundchn = 0
		ne\sound = 0
		Delete ne
	Next
	
End Function

Function UpdateSmallHeadMode()
	Local n.NPCs, bonename$, bone%
	
	For n = Each NPCs
		bonename$ = GetNPCManipulationValue(n\NPCNameInSection,"Head","bonename",0)
		If bonename$<>""
			bone% = FindChild(n\obj,bonename$)
			ScaleEntity bone%,0.5,0.5,0.5
		EndIf
		If n\NPCtype = NPCtype049 Then
			bone% = FindChild(n\obj, "Bone_019")
			ScaleEntity bone%,0.5,0.5,0.5
		ElseIf n\NPCtype = NPCtype106 Then
			bone% = FindChild(n\obj, "Bone_022")
			ScaleEntity bone%,0.5,0.5,0.5
		ElseIf n\NPCtype = NPCtypeClerk Then
			bone% = FindChild(n\obj, "Bip01_Head")
			ScaleEntity bone%,0.5,0.5,0.5
		EndIf
	Next
	
End Function

Function ApplyBumpMap(texture%)
	
	;TextureBlend texture%,6
	;TextureBumpEnvMat texture%,0,0,-0.012
	;TextureBumpEnvMat texture%,0,1,-0.012
	;TextureBumpEnvMat texture%,1,0,0.012
	;TextureBumpEnvMat texture%,1,1,0.012
	;TextureBumpEnvOffset texture%,0.5
	;TextureBumpEnvScale texture%,1.0
	
End Function

Function PointEntity2(source_ent%,dest_ent%,roll#=0.0,usepitch%=True,useyaw%=True)
	Local pitch#,yaw#
	
	pitch# = EntityPitch(source_ent%)
	If usepitch%
		pitch# = pitch# + DeltaPitch(source_ent%,dest_ent%)
	EndIf
	yaw# = EntityYaw(source_ent%)
	If useyaw%
		yaw# = yaw# + DeltaYaw(source_ent%,dest_ent%)
	EndIf
	RotateEntity source_ent%,pitch#,yaw#,roll#
	
End Function

Type HitBox
	Field HitBox1[25]
	Field HitBox2[25]
	Field HitBox3[25]
	Field BoneName$[25]
	Field HitBoxPosX#[25]
	Field HitBoxPosY#[25]
	Field HitBoxPosZ#[25]
	Field NPCtype%
	Field ID%
End Type

Function ApplyHitBoxes.HitBox(npctype,npcname$)
	Local hb.HitBox = New HitBox
	
	hb\NPCtype = npctype
	
	Local i%,htype%,bonename$
	Local scaleX#,scaleY#,scaleZ#,posX#,posY#,posZ#
	Local file$ = "Data\NPCBones.ini"
	
	For i = 0 To GetINIInt(file$,npcname$,"hitbox_amount")-1
		htype% = GetINIInt(file$,npcname$,"hitbox"+(i+1)+"_type")
		bonename$ = GetINIString(file$,npcname$,"hitbox"+(i+1)+"_parent")
		hb\BoneName[i] = bonename
		If htype = 0
			hb\HitBox1[i] = CreateCube()
			scaleX# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleX",1.0)
			scaleY# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleY",1.0)
			scaleZ# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleZ",1.0)
			posX# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posX",0.0)
			posY# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posY",0.0)
			posZ# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posZ",0.0)
			ScaleEntity hb\HitBox1[i],scaleX,scaleY,scaleZ
			PositionEntity hb\HitBox1[i],posX,posY,posZ
			EntityPickMode hb\HitBox1[i],2
			EntityAlpha hb\HitBox1[i],0.0
			HideEntity hb\HitBox1[i]
		ElseIf htype = 1
			hb\HitBox2[i] = CreateCube()
			scaleX# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleX",1.0)
			scaleY# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleY",1.0)
			scaleZ# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleZ",1.0)
			posX# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posX",0.0)
			posY# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posY",0.0)
			posZ# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posZ",0.0)
			ScaleEntity hb\HitBox2[i],scaleX,scaleY,scaleZ
			PositionEntity hb\HitBox2[i],posX,posY,posZ
			EntityPickMode hb\HitBox2[i],2
			EntityAlpha hb\HitBox2[i],0.0
			HideEntity hb\HitBox2[i]
		Else
			hb\HitBox3[i] = CreateCube()
			scaleX# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleX",1.0)
			scaleY# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleY",1.0)
			scaleZ# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleZ",1.0)
			posX# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posX",0.0)
			posY# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posY",0.0)
			posZ# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posZ",0.0)
			ScaleEntity hb\HitBox3[i],scaleX,scaleY,scaleZ
			PositionEntity hb\HitBox3[i],posX,posY,posZ
			EntityPickMode hb\HitBox3[i],2
			EntityAlpha hb\HitBox3[i],0.0
			HideEntity hb\HitBox3[i]
		EndIf
		hb\HitBoxPosX[i]=posX
		hb\HitBoxPosY[i]=posY
		hb\HitBoxPosZ[i]=posZ
	Next
	
	Return hb
End Function

Function CopyHitBoxes(n.NPCs)
	Local hb.HitBox,bone%,i%
	
	For hb = Each HitBox
		If hb\NPCtype = n\NPCtype Then
			For i = 0 To 24
				If hb\BoneName[i]<>"" Then
					n\BoneName[i]=hb\BoneName[i]
					bone = FindChild(n\obj,n\BoneName[i])
				EndIf
				If hb\HitBox1[i]<>0 Then
					n\HitBox1[i] = CopyEntity(hb\HitBox1[i],bone)
					PositionEntity n\HitBox1[i],hb\HitBoxPosX[i],hb\HitBoxPosY[i],hb\HitBoxPosZ[i]
				EndIf
				If hb\HitBox2[i]<>0 Then
					n\HitBox2[i] = CopyEntity(hb\HitBox2[i],bone)
					PositionEntity n\HitBox2[i],hb\HitBoxPosX[i],hb\HitBoxPosY[i],hb\HitBoxPosZ[i]
				EndIf
				If hb\HitBox3[i]<>0 Then
					n\HitBox3[i] = CopyEntity(hb\HitBox3[i],bone)
					PositionEntity n\HitBox3[i],hb\HitBoxPosX[i],hb\HitBoxPosY[i],hb\HitBoxPosZ[i]
				EndIf
			Next
			Exit
		EndIf
	Next
	
End Function

Function HideNPCHitBoxes(n.NPCs)
	Local i%
	
	For i = 0 To 24
		If n\HitBox1[i]<>0 Then HideEntity n\HitBox1[i]
		If n\HitBox2[i]<>0 Then HideEntity n\HitBox2[i]
		If n\HitBox3[i]<>0 Then HideEntity n\HitBox3[i]
	Next
	
End Function

Function ShowNPCHitBoxes(n.NPCs)
	Local i%
	
	For i = 0 To 24
		If n\HitBox1[i]<>0 Then ShowEntity n\HitBox1[i]
		If n\HitBox2[i]<>0 Then ShowEntity n\HitBox2[i]
		If n\HitBox3[i]<>0 Then ShowEntity n\HitBox3[i]
	Next
	
End Function

Function FreeNPCHitBoxes(n.NPCs)
	Local i%
	
	For i = 0 To 24
		If n\HitBox1[i]<>0 Then FreeEntity n\HitBox1[i] : n\HitBox1[i]=0
		If n\HitBox2[i]<>0 Then FreeEntity n\HitBox2[i] : n\HitBox2[i]=0
		If n\HitBox3[i]<>0 Then FreeEntity n\HitBox3[i] : n\HitBox3[i]=0
	Next
	
End Function

Function GetMeshExtents2(mesh)
	Local xmax#=-1000000
	Local xmin#= 1000000
	Local ymax#=-1000000
	Local ymin#= 1000000
	Local zmax#=-1000000
	Local zmin#= 1000000
	Local su,s,i,x#,y#,z#
	For su=1 To CountSurfaces(mesh)
		s=GetSurface(mesh,su)
		For i=0 To CountVertices(s)-1
			x#=VertexX(s,i)
			y#=VertexY(s,i)
			z#=VertexZ(s,i)
			TFormPoint x,y,z,mesh,0
			x=TFormedX()
			y=TFormedY()
			z=TFormedZ()
			If x>xmax Then xmax=x
			If x<xmin Then xmin=x
			If y>ymax Then ymax=y
			If y<ymin Then ymin=y
			If z>zmax Then zmax=z
			If z<zmin Then zmin=z
		Next
	Next
	
	Mesh_MinX = xmin
	Mesh_MinY = ymin
	Mesh_MinZ = zmin
	Mesh_MaxX = xmax
	Mesh_MaxY = ymax
	Mesh_MaxZ = zmax
	Mesh_MagX = xmax-xmin
	Mesh_MagY = ymax-ymin
	Mesh_MagZ = zmax-zmin
	
End Function

Function TurnIntoSeconds(number#)
	
	Return Ceil(Int(number)/70.0)
	
End Function

Function GetAnimationSequences(n.NPCs,npcname$)
	Local i%
	Local animstart%,animstop%
	Local file$ = "Data\NPCBones.ini"
	
	For i = 1 To GetINIInt(file$,npcname$,"anim_amount")
		animstart = GetINIInt(file$,npcname$,"anim"+i+"_start")
		animstop = GetINIInt(file$,npcname$,"anim"+i+"_stop")
		ExtractAnimSeq(n\obj,animstart,animstop,0)
	Next
	
End Function

Function GetAnimationSpeed(n.NPCs,npcname$,currsequence%)
	Local file$ = "Data\NPCBones.ini"
	
	Return GetINIFloat(file$,npcname$,"anim"+currsequence%+"_speed",0.5)
End Function

Function ApplyAnimation(n.NPCs,sequence%,speed#,animmode%=1)
	
	If n\CurrAnimSeq<>sequence%
		Animate(n\obj,animmode%,speed#,sequence%,5)
		n\CurrAnimSeq = sequence
	EndIf
	n\Frame = AnimTime(n\obj)
	DebugLog n\Frame
	
End Function

Global ShouldUpdateWater$ = ""
Global WaterRender_IgnoreObject%

Type Water
	Field obj%
	Field VexX#[256]
	Field VexY#[256]
	Field VexZ#[256]
	Field PrevVexY#[256]
	Field name$
	Field timer#
	Field isrendering%
	Field customY#
End Type

Function CreateWater.Water(filepath$,name$,x#,y#,z#,parent%=0,customY#=0.0)
	Local wa.Water = New Water
	
	wa\obj = LoadMesh_Strict(filepath,parent)
	PositionEntity wa\obj,x,y,z
	wa\name = name$
	Local surf=GetSurface(wa\obj,1)
	Local i%
	For i=0 To CountVertices(surf)-1
		wa\VexX#[i]=VertexX#(surf,i)
		wa\VexY#[i]=VertexY#(surf,i)
		wa\VexZ#[i]=VertexZ#(surf,i)
		wa\PrevVexY#[i]=wa\VexY#[i]
	Next
	EntityTexture wa\obj,MapCubeMap\Texture,0,1
	wa\customY=customY
	
	Return wa
End Function

Function UpdateWater(name$)
	Local wa.Water,it.Items
	
	For wa = Each Water
		wa\isrendering=False
		If wa\name = name
			wa\isrendering=True
			wa\timer=wa\timer+2*fps\Factor[0]
			UpdateCubeMap(wa\obj,"MapCubeMap")
		EndIf
	Next
	
End Function

Function RenderWater(name$)
	Local wa.Water,it.Items
	Local i,s
	
	For wa = Each Water
		If wa\name = name Then
			For it = Each Items
				If EntityY(it\Collider)<wa\customY Then
					HideEntity(it\model)
				EndIf
			Next
			If WaterRender_IgnoreObject<>0 Then
				HideEntity WaterRender_IgnoreObject
			EndIf
			MapCubeMap\RenderY = wa\customY
			s=GetSurface(wa\obj,1)
			For i=0 To CountVertices(s)-1
				wa\VexY[i]=Sin(wa\timer+wa\VexX[i]*500+wa\VexZ[i]*300)*5.0 ;2.5
				VertexCoords s,i,wa\VexX[i],wa\PrevVexY[i]-wa\VexY[i],wa\VexZ[i]
			Next
			UpdateNormals wa\obj
			RenderCubeMap(wa\obj,"MapCubeMap")
			If WaterRender_IgnoreObject<>0
				ShowEntity WaterRender_IgnoreObject
			EndIf
			For it = Each Items
				If EntityY(it\collider)<wa\customY Then
					ShowEntity(it\model)
				EndIf
			Next
		EndIf
	Next
	
End Function

Function SZL_LoadEntitiesForZone()
	Local i%
	
	If ApacheObj<>0
		ApacheObj = FreeEntity_Strict(ApacheObj)
		ApacheRotorObj = FreeEntity_Strict(ApacheRotorObj)
	EndIf
	If Room2slCam<>0
		Room2slCam = FreeEntity_Strict(Room2slCam)
	EndIf
	
	SelectedElevatorFloor = 0
	
	Select gc\CurrZone
		Case GATE_A_INTRO
			ApacheObj = LoadAnimMesh_Strict("GFX\Map\apaches\apache.b3d")
			HideEntity ApacheObj
			ApacheRotorObj = LoadAnimMesh_Strict("GFX\Map\apaches\apacherotor.b3d")
			HideEntity ApacheRotorObj
		Case LCZ
			Room2slCam = CreateCamera()
			CameraViewport(Room2slCam, 0, 0, 128, 128)
			CameraRange Room2slCam, 0.05, 6.0
			CameraZoom(Room2slCam, 0.8)
			HideEntity(Room2slCam)
	End Select
	
	SelectedElevatorEvent = 0
	
End Function

Function DeInitZoneEntities()
	
	Room2slCam = 0
	ApacheObj = 0
	ApacheRotorObj = 0
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D