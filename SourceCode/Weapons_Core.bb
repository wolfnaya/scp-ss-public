
; ~ [Weapon constants]

;[Block]
; ~ [UNARMED]
Const GUN_UNARMED = 0
; ~ [MELEE]
Const GUN_KNIFE = 1
Const GUN_CROWBAR = 2
; ~ [HANDGUNS]
Const GUN_BERETTA = 3
Const GUN_P99 = 4
Const GUN_USP = 5
Const GUN_P30L = 6
Const GUN_FIVESEVEN = 7
Const GUN_GLOCK = 8
Const GUN_SW500 = 9
; ~ [SMGs&PDWs]
Const GUN_MP7 = 10
Const GUN_P90 = 11
Const GUN_MP5K = 12
; ~ [ASSAULT RIFLES]
Const GUN_HK416 = 13
Const GUN_AK12 = 14
Const GUN_XM29 = 15
; ~ [SHOTGUNS]
Const GUN_SPAS12 = 16
; ~ [MISC]
Const GUN_GRENADE = 17
Const SCP_127 = 18
Const GUN_EMRP = 19

; ~ [Weapon Types]

Const GUNTYPE_UNKNOWN = -1
Const GUNTYPE_HANDGUN = 0
Const GUNTYPE_SMG = 1
Const GUNTYPE_SHOTGUN = 2
Const GUNTYPE_MELEE = 3
Const GUNTYPE_RIFLE = 4
Const GUNTYPE_SCP_127 = 5
Const GUNTYPE_GRENADE = 6
Const GUNTYPE_OICW = 7
Const GUNTYPE_EMRP = 8
Const GUNTYPE_HANDGUN_AUTO = 9

; ~ [Animation Types]

Const GUNANIM_SMG = 0
Const GUNANIM_PISTOL = 1
Const GUNANIM_MP5K = 2
Const GUNANIM_SHOTGUN = 3
Const GUNANIM_MELEE = 4
Const GUNANIM_RIFLE = 5

; ~ [Weapon Decal Types]

Const GUNDECAL_DEFAULT = 0
Const GUNDECAL_SLASH = 1

; ~ [Misc]

Const MaxGunSlots = 3
Const GunSlot1 = 0
Const GunSlot2 = 1
Const GunSlot3 = 2
Const MaxShootSounds = 4
Const MaxShootSilencedSounds = 4
Const MaxExplodeSounds = 4
Const ScopeChargeTime# = 70.0*300
;[End Block]

Global AimCrossIMG
Global BulletIcon%
Global RifleBulletIcon%
Global ShotgunBulletIcon%
Global BoltIcon%
Global XIcon%
Global ToothIcon%
Global GrenadeIcon%
Global Crowbar_HitPivot
Global MuzzleFlash
Global BulletHole1,BulletHole2,DustParticle
Global GunPickPivot
Global GunPivot_Y#
Global GunPivot_YSide% = 0
Global GunPivot_X#
Global GunPivot_XSide% = 0
Global UsingScope%
Global ScopeNVG%
Global ScopeTexture
Global ScopeCam
Global ScopeZoom#
Global GunSFX,GunSFX2,GunCHN,GunCHN2

Global IsPlayerShooting% = False

Global IronSightPivot%,IronSightPivot2%
Global NVGOnSFX%, NVGOffSFX%

Global NTF_InfiniteAmmo% = False
Global NTF_NoReload% = False
Global NTF_NoRecoil% = False

Global GunParticle
Global Charge_Interface%
Global XM29_Interface%
Global MUI_Interface%
Global ScopeLowPowerSFX%
Global ScopeLowPowerChnSFX%
Global Grenade_Model
Global Grenade2_Model

Type GunInstance
	Field GunAnimFLAG%
	Field GunChangeFLAG%
	Field Weapon_CurrSlot%
	Field HoldingGun%
	Field Weapon_InSlot$[MaxGunSlots]
	Field KevlarSFX%
	Field GunPivot
	Field UI_Select_SFX%
	Field UI_Deny_SFX%
	Field GunLight%
	Field GunLightTimer#
	Field AttachSFX%
	Field DetachSFX%
	Field IronSight%
	Field IronSightAnim%
End Type

Type Guns
	Field GunType%
	Field ID
	Field IMG
	Field CurrAmmo
	Field MaxCurrAmmo
	Field CurrReloadAmmo
	Field MaxReloadAmmo
	
	Field CurrAltAmmo
	Field MaxCurrAltAmmo
	Field CurrReloadAltAmmo
	Field MaxReloadAltAmmo
	
	Field AmmoType
	Field DamageOnEntity
	Field Accuracy#
	Field ShootState#
	Field ReloadState#
	Field DeployState#
	Field GunState
	Field Deployed%
	Field Holster%
	Field ShootAnim
	Field SilenState#
	Field Frame#
	Field name$
	Field DisplayName$
	Field MouseDownTimer#
	Field obj%
	Field CanUseIronSight
	Field ViewModelPath$
	Field ViewModelPath2$
	Field IMGPath$
	Field IMGPath2$
	Field IronSightCoords.Vector3D
	
	Field Anim_Deploy.Vector3D
	Field Anim_Shoot.Vector3D
	Field Anim_Reload_Empty.Vector3D
	Field Anim_Reload.Vector3D
	Field Anim_Reload_Start.Vector3D
	Field Anim_Reload_Stop.Vector3D
	Field Anim_Sprint_Transition.Vector3D
	Field Anim_Sprint_Cycle.Vector3D
	Field Frame_Idle#
	
	Field Anim_NoAmmo_Deploy.Vector3D
	Field Anim_NoAmmo_Shoot.Vector3D
	Field Anim_NoAmmo_Sprint_Transition.Vector3D
	Field Anim_NoAmmo_Sprint_Cycle.Vector3D
	Field Frame_NoAmmo_Idle#
	
	Field Anim_Deploy_Grip.Vector3D
	Field Anim_Shoot_Grip.Vector3D
	Field Anim_Reload_Empty_Grip.Vector3D
	Field Anim_Reload_Grip.Vector3D
	Field Anim_Sprint_Transition_Grip.Vector3D
	Field Anim_Sprint_Cycle_Grip.Vector3D
	Field Frame_Idle_Grip#
	
	Field Anim_Shoot_Alt.Vector3D
	Field Anim_Reload_Alt.Vector3D
	
	Field Knockback#
	Field Rate_Of_Fire#
	Field Reload_Empty_Time#
	Field Reload_Time#
	Field Reload_Start_Time#
	Field Deploy_Time#
	
	Field MaxShootSounds%
	Field MaxShootSilencedSounds%
	Field MaxExplodeSounds%
	Field MaxReloadSounds%
	Field MaxWallhitSounds%
	Field Slot%	
	Field Amount_Of_Bullets%
	Field ShootDelay#
	Field Range#
	Field ShootSounds%[MaxShootSounds]
	Field ShootSilencedSounds%[MaxShootSilencedSounds]
	Field ExplodeSounds%[MaxExplodeSounds]
	Field MuzzleFlash%
	Field PlayerModel%
	Field PlayerModelAnim%
	Field ShouldCreateItem%
	Field AttachedItemTemplate.ItemTemplates
	Field DecalType%
	
	Field CanOpenAttachments%
	Field CurrAttachmentSection%
	Field AttachmentSection[5]
	
	Field ChargeTimer#
	Field ChargeTimer2#
	
	Field ScopeCharge#
	
	Field IronSightObj
	
	Field Modelpath$
	
	Field ChangeFiremode%
	
	Field RedDotOffset.Vector3D
	Field AcogOffset.Vector3D
	Field AimPointOffset.Vector3D
	Field EoTechOffset.Vector3D
	
	Field XM29LenseObj
	
	Field CanHaveSuppressor
	Field CanHaveAimPoint
	Field CanHaveRedDot
	Field CanHaveAcog
	Field CanHaveRail
	Field CanHaveStock
	Field CanHaveFoldingStock
	Field CanHaveVerticalGrip
	Field CanHaveEoTech
	Field CanHaveExtMag
	Field CanHaveLaserSight
	Field CanHaveMUI
	
	Field HasSuppressor%
	Field HasAimPoint%
	Field HasRedDot%
	Field HasAcog%
	Field HasRail%
	Field HasStock%
	Field HasFoldingStock%
	Field HasVerticalGrip%
	Field HasEoTech%
	Field HasExtMag%
	Field HasLaserSight%
	Field HasMUI%
	
	Field SilencerObj
	Field SuppressorPos.Vector3D
	Field SuppressorRot.Vector3D
	Field SuppressorSc.Vector3D
	
	Field SightObj
	Field RedDotPos.Vector3D
	Field RedDotRot.Vector3D
	Field RedDotSc.Vector3D
	
	Field AimPointObj
	Field AimPointPos.Vector3D
	Field AimPointRot.Vector3D
	Field AimPointSc.Vector3D
	
	Field EoTechObj
	Field EoTechPos.Vector3D
	Field EoTechRot.Vector3D
	Field EoTechSc.Vector3D
	
	Field ExtMagObj
	Field ExtMagPos.Vector3D
	Field ExtMagRot.Vector3D
	Field ExtMagSc.Vector3D
	
	Field RailObj
	Field RailPos.Vector3D
	Field RailRot.Vector3D
	Field RailSc.Vector3D
	
	Field ScopeObj
	Field ScopeLenseObj
	Field AcogPos.Vector3D
	Field AcogRot.Vector3D
	Field AcogSc.Vector3D
	
	Field StockObj
	Field FoldingStockObj
	Field StockPos.Vector3D
	Field StockRot.Vector3D
	Field StockSc.Vector3D
	
	Field VerticalGripObj
	Field VerticalGripPos.Vector3D
	Field VerticalGripRot.Vector3D
	Field VerticalGripSc.Vector3D
	
	Field LaserSightObj
	Field LaserSightPos.Vector3D
	Field LaserSightRot.Vector3D
	Field LaserSightSc.Vector3D
	
	Field MUIObj
	Field MUIScreenObj
	Field MUIPos.Vector3D
	Field MUIPos2.Vector3D
	Field MUIRot.Vector3D
	Field MUISc.Vector3D
	
	Field PickedUpSuppressor%
	Field PickedUpAimPoint%
	Field PickedUpRedDot%
	Field PickedUpAcog%
	Field PickedUpStock%
	Field PickedUpFoldingStock%
	Field PickedUpVerticalGrip%
	Field PickedUpRail%
	Field PickedUpEotech%
	Field PickedUpExtMag%
	Field PickedUpLaserSight%
	Field PickedUpMUI%
	
	Field HasToggledSuppressor%
	Field HasToggledAimPoint%
	Field HasToggledRedDot%
	Field HasToggledAcog%
	Field HasToggledStock%
	Field HasToggledFoldingStock%
	Field HasToggledVerticalGrip%
	Field HasToggledRail%
	Field HasToggledEotech%
	Field HasToggledExtMag%
	Field HasToggledLaserSight%
	Field HasToggledMUI%
End Type

Type Grenades
	Field Pivot, obj
	Field Speed#, Rollcurr#, Angle#
	Field Channel
	Field Synced
	Field Constpitch#
	Field Constyaw#
	Field GrenadeParticle.Emitters
	Field Timer#
	Field Timer2#
	Field Prevfloor, Prevy#
	Field XSpeed#
	Field State
	Field CollisionTimer#
	Field Ticks%
	Field Gun.Guns
	Field PlayerID
	Field ID
End Type

Type launcherGrenades
	Field Pivot, obj
	Field Speed#, Rollcurr#, Angle#
	Field Channel
	Field Synced
	Field Constpitch#
	Field Constyaw#
	Field GrenadeParticle.Emitters
	Field Timer#
	Field Timer2#
	Field Prevfloor, Prevy#
	Field XSpeed#
	Field State
	Field CollisionTimer#
	Field Ticks%
	Field Gun.Guns
	Field PlayerID
	Field ID
	Field IsOnGround%
End Type

Type Bullet
	Field Numb
	Field DamageOnEntity
	Field FlySpeed
	Field Accuracy
End Type

Type BulletHole
	Field obj%
	Field obj2%
	Field obj3%
	Field obj4%
	Field obj5%
	Field obj6%
	Field KillTimer#,KillTimer2#
End Type

Function CreateGun.Guns(DisplayName$,Name$,Id,Model$,Img$,CanAim=True,Slot$="melee",GunType$="melee",Ammo#=1,ReloadAmmo#=1,Ammo2#=1,ReloadAmmo2#=1,Damage#=1,Accuracy#=1,ShootDelay#=1,Range#=0,Knockback#=1,FireRate#=1,BulletAmount#=1,DeployTime#=1,ReloadTime#=1,ReloadEmptyTime#=1,ReloadStartTime#=1)
	Local g.Guns = New Guns
	Local i%
	
	g\name$ = Name$
	g\ID = Id
	g\IMG = LoadImage("GFX\weapons\"+Img$)
	MaskImage(g\IMG, 255, 0, 255)
	
	g\CurrAmmo = Ammo
	g\MaxCurrAmmo = g\CurrAmmo
	g\CurrAltAmmo = Ammo2
	g\MaxCurrAltAmmo = g\CurrAltAmmo
	g\CurrReloadAmmo = 0
	g\MaxReloadAmmo = ReloadAmmo
	g\CurrReloadAltAmmo = 0
	g\MaxReloadAltAmmo = ReloadAmmo2
	g\DamageOnEntity = Damage
	g\Accuracy = Accuracy
	
	g\CanHaveSuppressor = GetINIInt(gv\WeaponFile,Name$,"can_use_suppressor",0)
	g\CanHaveAimPoint = GetINIInt(gv\WeaponFile,Name$,"can_use_aimpoint",0)
	g\CanHaveRedDot = GetINIInt(gv\WeaponFile,Name$,"can_use_reddot",0)
	g\CanHaveAcog = GetINIInt(gv\WeaponFile,Name$,"can_use_acog",0)
	g\CanHaveRail = GetINIInt(gv\WeaponFile,Name$,"can_use_rail",0)
	g\CanHaveStock = GetINIInt(gv\WeaponFile,Name$,"can_use_stock",0)
	g\CanHaveFoldingStock = GetINIInt(gv\WeaponFile,Name$,"can_use_folding_stock",0)
	g\CanHaveVerticalGrip = GetINIInt(gv\WeaponFile,Name$,"can_use_vertical_grip",0)
	g\CanHaveEoTech = GetINIInt(gv\WeaponFile,Name$,"can_use_eotech",0)
	g\CanHaveExtMag = GetINIInt(gv\WeaponFile,Name$,"can_use_ext_mag",0)
	g\CanHaveLaserSight = GetINIInt(gv\WeaponFile,Name$,"can_use_laser_sight",0)
	g\CanHaveMUI = GetINIInt(gv\WeaponFile,Name$,"can_use_mui",0)
	
	g\obj = LoadAnimMesh_Strict("GFX\weapons\"+Model$,g_I\GunPivot)
	ScaleEntity g\obj,0.005,0.005,0.005
	MeshCullBox(g\obj,-MeshWidth(g\obj)*3,-MeshHeight(g\obj)*3,-MeshDepth(g\obj)*3,MeshWidth(g\obj)*6,MeshHeight(g\obj)*6,MeshDepth(g\obj)*6)
	HideEntity g\obj
	g\CanUseIronSight = CanAim
	
	g\ViewModelPath = Model$
	g\IMGPath = Img$
	
	Local StrTemp$ = GunType
	Select StrTemp
		Case "handgun"
			g\GunType = GUNTYPE_HANDGUN
		Case "auto_handgun"
			g\GunType = GUNTYPE_HANDGUN_AUTO
		Case "smg"
			g\GunType = GUNTYPE_SMG
		Case "shotgun"
			g\GunType = GUNTYPE_SHOTGUN
		Case "melee"
			g\GunType = GUNTYPE_MELEE
		Case "rifle"
			g\GunType = GUNTYPE_RIFLE
		Case "scp-127"
			g\GunType = GUNTYPE_SCP_127
		Case "grenade"
			g\GunType = GUNTYPE_GRENADE
		Case "oicw"
			g\GunType = GUNTYPE_OICW
		Case "gauss"
			g\GunType = GUNTYPE_EMRP
		Default
			g\GunType = GUNTYPE_UNKNOWN
	End Select
	
	g\Knockback = Knockback
	g\Rate_Of_Fire = FireRate
	g\Reload_Empty_Time = ReloadEmptyTime
	g\Reload_Time = ReloadTime
	g\Reload_Start_Time = ReloadStartTime
	g\Deploy_Time = DeployTime
	g\Amount_Of_Bullets = BulletAmount
	
	g\MaxShootSounds = GetINIInt(gv\WeaponFile,Name,"sounds_shoot",1)
	g\MaxShootSilencedSounds = GetINIInt(gv\WeaponFile,Name,"sounds_shoot_silenced",1)
	g\MaxExplodeSounds = GetINIInt(gv\WeaponFile,Name,"sounds_explode",1)
	g\MaxReloadSounds = GetINIInt(gv\WeaponFile,Name,"sounds_reload",1)
	g\MaxWallhitSounds = GetINIInt(gv\WeaponFile,Name,"sounds_wallhit",1)
	
	Local AnimString$
	;Deploy
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_deploy","")
	g\Anim_Deploy = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	;Shoot
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_shoot","")
	g\Anim_Shoot = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	;Alt Shoot
	AnimString = GetINIString(gv\WeaponFile,Name,"Anim_Shoot_Alt","")
	g\Anim_Shoot_Alt = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	;Reload
	AnimString = GetINIString(gv\WeaponFile,Name,"Anim_Reload_Empty","")
	g\Anim_Reload_Empty = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	;Reload Not Full
	AnimString = GetINIString(gv\WeaponFile,Name,"Anim_Reload","")
	g\Anim_Reload = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	;Reload Not Full Grip
	AnimString = GetINIString(gv\WeaponFile,Name,"Anim_Reload_Grip","")
	g\Anim_Reload_Grip = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	;Alt Reload
	AnimString = GetINIString(gv\WeaponFile,Name,"Anim_Reload_Alt","")
	g\Anim_Reload_Alt = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	;Sprint Transition
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_sprint_transition","")
	g\Anim_Sprint_Transition = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	;Sprint Cycle
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_sprint_cycle","")
	g\Anim_Sprint_Cycle = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	;Deploy No Ammo
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_noammo_deploy","")
	If AnimString<>"" Then
		g\Anim_NoAmmo_Deploy = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	;Shoot No Ammo
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_noammo_shoot","")
	If AnimString<>"" Then
		g\Anim_NoAmmo_Shoot = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	;Sprint Transition No Ammo
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_noammo_sprint_transition","")
	If AnimString<>"" Then
		g\Anim_NoAmmo_Sprint_Transition = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	;Sprint Cycle No Ammo
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_noammo_sprint_cycle","")
	If AnimString<>"" Then
		g\Anim_NoAmmo_Sprint_Cycle = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	;Reload Start
	AnimString = GetINIString(gv\WeaponFile,Name,"Anim_Reload_Start","")
	If AnimString<>"" Then
		g\Anim_Reload_Start = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	;Reload Stop
	AnimString = GetINIString(gv\WeaponFile,Name,"Anim_Reload_Stop","")
	If AnimString<>"" Then
		g\Anim_Reload_Stop = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	
	g\Frame_Idle = GetINIFloat(gv\WeaponFile,Name,"frame_idle")
	g\Frame_Idle_Grip = GetINIFloat(gv\WeaponFile,Name,"frame_idle_grip")
	g\Frame_NoAmmo_Idle = GetINIFloat(gv\WeaponFile,Name,"frame_noammo_idle")
	
	;Deploy with Grip
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_deploy_grip","")
	g\Anim_Deploy_Grip = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	;Shoot with Grip
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_shoot_grip","")
	g\Anim_Shoot_Grip = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	;Reload with Grip
	AnimString = GetINIString(gv\WeaponFile,Name,"Anim_Reload_Empty_Grip","")
	g\Anim_Reload_Empty_Grip = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	;Sprint Transition with Grip
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_sprint_transition_grip","")
	g\Anim_Sprint_Transition_Grip = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	;Sprint Cycle with Grip
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_sprint_cycle_grip","")
	g\Anim_Sprint_Cycle_Grip = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	
	Local VectorString$
	VectorString = GetINIString(gv\WeaponFile,Name,"offset","")
	MoveEntity g\obj,Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|")
	VectorString = GetINIString(gv\WeaponFile,Name,"reddot_offset","")
	If VectorString<>"" Then
		g\RedDotOffset = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"aimpoint_offset","")
	If VectorString<>"" Then
		g\AimPointOffset = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"acog_offset","")
	If VectorString<>"" Then
		g\AcogOffset = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"aimoffset","")
	If VectorString<>"" Then
		g\IronSightCoords = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"eotech_offset","")
	If VectorString<>"" Then
		g\EoTechOffset = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	; ~ Attachments
	VectorString = GetINIString(gv\WeaponFile,Name,"suppressor_position","")
	If VectorString<>"" Then
		g\SuppressorPos = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"suppressor_rotation","")
	If VectorString<>"" Then
		g\SuppressorRot = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"suppressor_scale","")
	If VectorString<>"" Then
		g\SuppressorSc = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"aimpoint_position","")
	If VectorString<>"" Then
		g\AimPointPos = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"aimpoint_rotation","")
	If VectorString<>"" Then
		g\AimPointRot = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"aimpoint_scale","")
	If VectorString<>"" Then
		g\AimPointSc = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"reddot_position","")
	If VectorString<>"" Then
		g\RedDotPos = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"reddot_rotation","")
	If VectorString<>"" Then
		g\RedDotRot = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"reddot_scale","")
	If VectorString<>"" Then
		g\RedDotSc = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"acog_position","")
	If VectorString<>"" Then
		g\AcogPos = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"acog_rotation","")
	If VectorString<>"" Then
		g\AcogRot = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"acog_scale","")
	If VectorString<>"" Then
		g\AcogSc = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"rail_position","")
	If VectorString<>"" Then
		g\RailPos = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"rail_rotation","")
	If VectorString<>"" Then
		g\RailRot = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"rail_scale","")
	If VectorString<>"" Then
		g\RailSc = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"ext_mag_position","")
	If VectorString<>"" Then
		g\ExtMagPos = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"ext_mag_rotation","")
	If VectorString<>"" Then
		g\ExtMagRot = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"ext_mag_scale","")
	If VectorString<>"" Then
		g\ExtMagSc = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"stock_position","")
	If VectorString<>"" Then
		g\StockPos = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"stock_rotation","")
	If VectorString<>"" Then
		g\StockRot = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"stock_scale","")
	If VectorString<>"" Then
		g\StockSc = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"vertical_grip_position","")
	If VectorString<>"" Then
		g\VerticalGripPos = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"vertical_grip_rotation","")
	If VectorString<>"" Then
		g\VerticalGripRot = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"vertical_grip_scale","")
	If VectorString<>"" Then
		g\VerticalGripSc = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"eotech_position","")
	If VectorString<>"" Then
		g\EoTechPos = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"eotech_rotation","")
	If VectorString<>"" Then
		g\EoTechRot = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"eotech_scale","")
	If VectorString<>"" Then
		g\EoTechSc = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"laser_sight_position","")
	If VectorString<>"" Then
		g\LaserSightPos = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"laser_sight_rotation","")
	If VectorString<>"" Then
		g\LaserSightRot = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"laser_sight_scale","")
	If VectorString<>"" Then
		g\LaserSightSc = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"mui_position_opened","")
	If VectorString<>"" Then
		g\MUIPos = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"mui_position_closed","")
	If VectorString<>"" Then
		g\MUIPos2 = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"mui_rotation","")
	If VectorString<>"" Then
		g\MUIRot = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"mui_scale","")
	If VectorString<>"" Then
		g\MUISc = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	; ~ End
	g\DisplayName = DisplayName
	StrTemp$ = Slot
	Select StrTemp
		Case "primary"
			g\Slot = SLOT_PRIMARY
		Case "secondary"
			g\Slot = SLOT_SECONDARY
		Case "melee"
			g\Slot = SLOT_MELEE
		Case "none"
			g\Slot = 4
	End Select
	
	g\ShootDelay = ShootDelay
	g\Range = Range
	
	; ~ Preload gun sounds
	If g\MaxShootSounds > 1 Then
		For i = 0 To g\MaxShootSounds-1
			g\ShootSounds[i] = LoadSound_Strict("SFX\Guns\"+g\name+"\shoot"+(i+1)+".ogg")
		Next
	Else
		g\ShootSounds[0] = LoadSound_Strict("SFX\Guns\"+g\name+"\shoot.ogg")
	EndIf
	; ~ Silenced Shoot
	If g\MaxShootSilencedSounds > 1 Then
		For i = 0 To g\MaxShootSilencedSounds-1
			g\ShootSilencedSounds[i] = LoadSound_Strict("SFX\Guns\"+g\name+"\silenced_shoot"+(i+1)+".ogg")
		Next
	Else
		g\ShootSilencedSounds[0] = LoadSound_Strict("SFX\Guns\"+g\name+"\silenced_shoot.ogg")
	EndIf
	; ~ Explosion sound
	If g\MaxExplodeSounds > 1 Then
		For i = 0 To g\MaxExplodeSounds-1
			g\ExplodeSounds[i] = LoadSound_Strict("SFX\Guns\"+g\name+"\explode"+(i+1)+".ogg")
		Next
	Else
		g\ExplodeSounds[0] = LoadSound_Strict("SFX\Guns\"+g\name+"\explode.ogg")
	EndIf
	
	VectorString = GetINIString(gv\WeaponFile,Name,"muzzleoffset","")
	g\MuzzleFlash = CreateSprite()
	EntityFX g\MuzzleFlash,1
	SpriteViewMode g\MuzzleFlash,2
	EntityTexture g\MuzzleFlash,ParticleTextures[1]
	EntityParent g\MuzzleFlash,g\obj
	PositionEntity g\MuzzleFlash,Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|")
	HideEntity g\MuzzleFlash
	
	g\PlayerModel = LoadMesh_Strict("GFX\weapons\Models\"+g\name+"_worldmodel.b3d")
	HideEntity g\PlayerModel
	
	Local SimpleString$ = GetINIString(gv\WeaponFile,Name,"player_model_anim","")
	Select Lower(SimpleString)
		Case "smg"
			g\PlayerModelAnim = GUNANIM_SMG
		Case "pistol"
			g\PlayerModelAnim = GUNANIM_PISTOL
		Case "mp5k"
			g\PlayerModelAnim = GUNANIM_MP5K
		Case "shotgun"
			g\PlayerModelAnim = GUNANIM_SHOTGUN
		Case "melee"
			g\PlayerModelAnim = GUNANIM_MELEE
		Case "rifle"
			g\PlayerModelAnim = GUNANIM_RIFLE
		Default
			RuntimeError "ERROR: Weapon type " + SimpleString + " doesn't exist!"
	End Select
	
	g\ShouldCreateItem = GetINIInt(gv\WeaponFile,Name,"generate_item",1)
	
	SimpleString = GetINIString(gv\WeaponFile,Name,"decal_type","bullet")
	Select Lower(SimpleString)
		Case "bullet"
			g\DecalType = GUNDECAL_DEFAULT
		Case "slash"
			g\DecalType = GUNDECAL_SLASH
		Default
			RuntimeError "ERROR: Weapon decal type " + SimpleString + " doesn't exist!"
	End Select
	
	Return g
End Function

Function InitGuns()
	Local g.Guns
	Local it.ItemTemplates
	Local f%,l$, i%
	Local gunID%
	Local gr.Grenades
	
	g_I.GunInstance = New GunInstance
	
	g_I\GunAnimFLAG = False
	g_I\GunChangeFLAG = False
	
	UsingScope% = False
	
	ScopeNVG% = False
	
	CanPlayerUseGuns% = True
	
	NTF_InfiniteAmmo% = False
	NTF_NoReload% = False
	
	For i = 0 To MaxGunSlots-1
		g_I\Weapon_InSlot[i] = ""
	Next
	
	Charge_Interface = LoadAnimTexture("GFX\Weapons\Models\Attachments\Scope_battery.png", 1+2, 128, 128, 0, 5)
	XM29_Interface = LoadAnimTexture("GFX\Weapons\Models\Attachments\xm29_scope_interface.png", 1+2, 128, 128, 0, 0)
	MUI_Interface = LoadAnimTexture("GFX\Weapons\Models\Attachments\MUI_STATES\MUI_INTERFACE.png", 1, 256, 256, 0, 4)
	
	Grenade_Model = LoadMesh_Strict("GFX\weapons\Models\Grenade_Worldmodel.b3d")
	HideEntity Grenade_Model
	Grenade2_Model = LoadMesh_Strict("GFX\weapons\Models\XM29_Grenade_Worldmodel.b3d")
	HideEntity Grenade2_Model
	AimCrossIMG = LoadImage_Strict("GFX\HUD\Aim_Cross.png")
	MidHandle AimCrossIMG
	
	BulletIcon% = LoadImage_Strict("GFX\HUD\bullet_icon.png")
	RifleBulletIcon% = LoadImage_Strict("GFX\HUD\bullet_icon_rifle.png")
	ShotgunBulletIcon% = LoadImage_Strict("GFX\HUD\bullet_icon_shotgun.png")
	BoltIcon% = LoadImage_Strict("GFX\HUD\bolt_icon.png")
	XIcon% = LoadImage_Strict("GFX\HUD\x_icon.png")
	ToothIcon% = LoadImage_Strict("GFX\HUD\tooth_icon.png")
	GrenadeIcon% = LoadImage_Strict("GFX\HUD\grenade_icon.png")
	
	ScopeLowPowerSFX = LoadSound_Strict("SFX\General\LowBattery1.ogg")
	g_I\GunPivot = CreatePivot()
	
	GunPickPivot = CreatePivot()
	EntityParent GunPickPivot,g_I\GunPivot
	
	ScopeZoom# = 25.0
	
	ScopeTexture = CreateTextureUsingCacheSystem(128,128,1)
	ScopeCam = CreateCamera(g_I\GunPivot)
	MoveEntity ScopeCam,0,0,0.15
	CameraZoom ScopeCam,ScopeZoom#
	CameraViewport ScopeCam,0,0,128,128
	CameraRange ScopeCam,0.005,16
	CameraFogMode ScopeCam,1
	CameraFogRange (ScopeCam, CameraFogNear, CameraFogFar)
	CameraFogColor (ScopeCam,255,255,255)
	HideEntity ScopeCam
	
;! ~ Creating weapons manually
	
	;! ~ [MELEE]
	
	; ~ Knife
	g.Guns = CreateGun("Combat Knife","knife",GUN_KNIFE,"Models\Knife_Viewmodel.b3d","Icons\INVknife.jpg",False,"melee","melee",0,0,0,0,20,1,15,0.85,0,35,1,70)
	; ~ Crowbar
	g.Guns = CreateGun("Crowbar","crowbar",GUN_CROWBAR,"Models\Crowbar_Viewmodel.b3d","Icons\INVcrowbar.jpg",False,"melee","melee",0,0,0,0,35,1,10,0.85,0,65,1,85)
	
	;! ~ [HANDGUNS]
	
	; ~ M9 Beretta
	g.Guns = CreateGun("M9 Beretta","beretta",GUN_BERETTA,"Models\Beretta_Viewmodel.b3d","Icons\INVberetta.jpg",True,"secondary","handgun",15,105,0,0,17,1,1,0,3,7,1,20,170,220)
	; ~ Walther P99
	g.Guns = CreateGun("Walther P99","p99",GUN_P99,"Models\p99_Viewmodel.b3d","Icons\INVp99.jpg",True,"secondary","handgun",16,112,0,0,15,1,1,0,2,8,1,25,140,180)
	; ~ H&K USP
	g.Guns = CreateGun("H&K USP","usp",GUN_USP,"Models\USP_Viewmodel.b3d","Icons\INVusp.jpg",True,"secondary","handgun",12,84,0,0,20,1,1,0,4,10,1,70,170,180)
	; ~ H&K P30L
	g.Guns = CreateGun("H&K P30L","p30l",GUN_P30L,"Models\P30L_Viewmodel.b3d","Icons\INVp30l.jpg",True,"secondary","handgun",17,119,0,0,19,1,1,0,5,9,1,60,160,180)
	; ~ FN Five-Seven
	g.Guns = CreateGun("FN Five-Seven","fiveseven",GUN_FIVESEVEN,"Models\Fiveseven_Viewmodel.b3d","Icons\INVfiveseven.jpg",True,"secondary","handgun",20,140,0,0,12,1,1,0,2,8,1,25,140,180)
	; ~ Glock-20C
	g.Guns = CreateGun("Glock-20C","glock",GUN_GLOCK,"Models\Glock_Viewmodel.b3d","Icons\INVglock.jpg",True,"secondary","auto_handgun",15,105,0,0,25,1,1,0,3.5,4.5,1,25,140,180)
	; ~ S&W Model 500
	g.Guns = CreateGun("S&W Model 500","sw500",GUN_SW500,"Models\Sw500_Viewmodel.b3d","Icons\INVsw500.jpg",True,"secondary","handgun",5,45,0,0,61,1,1,0,12,20,1,70,220,180)
	
	;! ~ [SMGs&PDWs]
	
	; ~ H&K MP7
	g.Guns = CreateGun("H&K MP7","mp7",GUN_MP7,"Models\mp7_Viewmodel.b3d","Icons\INVmp7.jpg",True,"primary","smg",30,210,0,0,17,2,1,0,1.95,5.5,1,70,200,200)
	; ~ FN P90
	g.Guns = CreateGun("FN P90","p90",GUN_P90,"Models\p90_Viewmodel.b3d","Icons\INVp90.jpg",True,"primary","smg",50,350,0,0,10,2,1,0,1.65,3.67,1,70,240,280)
	; ~ MP5K
	g.Guns = CreateGun("H&K MP5K","mp5k",GUN_MP5K,"Models\mp5k_Viewmodel.b3d","Icons\INVmp5k.jpg",True,"primary","smg",30,210,0,0,15,2,1,0,1.8,4.67,1,70,200,240)
	
	;! ~ [ASSAULT RIFLES]
	
	; ~ H&K 416
	g.Guns = CreateGun("H&K 416","hk416",GUN_HK416,"Models\hk416_Viewmodel.b3d","Icons\INVhk416.jpg",True,"primary","rifle",30,210,0,0,20,2,1,0,2,6.85,1,60,270,290)
	; ~ AK-12
	g.Guns = CreateGun("AK-12","ak12",GUN_AK12,"Models\ak12_Viewmodel.b3d","Icons\INVak12.jpg",True,"primary","rifle",30,210,0,0,27,1,1,0,2.5,6.85,1,70,250,350)
	; ~ XM29
	g.Guns = CreateGun("XM29","xm29",GUN_XM29,"Models\xm29_Viewmodel.b3d","Icons\INVxm29.jpg",True,"primary","oicw",30,210,6,18,25,1,1,0,1.95,4.65,1,100,270,310)
	
	;! ~ [SHOTGUNS]
	
	; ~ Franchi SPAS-12
	g.Guns = CreateGun("Franchi SPAS-12","spas12",GUN_SPAS12,"Models\spas12_Viewmodel.b3d","Icons\INVspas12.jpg",True,"primary","shotgun",6,42,0,0,24,6,1,0,8,60,6,60,0,48,80)
	
	;! ~ [GRENADES]
	
	; ~ M61 Grenade
	g.Guns = CreateGun("M61 Grenade","grenade",GUN_GRENADE,"Models\grenade_Viewmodel.b3d","Icons\INVgrenade.jpg",False,"melee","grenade",0,0,0,0,0,1,70,0.85,0,140,1,60)
	
	;! ~ [SCPs]
	
	; ~ SCP-127
	g.Guns = CreateGun("SCP-127","scp127",SCP_127,"Models\scp127_Viewmodel.b3d","Icons\INVscp127.jpg",True,"primary","scp-127",60,0,0,0,9,2,1,0,0.2,6,1,70)
	
	;! ~ [EXPEREMENTAL]
	
	; ~ Electrical Magnetive Rifle - Prototype
	g.Guns = CreateGun("Electrical Magnetive Rifle - Prototype","emr-p",GUN_EMRP,"Models\emr-p_Viewmodel.b3d","Icons\INVemr-p.jpg",True,"primary","gauss",10,50,0,0,500,0.5,1,0,25,200,1,70,240,240)
	
;! ~ Creating items for weapons
	
	;! ~ [MELEE]
	
	; ~ Knife
	it = CreateItemTemplate("Combat Knife","knife","GFX\weapons\Models\Knife_Worldmodel.b3d","GFX\weapons\Icons\INVknife.jpg","", 0.006) : it\sound = 66 : it\isGun% = True
	; ~ Crowbar
	it = CreateItemTemplate("Crowbar","crowbar","GFX\weapons\Models\Crowbar_Worldmodel.b3d","GFX\weapons\Icons\INVcrowbar.jpg","", 0.025) : it\sound = 66 : it\isGun% = True
	
	;! ~ [HANDGUNS]
	
	; ~ M9 Beretta
	it = CreateItemTemplate("M9 Beretta","beretta","GFX\weapons\Models\Beretta_Worldmodel.b3d","GFX\weapons\Icons\INVberetta.jpg","", 0.02) : it\sound = 66 : it\isGun% = True
	; ~ Walther P99
	it = CreateItemTemplate("Walther P99","p99","GFX\weapons\Models\p99_Worldmodel.b3d","GFX\weapons\Icons\INVp99.jpg","", 0.02) : it\sound = 66 : it\isGun% = True
	; ~ H&K USP
	it = CreateItemTemplate("H&K USP","usp","GFX\weapons\Models\USP_Worldmodel.b3d","GFX\weapons\Icons\INVusp.jpg","", 0.02) : it\sound = 66 : it\isGun% = True
	; ~ H&K P30L
	it = CreateItemTemplate("H&K P30L","p30l","GFX\weapons\Models\P30L_Worldmodel.b3d","GFX\weapons\Icons\INVp30l.jpg","", 0.02) : it\sound = 66 : it\isGun% = True
	; ~ FN Five-Seven
	it = CreateItemTemplate("FN Five-Seven","fiveseven","GFX\weapons\Models\Fiveseven_Worldmodel.b3d","GFX\weapons\Icons\INVfiveseven.jpg","", 0.02) : it\sound = 66 : it\isGun% = True
	; ~ Glock-20C
	it = CreateItemTemplate("Glock-20C","glock","GFX\weapons\Models\Glock_Worldmodel.b3d","GFX\weapons\Icons\INVglock.jpg","", 0.02) : it\sound = 66 : it\isGun% = True
	; ~ S&W Model 500
	it = CreateItemTemplate("S&W Model 500","sw500","GFX\weapons\Models\SW500_Worldmodel.b3d","GFX\weapons\Icons\INVsw500.jpg","", 0.02) : it\sound = 66 : it\isGun% = True
	
	;! ~ [SMGs&PDWs]
	
	; ~ H&K MP7
	it = CreateItemTemplate("H&K MP7","mp7","GFX\weapons\Models\mp7_Worldmodel.b3d","GFX\weapons\Icons\INVmp7.jpg","", 0.0026) : it\sound = 66 : it\isGun% = True
	; ~ FN P90
	it = CreateItemTemplate("FN P90","p90","GFX\weapons\Models\p90_Worldmodel.b3d","GFX\weapons\Icons\INVp90.jpg","", 0.02) : it\sound = 66 : it\isGun% = True
	; ~ MP5K
	it = CreateItemTemplate("H&K MP5K","mp5k","GFX\weapons\Models\mp5k_Worldmodel.b3d","GFX\weapons\Icons\INVmp5k.jpg","", 0.0026) : it\sound = 66 : it\isGun% = True
	
	;! ~ [ASSAULT RIFLES]
	
	; ~ H&K 416
	it = CreateItemTemplate("H&K 416","hk416","GFX\weapons\Models\hk416_Worldmodel.b3d","GFX\weapons\Icons\INVhk416.jpg","", 0.006) : it\sound = 66 : it\isGun% = True
	; ~ AK-12
	it = CreateItemTemplate("AK-12","ak12","GFX\weapons\Models\ak12_Worldmodel.b3d","GFX\weapons\Icons\INVak12.jpg","", 0.006) : it\sound = 66 : it\isGun% = True
	; ~ XM29
	it = CreateItemTemplate("XM29","xm29","GFX\weapons\Models\xm29_Worldmodel.b3d","GFX\weapons\Icons\INVxm29.jpg","", 0.02) : it\sound = 66 : it\isGun% = True
	
	;! ~ [SHOTGUNS]
	
	; ~ Franchi SPAS-12
	it = CreateItemTemplate("Franchi SPAS-12","spas12","GFX\weapons\Models\spas12_Worldmodel.b3d","GFX\weapons\Icons\INVspas12.jpg","", 0.006) : it\sound = 66 : it\isGun% = True
	
	;! ~ [GRENADES]
	
	; ~ M61 Grenade
	it = CreateItemTemplate("M61 Grenade","grenade","GFX\weapons\Models\grenade_Worldmodel.b3d","GFX\weapons\Icons\INVgrenade.jpg","", 0.012) : it\sound = 66 : it\isGun% = True
	
	;! ~ [EXPEREMENTAL]
	
	; ~ Electrical Magnetive Rifle - Prototype
	it = CreateItemTemplate("Electrical Magnetive Rifle - Prototype","emr-p","GFX\weapons\Models\emr-p_Worldmodel.b3d","GFX\weapons\Icons\INVemr-p.jpg","", 0.006) : it\sound = 66 : it\isGun% = True
	
	;! ~ [AMMUNITION]
	
	; ~ Ammo Crate
	it = CreateItemTemplate("Ammo Crate", "ammocrate", "GFX\Weapons\Models\Ammo\ammo_crate.b3d", "GFX\weapons\icons\INV_ammo_crate.jpg", "", 0.01)
	; ~ Big Ammo Crate
	it = CreateItemTemplate("Big Ammo Crate", "bigammocrate", "GFX\Weapons\Models\Ammo\big_ammo_crate.b3d", "GFX\weapons\icons\INV_big_ammo_crate.jpg", "", 0.01)
	
;! ~ End
	
	g_I\UI_Select_SFX = LoadSound_Strict("SFX\HUD\GunSelect_Accept.ogg")
	g_I\UI_Deny_SFX = LoadSound_Strict("SFX\HUD\GunSelect_Deny.ogg")
	
	IsPlayerShooting% = False
	
	IronSightPivot% = CreatePivot(g_I\GunPivot)
	IronSightPivot2% = CreatePivot(g_I\GunPivot)
	g_I\IronSight% = False
	g_I\IronSightAnim% = False
	
	NVGOnSFX% = LoadSound_Strict("SFX\Interact\NVGOn.ogg")
	NVGOffSFX% = LoadSound_Strict("SFX\Interact\NVGOff.ogg")
	
	g_I\GunLight = CreateLight(2,g_I\GunPivot)
;	LightColor g_I\GunLight,76,94,255 ; <---- for EMR-P
;	LightRange g_I\GunLight,0.5
	LightColor g_I\GunLight,235,55,0
	LightRange g_I\GunLight,0.4
	HideEntity g_I\GunLight
	g_I\GunLightTimer = 0.0
	
	g_I\AttachSFX = LoadSound_Strict("SFX\Guns\Attachments\attach_addon.ogg")
	g_I\DetachSFX = LoadSound_Strict("SFX\Guns\Attachments\detach_addon.ogg")
	
End Function

Function DeleteGuns()
	Local g.Guns,i
	
	For g.Guns = Each Guns
		Delete g
	Next
	
	FreeImage AimCrossIMG : AimCrossIMG = 0
	FreeImage BulletIcon% : BulletIcon% = 0
	
	FreeSound_Strict g_I\KevlarSFX : g_I\KevlarSFX = 0
	
	If g_I\UI_Select_SFX <> 0 Then FreeSound_Strict(g_I\UI_Select_SFX) : g_I\UI_Select_SFX=0
	If g_I\UI_Deny_SFX <> 0 Then FreeSound_Strict(g_I\UI_Deny_SFX) : g_I\UI_Deny_SFX=0
	
	If NVGOnSFX <> 0 Then FreeSound_Strict(NVGOnSFX) : NVGOnSFX=0
	If NVGOffSFX <> 0 Then FreeSound_Strict(NVGOffSFX) : NVGOffSFX=0
	
End Function

Function UpdateGuns()
	Local isMultiplayer% = (gc\CurrGamemode = 3)
	Local g.Guns,g2.Guns,p.Particles,n.NPCs,pl.Player,i%
	Local shooting% = False
	Local currGun.Guns
	Local gr.Grenades
	
	Local campitch#, camyaw#, playerAlive%
	If isMultiplayer Then
		campitch# = EntityPitch(mpl\CameraPivot)+180
		camyaw# = EntityYaw(mpl\CameraPivot)
		Players[mp_I\PlayerID]\PressMouse1=False
		playerAlive = Players[mp_I\PlayerID]\CurrHP > 0
	Else
		campitch# = EntityPitch(Camera)+180
		camyaw# = EntityYaw(Camera)
		playerAlive = psp\Health > 0
	EndIf
	
	If g_I\IronSight Then
		RotateEntity g_I\GunPivot, campitch-180, camyaw, 0
	Else
		Local gpivotpitch# = EntityPitch(g_I\GunPivot)+180
		Local gpivotyaw# = EntityYaw(g_I\GunPivot)
		Local pitch# = Clamp(CurveAngle(campitch, gpivotpitch, 10.0), campitch-5, campitch+5)
		Local yaw# = CurveAngle(camyaw, gpivotyaw, 10.0)
		yaw = ClampAngle(yaw, camyaw, 5)
		RotateEntity g_I\GunPivot, pitch-180, yaw, 0
	EndIf
	
	g_I\GunAnimFLAG = True
	UsingScope% = False
	IsPlayerShooting% = False
	
	For g.Guns = Each Guns
		If g\HasAcog Then
			If g_I\HoldingGun = g\ID And g\name = "xm29" Then
				If ScopeNVG
					EntityColor g\XM29LenseObj,25,255,255
				Else
					EntityColor g\XM29LenseObj,255,255,255
				EndIf
			ElseIf g_I\HoldingGun = g\ID And g\name = "emr-p" Then
				If ScopeNVG
					EntityColor g\ScopeLenseObj,255,255,0
				Else
					EntityColor g\ScopeLenseObj,255,255,255
				EndIf	
			ElseIf g_I\HoldingGun = g\ID Then
				; ~ Scope Charge
				If ScopeNVG Then 
					g\ScopeCharge# = g\ScopeCharge# + fps\Factor[0]
				EndIf
				If g\ScopeCharge# >= ScopeChargeTime# And ScopeNVG Then 
					ScopeNVG = False
					PlaySound_Strict NVGOffSFX
				EndIf
				
				If ScopeNVG
					EntityColor g\ScopeLenseObj,0,255,0
				Else
					EntityColor g\ScopeLenseObj,255,255,255
				EndIf
			EndIf
		EndIf
	Next
	
	ShowEntity g_I\GunPivot
	If g_I\GunLightTimer > 0.0 And g_I\GunLightTimer < 2.5 Then
		g_I\GunLightTimer = g_I\GunLightTimer + fps\Factor[0]
		ShowEntity g_I\GunLight
	Else
		g_I\GunLightTimer = 0.0
		HideEntity g_I\GunLight
	EndIf
	
	Local prevFrame#
	Local j
	
	Local pHoldingGun%, pDeployState#, pReloadState#, pShootState#, pPressMouse1%, pPressReload%, pAmmo%, pReloadAmmo%, pIsPlayerSprinting%, pIronSight%, pChangeFiremode%, pAmmo2%, pReloadAmmo2%
	If isMultiplayer Then
		pHoldingGun = Players[mp_I\PlayerID]\WeaponInSlot[Players[mp_I\PlayerID]\SelectedSlot]
		pDeployState = Players[mp_I\PlayerID]\DeployState
		pReloadState = Players[mp_I\PlayerID]\ReloadState
		pShootState = Players[mp_I\PlayerID]\ShootState
		pPressMouse1 = Players[mp_I\PlayerID]\PressMouse1
		pPressReload = Players[mp_I\PlayerID]\PressReload
		If Players[mp_I\PlayerID]\SelectedSlot < (MaxSlots - SlotsWithNoAmmo) Then
			pAmmo = Players[mp_I\PlayerID]\Ammo[Players[mp_I\PlayerID]\SelectedSlot]
			pReloadAmmo = Players[mp_I\PlayerID]\ReloadAmmo[Players[mp_I\PlayerID]\SelectedSlot]
		EndIf
		pIsPlayerSprinting = Players[mp_I\PlayerID]\IsPlayerSprinting
		pIronSight = Players[mp_I\PlayerID]\IronSight
	Else
		For g = Each Guns
			If g\ID = g_I\HoldingGun Then
				currGun = g
				Exit
			EndIf
		Next
		pHoldingGun = g_I\HoldingGun
		pDeployState = psp\DeployState
		pReloadState = psp\ReloadState
		pShootState = psp\ShootState
		pChangeFiremode = False
		pPressMouse1 = False
		pPressReload = False
		If currGun <> Null Then
			pAmmo = currGun\CurrAmmo
			pReloadAmmo = currGun\CurrReloadAmmo
			pAmmo2 = currGun\CurrAltAmmo
			pReloadAmmo2 = currGun\CurrReloadAltAmmo
		EndIf
		pIsPlayerSprinting = IsPlayerSprinting
		pIronSight = g_I\IronSight
	EndIf
	Local shootCondition%
	If playerAlive Then
		For g = Each Guns
			HideEntity g\MuzzleFlash
			
			If g_I\GunChangeFLAG = False Then
				For g2.Guns = Each Guns
					If g2\ID%<>pHoldingGun Then
						SetAnimTime g2\obj,0
						HideEntity g2\obj
					Else
						ShowEntity g2\obj
					EndIf
				Next
				DeselectIronSight()
				pDeployState = 0
				pReloadState = 0
				pShootState = 0
				pChangeFiremode = False
				pPressMouse1 = False
				pPressReload = False
				MouseHit1 = False
				MouseDown1 = False
				MouseHit(1)
				g_I\HoldingGun = pHoldingGun
				g_I\GunChangeFLAG = True
				pIronSight = False
			EndIf
			
			prevFrame# = AnimTime(g\obj)
			
			If g\ID = pHoldingGun Then
				shootCondition = (SelectedItem=Null) And ((Not MenuOpen) And (Not AttachmentOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not InLobby()) And (Not mp_I\ChatOpen) And (Not mp_I\Gamemode\DisableMovement)) And (Not IsPlayerListOpen()) And (Not IsModerationOpen()))
				Select g\GunType
					Case GUNTYPE_SMG, GUNTYPE_RIFLE
						;[Block]
						
						If (Not g\ChangeFiremode) Then
							
							If pAmmo=0 Then
								If MouseHit1 And shootCondition Then
									pPressMouse1=True
									pPressReload=False
								EndIf
							Else
								If MouseDown1 And shootCondition Then
									pPressMouse1=True
									pPressReload=False
								EndIf
							EndIf
							
							If KeyHit(KEY_CHANGEFIREMODE) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
								If (Not pReloadState > 0.0) Then
									g\ChangeFiremode = True
									PlayGunSound("change_fire_semi",1,1,False)
								EndIf
							EndIf
							
						ElseIf g\ChangeFiremode Then
							
							If MouseHit1 And shootCondition Then
								pPressMouse1=True
								pPressReload=False
							EndIf
							
							If KeyHit(KEY_CHANGEFIREMODE) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
								If (Not pReloadState > 0.0) Then
									g\ChangeFiremode = False
									PlayGunSound("change_fire_auto",1,1,False)
								EndIf
							EndIf
							
						EndIf
						
						If (pAmmo = 0 And pShootState > 0.0) Lor pReloadState > 0.0 Lor pIsPlayerSprinting Then
							pPressMouse1=False
						EndIf
						If pShootState = 0.0 And pPressMouse1 And pAmmo > 0 And pDeployState >= g\Deploy_Time Then
							If (Not g\HasVerticalGrip) Then
								SetAnimTime(g\obj,g\Frame_Idle)
							Else
								SetAnimTime(g\obj,g\Frame_Idle_Grip)
							EndIf
						EndIf
						
						If pDeployState < g\Deploy_Time Lor pAmmo > g\MaxCurrAmmo Lor pReloadState = 0.0 Then
							pPressReload=False
						EndIf
						If pReloadAmmo = 0 Then
							pPressReload=False
						EndIf
						
						If KeyHit(KEY_RELOAD) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
							If pReloadState = 0.0 And pAmmo <= g\MaxCurrAmmo And pReloadAmmo > 0 Then
								pPressReload=True
								DeselectIronSight()
							EndIf
						EndIf
						
						If KeyHit(KEY_CHANGEFIREMODE) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
							If (Not pReloadState > 0.0) Then
								g\ChangeFiremode = True
								PlayGunSound("change_fire_semi",1,1,False)
							EndIf
						EndIf
						
						shooting = False
						If pDeployState < g\Deploy_Time Then
							If (Not g\HasVerticalGrip) Then
								ChangeGunFrames(g,g\Anim_Deploy,False)
								If prevFrame# < (g\Anim_Deploy\x+1) And AnimTime(g\obj) >= (g\Anim_Deploy\x+1) Then
									PlayGunSound(g\name+"\deploy",1,1,False)
								EndIf
							Else
								ChangeGunFrames(g,g\Anim_Deploy_Grip,False)
								If prevFrame# < (g\Anim_Deploy_Grip\x+1) And AnimTime(g\obj) >= (g\Anim_Deploy_Grip\x+1) Then
									PlayGunSound(g\name+"\deploy",1,1,False)
								EndIf
							EndIf
						Else
							If pReloadState = 0.0 Then
								If pShootState = 0.0 Then
									If (Not g\HasVerticalGrip) Then
										If AnimTime(g\obj) > g\Anim_Shoot\x And AnimTime(g\obj) < g\Anim_Shoot\y-0.5 Then
											ChangeGunFrames(g,g\Anim_Shoot,False)
										Else
											If pIsPlayerSprinting Then
												pPressMouse1 = False
												pPressReload = False
												
												If AnimTime(g\obj)<=(g\Anim_Sprint_Transition\y-0.5) Lor AnimTime(g\obj)>(g\Anim_Sprint_Cycle\y) Then
													ChangeGunFrames(g,g\Anim_Sprint_Transition,False)
												Else
													ChangeGunFrames(g,g\Anim_Sprint_Cycle,True)
												EndIf
											Else
												If AnimTime(g\obj)>(g\Anim_Sprint_Transition\x+0.5) And AnimTime(g\obj)<=g\Anim_Sprint_Cycle\y Then
													ChangeGunFrames(g,g\Anim_Sprint_Transition,False,True)
												Else
													SetAnimTime(g\obj,g\Frame_Idle)
												EndIf
											EndIf
										EndIf
									Else
										If AnimTime(g\obj) > g\Anim_Shoot_Grip\x And AnimTime(g\obj) < g\Anim_Shoot_Grip\y-0.5 Then
											ChangeGunFrames(g,g\Anim_Shoot_Grip,False)
										Else
											If pIsPlayerSprinting Then
												pPressMouse1 = False
												pPressReload = False
												
												If AnimTime(g\obj)<=(g\Anim_Sprint_Transition_Grip\y-0.5) Lor AnimTime(g\obj)>(g\Anim_Sprint_Cycle_Grip\y) Then
													ChangeGunFrames(g,g\Anim_Sprint_Transition_Grip,False)
												Else
													ChangeGunFrames(g,g\Anim_Sprint_Cycle_Grip,True)
												EndIf
											Else
												If AnimTime(g\obj)>(g\Anim_Sprint_Transition_Grip\x+0.5) And AnimTime(g\obj)<=g\Anim_Sprint_Cycle_Grip\y Then
													ChangeGunFrames(g,g\Anim_Sprint_Transition_Grip,False,True)
												Else
													SetAnimTime(g\obj,g\Frame_Idle_Grip)
												EndIf
											EndIf
										EndIf
									EndIf
									If pPressMouse1 And pAmmo = 0 Then
										PlayGunSound(g\name+"\shoot_empty",1,1,False)
									EndIf
								Else
									If (Not g\HasVerticalGrip) Then
										ChangeGunFrames(g,g\Anim_Shoot,False)
									Else
										ChangeGunFrames(g,g\Anim_Shoot_Grip,False)
									EndIf
									If (Not g\HasVerticalGrip) Then
										If Ceil(AnimTime(g\obj)) = g\Anim_Shoot\x Then
											If (Not NTF_NoRecoil) Then
												If (Not g\HasSuppressor) And (Not g\HasStock) And (Not g\HasFoldingStock) Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													CameraShake = g\Knockback/2.0
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback
													g_I\GunLightTimer = fps\Factor[0]
													ShowEntity g\MuzzleFlash
													If g\GunType = GUNTYPE_SMG Then
														EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
													Else
														EntityTexture g\MuzzleFlash,ParticleTextures[14],Rand(0,3)
													EndIf
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												ElseIf g\HasStock Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													CameraShake = g\Knockback/3.2
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback/3.2
													If g\HasSuppressor Then
														ShowEntity g\MuzzleFlash
														EntityTexture g\MuzzleFlash,ParticleTextures[19]
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													Else
														g_I\GunLightTimer = fps\Factor[0]
														ShowEntity g\MuzzleFlash
														EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													EndIf
												ElseIf g\HasFoldingStock Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													CameraShake = g\Knockback/2.75
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback/2.75
													If g\HasSuppressor Then
														ShowEntity g\MuzzleFlash
														EntityTexture g\MuzzleFlash,ParticleTextures[19]
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													Else
														g_I\GunLightTimer = fps\Factor[0]
														ShowEntity g\MuzzleFlash
														If g\GunType = GUNTYPE_SMG Then
															EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
														Else
															EntityTexture g\MuzzleFlash,ParticleTextures[14],Rand(0,3)
														EndIf
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													EndIf
												ElseIf g\HasSuppressor Then
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[19],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													PlayGunSound(g\name,g\MaxShootSilencedSounds,0,True)
													CameraShake = g\Knockback/2.5
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback/2.5
												EndIf
												If pAmmo = 0 And pReloadAmmo = 0 Then
													If gc\CurrGamemode <> 3 Then
														If hds\Wearing Then
															If (Not hds\isBroken) Then
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
															Else
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
																		;If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[23])
															EndIf
														EndIf
													EndIf
												EndIf
											Else
												If (Not g\HasSuppressor) Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													g_I\GunLightTimer = fps\Factor[0]
													ShowEntity g\MuzzleFlash
													If g\GunType = GUNTYPE_SMG Then
														EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
													Else
														EntityTexture g\MuzzleFlash,ParticleTextures[14],Rand(0,3)
													EndIf
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												Else
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[19],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													PlayGunSound(g\name,g\MaxShootSilencedSounds,0,True)
												EndIf
											EndIf
											shooting = True
										EndIf
									Else
										If Ceil(AnimTime(g\obj)) = g\Anim_Shoot_Grip\x Then
											If (Not NTF_NoRecoil) Then
												If (Not g\HasSuppressor) And (Not g\HasStock) And (Not g\HasFoldingStock) Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													CameraShake = g\Knockback/2.0
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback
													g_I\GunLightTimer = fps\Factor[0]
													ShowEntity g\MuzzleFlash
													If g\GunType = GUNTYPE_SMG Then
														EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
													Else
														EntityTexture g\MuzzleFlash,ParticleTextures[14],Rand(0,3)
													EndIf
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												ElseIf g\HasStock Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													CameraShake = g\Knockback/3.2
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback/3.2
													If g\HasSuppressor Then
														ShowEntity g\MuzzleFlash
														EntityTexture g\MuzzleFlash,ParticleTextures[19]
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													Else
														g_I\GunLightTimer = fps\Factor[0]
														ShowEntity g\MuzzleFlash
														If g\GunType = GUNTYPE_SMG Then
															EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
														Else
															EntityTexture g\MuzzleFlash,ParticleTextures[14],Rand(0,3)
														EndIf
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													EndIf
												ElseIf g\HasFoldingStock Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													CameraShake = g\Knockback/2.75
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback/2.75
													If g\HasSuppressor Then
														ShowEntity g\MuzzleFlash
														EntityTexture g\MuzzleFlash,ParticleTextures[19],Rand(0,3)
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													Else
														g_I\GunLightTimer = fps\Factor[0]
														ShowEntity g\MuzzleFlash
														If g\GunType = GUNTYPE_SMG Then
															EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
														Else
															EntityTexture g\MuzzleFlash,ParticleTextures[14],Rand(0,3)
														EndIf
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													EndIf
												ElseIf g\HasSuppressor Then
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[19],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													PlayGunSound(g\name,g\MaxShootSilencedSounds,0,True)
													CameraShake = g\Knockback/2.5
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback/2.5
												EndIf
												If pAmmo = 0 And pReloadAmmo = 0 Then
													If gc\CurrGamemode <> 3 Then
														If hds\Wearing Then
															If (Not hds\isBroken) Then
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
															Else
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
																		;If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[23])
															EndIf
														EndIf
													EndIf
												EndIf
											Else
												If (Not g\HasSuppressor) Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													g_I\GunLightTimer = fps\Factor[0]
													ShowEntity g\MuzzleFlash
													If g\GunType = GUNTYPE_SMG Then
														EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
													Else
														EntityTexture g\MuzzleFlash,ParticleTextures[14],Rand(0,3)
													EndIf
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												Else
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[19],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													PlayGunSound(g\name,g\MaxShootSilencedSounds,0,True)
												EndIf
											EndIf
											shooting = True
										EndIf
									EndIf
									If (Not g\HasVerticalGrip) Then
										If pShootState >= g\Rate_Of_Fire-fps\Factor[0] And pPressMouse1 Then
											SetAnimTime g\obj,g\Anim_Shoot\x
										EndIf
									Else
										If pShootState >= g\Rate_Of_Fire-fps\Factor[0] And pPressMouse1 Then
											SetAnimTime g\obj,g\Anim_Shoot_Grip\x
										EndIf
									EndIf
								EndIf
							Else
								If (Not g\HasVerticalGrip) Then
									If pAmmo = 0 Then
										If AnimTime(g\obj)>g\Anim_Reload_Empty\y Lor AnimTime(g\obj)<g\Anim_Reload_Empty\x Then
											SetAnimTime(g\obj,g\Anim_Reload_Empty\x)
										EndIf
										ChangeGunFrames(g,g\Anim_Reload_Empty,False)
										If prevFrame# < (g\Anim_Reload_Empty\x+1) And AnimTime(g\obj) >= (g\Anim_Reload_Empty\x+1) Then
											PlayGunSound(g\name+"\reload_empty",g\MaxReloadSounds,1,False)
										ElseIf prevFrame# < (g\Anim_Reload_Empty\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload_Empty\y-0.5) Then
											pPressReload = False
										EndIf
									Else
										If AnimTime(g\obj)>g\Anim_Reload\y Lor AnimTime(g\obj)<g\Anim_Reload\x Then
											SetAnimTime(g\obj,g\Anim_Reload\x)
										EndIf
										ChangeGunFrames(g,g\Anim_Reload,False)
										If prevFrame# < (g\Anim_Reload\x+1) And AnimTime(g\obj) >= (g\Anim_Reload\x+1) Then
											PlayGunSound(g\name+"\reload",g\MaxReloadSounds,1,False)
										ElseIf prevFrame# < (g\Anim_Reload\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload\y-0.5) Then
											pPressReload = False
										EndIf
									EndIf
								Else
									If pAmmo = 0 Then
										If AnimTime(g\obj)>g\Anim_Reload_Empty_Grip\y Lor AnimTime(g\obj)<g\Anim_Reload_Empty_Grip\x Then
											SetAnimTime(g\obj,g\Anim_Reload_Empty_Grip\x)
										EndIf
										ChangeGunFrames(g,g\Anim_Reload_Empty_Grip,False)
										If prevFrame# < (g\Anim_Reload_Empty_Grip\x+1) And AnimTime(g\obj) >= (g\Anim_Reload_Empty_Grip\x+1) Then
											PlayGunSound(g\name+"\reload_empty",g\MaxReloadSounds,1,False)
										ElseIf prevFrame# < (g\Anim_Reload_Empty_Grip\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload_Empty_Grip\y-0.5) Then
											pPressReload = False
										EndIf
									Else
										If AnimTime(g\obj)>g\Anim_Reload_Grip\y Lor AnimTime(g\obj)<g\Anim_Reload_Grip\x Then
											SetAnimTime(g\obj,g\Anim_Reload_Grip\x)
										EndIf
										ChangeGunFrames(g,g\Anim_Reload_Grip,False)
										If prevFrame# < (g\Anim_Reload_Grip\x+1) And AnimTime(g\obj) >= (g\Anim_Reload_Grip\x+1) Then
											PlayGunSound(g\name+"\reload",g\MaxReloadSounds,1,False)
										ElseIf prevFrame# < (g\Anim_Reload_Grip\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload_Grip\y-0.5) Then
											pPressReload = False
										EndIf
									EndIf
								EndIf
								pIronSight = False
							EndIf
						EndIf
						
						If (Not pIsPlayerSprinting) And (Not shooting) Then
							g_I\GunAnimFLAG = False
						Else
							g_I\GunAnimFLAG = True
						EndIf
						;[End Block]
					Case GUNTYPE_HANDGUN, GUNTYPE_HANDGUN_AUTO
						;[Block]
						
						If g\GunType = GUNTYPE_HANDGUN Then
							If MouseHit1 And shootCondition Then
								pPressMouse1=True
								pPressReload=False
							EndIf
						Else
							If (Not g\ChangeFiremode) Then
								If pAmmo=0 Then
									If MouseHit1 And shootCondition Then
										pPressMouse1=True
										pPressReload=False
									EndIf
								Else
									If MouseDown1 And shootCondition Then
										pPressMouse1=True
										pPressReload=False
									EndIf
								EndIf
								
								If KeyHit(KEY_CHANGEFIREMODE) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
									If (Not pReloadState > 0.0) Then
										g\ChangeFiremode = True
										PlayGunSound("change_fire_semi",1,1,False)
									EndIf
								EndIf
								
							ElseIf g\ChangeFiremode Then
								
								If MouseHit1 And shootCondition Then
									pPressMouse1=True
									pPressReload=False
								EndIf
								
								If KeyHit(KEY_CHANGEFIREMODE) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
									If (Not pReloadState > 0.0) Then
										g\ChangeFiremode = False
										PlayGunSound("change_fire_auto",1,1,False)
									EndIf
								EndIf
							EndIf
						EndIf
						
						If (pAmmo = 0 And pShootState > 0.0) Lor pReloadState > 0.0 Lor pIsPlayerSprinting Then
							pPressMouse1=False
						EndIf
						
						If pShootState = 0.0 And pPressMouse1 And pAmmo > 0 And pDeployState >= g\Deploy_Time Then
							SetAnimTime(g\obj,g\Frame_Idle)
						EndIf
						
						If pDeployState < g\Deploy_Time Lor pAmmo > g\MaxCurrAmmo Lor pReloadState = 0.0 Then
							pPressReload=False
						EndIf
						If pReloadAmmo = 0 Then
							pPressReload=False
						EndIf
						
						If g\GunType = GUNTYPE_HANDGUN Then
							
							If g_I\HoldingGun <> 9 Then
								If KeyHit(KEY_RELOAD) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
									If pReloadState = 0.0 And pAmmo <= g\MaxCurrAmmo And pReloadAmmo > 0 Then
										pPressReload=True
										DeselectIronSight()
									EndIf
								EndIf
							Else
								If KeyHit(KEY_RELOAD) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
									If pReloadState = 0.0 And pAmmo < g\MaxCurrAmmo And pReloadAmmo > 0 Then
										pPressReload=True
										DeselectIronSight()
									EndIf
								EndIf
							EndIf
							
						Else
							If KeyHit(KEY_RELOAD) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
								If pReloadState = 0.0 And pAmmo <= g\MaxCurrAmmo And pReloadAmmo > 0 Then
									pPressReload=True
									DeselectIronSight()
								EndIf
							EndIf
							
							If KeyHit(KEY_CHANGEFIREMODE) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
								If (Not pReloadState > 0.0) Then
									g\ChangeFiremode = True
									PlayGunSound("change_fire_semi",1,1,False)
								EndIf
							EndIf
						EndIf
						
						shooting = False
						If pDeployState < g\Deploy_Time Then
							If pAmmo = 0 Then
								ChangeGunFrames(g,g\Anim_NoAmmo_Deploy,False)
							Else
								ChangeGunFrames(g,g\Anim_Deploy,False)
							EndIf
							If prevFrame# < (g\Anim_Deploy\x+1) And AnimTime(g\obj) >= (g\Anim_Deploy\x+1) Then
								PlayGunSound(g\name+"\deploy",1,1,False)
							EndIf
						Else
							If pReloadState = 0.0 Then
								If pShootState = 0.0 Then
									If AnimTime(g\obj) > g\Anim_Shoot\x And AnimTime(g\obj) < g\Anim_Shoot\y-0.5 Then
										ChangeGunFrames(g,g\Anim_Shoot,False)
									ElseIf AnimTime(g\obj) > g\Anim_NoAmmo_Shoot\x And AnimTime(g\obj) < g\Anim_NoAmmo_Shoot\y-0.5 Then
										ChangeGunFrames(g,g\Anim_NoAmmo_Shoot,False)
									Else
										If pIsPlayerSprinting Then
											pPressMouse1 = False
											pPressReload = False
											
											If pAmmo = 0 Then
												If AnimTime(g\obj)<=(g\Anim_NoAmmo_Sprint_Transition\y-0.5) Lor AnimTime(g\obj)>(g\Anim_NoAmmo_Sprint_Cycle\y) Then
													ChangeGunFrames(g,g\Anim_NoAmmo_Sprint_Transition,False)
												Else
													ChangeGunFrames(g,g\Anim_NoAmmo_Sprint_Cycle,True)
												EndIf
											Else
												If AnimTime(g\obj)<=(g\Anim_Sprint_Transition\y-0.5) Lor AnimTime(g\obj)>(g\Anim_Sprint_Cycle\y) Then
													ChangeGunFrames(g,g\Anim_Sprint_Transition,False)
												Else
													ChangeGunFrames(g,g\Anim_Sprint_Cycle,True)
												EndIf
											EndIf
										Else
											If pAmmo = 0 Then
												If AnimTime(g\obj)>(g\Anim_NoAmmo_Sprint_Transition\x+0.5) And AnimTime(g\obj)<=g\Anim_NoAmmo_Sprint_Cycle\y Then
													ChangeGunFrames(g,g\Anim_NoAmmo_Sprint_Transition,False,True)
												Else
													SetAnimTime(g\obj,g\Frame_NoAmmo_Idle)
												EndIf
											Else
												If AnimTime(g\obj)>(g\Anim_Sprint_Transition\x+0.5) And AnimTime(g\obj)<=g\Anim_Sprint_Cycle\y Then
													ChangeGunFrames(g,g\Anim_Sprint_Transition,False,True)
												Else
													SetAnimTime(g\obj,g\Frame_Idle)
												EndIf
											EndIf
										EndIf
									EndIf
									
									If pPressMouse1 And pAmmo = 0 Then
										PlayGunSound(g\name+"\shoot_empty",1,1,False)
									EndIf
								Else
									If pAmmo = 0 Then
										ChangeGunFrames(g,g\Anim_NoAmmo_Shoot,False)
										If Ceil(AnimTime(g\obj)) = g\Anim_NoAmmo_Shoot\x Then
											PlayGunSound(g\name+"\slide_back",1,1,False)
											If (Not NTF_NoRecoil) Then
												If (Not g\HasSuppressor) And (Not g\HasStock) And (Not g\HasFoldingStock) Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													CameraShake = g\Knockback/2.0
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback
													g_I\GunLightTimer = fps\Factor[0]
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												ElseIf g\HasStock Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													CameraShake = g\Knockback/3.2
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback/3.2
													If g\HasSuppressor Then
														ShowEntity g\MuzzleFlash
														EntityTexture g\MuzzleFlash,ParticleTextures[19]
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													Else
														g_I\GunLightTimer = fps\Factor[0]
														ShowEntity g\MuzzleFlash
														EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													EndIf
												ElseIf g\HasFoldingStock Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													CameraShake = g\Knockback/2.75
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback/2.75
													If g\HasSuppressor Then
														ShowEntity g\MuzzleFlash
														EntityTexture g\MuzzleFlash,ParticleTextures[19]
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													Else
														g_I\GunLightTimer = fps\Factor[0]
														ShowEntity g\MuzzleFlash
														EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													EndIf
												ElseIf g\HasSuppressor Then
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[19],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													PlayGunSound(g\name,g\MaxShootSilencedSounds,0,True)
													CameraShake = g\Knockback/2.5
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback/2.5
												EndIf
												If pAmmo = 0 And pReloadAmmo = 0 Then
													If gc\CurrGamemode <> 3 Then
														If hds\Wearing Then
															If (Not hds\isBroken) Then
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
															Else
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
															EndIf
														EndIf
													EndIf
												EndIf
											Else
												If (Not g\HasSuppressor) Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													g_I\GunLightTimer = fps\Factor[0]
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												Else
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[19],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													PlayGunSound(g\name,g\MaxShootSilencedSounds,0,True)
												EndIf
											EndIf
											shooting = True
										EndIf
									Else
										ChangeGunFrames(g,g\Anim_Shoot,False)
										If Ceil(AnimTime(g\obj)) = g\Anim_Shoot\x Then
											If (Not NTF_NoRecoil) Then
												If (Not g\HasSuppressor) And (Not g\HasStock) And (Not g\HasFoldingStock) Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													CameraShake = g\Knockback/2.0
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback
													g_I\GunLightTimer = fps\Factor[0]
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												ElseIf g\HasStock Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													CameraShake = g\Knockback/3.2
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback/3.2
													If g\HasSuppressor Then
														ShowEntity g\MuzzleFlash
														EntityTexture g\MuzzleFlash,ParticleTextures[19]
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													Else
														g_I\GunLightTimer = fps\Factor[0]
														ShowEntity g\MuzzleFlash
														EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													EndIf
												ElseIf g\HasFoldingStock Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													CameraShake = g\Knockback/2.75
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback/2.75
													If g\HasSuppressor Then
														ShowEntity g\MuzzleFlash
														EntityTexture g\MuzzleFlash,ParticleTextures[19]
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													Else
														g_I\GunLightTimer = fps\Factor[0]
														ShowEntity g\MuzzleFlash
														EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
														TurnEntity g\MuzzleFlash,0,0,Rnd(360)
														ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													EndIf
												ElseIf g\HasSuppressor Then
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[19],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													PlayGunSound(g\name,g\MaxShootSilencedSounds,0,True)
													CameraShake = g\Knockback/2.5
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback/2.5
												EndIf
												If pAmmo = 0 And pReloadAmmo = 0 Then
													If gc\CurrGamemode <> 3 Then
														If hds\Wearing Then
															If (Not hds\isBroken) Then
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
															Else
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
															EndIf
														EndIf
													EndIf
												EndIf
											Else
												If (Not g\HasSuppressor) Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													g_I\GunLightTimer = fps\Factor[0]
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[3],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												Else
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[19],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
													PlayGunSound(g\name,g\MaxShootSilencedSounds,0,True)
												EndIf
											EndIf
											shooting = True
										EndIf
									EndIf
									If pShootState >= g\Rate_Of_Fire-fps\Factor[0] And pPressMouse1 Then
										SetAnimTime g\obj,g\Anim_Shoot\x
									EndIf
								EndIf
							Else
								If pAmmo = 0 Then
									If AnimTime(g\obj)>g\Anim_Reload_Empty\y Lor AnimTime(g\obj)<g\Anim_Reload_Empty\x Then
										SetAnimTime(g\obj,g\Anim_Reload_Empty\x)
									EndIf
									ChangeGunFrames(g,g\Anim_Reload_Empty,False)
									If prevFrame# < (g\Anim_Reload_Empty\x+1) And AnimTime(g\obj) >= (g\Anim_Reload_Empty\x+1) Then
										PlayGunSound(g\name+"\reload_empty",g\MaxReloadSounds,1,False)
									ElseIf prevFrame# < (g\Anim_Reload_Empty\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload_Empty\y-0.5) Then
										pPressReload = False
									EndIf
								Else
									If AnimTime(g\obj)>g\Anim_Reload\y Lor AnimTime(g\obj)<g\Anim_Reload\x Then
										SetAnimTime(g\obj,g\Anim_Reload\x)
									EndIf
									ChangeGunFrames(g,g\Anim_Reload,False)
									If prevFrame# < (g\Anim_Reload\x+1) And AnimTime(g\obj) >= (g\Anim_Reload\x+1) Then
										PlayGunSound(g\name+"\reload",g\MaxReloadSounds,1,False)
									ElseIf prevFrame# < (g\Anim_Reload\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload\y-0.5) Then
										pPressReload = False
									EndIf
								EndIf
								pIronSight = False
							EndIf
						EndIf
						
						If (Not pIsPlayerSprinting) And (Not shooting) Then
							g_I\GunAnimFLAG = False
						Else
							g_I\GunAnimFLAG = True
						EndIf
						;[End Block]
					Case GUNTYPE_SHOTGUN
						;[Block]
						If MouseHit1 And shootCondition Then
							pPressMouse1=True
							pPressReload=False
						EndIf
						
						If (pAmmo = 0 And pShootState > 0.0) Lor pIsPlayerSprinting Then
							pPressMouse1=False
						EndIf
						
						If pShootState = 0.0 And pPressMouse1 And pAmmo > 0 And pDeployState >= g\Deploy_Time Then
							SetAnimTime(g\obj,g\Frame_Idle)
						EndIf
						
						If pDeployState < g\Deploy_Time Lor pAmmo = g\MaxCurrAmmo Lor pReloadState = 0.0 Then
							pPressReload=False
						EndIf
						If pReloadAmmo = 0 Then
							pPressReload=False
						EndIf
						
						If KeyHit(KEY_RELOAD) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen) And (Not IsInVote())) And (Not IsModerationOpen()) Then
							If pReloadState = 0.0 And pAmmo < g\MaxCurrAmmo And pReloadAmmo > 0 Then
								pPressReload=True
								DeselectIronSight()
							EndIf
						EndIf
						
						shooting = False
						If pDeployState < g\Deploy_Time Then
							ChangeGunFrames(g,g\Anim_Deploy,False)
							If prevFrame# < (g\Anim_Deploy\x+1) And AnimTime(g\obj) >= (g\Anim_Deploy\x+1) Then
								PlayGunSound(g\name+"\deploy",1,1,False)
							EndIf
						Else
							If pReloadState = 0.0 Then
								If pShootState = 0.0 Then
									If AnimTime(g\obj) > g\Anim_Shoot\x And AnimTime(g\obj) < g\Anim_Shoot\y-0.5 Then
										ChangeGunFrames(g,g\Anim_Shoot,False)
									ElseIf AnimTime(g\obj) >= g\Anim_Reload_Stop\x And AnimTime(g\obj) < (g\Anim_Reload_Stop\y-0.5) Then
										ChangeGunFrames(g,g\Anim_Reload_Stop,False)
										If AnimTime(g\obj) >= (g\Anim_Reload_Stop\y-0.5) Then
											SetAnimTime(g\obj,g\Frame_Idle)
											pPressReload = False
										EndIf
									Else
										If pIsPlayerSprinting Then
											pPressMouse1 = False
											pPressReload = False
											
											If AnimTime(g\obj)<=(g\Anim_Sprint_Transition\y-0.5) Lor AnimTime(g\obj)>(g\Anim_Sprint_Cycle\y) Then
												ChangeGunFrames(g,g\Anim_Sprint_Transition,False)
											Else
												ChangeGunFrames(g,g\Anim_Sprint_Cycle,True)
											EndIf
										Else
											If AnimTime(g\obj)>(g\Anim_Sprint_Transition\x+0.5) And AnimTime(g\obj)<=g\Anim_Sprint_Cycle\y Then
												ChangeGunFrames(g,g\Anim_Sprint_Transition,False,True)
											Else
												SetAnimTime(g\obj,g\Frame_Idle)
											EndIf
										EndIf
									EndIf
									
									If pPressMouse1 And pAmmo = 0 Then
										PlayGunSound(g\name+"\shoot_empty",1,1,False)
									EndIf
								Else
									ChangeGunFrames(g,g\Anim_Shoot,False)
									If Ceil(AnimTime(g\obj)) = g\Anim_Shoot\x Then
										If (Not NTF_NoRecoil)
											PlayGunSound(g\name,g\MaxShootSounds,0,True)
											CameraShake = g\Knockback/2.0
											User_Camera_Pitch = User_Camera_Pitch - g\Knockback
											g_I\GunLightTimer = fps\Factor[0]
											shooting = True
											ShowEntity g\MuzzleFlash
											TurnEntity g\MuzzleFlash,0,0,Rnd(360)
											ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
											If pAmmo = 0 And pReloadAmmo = 0 Then
												If gc\CurrGamemode <> 3 Then
													If hds\Wearing Then
														If (Not hds\isBroken) Then
															If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
														Else
															If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
																		;If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[23])
														EndIf
													EndIf
												EndIf
											EndIf
										Else
											PlayGunSound(g\name,g\MaxShootSounds,0,True)
											g_I\GunLightTimer = fps\Factor[0]
											shooting = True
											ShowEntity g\MuzzleFlash
											TurnEntity g\MuzzleFlash,0,0,Rnd(360)
											ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
										EndIf
									EndIf
									If pShootState >= g\Rate_Of_Fire-fps\Factor[0] And pPressMouse1 Then
										SetAnimTime g\obj,g\Anim_Shoot\x
									EndIf
								EndIf
							Else
								If AnimTime(g\obj)>g\Anim_Reload_Stop\y Lor AnimTime(g\obj)<g\Anim_Reload_Start\x Then
									SetAnimTime(g\obj,g\Anim_Reload_Start\x)
								EndIf
								If AnimTime(g\obj) >= g\Anim_Reload_Start\x And AnimTime(g\obj) < (g\Anim_Reload_Start\y-0.5) Then
									ChangeGunFrames(g,g\Anim_Reload_Start,False)
									If AnimTime(g\obj) >= (g\Anim_Reload_Start\y-0.5) Then
										SetAnimTime(g\obj,g\Anim_Reload_Empty\x)
										PlayGunSound(g\name+"\reload",g\MaxReloadSounds,1,False)
									EndIf
								ElseIf AnimTime(g\obj) >= g\Anim_Reload_Empty\x And AnimTime(g\obj) < (g\Anim_Reload_Empty\y-0.5) Then
									ChangeGunFrames(g,g\Anim_Reload_Empty,False)
									If AnimTime(g\obj) >= (g\Anim_Reload_Empty\y-0.5) Then
										If pAmmo < g\MaxCurrAmmo And pReloadAmmo > 0 Then
											SetAnimTime(g\obj,g\Anim_Reload_Empty\x)
											PlayGunSound(g\name+"\reload",g\MaxReloadSounds,1,False)
										Else
											SetAnimTime(g\obj,g\Anim_Reload_Stop\x)
											PlayGunSound(g\name+"\reload_stop",1,1,False)
										EndIf
									EndIf
								ElseIf AnimTime(g\obj) >= g\Anim_Reload_Stop\x And AnimTime(g\obj) < (g\Anim_Reload_Stop\y-0.5) Then
									ChangeGunFrames(g,g\Anim_Reload_Stop,False)
									If AnimTime(g\obj) >= (g\Anim_Reload_Stop\y-0.5) Then
										SetAnimTime(g\obj,g\Frame_Idle)
										pPressReload = False
									EndIf
								EndIf
								pIronSight = False
							EndIf
						EndIf
						
						If (Not pIsPlayerSprinting) And (Not shooting) Then
							g_I\GunAnimFLAG = False
						Else
							g_I\GunAnimFLAG = True
						EndIf
						;[End Block]
					Case GUNTYPE_MELEE
						;[Block]
						If MouseDown1 And shootCondition Then
							pPressMouse1=True
						EndIf
						
						If pShootState > 0.0 Then
							pPressMouse1=False
						EndIf
						
						If pShootState = 0.0 And pPressMouse1 And pDeployState >= g\Deploy_Time Then
							SetAnimTime(g\obj,g\Frame_Idle)
						EndIf
						
						shooting = False
						If pDeployState < g\Deploy_Time Then
							ChangeGunFrames(g,g\Anim_Deploy,False)
							If prevFrame# < (g\Anim_Deploy\x+1) And AnimTime(g\obj) >= (g\Anim_Deploy\x+1) Then
								PlayGunSound(g\name+"\deploy",1,1,False)
							EndIf
						Else
							If pShootState = 0.0 Then
								If AnimTime(g\obj) > g\Anim_Shoot\x And AnimTime(g\obj) < g\Anim_Shoot\y-0.5 Then
									ChangeGunFrames(g,g\Anim_Shoot,False)
								Else
									SetAnimTime(g\obj,g\Frame_Idle)
								EndIf
								pReloadState = 0.0
							Else
								ChangeGunFrames(g,g\Anim_Shoot,False)
								If Ceil(AnimTime(g\obj)) = g\Anim_Shoot\x Then
									PlayGunSound(g\name+"\miss",1,1,True)
								EndIf
								
								If pShootState >= g\ShootDelay And pReloadState = 0.0 Then
									pReloadState = 1.0
								EndIf
								If pShootState >= g\Rate_Of_Fire-fps\Factor[0] And pPressMouse1 Then
									SetAnimTime g\obj,g\Anim_Shoot\x
									pReloadState = 0.0
								EndIf
							EndIf
						EndIf
						
						If (Not shooting) Then
							g_I\GunAnimFLAG = False
						Else
							g_I\GunAnimFLAG = True
						EndIf
						;[End Block]
					Case GUNTYPE_SCP_127
						;[Block]
						If (Not g\ChangeFiremode) Then
							
							If pAmmo=0 Then
								If MouseHit1 And shootCondition Then
									pPressMouse1=True
									pPressReload=False
								EndIf
							Else
								If MouseDown1 And shootCondition Then
									pPressMouse1=True
									pPressReload=False
								EndIf
							EndIf
							
							If KeyHit(KEY_CHANGEFIREMODE) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
								If (Not pReloadState > 0.0) Then
									g\ChangeFiremode = True
									PlayGunSound("change_fire_semi",1,1,False)
								EndIf
							EndIf
							
						ElseIf g\ChangeFiremode Then
							
							If MouseHit1 And shootCondition Then
								pPressMouse1=True
								pPressReload=False
							EndIf
							
							If KeyHit(KEY_CHANGEFIREMODE) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
								If (Not pReloadState > 0.0) Then
									g\ChangeFiremode = False
									PlayGunSound("change_fire_auto",1,1,False)
								EndIf
							EndIf
							
						EndIf
						
						If (pAmmo = 0 And pShootState > 0.0) Lor pIsPlayerSprinting Then
							pPressMouse1=False
						EndIf
						If pShootState = 0.0 And pPressMouse1 And pAmmo > 0 And pDeployState >= g\Deploy_Time Then
							SetAnimTime(g\obj,g\Frame_Idle)
						EndIf
						
						If pDeployState < g\Deploy_Time Lor pAmmo > g\MaxCurrAmmo Lor pReloadState = 0.0 Then
							pPressReload=False
						EndIf
						If pReloadAmmo = 0 Then
							pPressReload=False
						EndIf
						
						shooting = False
						If pDeployState < g\Deploy_Time Then
							ChangeGunFrames(g,g\Anim_Deploy,False)
							If prevFrame# < (g\Anim_Deploy\x+1) And AnimTime(g\obj) >= (g\Anim_Deploy\x+1) Then
								PlayGunSound(g\name+"\deploy",1,1,False)
							EndIf
						Else
							If pShootState = 0.0 Then
								If AnimTime(g\obj) > g\Anim_Shoot\x And AnimTime(g\obj) < g\Anim_Shoot\y-0.5 Then
									ChangeGunFrames(g,g\Anim_Shoot,False)
								Else
									If pIsPlayerSprinting Then
										pPressMouse1 = False
										pPressReload = False
										
										If AnimTime(g\obj)<=(g\Anim_Sprint_Transition\y-0.5) Lor AnimTime(g\obj)>(g\Anim_Sprint_Cycle\y) Then
											ChangeGunFrames(g,g\Anim_Sprint_Transition,False)
										Else
											ChangeGunFrames(g,g\Anim_Sprint_Cycle,True)
										EndIf
									Else
										If AnimTime(g\obj)>(g\Anim_Sprint_Transition\x+0.5) And AnimTime(g\obj)<=g\Anim_Sprint_Cycle\y Then
											ChangeGunFrames(g,g\Anim_Sprint_Transition,False,True)
										Else
											SetAnimTime(g\obj,g\Frame_Idle)
										EndIf
									EndIf
								EndIf
								
								If pPressMouse1 And pAmmo = 0 Then
									PlayGunSound(g\name+"\shoot_empty",1,1,False)
								EndIf
							Else
								ChangeGunFrames(g,g\Anim_Shoot,False)
								If Ceil(AnimTime(g\obj)) = g\Anim_Shoot\x Then
									If (Not NTF_NoRecoil) Then
										PlayGunSound(g\name,g\MaxShootSounds,0,True)
										CameraShake = g\Knockback/2.0
										User_Camera_Pitch = User_Camera_Pitch - g\Knockback
									Else
										PlayGunSound(g\name,g\MaxShootSounds,0,True)
									EndIf
									shooting = True
								EndIf
								If pShootState >= g\Rate_Of_Fire-fps\Factor[0] And pPressMouse1 Then
									SetAnimTime g\obj,g\Anim_Shoot\x
								EndIf
							EndIf
						EndIf
						
						If (Not pIsPlayerSprinting) And (Not shooting) Then
							g_I\GunAnimFLAG = False
						Else
							g_I\GunAnimFLAG = True
						EndIf
						;[End Block]
					Case GUNTYPE_GRENADE
						;[Block]
						If MouseHit1 And shootCondition Then
							pPressMouse1=True
						EndIf
						
						If pShootState > 0.0 Then
							pPressMouse1=False
						EndIf
						
						If pShootState = 0.0 And pPressMouse1 And pDeployState >= g\Deploy_Time Then
							SetAnimTime(g\obj,g\Frame_Idle)
						EndIf
						
						shooting = False
						If pDeployState < g\Deploy_Time Then
							ChangeGunFrames(g,g\Anim_Deploy,False)
							If prevFrame# < (g\Anim_Deploy\x+1) And AnimTime(g\obj) >= (g\Anim_Deploy\x+1) Then
								PlayGunSound(g\name+"\deploy",1,1,False)
							EndIf
						Else
							If pShootState = 0.0 Then
								If AnimTime(g\obj) > g\Anim_Shoot\x And AnimTime(g\obj) < g\Anim_Shoot\y-0.5 Then
									ChangeGunFrames(g,g\Anim_Shoot,False)
								Else
									SetAnimTime(g\obj,g\Frame_Idle)
								EndIf
								pReloadState = 0.0
							Else
								ChangeGunFrames(g,g\Anim_Shoot,False)
								If Ceil(AnimTime(g\obj)) = g\Anim_Shoot\x Then
									PlayGunSound(g\name+"\throw",1,1,True)
								EndIf
								
								If pShootState >= g\ShootDelay And pReloadState = 0.0 Then
									pReloadState = 1.0
								EndIf
								If pShootState >= g\Rate_Of_Fire-fps\Factor[0] And pPressMouse1 Then
									SetAnimTime g\obj,g\Anim_Shoot\x
									pReloadState = 0.0
								EndIf
							EndIf
						EndIf
						
						If (Not shooting) Then
							g_I\GunAnimFLAG = False
						Else
							g_I\GunAnimFLAG = True
						EndIf
						;[End Block]
					Case GUNTYPE_OICW
						;[Block]
						If (Not g\ChangeFiremode) Then
							
							If pAmmo=0 Then
								If MouseHit1 And shootCondition Then
									pPressMouse1=True
									pPressReload=False
								EndIf
							Else
								If MouseDown1 And shootCondition Then
									pPressMouse1=True
									pPressReload=False
								EndIf
							EndIf
							
							If (pAmmo = 0 And pShootState > 0.0) Lor pReloadState > 0.0 Lor pIsPlayerSprinting Then
								pPressMouse1=False
							EndIf
							If pShootState = 0.0 And pPressMouse1 And pAmmo > 0 And pDeployState >= g\Deploy_Time Then
								SetAnimTime(g\obj,g\Frame_Idle)
							EndIf
							
							If pDeployState < g\Deploy_Time Lor pAmmo > g\MaxCurrAmmo Lor pReloadState = 0.0 Then
								pPressReload=False
							EndIf
							If pReloadAmmo = 0 Then
								pPressReload=False
							EndIf
							
							If KeyHit(KEY_RELOAD) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
								If pReloadState = 0.0 And pAmmo <= g\MaxCurrAmmo And pReloadAmmo > 0 Then
									pPressReload=True
									DeselectIronSight()
								EndIf
							EndIf
							
							If KeyHit(KEY_CHANGEFIREMODE) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
								If (Not pReloadState > 0.0) Then
									g\ChangeFiremode = True
									PlayGunSound("change_fire_semi",1,1,False)
								EndIf
							EndIf
							
							shooting = False
							If pDeployState < g\Deploy_Time Then
								ChangeGunFrames(g,g\Anim_Deploy,False)
								If prevFrame# < (g\Anim_Deploy\x+1) And AnimTime(g\obj) >= (g\Anim_Deploy\x+1) Then
									PlayGunSound(g\name+"\deploy",1,1,False)
								EndIf
							Else
								If pReloadState = 0.0 Then
									If pShootState = 0.0 Then
										If AnimTime(g\obj) > g\Anim_Shoot\x And AnimTime(g\obj) < g\Anim_Shoot\y-0.5 Then
											ChangeGunFrames(g,g\Anim_Shoot,False)
										Else
											If pIsPlayerSprinting Then
												pPressMouse1 = False
												pPressReload = False
												
												If AnimTime(g\obj)<=(g\Anim_Sprint_Transition\y-0.5) Lor AnimTime(g\obj)>(g\Anim_Sprint_Cycle\y) Then
													ChangeGunFrames(g,g\Anim_Sprint_Transition,False)
												Else
													ChangeGunFrames(g,g\Anim_Sprint_Cycle,True)
												EndIf
											Else
												If AnimTime(g\obj)>(g\Anim_Sprint_Transition\x+0.5) And AnimTime(g\obj)<=g\Anim_Sprint_Cycle\y Then
													ChangeGunFrames(g,g\Anim_Sprint_Transition,False,True)
												Else
													SetAnimTime(g\obj,g\Frame_Idle)
												EndIf
											EndIf
										EndIf
										
										If pPressMouse1 And pAmmo = 0 Then
											PlayGunSound(g\name+"\shoot_empty",1,1,False)
										EndIf
									Else
										ChangeGunFrames(g,g\Anim_Shoot,False)
										If Ceil(AnimTime(g\obj)) = g\Anim_Shoot\x Then
											If (Not NTF_NoRecoil) Then
												If (Not g\HasSuppressor) Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													CameraShake = g\Knockback/2.0
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback
													g_I\GunLightTimer = fps\Factor[0]
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[14],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												Else
													PlayGunSound(g\name,g\MaxShootSilencedSounds,0,True)
													CameraShake = g\Knockback/2.5
													User_Camera_Pitch = User_Camera_Pitch - g\Knockback/2.5
												EndIf
												If pAmmo = 0 And pReloadAmmo = 0 Then
													If gc\CurrGamemode <> 3 Then
														If hds\Wearing Then
															If (Not hds\isBroken) Then
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
															Else
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
															EndIf
														EndIf
													EndIf
												EndIf
											Else
												If (Not g\HasSuppressor) Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
													g_I\GunLightTimer = fps\Factor[0]
													ShowEntity g\MuzzleFlash
													EntityTexture g\MuzzleFlash,ParticleTextures[14],Rand(0,3)
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												Else
													PlayGunSound(g\name,g\MaxShootSilencedSounds,0,True)
												EndIf
											EndIf
											shooting = True
										EndIf
										If pShootState >= g\Rate_Of_Fire-fps\Factor[0] And pPressMouse1 Then
											SetAnimTime g\obj,g\Anim_Shoot\x
										EndIf
									EndIf
								Else
									If pAmmo = 0 Then
										If AnimTime(g\obj)>g\Anim_Reload_Empty\y Lor AnimTime(g\obj)<g\Anim_Reload_Empty\x Then
											SetAnimTime(g\obj,g\Anim_Reload_Empty\x)
										EndIf
										ChangeGunFrames(g,g\Anim_Reload_Empty,False)
										If prevFrame# < (g\Anim_Reload_Empty\x+1) And AnimTime(g\obj) >= (g\Anim_Reload_Empty\x+1) Then
											PlayGunSound(g\name+"\reload_empty",g\MaxReloadSounds,1,False)
										ElseIf prevFrame# < (g\Anim_Reload_Empty\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload_Empty\y-0.5) Then
											pPressReload = False
										EndIf
									Else
										If AnimTime(g\obj)>g\Anim_Reload\y Lor AnimTime(g\obj)<g\Anim_Reload\x Then
											SetAnimTime(g\obj,g\Anim_Reload\x)
										EndIf
										ChangeGunFrames(g,g\Anim_Reload,False)
										If prevFrame# < (g\Anim_Reload\x+1) And AnimTime(g\obj) >= (g\Anim_Reload\x+1) Then
											PlayGunSound(g\name+"\reload",g\MaxReloadSounds,1,False)
										ElseIf prevFrame# < (g\Anim_Reload\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload\y-0.5) Then
											pPressReload = False
										EndIf
									EndIf
									pIronSight = False
								EndIf
							EndIf
							
						Else
							
							If MouseHit1 And shootCondition Then
								pPressMouse1=True
								pPressReload=False
							EndIf
							
							If (pAmmo2 = 0 And pShootState > 0.0) Lor pReloadState > 0.0 Lor pIsPlayerSprinting Then
								pPressMouse1=False
							EndIf
							If pShootState = 0.0 And pPressMouse1 And pAmmo2 > 0 And pDeployState >= g\Deploy_Time Then
								SetAnimTime(g\obj,g\Frame_Idle)
							EndIf
							
							If pDeployState < g\Deploy_Time Lor pAmmo2 = g\MaxCurrAltAmmo Lor pReloadState = 0.0 Then
								pPressReload=False
							EndIf
							If pReloadAmmo2 = 0 Then
								pPressReload=False
							EndIf
							
							If KeyHit(KEY_RELOAD) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
								If pReloadState = 0.0 And pAmmo2 < g\MaxCurrAltAmmo And pReloadAmmo2 > 0 Then
									pPressReload=True
									DeselectIronSight()
								EndIf
							EndIf
							
							If KeyHit(KEY_CHANGEFIREMODE) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
								If (Not pReloadState > 0.0) Then
									g\ChangeFiremode = False
									PlayGunSound("change_fire_auto",1,1,False)
								EndIf
							EndIf
							
							shooting = False
							If pDeployState < g\Deploy_Time Then
								ChangeGunFrames(g,g\Anim_Deploy,False)
								If prevFrame# < (g\Anim_Deploy\x+1) And AnimTime(g\obj) >= (g\Anim_Deploy\x+1) Then
									PlayGunSound(g\name+"\deploy",1,1,False)
								EndIf
							Else
								If pReloadState = 0.0 Then
									If pShootState = 0.0 Then
										If AnimTime(g\obj) > g\Anim_Shoot_Alt\x And AnimTime(g\obj) < g\Anim_Shoot_Alt\y-0.5 Then
											ChangeGunFrames(g,g\Anim_Shoot_Alt,False)
										Else
											If pIsPlayerSprinting Then
												pPressMouse1 = False
												pPressReload = False
												
												If AnimTime(g\obj)<=(g\Anim_Sprint_Transition\y-0.5) Lor AnimTime(g\obj)>(g\Anim_Sprint_Cycle\y) Then
													ChangeGunFrames(g,g\Anim_Sprint_Transition,False)
												Else
													ChangeGunFrames(g,g\Anim_Sprint_Cycle,True)
												EndIf
											Else
												If AnimTime(g\obj)>(g\Anim_Sprint_Transition\x+0.5) And AnimTime(g\obj)<=g\Anim_Sprint_Cycle\y Then
													ChangeGunFrames(g,g\Anim_Sprint_Transition,False,True)
												Else
													SetAnimTime(g\obj,g\Frame_Idle)
												EndIf
											EndIf
										EndIf
										
										If pPressMouse1 And pAmmo2 = 0 Then
											PlayGunSound(g\name+"\shoot_empty",1,1,False)
										EndIf
									Else
										ChangeGunFrames(g,g\Anim_Shoot_Alt,False)
										If Ceil(AnimTime(g\obj)) = g\Anim_Shoot_Alt\x Then
											If (Not NTF_NoRecoil) Then
												PlayGunSound(g\name+"\grenade_shot",1,1,True)
												CameraShake = g\Knockback/2.0
												User_Camera_Pitch = User_Camera_Pitch - g\Knockback*1.4
												g_I\GunLightTimer = fps\Factor[0]
												If pAmmo2 = 0 And pReloadAmmo2 = 0 Then
													If gc\CurrGamemode <> 3 Then
														If hds\Wearing Then
															If (Not hds\isBroken) Then
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
															Else
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
															EndIf
														EndIf
													EndIf
												EndIf
											Else
												PlayGunSound(g\name+"\grenade_shot",1,1,True)
												g_I\GunLightTimer = fps\Factor[0]
											EndIf
											shooting = True
										EndIf
										If pShootState >= g\Rate_Of_Fire-fps\Factor[0] And pPressMouse1 Then
											SetAnimTime g\obj,g\Anim_Shoot_Alt\x
										EndIf
									EndIf
								Else
									If AnimTime(g\obj)>g\Anim_Reload_Alt\y Lor AnimTime(g\obj)<g\Anim_Reload_Alt\x Then
										SetAnimTime(g\obj,g\Anim_Reload_Alt\x)
									EndIf
									ChangeGunFrames(g,g\Anim_Reload_Alt,False)
									If prevFrame# < (g\Anim_Reload_Alt\x+1) And AnimTime(g\obj) >= (g\Anim_Reload_Alt\x+1) Then
										PlayGunSound(g\name+"\reload_empty",g\MaxReloadSounds,1,False)
									ElseIf prevFrame# < (g\Anim_Reload_Alt\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload_Alt\y-0.5) Then
										pPressReload = False
									EndIf
									pIronSight = False
								EndIf
							EndIf
							
						EndIf
						
						If (Not pIsPlayerSprinting) And (Not shooting) Then
							g_I\GunAnimFLAG = False
						Else
							g_I\GunAnimFLAG = True
						EndIf
						;[End Block]
					Case GUNTYPE_EMRP
						;[Block]
						If MouseHit1 And shootCondition Then
							pPressMouse1=True
							pPressReload=False
						EndIf
						
						If g\HasMUI Then
							
							If (Not g\ChangeFiremode) Then
								 
								RotateEntity g\MUIObj,90,0,90
								RotateEntity g\MUIScreenObj,90,0,90
								
								;PositionEntity g\MUIScreenObj,g\MUIPos2\x,g\MUIPos2\y,g\MUIPos2\z
								;PositionEntity g\MUIObj,g\MUIPos2\x,g\MUIPos2\y,g\MUIPos2\z
								
								If KeyHit(KEY_CHANGEFIREMODE) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
									If (Not pReloadState > 0.0) Then
										g\ChangeFiremode = True
										PlayGunSound("change_fire_semi",1,1,False)
									EndIf
								EndIf
								
							ElseIf g\ChangeFiremode Then
								
								RotateEntity g\MUIObj,90,0,0
								RotateEntity g\MUIScreenObj,90,0,0
								
								;PositionEntity g\MUIScreenObj,g\MUIPos\x,g\MUIPos\y,g\MUIPos\z
								;PositionEntity g\MUIObj,g\MUIPos\x,g\MUIPos\y,g\MUIPos\z
								
								If KeyHit(KEY_CHANGEFIREMODE) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
									If (Not pReloadState > 0.0) Then
										g\ChangeFiremode = False
										PlayGunSound("change_fire_auto",1,1,False)
									EndIf
								EndIf
								
							EndIf
							
						EndIf
						
						If (pAmmo = 0 And pShootState > 0.0) Lor pReloadState > 0.0 Lor pIsPlayerSprinting Then
							pPressMouse1=False
						EndIf
						
						If pShootState = 0.0 And pPressMouse1 And pAmmo > 0 And pDeployState >= g\Deploy_Time Then
							SetAnimTime(g\obj,g\Frame_Idle)
						EndIf
						
						If pDeployState < g\Deploy_Time Lor pAmmo > g\MaxCurrAmmo Lor pReloadState = 0.0 Then
							pPressReload=False
						EndIf
						If pReloadAmmo = 0 Then
							pPressReload=False
						EndIf
						
						If KeyHit(KEY_RELOAD) And (Not MenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen)) And (Not IsModerationOpen()) Then
							If pReloadState = 0.0 And pAmmo <= g\MaxCurrAmmo And pReloadAmmo > 0 Then
								pPressReload=True
								DeselectIronSight()
								pAmmo = pAmmo
							EndIf
						EndIf
						
						shooting = False
						If pDeployState < g\Deploy_Time Then
							If pAmmo = 0 Then
								ChangeGunFrames(g,g\Anim_NoAmmo_Deploy,False)
							Else
								ChangeGunFrames(g,g\Anim_Deploy,False)
							EndIf
							If prevFrame# < (g\Anim_Deploy\x+1) And AnimTime(g\obj) >= (g\Anim_Deploy\x+1) Then
								PlayGunSound(g\name+"\deploy",1,1,False)
							EndIf
						Else
							If pReloadState = 0.0 Then
								If pShootState = 0.0 Then
									If AnimTime(g\obj) > g\Anim_Shoot\x And AnimTime(g\obj) < g\Anim_Shoot\y-0.5 Then
										ChangeGunFrames(g,g\Anim_Shoot,False)
									ElseIf AnimTime(g\obj) > g\Anim_NoAmmo_Shoot\x And AnimTime(g\obj) < g\Anim_NoAmmo_Shoot\y-0.5 Then
										ChangeGunFrames(g,g\Anim_NoAmmo_Shoot,False)
									Else
										If pIsPlayerSprinting Then
											pPressMouse1 = False
											pPressReload = False
											
											If pAmmo = 0 Then
												If AnimTime(g\obj)<=(g\Anim_NoAmmo_Sprint_Transition\y-0.5) Lor AnimTime(g\obj)>(g\Anim_NoAmmo_Sprint_Cycle\y) Then
													ChangeGunFrames(g,g\Anim_NoAmmo_Sprint_Transition,False)
												Else
													ChangeGunFrames(g,g\Anim_NoAmmo_Sprint_Cycle,True)
												EndIf
											Else
												If AnimTime(g\obj)<=(g\Anim_Sprint_Transition\y-0.5) Lor AnimTime(g\obj)>(g\Anim_Sprint_Cycle\y) Then
													ChangeGunFrames(g,g\Anim_Sprint_Transition,False)
												Else
													ChangeGunFrames(g,g\Anim_Sprint_Cycle,True)
												EndIf
											EndIf
										Else
											If pAmmo = 0 Then
												If AnimTime(g\obj)>(g\Anim_NoAmmo_Sprint_Transition\x+0.5) And AnimTime(g\obj)<=g\Anim_NoAmmo_Sprint_Cycle\y Then
													ChangeGunFrames(g,g\Anim_NoAmmo_Sprint_Transition,False,True)
												Else
													SetAnimTime(g\obj,g\Frame_NoAmmo_Idle)
												EndIf
											Else
												If AnimTime(g\obj)>(g\Anim_Sprint_Transition\x+0.5) And AnimTime(g\obj)<=g\Anim_Sprint_Cycle\y Then
													ChangeGunFrames(g,g\Anim_Sprint_Transition,False,True)
												Else
													SetAnimTime(g\obj,g\Frame_Idle)
												EndIf
											EndIf
										EndIf
									EndIf
									
									If pPressMouse1 And pAmmo = 0 Then
										PlayGunSound(g\name+"\shoot_empty",1,1,False)
									EndIf
								Else
									If pAmmo = 0 Then
										ChangeGunFrames(g,g\Anim_NoAmmo_Shoot,False)
										If Ceil(AnimTime(g\obj)) = g\Anim_NoAmmo_Shoot\x Then
											If BigCameraShake <= (g\Knockback/2)-fps\Factor[0]-0.05 Then
												;g\ShootTimer = 0
												;g\ShootTimer = g\ShootTimer + fps\Factor[0]
												If pAmmo > 0 Then
													PlayGunSound(g\name,g\MaxShootSounds,0,True)
												Else
													PlayGunSound(g\name+"\shoot_last",1,1,False)
												EndIf
												BigCameraShake = g\Knockback/2.0
												User_Camera_Pitch = User_Camera_Pitch - g\Knockback
												;g_I\GunLightTimer = fps\Factor[0]
												g_I\GunLightTimer = fps\Factor[0]/2
												ShowEntity g\MuzzleFlash
												EntityTexture g\MuzzleFlash,ParticleTextures[18],Rand(0,3)
												TurnEntity g\MuzzleFlash,0,0,Rnd(360)
												ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												
												If pAmmo = 0 And pReloadAmmo = 0 Then
													If gc\CurrGamemode <> 3 Then
														If hds\Wearing Then
															If (Not hds\isBroken) Then
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
															Else
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
																;If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[23])
															EndIf
														EndIf
													EndIf
												EndIf
											EndIf
											;g\ShootTimer = 0
											shooting = True
										EndIf
									Else
										ChangeGunFrames(g,g\Anim_Shoot,False)
										If Ceil(AnimTime(g\obj)) = g\Anim_Shoot\x Then
											If (Not NTF_NoRecoil) Then
												PlayGunSound(g\name,g\MaxShootSounds,0,True)
												BigCameraShake = g\Knockback/2.0
												User_Camera_Pitch = User_Camera_Pitch - g\Knockback
												;g_I\GunLightTimer = fps\Factor[0]
												g_I\GunLightTimer = fps\Factor[0]/2
												ShowEntity g\MuzzleFlash
												EntityTexture g\MuzzleFlash,ParticleTextures[18],Rand(0,3)
												TurnEntity g\MuzzleFlash,0,0,Rnd(360)
												ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												
												If pAmmo = 0 And pReloadAmmo = 0 Then
													If g\HasMUI Then
														;EntityTexture g\MUIScreenObj,MUI_Interface,4
													EndIf
													If gc\CurrGamemode <> 3 Then
														If hds\Wearing Then
															If (Not hds\isBroken) Then
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
															Else
																If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[22])
																;If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[23])
															EndIf
														EndIf
													EndIf
												EndIf
											Else
												PlayGunSound(g\name,g\MaxShootSounds,0,True)
												;g_I\GunLightTimer = fps\Factor[0]
												g_I\GunLightTimer = fps\Factor[0]/2
												ShowEntity g\MuzzleFlash
												EntityTexture g\MuzzleFlash,ParticleTextures[18],Rand(0,3)
												TurnEntity g\MuzzleFlash,0,0,Rnd(360)
												ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												
											EndIf
											shooting = True
										EndIf
									EndIf
									If pShootState >= g\Rate_Of_Fire-fps\Factor[0] And pPressMouse1 Then
										SetAnimTime g\obj,g\Anim_Shoot\x
									EndIf
								EndIf
							Else					
								If pAmmo = 0 Then
									If AnimTime(g\obj)>g\Anim_Reload_Empty\y Lor AnimTime(g\obj)<g\Anim_Reload_Empty\x Then
										SetAnimTime(g\obj,g\Anim_Reload_Empty\x)
									EndIf
									ChangeGunFrames(g,g\Anim_Reload_Empty,False)
									If prevFrame# < (g\Anim_Reload_Empty\x+1) And AnimTime(g\obj) >= (g\Anim_Reload_Empty\x+1) Then
										PlayGunSound(g\name+"\reload_empty",g\MaxReloadSounds,1,False)
									ElseIf prevFrame# < (g\Anim_Reload_Empty\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload_Empty\y-0.5) Then
										pPressReload = False
									EndIf
								Else
									If AnimTime(g\obj)>g\Anim_Reload\y Lor AnimTime(g\obj)<g\Anim_Reload\x Then
										SetAnimTime(g\obj,g\Anim_Reload\x)
									EndIf
									ChangeGunFrames(g,g\Anim_Reload,False)
									If prevFrame# < (g\Anim_Reload\x+1) And AnimTime(g\obj) >= (g\Anim_Reload\x+1) Then
										PlayGunSound(g\name+"\reload",g\MaxReloadSounds,1,False)
									ElseIf prevFrame# < (g\Anim_Reload\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload\y-0.5) Then
										pPressReload = False
									EndIf
								EndIf
								pIronSight = False
							EndIf
						EndIf
						
						If (Not pIsPlayerSprinting) And (Not shooting) Then
							g_I\GunAnimFLAG = False
						Else
							g_I\GunAnimFLAG = True
						EndIf
						;[End Block]
				End Select
				Select g_I\HoldingGun
					Case 15, 19 ; ~ XM29 and EMR-P
						;[Block]
						If g_I\HoldingGun = g\ID
							If (Not g\HasAcog) Then
								AddAcog(g)
								g\HasAcog = True
							EndIf
						Else
							g\Deployed = 0
						EndIf
						;[End Block]
				End Select
			EndIf
			
			Local pAmmoDelta%, pAmmoDelta2%
			
			If (Not isMultiplayer) And g\ID = pHoldingGun Then
				If pDeployState < g\Deploy_Time Then
					pDeployState = pDeployState + fps\Factor[0]
				Else
					If g\GunType = GUNTYPE_SHOTGUN Then
						If (pPressMouse1 And pShootState = 0.0) Then
							If pAmmo > 0 Then
								For j=1 To g\Amount_Of_Bullets
									ShootGun(g)
								Next
								pAmmo = pAmmo - 1
								pShootState = fps\Factor[0]
								pReloadState = 0.0
								pPressReload = False
							EndIf
						EndIf
						If pShootState > 0.0 And pShootState < g\Rate_Of_Fire Then
							pShootState = pShootState + fps\Factor[0]
							pReloadState = 0.0
							pPressReload = False
						Else
							pShootState = 0.0
						EndIf
						If pAmmo = g\MaxCurrAmmo Lor pReloadState > 0.0 Then
							pPressReload = False
						EndIf
						If pPressReload Then
							pReloadState = fps\Factor[0]
						EndIf
						
						If pReloadState > 0.0 And pReloadState < g\Reload_Start_Time Then
							pShootState = 0.0
							pReloadState = pReloadState + fps\Factor[0]
							If pReloadState >= g\Reload_Start_Time Then
								pAmmo = pAmmo + 1
								pReloadAmmo = pReloadAmmo - 1
							EndIf
						ElseIf pReloadState >= g\Reload_Start_Time And pReloadState < (g\Reload_Start_Time+g\Reload_Empty_Time) Then
							pReloadState = pReloadState + fps\Factor[0]
							If pReloadState >= (g\Reload_Start_Time+g\Reload_Empty_Time) Then
								If pAmmo < g\MaxCurrAmmo And pReloadAmmo > 0 Then
									pReloadState = g\Reload_Start_Time
									pAmmo = pAmmo + 1
									pReloadAmmo = pReloadAmmo - 1
								EndIf
							EndIf
						Else
							pReloadState = 0.0
						EndIf
					ElseIf g\GunType = GUNTYPE_MELEE Then
						pPressReload = False
						
						If (pPressMouse1 And pShootState = 0.0) Then
							pShootState = fps\Factor[0]
						EndIf
						If pShootState > 0.0 And pShootState < g\Rate_Of_Fire Then
							pShootState = pShootState + fps\Factor[0]
							If pShootState >= g\ShootDelay And pShootState <= g\ShootDelay+fps\Factor[0] Then
								For i=1 To g\Amount_Of_Bullets
									ShootGun(g)
								Next
							EndIf
						Else
							pShootState = 0.0
						EndIf
					ElseIf g\GunType = GUNTYPE_GRENADE Then
						pPressReload = False
						
						If (pPressMouse1 And pShootState = 0.0) Then
							pShootState = fps\Factor[0]
						EndIf
						If pShootState > 0.0 And pShootState < g\Rate_Of_Fire Then
							pShootState = pShootState + fps\Factor[0]
							If pShootState >= g\ShootDelay And pShootState <= g\ShootDelay+fps\Factor[0] Then
								For i=1 To g\Amount_Of_Bullets
									If gc\CurrGamemode <> 3 Then
										CreateGrenade(EntityX(Camera),EntityY(Camera),EntityZ(Camera), EntityPitch(Camera), EntityYaw(Camera))
										If (Not NTF_InfiniteAmmo) Then DropGrenade()
									EndIf
								Next
							EndIf
						Else
							pShootState = 0.0
						EndIf
					ElseIf g\GunType = GUNTYPE_OICW
						If (Not g\ChangeFiremode) Then
							If pReloadState = 0.0 Then
								If pPressMouse1 Lor pShootState = -1.0 Then
									If pShootState = 0.0 Lor pShootState = -1.0 Then
										If pAmmo > 0 Then
											For j=1 To g\Amount_Of_Bullets
												ShootGun(g)
											Next
											If (Not NTF_NoReload) Then pAmmo = pAmmo - 1
											pShootState = fps\Factor[0]
											pPressReload = False
										EndIf
									EndIf
								EndIf
								If pShootState > 0.0 And pShootState < g\Rate_Of_Fire Then
									pShootState = pShootState + fps\Factor[0]
									pPressReload = False
								Else
									pShootState = 0.0
								EndIf
								If pAmmo > g\MaxCurrAmmo Then
									pPressReload = False
								EndIf
								If pPressReload Then
									pReloadState = fps\Factor[0]
								EndIf
							ElseIf pReloadState > 0.0 And pReloadState < g\Reload_Empty_Time And pAmmo = 0 Then
								pShootState = 0.0
								pReloadState = pReloadState + fps\Factor[0]
							ElseIf pReloadState > 0.0 And pReloadState < g\Reload_Time And pAmmo > 0 Then
								pShootState = 0.0
								pReloadState = pReloadState + fps\Factor[0]
							Else
								pReloadState = 0.0
								pAmmoDelta% = g\MaxCurrAmmo - pAmmo
								If pReloadAmmo <> 0 Then
									If pAmmo = 0 Then
										pAmmo = pAmmo + Min(pAmmoDelta, pReloadAmmo)
									Else
										pAmmo = pAmmo + Min(pAmmoDelta, pReloadAmmo)+1
										pReloadAmmo = pReloadAmmo - 1
									EndIf
								Else
									pAmmo = pAmmo + Min(pAmmoDelta, pReloadAmmo)
								EndIf
								If (Not NTF_InfiniteAmmo) Then pReloadAmmo = Max(pReloadAmmo - pAmmoDelta, 0)
								If NTF_NoReload Then pAmmo = g\MaxCurrAmmo
							EndIf
						Else
							If pReloadState = 0.0 Then
								If pPressMouse1 Lor pShootState = -1.0 Then
									If pShootState = 0.0 Lor pShootState = -1.0 Then
										If pAmmo2 > 0 Then
											For j=1 To g\Amount_Of_Bullets
												CreateGrenadeLauncherGrenade(EntityX(Camera),EntityY(Camera),EntityZ(Camera), EntityPitch(Camera), EntityYaw(Camera))
											Next
											If (Not NTF_NoReload) Then pAmmo2 = pAmmo2 - 1
											pShootState = fps\Factor[0]
											pPressReload = False
										EndIf
									EndIf
								EndIf
								If pShootState > 0.0 And pShootState < g\Rate_Of_Fire Then
									pShootState = pShootState + fps\Factor[0]
									pPressReload = False
								Else
									pShootState = 0.0
								EndIf
								If pAmmo2 > g\MaxCurrAltAmmo Then
									pPressReload = False
								EndIf
								If pPressReload Then
									pReloadState = fps\Factor[0]
								EndIf
							ElseIf pReloadState > 0.0 And pReloadState < g\Reload_Empty_Time And pAmmo2 = 0 Then
								pShootState = 0.0
								pReloadState = pReloadState + fps\Factor[0]
							ElseIf pReloadState > 0.0 And pReloadState < g\Reload_Time And pAmmo2 > 0 Then
								pShootState = 0.0
								pReloadState = pReloadState + fps\Factor[0]
							Else
								pReloadState = 0.0
								Local pAmmoDelta3% = g\MaxCurrAltAmmo - pAmmo2
								pAmmo2 = pAmmo2 + Min(pAmmoDelta3, pReloadAmmo2)
								If (Not NTF_InfiniteAmmo) Then pReloadAmmo2 = Max(pReloadAmmo2 - pAmmoDelta3, 0)
								If NTF_NoReload Then pAmmo2 = g\MaxCurrAltAmmo
							EndIf
						EndIf
					ElseIf  g\GunType = GUNTYPE_RIFLE Then
						If pReloadState = 0.0 Then
							If pPressMouse1 Lor pShootState = -1.0 Then
								If pShootState = 0.0 Lor pShootState = -1.0 Then
									If pAmmo > 0 Then
										For j=1 To g\Amount_Of_Bullets
											ShootGun(g)
										Next
										If (Not NTF_NoReload) Then pAmmo = pAmmo - 1
										pShootState = fps\Factor[0]
										pPressReload = False
									EndIf
								EndIf
							EndIf
							If pShootState > 0.0 And pShootState < g\Rate_Of_Fire Then
								pShootState = pShootState + fps\Factor[0]
								pPressReload = False
							Else
								pShootState = 0.0
							EndIf
							If pAmmo > g\MaxCurrAmmo Then
								pPressReload = False
							EndIf
							If pPressReload Then
								pReloadState = fps\Factor[0]
							EndIf
						ElseIf pReloadState > 0.0 And pReloadState < g\Reload_Empty_Time And pAmmo = 0 Then
							pShootState = 0.0
							pReloadState = pReloadState + fps\Factor[0]
						ElseIf pReloadState > 0.0 And pReloadState < g\Reload_Time And pAmmo > 0 Then
							pShootState = 0.0
							pReloadState = pReloadState + fps\Factor[0]
						Else
							pReloadState = 0.0
							pAmmoDelta% = g\MaxCurrAmmo - pAmmo
							If pReloadAmmo <> 0 Then
									If pAmmo = 0 Then
										pAmmo = pAmmo + Min(pAmmoDelta, pReloadAmmo)
									Else
										pAmmo = pAmmo + Min(pAmmoDelta, pReloadAmmo)+1
										pReloadAmmo = pReloadAmmo - 1
									EndIf
							Else
								pAmmo = pAmmo + Min(pAmmoDelta, pReloadAmmo)
							EndIf
							If (Not NTF_InfiniteAmmo) Then pReloadAmmo = Max(pReloadAmmo - pAmmoDelta, 0)
							If NTF_NoReload Then pAmmo = g\MaxCurrAmmo
						EndIf
					Else
						If g\GunType = GUNTYPE_EMRP Then
							If pAmmo > 0 Then
								If pShootState = 0.0 Then
									g\ChargeTimer = 1.26*70
								ElseIf pShootState > 0.8*70 Then
									g\ChargeTimer = 0
								ElseIf pShootState < 0.8*70 Then
									g\ChargeTimer = 2
								EndIf
								If g\ChargeTimer = 0 Then
									g\ChargeTimer2 = g\ChargeTimer2 + fps\Factor[0]
								ElseIf g\ChargeTimer = 2 Then
									g\ChargeTimer2 = 2
								EndIf
								If g\HasMUI Then
									EntityTexture g\MUIScreenObj,MUI_Interface,2
								EndIf
							ElseIf pAmmo = 0 Then
								If pReloadState > 2.13*70 Then
									g\ChargeTimer2 = g\ChargeTimer2 + fps\Factor[0]
								ElseIf pReloadState = 0 Then
									g\ChargeTimer = 0
									g\ChargeTimer2 = 0
								EndIf
							EndIf
						EndIf
						If pReloadState = 0.0 Then
							If pPressMouse1 Lor pShootState = -1.0 Then
								If pShootState = 0.0 Lor pShootState = -1.0 Then
									If pAmmo > 0 Then
										For j=1 To g\Amount_Of_Bullets
											ShootGun(g)
										Next
										If (Not NTF_NoReload) Then pAmmo = pAmmo - 1
										pShootState = fps\Factor[0]
										pPressReload = False
										If g\GunType = GUNTYPE_EMRP Then
											g\ChargeTimer = 2
											
											If g\HasMUI Then
												EntityTexture g\MUIScreenObj,MUI_Interface,3
											EndIf
											
										EndIf
									EndIf
								EndIf
							EndIf
							If pShootState > 0.0 And pShootState < g\Rate_Of_Fire Then
								pShootState = pShootState + fps\Factor[0]
								pPressReload = False
							Else
								pShootState = 0.0
							EndIf
							If pAmmo > g\MaxCurrAmmo Then
								pPressReload = False
							EndIf
							If pPressReload Then
								pReloadState = fps\Factor[0]
							EndIf
						ElseIf pReloadState > 0.0 And pReloadState < g\Reload_Empty_Time And pAmmo = 0 Then
							pShootState = 0.0
							pReloadState = pReloadState + fps\Factor[0]
						ElseIf pReloadState > 0.0 And pReloadState < g\Reload_Time And pAmmo > 0 Then
							pShootState = 0.0
							pReloadState = pReloadState + fps\Factor[0]
						Else
							pReloadState = 0.0
							pAmmoDelta% = g\MaxCurrAmmo - pAmmo
							If pReloadAmmo <> 0 Then
								If g_I\HoldingGun <> 9 Then
									If pAmmo = 0 Then
										pAmmo = pAmmo + Min(pAmmoDelta, pReloadAmmo)
									Else
										pAmmo = pAmmo + Min(pAmmoDelta, pReloadAmmo)+1
										pReloadAmmo = pReloadAmmo - 1
									EndIf
								Else
									pAmmo = pAmmo + Min(pAmmoDelta, pReloadAmmo)
								EndIf
							Else
								pAmmo = pAmmo + Min(pAmmoDelta, pReloadAmmo)
							EndIf
							If (Not NTF_InfiniteAmmo) Then pReloadAmmo = Max(pReloadAmmo - pAmmoDelta, 0)
							If NTF_NoReload Then pAmmo = g\MaxCurrAmmo
						EndIf
					EndIf
				EndIf
			EndIf
		Next
	EndIf
	If isMultiplayer Then
		Players[mp_I\PlayerID]\WeaponInSlot[Players[mp_I\PlayerID]\SelectedSlot] = pHoldingGun
		Players[mp_I\PlayerID]\DeployState = pDeployState
		Players[mp_I\PlayerID]\ReloadState = pReloadState
		Players[mp_I\PlayerID]\ShootState = pShootState
		Players[mp_I\PlayerID]\PressMouse1 = pPressMouse1
		Players[mp_I\PlayerID]\PressReload = pPressReload
		If Players[mp_I\PlayerID]\SelectedSlot < (MaxSlots - SlotsWithNoAmmo) Then
			Players[mp_I\PlayerID]\Ammo[Players[mp_I\PlayerID]\SelectedSlot] = pAmmo
			Players[mp_I\PlayerID]\ReloadAmmo[Players[mp_I\PlayerID]\SelectedSlot] = pReloadAmmo
		EndIf
		Players[mp_I\PlayerID]\IsPlayerSprinting = pIsPlayerSprinting
		Players[mp_I\PlayerID]\IronSight = pIronSight
	Else
		g_I\HoldingGun = pHoldingGun
		psp\DeployState = pDeployState
		psp\ReloadState = pReloadState
		psp\ShootState = pShootState
		If currGun <> Null Then
			currGun\CurrAmmo = pAmmo
			currGun\CurrReloadAmmo = pReloadAmmo
			currGun\CurrAltAmmo = pAmmo2
			currGun\CurrReloadAltAmmo = pReloadAmmo2
		EndIf
		IsPlayerSprinting = pIsPlayerSprinting
	EndIf
	g_I\IronSight = pIronSight
	
End Function

Function ToggleGuns()
	Local g.Guns
	Local i%, j%
	Local case1%, case2%
	Local GunInInventory%
	Local KeyPressed%[MaxGunSlots]
	Local KeyPressedHolster% = KeyHit(KEY_HOLSTERGUN)
	
	For i = 0 To MaxGunSlots-1
		If co\Enabled Then
			KeyPressed[i] = GetDPadButtonPress()
		Else
			KeyPressed[i] = KeyHit(i + 2)
		EndIf
	Next
	
	If KillTimer >= 0 And CanPlayerUseGuns And fps\Factor[0] > 0.0 And (Not g_I\IronSight) Then
		For i = 0 To MaxGunSlots-1
			If co\Enabled Then
				case1 = -1
				case2 = KeyPressed[i]
			Else
				case1 = KeyPressed[i]
				case2 = False
			EndIf
			
			If (case1 = -1 And case2 = Int(WrapAngle(180 + (90 * i)))) Lor (case1 And (Not case2)) Then
				If g_I\Weapon_InSlot[i] <> "" Then
					g_I\GunChangeFLAG = False
					For g = Each Guns
						If g\name = g_I\Weapon_InSlot[i] Then
							g_I\HoldingGun = g\ID
							Exit
						EndIf
					Next
					g_I\Weapon_CurrSlot = (i + 1)
					mpl\SlotsDisplayTimer = 70*3
				EndIf
			EndIf
		Next
		
		If (Not co\Enabled) Then
			case1 = KeyPressedHolster
		EndIf
		If (case1 = -1 And case2 = 90) Lor (case1 And (Not case2)) Lor FallTimer < 0 Then
			For g.Guns = Each Guns
				If g\ID = g_I\HoldingGun Then
					PlayGunSound(g\name$+"\holster",1,1,False)
				EndIf
			Next
			g_I\GunChangeFLAG = False
			g_I\HoldingGun = 0
			g_I\Weapon_CurrSlot = 0
			mpl\SlotsDisplayTimer = 0
		EndIf
		
		For i = 0 To MaxGunSlots-1
			GunInInventory = False
			For j = 0 To MaxItemAmount-1
				If Inventory[j] <> Null And Inventory[j]\itemtemplate\tempname = g_I\Weapon_InSlot[i] Then
					GunInInventory = True
					Exit
				EndIf
			Next
			If (Not GunInInventory) Then
				For g = Each Guns
					If g\name = g_I\Weapon_InSlot[i] And g\ID = g_I\HoldingGun Then
						g_I\GunChangeFLAG = False
						g_I\HoldingGun = 0
						g_I\Weapon_CurrSlot = 0
						mpl\SlotsDisplayTimer = 0
						PlayGunSound(g\name$+"\holster",1,1,False)
						Exit
					EndIf
				Next
				g_I\Weapon_InSlot[i] = ""
			EndIf
		Next
	EndIf
	
End Function

Function AnimateGuns()
	
	If (Not g_I\GunAnimFLAG) And (CurrSpeed=0.0 Lor mi_I\EndingTimer>0.0) And (Not IsPlayerSprinting%) And (Not g_I\IronSight)
		If GunPivot_YSide%=0
			If GunPivot_Y# > -0.005
				GunPivot_Y# = GunPivot_Y# - (0.00005*fps\Factor[0])
			Else
				GunPivot_Y# = -0.005
				GunPivot_YSide% = 1
			EndIf
		Else
			If GunPivot_Y# < 0.0
				GunPivot_Y# = GunPivot_Y# + (0.00005*fps\Factor[0])
			Else
				GunPivot_Y# = 0.0
				GunPivot_YSide% = 0
			EndIf
		EndIf
		
		If GunPivot_X# < 0.0
			GunPivot_X# = Min(GunPivot_X#+(0.0001*fps\Factor[0]),0.0)
		ElseIf GunPivot_X# > 0.0
			GunPivot_X# = Max(GunPivot_X#-(0.0001*fps\Factor[0]),0.0)
		EndIf
	ElseIf (Not g_I\GunAnimFLAG) And CurrSpeed<>0.0 And (Not IsPlayerSprinting%) And (Not g_I\IronSight) And (Not mi_I\EndingTimer>0.0)
		If GunPivot_XSide%=0
			If GunPivot_X# > -0.0025
				GunPivot_X# = GunPivot_X# - (0.000075/(1.0 + Crouch)*fps\Factor[0])
				If GunPivot_X# > -0.00125
					GunPivot_Y# = Min(GunPivot_Y#+(0.000125/(1.0 + Crouch)*fps\Factor[0]),0.001)
				Else
					GunPivot_Y# = Max(GunPivot_Y#-(0.000125/(1.0 + Crouch)*fps\Factor[0]),0.0)
				EndIf
			Else
				GunPivot_X# = -0.0025
				GunPivot_Y# = 0.0
				GunPivot_XSide% = 1
			EndIf
		Else
			If GunPivot_X# < 0.0
				GunPivot_X# = GunPivot_X# + (0.000075/(1.0 + Crouch)*fps\Factor[0])
				If GunPivot_X# < -0.00125
					GunPivot_Y# = Min(GunPivot_Y#+(0.000125/(1.0 + Crouch)*fps\Factor[0]),0.001)
				Else
					GunPivot_Y# = Max(GunPivot_Y#-(0.000125/(1.0 + Crouch)*fps\Factor[0]),0.0)
				EndIf
			Else
				GunPivot_X# = 0.0
				GunPivot_Y# = 0.0
				GunPivot_XSide% = 0
			EndIf
		EndIf
	Else
		If GunPivot_Y# < 0.0
			GunPivot_Y# = Max(GunPivot_Y#+(0.0001*fps\Factor[0]),0.0)
		Else
			GunPivot_Y# = 0.0
		EndIf
		
		If GunPivot_X# < 0.0
			GunPivot_X# = Min(GunPivot_X#+(0.0001*fps\Factor[0]),0.0)
		ElseIf GunPivot_X# > 0.0
			GunPivot_X# = Max(GunPivot_X#-(0.0001*fps\Factor[0]),0.0)
		EndIf
	EndIf
	
	PositionEntity g_I\GunPivot,EntityX(Camera), EntityY(Camera)+GunPivot_Y#, EntityZ(Camera)
	MoveEntity g_I\GunPivot,GunPivot_X#,0,0
	
End Function

Function ChangeGunFrames(g.Guns,anim.Vector3D,loop%=True,reverse%=False)
	Local newTime#,temp%
	
	;x = Start frame
	;y = End frame
	;z = Speed
	
	Local speed# = anim\z
	If reverse Then speed=-anim\z
	
	If speed > 0.0 Then
		newTime = Max(Min(AnimTime(g\obj) + speed * fps\Factor[0],anim\y),anim\x)
		
		If loop And newTime => anim\y Then
			newTime = anim\x
		EndIf
	Else
		Local anim_start% = anim\x
		Local anim_end% = anim\y
		If anim\x < anim\y Then
			anim_start = anim\y
			anim_end = anim\x
		EndIf
		
		If loop Then
			newTime = AnimTime(g\obj) + speed * fps\Factor[0]
			
			If newTime < anim_end Then 
				newTime = anim_start
			ElseIf newTime > anim_start Then
				newTime = anim_end
			EndIf
		Else
			newTime = Max(Min(AnimTime(g\obj) + speed * fps\Factor[0],anim_start),anim_end)
		EndIf
	EndIf
	SetAnimTime g\obj,newTime
	
End Function

Function ShootPlayer(x#,y#,z#,hitProb#=1.0,particles%=True,damage%=10)
	Local pvt,i,tempchn%,de.Decals
	
	; ~ Muzzle flash
	
	Local p.Particles = CreateParticle(x,y,z, 1, Rnd(0.08,0.1), 0.0, 5)
	TurnEntity p\obj, 0,0,Rnd(360)
	p\Achange = -0.15
	
	LightVolume = TempLightVolume*1.2
	
	If (Not GodMode) Then 
		
		If Rnd(1.0)=<hitProb Then
			TurnEntity Camera, Rnd(-3,3), Rnd(-3,3), 0
			
			If I_1033RU\HP = 0
				DamageSPPlayer(damage)
			Else
				Damage1033RU(35 + 5 * SelectedDifficulty\AggressiveNPCs)
			EndIf
			
			mpl\DamageTimer = 70
			
			If gc\CurrGamemode <> 3 Then
				If (Not hds\Wearing) Then
					PlaySound_Strict BullethitSFX
				Else
					PlaySound_Strict(LoadTempSound("SFX\General\BulletImpact"+(Rand(1,5))+".ogg"))
				EndIf
			Else
				PlaySound_Strict BullethitSFX
			EndIf
			
			If hds\Wearing And hds\Health < 35 Lor (Not hds\Wearing) Then
				PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Collider)
			EndIf
			
			If gc\CurrGamemode <> 3 Then
				If (Not hds\Wearing) Then
					If (Not I_1079\Foam) Then
						
						;PlaySound_Strict(BloodSFX(Rand(0,1)))
						
						Local pvt2 = CreatePivot()
						PositionEntity pvt2, EntityX(Collider)+Rnd(-0.05,0.05),EntityY(Collider)-0.05,EntityZ(Collider)+Rnd(-0.05,0.05)
						TurnEntity pvt2, 90, 0, 0
						EntityPick(pvt2,0.3)
						de.Decals = CreateDecal(DECAL_BLOODDROP1, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
						de\Size = Rnd(0.1,0.2) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
						ChannelVolume tempchn, Rnd(0.0,0.8)*opt\SFXVolume
						ChannelPitch tempchn, Rand(20000,30000)
						
						FreeEntity pvt2
					Else
						
						PlaySound_Strict(SizzSFX(Rand(0,1)))
						
						pvt2 = CreatePivot()
						PositionEntity pvt2, EntityX(Collider)+Rnd(-0.05,0.05),EntityY(Collider)-0.05,EntityZ(Collider)+Rnd(-0.05,0.05)
						TurnEntity pvt2, 90, 0, 0
						EntityPick(pvt2,0.3)
						de.Decals = CreateDecal(DECAL_1079, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
						de\Size = Rnd(0.1,0.2) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
						de.Decals = CreateDecal(DECAL_1079, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
						de\Size = Rnd(0.06,0.1) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
						de.Decals = CreateDecal(DECAL_1079, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
						de\Size = Rnd(0.2,0.25) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
						de.Decals = CreateDecal(DECAL_1079, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
						de\Size = Rnd(0.3,0.31) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
						ChannelVolume tempchn, Rnd(0.0,0.8)*opt\SFXVolume
						ChannelPitch tempchn, Rand(20000,30000)
						
						FreeEntity pvt2
					EndIf
				EndIf
				
				pvt2 = CreatePivot()
				PositionEntity pvt2, EntityX(Collider)+Rnd(-0.05,0.05),EntityY(Collider)-0.05,EntityZ(Collider)+Rnd(-0.05,0.05)
				TurnEntity pvt2, 90, 0, 0
				EntityPick(pvt2,0.3)
				de.Decals = CreateDecal(DECAL_BLOODDROP1, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
				de\Size = Rnd(0.1,0.2) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
				ChannelVolume tempchn, Rnd(0.0,0.8)*opt\SFXVolume
				ChannelPitch tempchn, Rand(20000,30000)
				
				FreeEntity pvt2
				
			EndIf
			
		ElseIf particles
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(Collider),(EntityY(Collider)+EntityY(Camera))/2,EntityZ(Collider)
			PointEntity pvt, p\obj
			TurnEntity pvt, 0, 180, 0
			
			EntityPick(pvt, 2.5)
			
			If PickedEntity() <> 0 Then 
				PlaySound2(Gunshot3SFX, Camera, pvt, 0.4, Rnd(0.8,1.0))
				
				If particles Then 
					;dust/smoke particles
					p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
					p\speed = 0.001
					p\SizeChange = 0.003
					p\A = 0.8
					p\Achange = -0.01
					RotateEntity p\pvt, EntityPitch(pvt)-180, EntityYaw(pvt),0
					
					For i = 0 To Rand(2,3)
						p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.006, 0.003, 80)
						p\speed = 0.02
						p\A = 0.8
						p\Achange = -0.01
						RotateEntity p\pvt, EntityPitch(pvt)+Rnd(170,190), EntityYaw(pvt)+Rnd(-10,10),0	
					Next
					
					;bullet hole decal
					de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BULLETHOLE), PickedX(),PickedY(),PickedZ(), 0,0,0)
					AlignToVector de\obj,-PickedNX(),-PickedNY(),-PickedNZ(),3
					MoveEntity de\obj, 0,0,-0.001
					EntityFX de\obj, 1+8
					de\fx = 1+8
					de\lifetime = 70*20
					EntityBlend de\obj, 2
					de\blendmode = 2
					de\Size = Rnd(0.028,0.034)
					ScaleSprite de\obj, de\Size, de\Size
				EndIf				
			EndIf
			
			FreeEntity pvt
			
		EndIf
		
	EndIf
	
End Function

Function ShootTarget(x#,y#,z#,n.NPCs,hitProb#=1.0,particles%=True,damage%=10)
	Local pvt%,i%,de.Decals
	
	;muzzle flash
	Local p.Particles = CreateParticle(x,y,z, 1, Rnd(0.08,0.1), 0.0, 5)
	TurnEntity p\obj, 0,0,Rnd(360)
	p\Achange = -0.15
	
	If Rnd(1.0)=<hitProb Then
		p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 5, 0.06, 0.2, 80)
		p\speed = 0.001
		p\SizeChange = 0.003
		p\A = 0.8
		p\Achange = -0.02
		
		If n\Target <> Null
			n\Target\HP% = n\Target\HP% - damage%
		EndIf
		
		Local tempchn%
		
		Local pvt2 = CreatePivot()
		PositionEntity pvt2, EntityX(n\Target\Collider)+Rnd(-0.05,0.05),EntityY(n\Target\Collider)-0.05,EntityZ(n\Target\Collider)+Rnd(-0.05,0.05)
		TurnEntity pvt2, 90, 0, 0
		EntityPick(pvt2,0.3)
		de.Decals = CreateDecal(DECAL_BLOODDROP1, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
		de\Size = Rnd(0.1,0.2) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
		ChannelVolume tempchn, Rnd(0.0,0.8)*opt\SFXVolume
		ChannelPitch tempchn, Rand(20000,30000)
		
		FreeEntity pvt2
		
	ElseIf particles Then
		pvt = CreatePivot()
		PositionEntity pvt, EntityX(Collider),(EntityY(Collider)+EntityY(Camera))/2,EntityZ(Collider)
		PointEntity pvt, p\obj
		TurnEntity pvt, 0, 180, 0
		
		EntityPick(pvt, 2.5)
		
		If PickedEntity() <> 0 Then 
			PlaySound2(Gunshot3SFX, Camera, pvt, 0.4, Rnd(0.8,1.0))
			
			If particles Then 
				;dust/smoke particles
				p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
				p\speed = 0.001
				p\SizeChange = 0.003
				p\A = 0.8
				p\Achange = -0.01
				RotateEntity p\pvt, EntityPitch(pvt)-180, EntityYaw(pvt),0
				
				For i = 0 To Rand(2,3)
					p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.006, 0.003, 80)
					p\speed = 0.02
					p\A = 0.8
					p\Achange = -0.01
					RotateEntity p\pvt, EntityPitch(pvt)+Rnd(170,190), EntityYaw(pvt)+Rnd(-10,10),0	
				Next
				;bullet hole decal
				de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BULLETHOLE), PickedX(),PickedY(),PickedZ(), 0,0,0)
				AlignToVector de\obj,-PickedNX(),-PickedNY(),-PickedNZ(),3
				MoveEntity de\obj, 0,0,-0.001
				EntityFX de\obj, 1+8
				de\fx = 1+8
				de\lifetime = 70*20
				EntityBlend de\obj, 2
				de\blendmode = 2
				de\Size = Rnd(0.028,0.034)
				ScaleSprite de\obj, de\Size, de\Size
			EndIf				
		EndIf
		
		FreeEntity pvt
	EndIf
	
End Function

Function ShootGun(g.Guns)
	Local temp,n.NPCs,p.Particles,j,de.Decals,ent_pick%,i%
	Local hitNPC.NPCs,wa.Water,hitwater.Water,d.Decals
	
	IsPlayerShooting% = True
	
	If g\GunType <> GUNTYPE_MELEE And g\GunType <> GUNTYPE_GRENADE And g\GunType <> GUNTYPE_SCP_127 And (Not g\HasSuppressor) Then
		LightVolume = TempLightVolume*1.2
		ShowEntity g_I\GunLight
		g_I\GunLightTimer = fps\Factor[0]
	EndIf
	
	If g\GunType <> GUNTYPE_SHOTGUN Then
		If (Not g_I\IronSight)
			RotateEntity GunPickPivot,Rnd(-g\Accuracy,g\Accuracy)/(1.0+(3.0*g_I\IronSight)),Rnd(-g\Accuracy,g\Accuracy)/(1.0+(3.0*g_I\IronSight)),0
		Else
			RotateEntity GunPickPivot,0,0,0
		EndIf
	Else
		RotateEntity GunPickPivot,Rnd(-g\Accuracy,g\Accuracy)/(1.0+(3.0*g_I\IronSight)),Rnd(-g\Accuracy,g\Accuracy)/(1.0+(3.0*g_I\IronSight)),0
	EndIf
	
	HideEntity Head
	If g\Range<=0.0 Then
		EntityPick GunPickPivot,10000.0
	Else
		EntityPick GunPickPivot,g\Range
	EndIf
	
	ent_pick% = PickedEntity()
	If ent_pick%<>0 Then
		For n.NPCs = Each NPCs
			If n\NPCtype = NPCtype106 Then
				;If g_I\HoldingGun = 19 Then
				If g_I\HoldingGun = g\ID And g\GunType = GUNTYPE_EMRP Then
					n\State = 70 * 60 * Rand(10,13)
					TranslateEntity(n\Collider,0,-10.0,0,True)
				EndIf
			EndIf
			For j = 0 To 24
				If ent_pick% = n\HitBox1[j] Then
					
					If n\BloodTimer > 0 And n\BloodTimer < 70*5 Then
						n\BloodTimer = n\BloodTimer + fps\Factor[0]/2
					Else
						n\BloodTimer = -1
					EndIf
					
					;If g_I\HoldingGun = 19 Lor g_I\HoldingGun = 9 Lor g_I\HoldingGun = 16 Then
					If g_I\HoldingGun = g\ID And g\GunType = GUNTYPE_SHOTGUN Lor g\GunType = GUNTYPE_EMRP Then
						If n\Boss <> n Then
							n\HP = 0
						Else
							If (Not g\HasSuppressor) Then
								n\HP = n\HP - g\DamageOnEntity*4
							Else
								n\HP = n\HP - g\DamageOnEntity*3
							EndIf
						EndIf
					Else
						If (Not g\HasSuppressor) Then
							n\HP = n\HP - g\DamageOnEntity*4
						Else
							n\HP = n\HP - g\DamageOnEntity*3
						EndIf
					EndIf
					hitNPC = n
					n\GotHit = True
					;If g_I\HoldingGun = 19 Lor g_I\HoldingGun = 9 Lor g_I\HoldingGun = 16 Then
					If g_I\HoldingGun = g\ID And g\GunType = GUNTYPE_SHOTGUN Lor g\GunType = GUNTYPE_EMRP Then
						n\GotHeadShot = True
						If n\Boss <> n Then
							PlaySound2(LoadTempSound("SFX\SCP\1048A\Explode.ogg"),Camera,n\Collider)
							p.Particles = CreateParticle(EntityX(n\Collider),EntityY(n\Collider)+0.8,EntityZ(n\Collider),5,0.25,0.0)
							EntityColor p\obj,100,100,100
							RotateEntity p\pvt,0,0,Rnd(360)
							p\Achange = -Rnd(0.02,0.03)
							For i = 0 To 1
								p.Particles = CreateParticle(EntityX(n\Collider)+Rnd(-0.2,0.2),EntityY(n\Collider)+0.85,EntityZ(n\Collider)+Rnd(-0.2,0.2),5,0.15,0.0)
								EntityColor p\obj,100,100,100
								RotateEntity p\pvt,0,0,Rnd(360)
								p\Achange = -Rnd(0.02,0.03)
							Next
							
							Local bonename$ = GetNPCManipulationValue(n\NPCNameInSection,"Head","bonename",0)
							Local headbone% = FindChild(n\obj,bonename$)
							
							If bonename$ <> ""
								If n\BloodTimer > 0 Then
									p.Particles = CreateParticle(EntityX(headbone,True), EntityY(headbone,True), EntityZ(headbone,True),5, 1, 0.5, 150)
									EntityParent p\obj,headbone
									If n\GotHeadShot Then
										ScaleEntity headbone%, 0, 0, 0
									EndIf
								EndIf
							EndIf
						EndIf
					EndIf
					Exit
				EndIf
				If ent_pick% = n\HitBox2[j] Then
					If (Not g\HasSuppressor) Then
						n\HP = n\HP - g\DamageOnEntity
					Else
						n\HP = n\HP - g\DamageOnEntity/1.5
					EndIf
					hitNPC = n
					n\GotHit = True
					Exit
				EndIf
				If ent_pick% = n\HitBox3[j] Then
					If (Not g\HasSuppressor) Then
						n\HP = n\HP - (g\DamageOnEntity/2)
					Else
						n\HP = n\HP - (g\DamageOnEntity/3)
					EndIf
					hitNPC = n
					n\GotHit = True
					Exit
				EndIf
			Next
			If hitNPC <> Null Then Exit
		Next
		For wa.Water = Each Water
			If ent_pick% = wa\obj Then
				hitwater = wa
				PlaySound2(LoadTempSound("SFX\General\Water_Splash.ogg"),Camera,wa\obj,50)
				
				p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 11, 0.2, 0.2, 80)
				p\speed = 0.001
				p\SizeChange = 0.003
				p\A = 0.8
				p\Achange = -0.02
				RotateEntity p\obj,0,0,0
			EndIf
			If hitwater <> Null Then Exit
		Next
	EndIf
	
	If hitNPC <> Null Then
		p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 5, 0.06, 0.2, 80)
		p\speed = 0.001
		p\SizeChange = 0.003
		p\A = 0.8
		p\Achange = -0.02
		
		If g\GunType = GUNTYPE_MELEE Then
			PlayGunSound(g\name+"\hitbody",g\MaxShootSounds,0,True,True)
		EndIf
	ElseIf ent_pick <> 0 Then
		p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
		p\speed = 0.001
		p\SizeChange = 0.003
		p\A = 0.8
		p\Achange = -0.01
		RotateEntity p\pvt, EntityPitch(g_I\GunPivot)-180, EntityYaw(g_I\GunPivot),0
		
		If g\GunType <> GUNTYPE_MELEE And g\GunType <> GUNTYPE_GRENADE Then
			PlaySound2(Gunshot3SFX, Camera, p\pvt, 0.4, Rnd(0.8,1.0))
		ElseIf g\GunType = GUNTYPE_MELEE
			PlayGunSound(g\name+"\hitwall",g\MaxWallhitSounds,0,True,True)
		EndIf
		
		For i = 0 To Rand(2,3)
			p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.006, 0.003, 80)
			p\speed = 0.02
			p\A = 0.8
			p\Achange = -0.01
			RotateEntity p\pvt, EntityPitch(g_I\GunPivot)+Rnd(170,190), EntityYaw(g_I\GunPivot)+Rnd(-10,10),0	
		Next
		
		Local DecalID% = 0
		Select g\DecalType
			Case GUNDECAL_DEFAULT
				DecalID = DECAL_TYPE_BULLETHOLE
			Case GUNDECAL_SLASH
				DecalID = DECAL_TYPE_SLASHHOLE
		End Select
		
		de.Decals = CreateDecal(GetRandomDecalID(DecalID), PickedX(),PickedY(),PickedZ(), 0,0,0)
		AlignToVector de\obj,-PickedNX(),-PickedNY(),-PickedNZ(),3
		MoveEntity de\obj, 0,0,-0.001
		EntityFX de\obj, 1+8
		de\fx = 1+8
		de\lifetime = 70*20
		EntityBlend de\obj, 2
		de\blendmode = 2
		de\Size = Rnd(0.028,0.034)
		ScaleSprite de\obj, de\Size, de\Size
		EntityParent de\obj,ent_pick
		
	EndIf
	
End Function

Function DropGrenade()
	Local n%
	
	For n = 0 To MaxItemAmount-1
		If Inventory[n] <> Null Then
			If Inventory[n]\itemtemplate\tempname = "grenade" Then
				RemoveItem(Inventory[n])
				Exit
			EndIf
		EndIf
	Next
	
End Function

Function CreateGrenade(x#, y#, z#, pitch#, yaw#)
	Local gr.Grenades = New Grenades
	Local g.Guns
	
	IsPlayerShooting% = True
	
	gr\obj = CopyEntity(Grenade_Model)
	ScaleEntity gr\obj, 0.012,0.012, 0.012
	PositionEntity gr\obj, x,y,z
	RotateEntity gr\obj, pitch, yaw, 45
	EntityType gr\obj, HIT_GRENADE
	EntityRadius gr\obj, 0.03
	gr\Speed = 0.12
	gr\XSpeed = 0.008
	gr\Angle = yaw
	
	gr\Gun = g
	
End Function

Function UpdateGrenades()
	Local PrevX#, PrevY#, PrevZ#
	Local pivot%,pvt%,de.Decals,e.Emitters
	Local gr.Grenades
	Local n.NPCs, i%
	
	For gr.Grenades = Each Grenades
		PrevX = EntityX(gr\obj)
		PrevY = EntityY(gr\obj)
		PrevZ = EntityZ(gr\obj)
		If gr\Speed > 0.01 Then
			If CountCollisions(gr\obj) <> 0 Then
				If CollisionNZ(gr\obj, 1) = 0 And gr\Prevfloor = False Then
					
					If CollisionNZ(gr\obj, 1) = 0 Then
						If Abs(CollisionNY(gr\obj, 1)) = 1 Then ; ~ Jump
							RotateEntity gr\obj, FlipAngle(EntityPitch(gr\obj)), EntityYaw(gr\obj), EntityRoll(gr\obj)
						Else
							RotateEntity gr\obj, FlipAngle(EntityPitch(gr\obj)), FlipAngle(EntityYaw(gr\obj)), EntityRoll(gr\obj)
						EndIf
					EndIf
					gr\Speed = gr\Speed*0.65 ; ~ Reducing speed if collided
				Else
					gr\Prevfloor = True
				EndIf
			Else
				If CountCollisions(gr\obj) <> 0 Then
					If Abs(CollisionNY(gr\obj, 1)) = 1 Then ; ~ Friction
						gr\Speed = gr\Speed-0.001*fps\Factor[0]
					EndIf
				EndIf
				MoveEntity gr\obj, 0, 0, gr\Speed*fps\Factor[0]
				TranslateEntity gr\obj, 0, -0.01*fps\Factor[0], 0
				If EntityPitch(gr\obj) < 90 Then 
					RotateEntity gr\obj, WrapAngle(EntityPitch(gr\obj)+0.8*fps\Factor[0]), EntityYaw(gr\obj), WrapAngle(EntityRoll(gr\obj)+(gr\Speed*24)*fps\Factor[0])
				Else
					RotateEntity gr\obj, EntityPitch(gr\obj), EntityYaw(gr\obj), WrapAngle(EntityRoll(gr\obj)+(gr\Speed*24)*fps\Factor[0])
				EndIf
			EndIf
		Else
			If CountCollisions(gr\obj) <> 0 Then 
				AlignToVector(gr\obj, CollisionNX(gr\obj, 1), CollisionNY(gr\obj, 1), CollisionNZ(gr\obj, 1), 2)
				RotateEntity(gr\obj, EntityPitch(gr\obj), EntityYaw(gr\obj), 90)
			EndIf
		EndIf
		If Distance3(PrevX, PrevY, PrevZ, EntityX(gr\obj), EntityY(gr\obj), EntityZ(gr\obj)) <= 0.026 Then
			gr\Speed = gr\Speed-0.001*fps\Factor[0]
		EndIf
		
		gr\Timer = gr\Timer + fps\Factor[0]
		If gr\Timer > 259 Then
			
			Local currsound$ = PlaySound_Strict(LoadTempSound("SFX\Guns\Grenade\Explode"+(Rand(1,3)+".ogg")))
			Local snd_real = LinePick(EntityX(gr\obj),EntityY(gr\obj),EntityZ(gr\obj), 0, 10, 0)
			Local snd_real_player = LinePick(EntityX(Collider),EntityY(Collider),EntityZ(Collider), 0, 10, 0)
			If (Not snd_real) And snd_real_player Then currsound = PlaySound_Strict(LoadTempSound("SFX\Guns\Grenade\Explode"+(Rand(1,3)+".ogg")))
			If (Not snd_real) And (Not snd_real_player) Then currsound = PlaySound_Strict(LoadTempSound("SFX\Guns\Grenade\Explode"+(Rand(1,3)+".ogg")))
			If gc\CurrGamemode <> 3 Then
				If EntityVisible(Camera, gr\obj) Then
					If EntityDistanceSquared(Collider, gr\obj) < PowTwo(4.0) Then
						If (Not GodMode) Then
							DamageSPPlayer(10-EntityDistance(Collider, gr\obj))
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","grenade_death",Designation)
						EndIf
					EndIf
					If EntityDistanceSquared(Collider, gr\obj) < PowTwo(0.5) Then
						If (Not hds\Wearing) Then
							If (Not GodMode) Then
								Kill()
								m_msg\DeathTxt = GetLocalStringR("Singleplayer","grenade_death",Designation)
							EndIf
						Else
							If hds\Health <= 90 And hds\Health > 89 Lor hds\Health <= 86 And hds\Health > 85 Then
								If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[1])
							EndIf
							If hds\Health <= 50 And hds\Health > 49 Lor hds\Health <= 46 And hds\Health > 45 Then
								If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[2])
							EndIf
							If hds\Health <= 35 And hds\Health > 34 Lor hds\Health <= 30 And hds\Health > 29 Then
								If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[3])
							EndIf
							If hds\Health <= 20 And hds\Health > 19 Lor hds\Health <= 15 And hds\Health > 14 Then
								If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[4])
							EndIf
						EndIf
					EndIf
				EndIf
				For n.NPCs = Each NPCs
					If n\NPCtype = NPCtypeCI Then
						If EntityDistanceSquared(gr\obj,n\Collider) =< PowTwo(1.6) Then
							n\State = CI_TAKE_COVER
						EndIf
					EndIf
					If (Not n\IsNPCThrownGrenade) Then
						If EntityDistanceSquared(n\Collider,gr\obj) < PowTwo(4.0) Then
							If n\HP > 0 Then
								n\HP = n\HP - (80-(EntityDistance(n\Collider, gr\obj))/2)
							EndIf
						EndIf
					EndIf
				Next
			Else
				For i = 0 To mp_I\MaxPlayers - 1
					If Players[i] <> Null Then
						If EntityVisible(Players[i]\Collider, gr\obj) Then
							If EntityDistance(Players[i]\Collider, gr\obj) < 6 Then
								DamagePlayer(i,50-EntityDistance(Players[i]\Collider, gr\obj),50,5)
							EndIf
							If EntityDistance(Players[i]\Collider, gr\obj) < 0.5 Then
								DamagePlayer(i,100-EntityDistance(Players[i]\Collider, gr\obj),100,0)
							EndIf
						EndIf
					EndIf
				Next
				For n.NPCs = Each NPCs
					If EntityDistanceSquared(n\Collider,gr\obj) < PowTwo(4.0) Then
						If n\HP > 0 Then
							n\HP = n\HP - (80-(EntityDistance(n\Collider, gr\obj))/2)
						EndIf
					EndIf
				Next
			EndIf
			pvt = CreatePivot() 
			PositionEntity pvt, EntityX(gr\obj),EntityY(gr\obj)-0.05,EntityZ(gr\obj)
			TurnEntity pvt, 90, 0, 0
			If EntityPick(pvt,10) <> 0 Then
				de.Decals = CreateDecal(1, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
				de\Size = Rnd(0.5,0.7) : EntityAlpha(de\obj, 1.0) : ScaleSprite de\obj, de\Size, de\Size
			EndIf
			FreeEntity pvt
			
			If EntityVisible(Collider, gr\obj) Then
				CreateExplosionWaveParticle(EntityX(gr\obj),EntityY(gr\obj),EntityZ(gr\obj))
			EndIf
			
			e.Emitters = CreateEmitter(EntityX(gr\obj, True), EntityY(gr\obj, True), EntityZ(gr\obj, True), 5, 25)
			TurnEntity(e\Obj, 90, Rand(180), 0, True)
			e\RandAngle = 5
			e\Speed = 0
			e\SizeChange = 0
			e\Size = 0.5
			e\MinImage = 16
			e\MaxImage = 16
			e\LifeTime = 25
			e\Room = PlayerRoom
			
			BigCameraShake = Max(0, 15-EntityDistance(gr\obj, Collider))
			
			gr\Channel = 0
			
			pivot = CreatePivot()
			PositionEntity pivot, EntityX(gr\obj),EntityY(gr\obj),EntityZ(gr\obj)
			PlaySound2(HitSnd[Rand(0,2)],Camera,pivot,Max(40000, 44100-(EntityDistance(Camera, pivot)*500)))
			FreeEntity gr\obj
			Delete gr
		EndIf
	Next
End Function

Function CreateGrenadeLauncherGrenade(x#, y#, z#, pitch#, yaw#)
	Local lgr.launcherGrenades = New launcherGrenades
	Local g.Guns
	
	IsPlayerShooting% = True
	
	lgr\obj = CopyEntity(Grenade2_Model)
	ScaleEntity lgr\obj, 0.012,0.012, 0.012
	PositionEntity lgr\obj, x,y,z
	RotateEntity lgr\obj, pitch, yaw, 45
	EntityType lgr\obj, HIT_GRENADE
	EntityRadius lgr\obj, 0.03
	lgr\Speed = 0.5
	lgr\XSpeed = 0.008
	lgr\Angle = yaw
	
	lgr\Gun = g
	
End Function

Function UpdateGrenadeLauncherGrenades()
	Local PrevX#, PrevY#, PrevZ#
	Local pvt%,de.Decals,e.Emitters
	Local lgr.launcherGrenades
	Local n.NPCs
	
	For lgr.launcherGrenades = Each launcherGrenades
		PrevX = EntityX(lgr\obj)
		PrevY = EntityY(lgr\obj)
		PrevZ = EntityZ(lgr\obj)
		If lgr\Speed > 0.01 Then
			If CountCollisions(lgr\obj) <> 0 Then
				If CollisionNZ(lgr\obj, 1) = 0 And lgr\Prevfloor = False Then
					
					If CollisionNZ(lgr\obj, 1) = 0 Then
						If Abs(CollisionNY(lgr\obj, 1)) = 1 Then ; ~ Jump
							RotateEntity lgr\obj, FlipAngle(EntityPitch(lgr\obj)), EntityYaw(lgr\obj), EntityRoll(lgr\obj)
							lgr\IsOnGround = True
						Else
							RotateEntity lgr\obj, FlipAngle(EntityPitch(lgr\obj)), FlipAngle(EntityYaw(lgr\obj)), EntityRoll(lgr\obj)
							lgr\IsOnGround = True
						EndIf
					EndIf
					lgr\Speed = lgr\Speed*0.65 ; ~ Reducing speed if collided
					lgr\IsOnGround = True
				Else
					lgr\Prevfloor = True
					lgr\IsOnGround = True
				EndIf
			Else
				If CountCollisions(lgr\obj) <> 0 Then
					If Abs(CollisionNY(lgr\obj, 1)) = 1 Then ; ~ Friction
						lgr\Speed = lgr\Speed-0.001*fps\Factor[0]
					EndIf
				EndIf
				MoveEntity lgr\obj, 0, 0, lgr\Speed*fps\Factor[0]
				TranslateEntity lgr\obj, 0, -0.01*fps\Factor[0], 0
				If EntityPitch(lgr\obj) < 90 Then 
					RotateEntity lgr\obj, WrapAngle(EntityPitch(lgr\obj)+0.8*fps\Factor[0]), EntityYaw(lgr\obj), WrapAngle(EntityRoll(lgr\obj)+(lgr\Speed*24)*fps\Factor[0])
				Else
					RotateEntity lgr\obj, EntityPitch(lgr\obj), EntityYaw(lgr\obj), WrapAngle(EntityRoll(lgr\obj)+(lgr\Speed*24)*fps\Factor[0])
				EndIf
			EndIf
		Else
			If CountCollisions(lgr\obj) <> 0 Then 
				AlignToVector(lgr\obj, CollisionNX(lgr\obj, 1), CollisionNY(lgr\obj, 1), CollisionNZ(lgr\obj, 1), 2)
				RotateEntity(lgr\obj, EntityPitch(lgr\obj), EntityYaw(lgr\obj), 90)
			EndIf
		EndIf
		If Distance3(PrevX, PrevY, PrevZ, EntityX(lgr\obj), EntityY(lgr\obj), EntityZ(lgr\obj)) <= 0.026 Then
			lgr\Speed = lgr\Speed-0.001*fps\Factor[0]
		EndIf
		
		If lgr\IsOnGround = True Then
			
			Local currsound$ = PlaySound_Strict(LoadTempSound("SFX\Guns\Grenade\Explode"+(Rand(1,3)+".ogg")))
			Local snd_real = LinePick(EntityX(lgr\obj),EntityY(lgr\obj),EntityZ(lgr\obj), 0, 10, 0)
			Local snd_real_player = LinePick(EntityX(Collider),EntityY(Collider),EntityZ(Collider), 0, 10, 0)
			If (Not snd_real) And snd_real_player Then currsound = PlaySound_Strict(LoadTempSound("SFX\Guns\Grenade\Explode"+(Rand(1,3)+".ogg")))
			If (Not snd_real) And (Not snd_real_player) currsound = PlaySound_Strict(LoadTempSound("SFX\Guns\Grenade\Explode"+(Rand(1,3)+".ogg")))
				If EntityVisible(Collider, lgr\obj) Then
					If EntityDistance(Collider, lgr\obj) < 6 Then
						If EntityDistance(Collider, lgr\obj) < 6 Then
							If (Not GodMode) Then
								DamageSPPlayer(20-EntityDistance(Collider, lgr\obj))
								m_msg\DeathTxt = GetLocalStringR("Singleplayer","grenade_death",Designation)
							EndIf
						EndIf
						If EntityDistanceSquared(Collider, lgr\obj) < PowTwo(0.5) Then
							If (Not hds\Wearing) Then
								If (Not GodMode) Then
									Kill()
									m_msg\DeathTxt = GetLocalStringR("Singleplayer","grenade_death",Designation)
								EndIf
							Else
								If hds\Health <= 90 And hds\Health > 89 Lor hds\Health <= 86 And hds\Health > 85 Then
									If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[1])
								EndIf
								If hds\Health <= 50 And hds\Health > 49 Lor hds\Health <= 46 And hds\Health > 45 Then
									If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[2])
								EndIf
								If hds\Health <= 35 And hds\Health > 34 Lor hds\Health <= 30 And hds\Health > 29 Then
									If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[3])
								EndIf
								If hds\Health <= 20 And hds\Health > 19 Lor hds\Health <= 15 And hds\Health > 14 Then
									If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[4])
								EndIf
							EndIf
						EndIf
					EndIf
					If EntityDistanceSquared(Collider, lgr\obj) < PowTwo(0.5) Then
						If (Not GodMode) Then
							Kill()
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","grenade_death",Designation)
						EndIf
					EndIf
				EndIf
				For n.NPCs = Each NPCs
					If n\NPCtype = NPCtypeCI Then
						If EntityDistanceSquared(lgr\obj,n\Collider) =< PowTwo(1.6) Then
							n\State = CI_TAKE_COVER
						EndIf
					EndIf
					If EntityDistanceSquared(n\Collider,lgr\obj) < PowTwo(5.0) Then
						If n\IsDead = False Then
							n\HP = n\HP - (100-(EntityDistance(n\Collider, lgr\obj))/2)
						EndIf
					EndIf
				Next
				pvt = CreatePivot() 
				PositionEntity pvt, EntityX(lgr\obj),EntityY(lgr\obj)-0.05,EntityZ(lgr\obj)
				TurnEntity pvt, 90, 0, 0
				If EntityPick(pvt,10) <> 0 Then
					de.Decals = CreateDecal(1, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
					de\Size = Rnd(0.5,0.7) : EntityAlpha(de\obj, 1.0) : ScaleSprite de\obj, de\Size, de\Size
				EndIf
				FreeEntity pvt
				
				If EntityVisible(Collider, lgr\obj) Then
					CreateExplosionWaveParticle(EntityX(lgr\obj),EntityY(lgr\obj),EntityZ(lgr\obj))
				EndIf
				
				e.Emitters = CreateEmitter(EntityX(lgr\obj, True), EntityY(lgr\obj, True), EntityZ(lgr\obj, True), 5, 25)
				TurnEntity(e\Obj, 90, Rand(180), 0, True)
				e\RandAngle = 5
				e\Speed = 0
				e\SizeChange = 0
				e\Size = 0.5
				e\MinImage = 16
				e\MaxImage = 16
				e\LifeTime = 25
				e\Room = PlayerRoom
				
				BigCameraShake = Max(0, 15-EntityDistance(lgr\obj, Collider))
				
				lgr\Channel = 0
				
				FreeEntity lgr\obj
				Delete lgr
			EndIf
		Next
End Function

Function PlayGunSound(name$,max_amount%=1,sfx%=0,pitchshift%=False,custom%=False)
	Local g.Guns, gun.Guns
	
	For g = Each Guns
		If name = g\name Then
			gun = g
			Exit
		EndIf
	Next
	
	If sfx%=0 Then
		If (Not custom) Then
			If (Not g\HasSuppressor) Then
				If max_amount% = 1 Then
					GunSFX = gun\ShootSounds[0]
				Else
					GunSFX = gun\ShootSounds[Rand(0,max_amount%-1)]
				EndIf
			Else
				If max_amount% = 1 Then
					GunSFX = gun\ShootSilencedSounds[0]
				Else
					GunSFX = gun\ShootSilencedSounds[Rand(0,max_amount%-1)]
				EndIf
			EndIf
		Else
			If max_amount% = 1 Then
				GunSFX = LoadSound_Strict("SFX\Guns\"+name$+".ogg")
			Else
				GunSFX = LoadSound_Strict("SFX\Guns\"+name$+Rand(1,max_amount%)+".ogg")
			EndIf
		EndIf
		GunCHN = PlaySound_Strict(GunSFX)
		If pitchshift% Then
			ChannelPitch GunCHN,Rand(38000,43000)
		EndIf
	Else
		If GunSFX2 <> 0 Then FreeSound_Strict GunSFX2 : GunSFX2=0
		If ChannelPlaying(GunCHN2) Then StopChannel(GunCHN2) : GunCHN2 = 0
		If max_amount% = 1 Then
			GunSFX2 = LoadSound_Strict("SFX\Guns\"+name$+".ogg")
		Else
			GunSFX2 = LoadSound_Strict("SFX\Guns\"+name$+Rand(1,max_amount%)+".ogg")
		EndIf
		GunCHN2 = PlaySound_Strict(GunSFX2)
	EndIf
	
End Function

Function UpdateIronSight()
	Local pvt%,g.Guns,hasIronSight%,prevIronSight%
	Local currGun.Guns
	
	If IsPlayerSprinting% Then
		DeselectIronSight()
	EndIf
	
	For g.Guns = Each Guns
		If g\ID = g_I\HoldingGun Then
			If g\GunType<>GUNTYPE_MELEE And g\GunType<>GUNTYPE_GRENADE Then
				hasIronSight% = True
				If g_I\IronSight% Lor g_I\IronSightAnim% Then
					EntityParent g\obj,IronSightPivot2%
				EndIf
				currGun = g
				Exit
			Else
				hasIronSight = False
				Exit
			EndIf
		EndIf
	Next
	
	If (Not hasIronSight) Then
		Return
	EndIf
	
	If g_I\IronSight% Then
		If g_I\IronSightAnim = 2 Then
			If currGun<>Null Then
				If (Not g\HasRedDot) And (Not g\HasAcog) And (Not g\HasAimPoint) And (Not g\HasEoTech) Then
					PositionEntity IronSightPivot%,currGun\IronSightCoords\x,currGun\IronSightCoords\y,currGun\IronSightCoords\z
				ElseIf g\HasRedDot Then
					PositionEntity IronSightPivot%,currGun\RedDotOffset\x,currGun\RedDotOffset\y,currGun\RedDotOffset\z
				ElseIf g\HasAimPoint Then
					PositionEntity IronSightPivot%,currGun\AimPointOffset\x,currGun\AimPointOffset\y,currGun\AimPointOffset\z
				ElseIf g\HasAcog Then
					PositionEntity IronSightPivot%,currGun\AcogOffset\x,currGun\AcogOffset\y,currGun\AcogOffset\z
				ElseIf g\HasEoTech Then
					PositionEntity IronSightPivot%,currGun\EoTechOffset\x,currGun\EoTechOffset\y,currGun\EoTechOffset\z
				EndIf
			Else
				PositionEntity IronSightPivot%,0,0,0
			EndIf
			g_I\IronSightAnim = 1
		EndIf
	Else
		If g_I\IronSightAnim = 2 Then
			PositionEntity IronSightPivot%,0,0,0
			g_I\IronSightAnim = 1
		EndIf
	EndIf
	
	PositionEntity IronSightPivot2%,CurveValue(EntityX(IronSightPivot),EntityX(IronSightPivot2),5.0),CurveValue(EntityY(IronSightPivot),EntityY(IronSightPivot2),5.0),CurveValue(EntityZ(IronSightPivot),EntityZ(IronSightPivot2),5.0)
	If EntityX(IronSightPivot2%) <= 0.001 And EntityX(IronSightPivot2%) >= -0.001 Then
		g_I\IronSightAnim = 0
	EndIf
	If currGun<>Null Then
		If EntityX(IronSightPivot2%) <= currGun\IronSightCoords\x+0.001 And EntityX(IronSightPivot2%) >= currGun\IronSightCoords\x-0.001 Then
			g_I\IronSightAnim = 0
		EndIf
	Else
		g_I\IronSightAnim = 0
	EndIf
	
	If opt\HoldToAim Then
		If (Not g_I\IronSightAnim) And hasIronSight Then
			prevIronSight = g_I\IronSight
			g_I\IronSight% = MouseDown2
			If g_I\IronSight <> prevIronSight Then
				g_I\IronSightAnim = 2
			EndIf
		EndIf
	Else
		If MouseHit2 Then
			If SelectedItem = Null Then
				If (Not g_I\IronSightAnim) And hasIronSight Then
					g_I\IronSight% = (Not g_I\IronSight%)
					g_I\IronSightAnim = 2
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

Function DeselectIronSight()
	Local g.Guns
	
	g_I\IronSight% = 0
	g_I\IronSightAnim% = 0
	PositionEntity IronSightPivot%,0,0,0
	PositionEntity IronSightPivot2%,0,0,0
	For g = Each Guns
		EntityParent g\obj,g_I\GunPivot
	Next
	
End Function

Include "SourceCode\Attachments_Core.bb"

Function GetWeaponMaxCurrAmmo(gunID%)
	Local g.Guns
	
	For g = Each Guns
		If g\ID = gunID Then
			Return g\MaxCurrAmmo
			
			Return g\MaxCurrAltAmmo
			
			Exit
		EndIf
	Next
	
End Function

Function GetWeaponMaxReloadAmmo(gunID%)
	Local g.Guns
	
	For g = Each Guns
		If g\ID = gunID Then
			Return g\MaxReloadAmmo
			
			Return g\MaxReloadAltAmmo
			
			Exit
		EndIf
	Next
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D