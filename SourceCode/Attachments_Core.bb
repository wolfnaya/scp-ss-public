Function AddSuppressor(g.Guns)
	Local g_I.GunInstance = First GunInstance
	Local it.Items
	
	If g <> Null
		If g\CanHaveSuppressor And g\SilencerObj = 0 Then
			; ~ Viewmodel
			g\SilencerObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\Suppressor.b3d",FindChild(g\obj, "att_muzzle"))
			ScaleEntity g\SilencerObj,g\SuppressorSc\x,g\SuppressorSc\y,g\SuppressorSc\z
			RotateEntity g\SilencerObj,g\SuppressorRot\x,g\SuppressorRot\y,g\SuppressorRot\z
			MoveEntity g\SilencerObj,g\SuppressorPos\x,g\SuppressorPos\y,g\SuppressorPos\z
			g\HasSuppressor = True
		EndIf
	EndIf
	
End Function

Function RemoveSuppressor(g.Guns)
	Local g_I.GunInstance = First GunInstance
	
	If g <> Null
		If g\CanHaveSuppressor And g\SilencerObj <> 0 Then
			g\SilencerObj = FreeEntity_Strict(g\SilencerObj)
		EndIf
		g\HasSuppressor = False
	EndIf
	
End Function

Function AddVerticalGrip(g.Guns)
	Local g_I.GunInstance = First GunInstance
	Local it.Items
	
	If g <> Null
		If g\CanHaveVerticalGrip And g\VerticalGripObj = 0 Then
			; ~ Viewmodel
			If g\name = "mp5k" Then
				g\VerticalGripObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\MP5K_Grip.b3d",FindChild(g\obj, "gun"))
				ScaleEntity g\VerticalGripObj,g\VerticalGripSc\x,g\VerticalGripSc\y,g\VerticalGripSc\z
				RotateEntity g\VerticalGripObj,g\VerticalGripRot\x,g\VerticalGripRot\y,g\VerticalGripRot\z
				MoveEntity g\VerticalGripObj,g\VerticalGripPos\x,g\VerticalGripPos\y,g\VerticalGripPos\z
			EndIf
			g\HasVerticalGrip = True
		EndIf
	EndIf
	
End Function

Function RemoveVerticalGrip(g.Guns)
	Local g_I.GunInstance = First GunInstance
	
	If g <> Null
		If g\CanHaveVerticalGrip And g\VerticalGripObj <> 0 Then
			g\VerticalGripObj = FreeEntity_Strict(g\VerticalGripObj)
		EndIf
		g\HasVerticalGrip = False
	EndIf
	
End Function

Function AddExtendedMag(g.Guns)
	Local g_I.GunInstance = First GunInstance
	Local it.Items
	
	If g <> Null
		If g\CanHaveExtMag And (Not g\HasExtMag) Then
			; ~ Viewmodel
			If g\name = "p90" Then
				g\MaxCurrAmmo = g\MaxCurrAmmo + 25
			Else
				g\MaxCurrAmmo = g\MaxCurrAmmo + 15
			EndIf
			g\HasExtMag = True
		EndIf
	EndIf
	
End Function

Function RemoveExtendedMag(g.Guns)
	Local g_I.GunInstance = First GunInstance
	
	If g <> Null
		If g\CanHaveExtMag And g\HasExtMag Then
			If g\name = "p90" Then
				g\MaxCurrAmmo = g\MaxCurrAmmo - 25
			Else
				g\MaxCurrAmmo = g\MaxCurrAmmo - 15
			EndIf
			g\CurrReloadAmmo = g\CurrReloadAmmo + g\CurrAmmo
			;g\CurrReloadAmmo = g\CurrReloadAmmo + Min(g\CurrAmmo, g\MaxReloadAmmo)
			g\CurrAmmo = 0
		EndIf
		g\HasExtMag = False
	EndIf
	
End Function

Function AddStock(g.Guns)
	Local g_I.GunInstance = First GunInstance
	Local it.Items
	
	If g <> Null
		If g\CanHaveStock And g\StockObj = 0 Then
			; ~ Viewmodel
			g\StockObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\MP5_Stock.b3d",FindChild(g\obj, "att_stock"))
			ScaleEntity g\StockObj,g\StockSc\x,g\StockSc\y,g\StockSc\z
			RotateEntity g\StockObj,g\StockRot\x,g\StockRot\y,g\StockRot\z
			MoveEntity g\StockObj,g\StockPos\x,g\StockPos\y,g\StockPos\z
			g\HasStock = True
		EndIf
	EndIf
	
End Function

Function RemoveStock(g.Guns)
	Local g_I.GunInstance = First GunInstance
	
	If g <> Null
		If g\CanHaveStock And g\StockObj <> 0 Then
			g\StockObj = FreeEntity_Strict(g\StockObj)
		EndIf
		g\HasStock = False
	EndIf
	
End Function

Function AddFoldingStock(g.Guns,render%=True)
	Local g_I.GunInstance = First GunInstance
	Local it.Items
	
	If g <> Null
		If g\CanHaveFoldingStock And g\FoldingStockObj = 0 Then
			; ~ Viewmodel
			g\FoldingStockObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\MP5_Folding_Stock.b3d",FindChild(g\obj, "att_stock"))
			ScaleEntity g\FoldingStockObj,g\StockSc\x,g\StockSc\y,g\StockSc\z
			RotateEntity g\FoldingStockObj,g\StockRot\x,g\StockRot\y,g\StockRot\z
			MoveEntity g\FoldingStockObj,g\StockPos\x,g\StockPos\y,g\StockPos\z
			g\HasFoldingStock = True
		EndIf
	EndIf
	
End Function

Function RemoveFoldingStock(g.Guns)
	Local g_I.GunInstance = First GunInstance
	
	If g <> Null
		If g\CanHaveFoldingStock And g\FoldingStockObj <> 0 Then
			g\FoldingStockObj = FreeEntity_Strict(g\FoldingStockObj)
		EndIf
		g\HasFoldingStock = False
	EndIf
	
End Function

Function AddRail(g.Guns)
	Local g_I.GunInstance = First GunInstance
	Local it.Items
	
	If g <> Null
		If (Not g\HasRail) Then
			If g\CanHaveRail And g\RailObj = 0 Then
				; ~ Viewmodel
				g\RailObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\MP5K_rail.b3d",FindChild(g\obj, "att_sight"))
				ScaleEntity g\RailObj,g\RailSc\x,g\RailSc\y,g\RailSc\z
				RotateEntity g\RailObj,g\RailRot\x,g\RailRot\y,g\RailRot\z
				MoveEntity g\RailObj,g\RailPos\x,g\RailPos\y,g\RailPos\z
				g\HasRail = True
			EndIf
		EndIf
	EndIf
	
End Function

Function RemoveRail(g.Guns)
	Local g_I.GunInstance = First GunInstance
	
	If g <> Null
		If g\CanHaveRail And g\RailObj <> 0 Then
			g\RailObj = FreeEntity_Strict(g\RailObj)
		EndIf
		g\HasRail = False
	EndIf
	
End Function

Function AddAimPoint(g.Guns)
	Local g_I.GunInstance = First GunInstance
	Local it.Items
	
	If g <> Null
		If g\CanHaveAimPoint And g\AimPointObj = 0 Then
				; ~ Viewmodel
			g\AimPointObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\AimPoint_Sight.b3d",FindChild(g\obj, "att_sight"))
			ScaleEntity g\AimPointObj,g\AimPointSc\x,g\AimPointSc\y,g\AimPointSc\z
			RotateEntity g\AimPointObj,g\AimPointRot\x,g\AimPointRot\y,g\AimPointRot\z
			MoveEntity g\AimPointObj,g\AimPointPos\x,g\AimPointPos\y,g\AimPointPos\z
			g\HasAimPoint = True
		EndIf
	EndIf
	
End Function

Function RemoveAimPoint(g.Guns)
	Local g_I.GunInstance = First GunInstance
	
	If g <> Null
		If g\CanHaveAimPoint And g\AimPointObj <> 0 Then
			g\AimPointObj = FreeEntity_Strict(g\AimPointObj)
		EndIf
		g\HasAimPoint = False
	EndIf
	
End Function

Function AddEoTech(g.Guns)
	Local g_I.GunInstance = First GunInstance
	Local it.Items
	
	If g <> Null
		If g\CanHaveEoTech And g\EoTechObj = 0 Then
				; ~ Viewmodel
			g\EoTechObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\EoTech_Sight.b3d",FindChild(g\obj, "att_sight"))
			ScaleEntity g\EoTechObj,g\EoTechSc\x,g\EoTechSc\y,g\EoTechSc\z
			RotateEntity g\EoTechObj,g\EoTechRot\x,g\EoTechRot\y,g\EoTechRot\z
			MoveEntity g\EoTechObj,g\EoTechPos\x,g\EoTechPos\y,g\EoTechPos\z
			g\HasEoTech = True
		EndIf
	EndIf
	
End Function

Function RemoveEoTech(g.Guns)
	Local g_I.GunInstance = First GunInstance
	
	If g <> Null
		If g\CanHaveEoTech And g\EoTechObj <> 0 Then
			g\EoTechObj = FreeEntity_Strict(g\EoTechObj)
		EndIf
		g\HasEoTech = False
	EndIf
	
End Function

Function AddRedDot(g.Guns)
	Local g_I.GunInstance = First GunInstance
	Local it.Items
	
	If g <> Null
		If g\CanHaveRedDot And g\SightObj = 0 Then
				; ~ Viewmodel
			g\SightObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\Red_Dot_Sight.b3d",FindChild(g\obj, "att_sight"))
			ScaleEntity g\SightObj,g\RedDotSc\x,g\RedDotSc\y,g\RedDotSc\z
			RotateEntity g\SightObj,g\RedDotRot\x,g\RedDotRot\y,g\RedDotRot\z
			MoveEntity g\SightObj,g\RedDotPos\x,g\RedDotPos\y,g\RedDotPos\z
			g\HasRedDot = True
		EndIf
	EndIf
	
End Function

Function RemoveRedDot(g.Guns)
	Local g_I.GunInstance = First GunInstance
	
	If g <> Null
		If g\CanHaveRedDot And g\SightObj <> 0  Then
			g\SightObj = FreeEntity_Strict(g\SightObj)
		EndIf
		g\HasRedDot = False
	EndIf
	
End Function

Function AddAcog(g.Guns)
	Local g_I.GunInstance = First GunInstance
	Local it.Items
	
	If g <> Null
		If g\CanHaveAcog And (g\ScopeObj = 0 And g\ScopeLenseObj = 0) Then
				; ~ Viewmodel
			If g\name = "xm29" Then
				g\ScopeObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\XM29_Scope.b3d",FindChild(g\obj, "att_sight"))
				g\XM29LenseObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\XM29_Lense.b3d",FindChild(g\obj, "att_sight"))
				ScaleEntity g\ScopeObj,g\AcogSc\x,g\AcogSc\y,g\AcogSc\z
				ScaleEntity g\XM29LenseObj,g\AcogSc\x,g\AcogSc\y,g\AcogSc\z
				RotateEntity g\ScopeObj,g\AcogRot\x,g\AcogRot\y,g\AcogRot\z
				RotateEntity g\XM29LenseObj,g\AcogRot\x,g\AcogRot\y,g\AcogRot\z
				MoveEntity g\ScopeObj,g\AcogPos\x,g\AcogPos\y,g\AcogPos\z
				MoveEntity g\XM29LenseObj,g\AcogPos\x,g\AcogPos\y,g\AcogPos\z
				If gc\CurrGamemode <> 3 Then
					If opt\RenderScope Then
						UsingScope = True
						EntityTexture g\XM29LenseObj,ScopeTexture,0,1
					Else
						EntityAlpha g\XM29LenseObj,0
					EndIf
				Else
					EntityAlpha g\XM29LenseObj,0
				EndIf
				EntityFX g\XM29LenseObj,1
				EntityTexture g\XM29LenseObj,XM29_Interface,0,0
				
			ElseIf g\name = "emr-p" Then
				g\ScopeLenseObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\EMR-P_Lense.b3d",FindChild(g\obj, "gun_lense"))
				ScaleEntity g\ScopeLenseObj,g\AcogSc\x,g\AcogSc\y,g\AcogSc\z
				RotateEntity g\ScopeLenseObj,g\AcogRot\x,g\AcogRot\y,g\AcogRot\z
				MoveEntity g\ScopeLenseObj,g\AcogPos\x,g\AcogPos\y,g\AcogPos\z
				UsingScope = True
				EntityTexture g\ScopeLenseObj,ScopeTexture,0,1
				EntityFX g\ScopeLenseObj,1
				EntityTexture g\ScopeLenseObj,XM29_Interface,0,0
			Else
				g\ScopeObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\Acog.b3d",FindChild(g\obj, "att_sight"))
				g\ScopeLenseObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\Acog_Lense.b3d",FindChild(g\obj, "att_sight"))
				ScaleEntity g\ScopeObj,g\AcogSc\x,g\AcogSc\y,g\AcogSc\z
				ScaleEntity g\ScopeLenseObj,g\AcogSc\x,g\AcogSc\y,g\AcogSc\z
				RotateEntity g\ScopeObj,g\AcogRot\x,g\AcogRot\y,g\AcogRot\z
				RotateEntity g\ScopeLenseObj,g\AcogRot\x,g\AcogRot\y,g\AcogRot\z
				MoveEntity g\ScopeObj,g\AcogPos\x,g\AcogPos\y,g\AcogPos\z
				MoveEntity g\ScopeLenseObj,g\AcogPos\x,g\AcogPos\y,g\AcogPos\z
				If gc\CurrGamemode <> 3 Then
					If opt\RenderScope Then
						UsingScope = True
						EntityTexture g\ScopeLenseObj,ScopeTexture,0,1
					Else
						EntityAlpha g\ScopeLenseObj,0.5
					EndIf
				Else
					EntityAlpha g\ScopeLenseObj,0
				EndIf
				EntityFX g\ScopeLenseObj,1
				EntityTexture g\ScopeLenseObj,Charge_Interface,0,0
			EndIf
			g\HasAcog = True
		EndIf
	EndIf
	
End Function

Function RemoveAcog(g.Guns)
	Local g_I.GunInstance = First GunInstance
	
	If g <> Null
		If g\CanHaveAcog And (g\ScopeObj <> 0 Lor g\ScopeLenseObj <> 0) Then
			g\ScopeObj = FreeEntity_Strict(g\ScopeObj)
			g\ScopeLenseObj = FreeEntity_Strict(g\ScopeLenseObj)
			UsingScope = False
		EndIf
		g\HasAcog = False
	EndIf
	
End Function

Function AddLaserSight(g.Guns)
	Local g_I.GunInstance = First GunInstance
	Local it.Items
	
	If g <> Null
		If g\CanHaveLaserSight And g\LaserSightObj = 0 Then
			; ~ Viewmodel
			g\LaserSightObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\LaserSight.b3d",FindChild(g\obj, "att_laser"))
			ScaleEntity g\LaserSightObj,g\LaserSightSc\x,g\LaserSightSc\y,g\LaserSightSc\z
			RotateEntity g\LaserSightObj,g\LaserSightRot\x,g\LaserSightRot\y,g\LaserSightRot\z
			MoveEntity g\LaserSightObj,g\LaserSightPos\x,g\LaserSightPos\y,g\LaserSightPos\z
			g\HasLaserSight = True
		EndIf
	EndIf
	
End Function

Function RemoveLaserSight(g.Guns)
	Local g_I.GunInstance = First GunInstance
	Local p.Particles
	
	If g <> Null
		If g\CanHaveLaserSight And g\LaserSightObj <> 0 Then
			For p = Each Particles
				If p <> Null Then
					RemoveParticle(p)
				EndIf
			Next
			g\LaserSightObj = FreeEntity_Strict(g\LaserSightObj)
		EndIf
		g\HasLaserSight = False
	EndIf
	
End Function

Function AddMUI(g.Guns)
	Local g_I.GunInstance = First GunInstance
	Local it.Items
	
	If g <> Null
		If g\CanHaveMUI And (g\MUIObj = 0 And g\MUIScreenObj = 0 )Then
			; ~ Viewmodel
			g\MUIObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\EMR-P_MUI.b3d",FindChild(g\obj, "att_mui"))
			ScaleEntity g\MUIObj,g\MUISc\x,g\MUISc\y,g\MUISc\z
			RotateEntity g\MUIObj,g\MUIRot\x,g\MUIRot\y,g\MUIRot\z
			MoveEntity g\MUIObj,g\MUIPos\x,g\MUIPos\y,g\MUIPos\z
			g\MUIScreenObj = LoadMesh_Strict("GFX\Weapons\Models\Attachments\EMR-P_MUI_Screen.b3d",FindChild(g\obj, "att_mui"))
			ScaleEntity g\MUIScreenObj,g\MUISc\x,g\MUISc\y,g\MUISc\z
			RotateEntity g\MUIScreenObj,g\MUIRot\x,g\MUIRot\y,g\MUIRot\z
			MoveEntity g\MUIScreenObj,g\MUIPos\x,g\MUIPos\y,g\MUIPos\z
			g\HasMUI = True
		EndIf
	EndIf
	
End Function

Function RemoveMUI(g.Guns)
	Local g_I.GunInstance = First GunInstance
	
	If g <> Null
		If g\CanHaveMUI And (g\MUIObj <> 0 Lor g\MUIScreenObj <> 0) Then
			g\MUIObj = FreeEntity_Strict(g\MUIObj)
			g\MUIScreenObj = FreeEntity_Strict(g\MUIScreenObj)
		EndIf
		g\HasMUI = False
	EndIf
	
End Function

Function UpdateScope()
	Local g.Guns
	Local ScopeHasCharge%
	
	If fps\Factor[0] > 0.0
		If MouseHit3
			For g = Each Guns
				If g\HasAcog And g\ID = g_I\HoldingGun Then
					If g\ScopeCharge# < ScopeChargeTime# Then
						ScopeHasCharge = True
						Exit
					EndIf
				EndIf
			Next
			If ScopeHasCharge% Then
				ScopeNVG = Not ScopeNVG
				If ScopeNVG
					PlaySound_Strict NVGOnSFX
				Else
					PlaySound_Strict NVGOffSFX
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

Function RenderScope()
	Local n.NPCs
	Local g.Guns
	
	If gc\CurrGamemode <> 3
		If PlayerRoom\RoomTemplate\Name$ = "gate_a_topside" Then
			CameraFogRange ScopeCam, 5,30
			CameraFogColor (ScopeCam,200,200,200)
			CameraClsColor (ScopeCam,200,200,200)
			CameraRange(ScopeCam, 0.005, 30)
		ElseIf PlayerRoom\RoomTemplate\Name$ = "gate_a_road" Then
			CameraFogRange ScopeCam, 5,30
			CameraFogColor (ScopeCam,200,200,200)
			CameraClsColor (ScopeCam,200,200,200)
			CameraRange(ScopeCam, 0.005, 30)
		ElseIf (PlayerRoom\RoomTemplate\Name = "gate_a_intro") Then
			CameraFogRange ScopeCam, 5,30
			CameraFogColor (ScopeCam,200,200,200)
			CameraClsColor (ScopeCam,200,200,200)					
			CameraRange(ScopeCam, 0.005, 100)
		ElseIf (PlayerRoom\RoomTemplate\Name = "gate_b_topside") Then
			CameraFogRange ScopeCam, 5,45
			CameraFogColor (ScopeCam,200,200,200)
			CameraClsColor (ScopeCam,200,200,200)					
			CameraRange(ScopeCam, 0.005, 60)
		ElseIf (PlayerRoom\RoomTemplate\Name = "gate_c_topside") Then
			CameraFogRange ScopeCam, 5,45
			CameraFogColor (ScopeCam,2,2,20)
			CameraClsColor (ScopeCam,2,2,20)					
			CameraRange(ScopeCam, 0.005, 60)
		ElseIf (PlayerRoom\RoomTemplate\Name = "room2_maintenance") And (EntityY(Collider)<-3500.0*RoomScale) Then
			CameraFogRange ScopeCam,1,6
			CameraFogColor ScopeCam,5,20,3
			CameraClsColor ScopeCam,5,20,3
			CameraRange ScopeCam,0.01,7
		Else
			If (Not ScopeNVG)
				CameraFogRange(ScopeCam, CameraFogNear*LightVolume,CameraFogFar*LightVolume)
				CameraRange(ScopeCam, 0.005, Min(CameraFogFar*LightVolume*1.5,32))
			Else
				CameraFogRange(ScopeCam, CameraFogNear*LightVolume,30*LightVolume)
				CameraRange(ScopeCam, 0.005, Min(30*LightVolume*1.5,32))
			EndIf
			CameraFogColor(ScopeCam, 0,0,0)
			CameraClsColor ScopeCam,0,0,0
			CameraFogMode ScopeCam,1
		EndIf
	Else
		CameraFogColor(ScopeCam, 0,0,0)
		CameraClsColor ScopeCam,0,0,0
		CameraFogMode ScopeCam,1
	EndIf
	
	HideEntity Camera
	ShowEntity ScopeCam
	Cls
	SetBuffer BackBuffer()
	If ScopeNVG
		For n = Each NPCs
			If n\NPCtype = NPCtype966
				ShowEntity n\obj
			EndIf
		Next
	EndIf
	For g = Each Guns
		If g\ID = g_I\HoldingGun Then
			If (Not g\name = "xm29") And (Not g\name = "emr-p") Then
				If g\CanHaveAcog And g\HasAcog Then
					EntityTexture g\ScopeLenseObj,Charge_Interface,Int((g\ScopeCharge/ScopeChargeTime#)*4),0
					If ScopeNVG And Int((g\ScopeCharge/ScopeChargeTime#)*4) >= 3 Then
						If (Not ChannelPlaying(ScopeLowPowerChnSFX)) Then
							ScopeLowPowerChnSFX = PlaySound_Strict(ScopeLowPowerSFX)
						EndIf
					EndIf
				EndIf
			EndIf
			Exit
		EndIf
	Next
	RenderWorld
	CopyRect 0,0,128,128,0,0,BackBuffer(),TextureBuffer(ScopeTexture)
	ShowEntity Camera
	HideEntity ScopeCam
	If (Not wbi\NightVision) Then
		If ScopeNVG
			For n = Each NPCs
				If n\NPCtype = NPCtype966
					HideEntity n\obj
				EndIf
			Next
		EndIf
	EndIf
	
End Function

Function UpdateLaserSight()
	Local p.Particles,g.Guns
	
	For g = Each Guns
		If g <> Null
			If g\CanHaveLaserSight Then
				If g\HasLaserSight Then
					If g_I\HoldingGun = g\ID Then
						If CameraPick(Camera, opt\GraphicWidth / 2.0, opt\GraphicHeight / 2.0) Then
							If g\GunType = GUNTYPE_RIFLE Lor g\GunType = GUNTYPE_OICW Then
								p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 21, 0.015, 0, 2)
							Else
								p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 20, 0.015, 0, 2)
							EndIf
							If (Not g_I\IronSight) Then
								EntityOrder(p\obj, -1)
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
	Next
	
End Function

;Function UpdateLaserSight()
;	Local p.Particles,g.Guns
;	Local SpriteSmoothness# = 0.01
;	Local CurveSmoothness# = 10.0
;	
;	;HideEntity g_I\LaserSprite
;	
;	For g = Each Guns
;		If g <> Null
;			If g\CanHaveLaserSight Then
;				If g\HasLaserSight Then
;					If g_I\HoldingGun = g\ID Then
;						If CameraPick(Camera, opt\GraphicWidth / 2.0, opt\GraphicHeight / 2.0) Then
;							
;							If g\GunType = GUNTYPE_RIFLE Lor g\GunType = GUNTYPE_OICW Then
;								EntityTexture g_I\LaserSprite, ParticleTextures[28]
;							Else
;								EntityTexture g_I\LaserSprite, ParticleTextures[27]
;							EndIf
;							
;							ShowEntity g_I\LaserSprite
;							
;							If Abs(EntityX(g_I\LaserSprite)-PickedX())>SpriteSmoothness Lor Abs(EntityY(g_I\LaserSprite)-PickedY())>SpriteSmoothness Lor Abs(EntityZ(g_I\LaserSprite)-PickedZ())>SpriteSmoothness Then
;								PositionEntity g_I\LaserSprite, PickedX(), PickedY(), PickedZ()
;							Else
;								PositionEntity g_I\LaserSprite, CurveValue(PickedX(),(EntityX(g_I\LaserSprite)),CurveSmoothness), CurveValue(PickedY(),(EntityY(g_I\LaserSprite)),CurveSmoothness), CurveValue(PickedZ(),(EntityZ(g_I\LaserSprite)),CurveSmoothness)
;							EndIf
;							
;							ScaleSprite g_I\LaserSprite, Rnd(0.01,0.012), Rnd(0.01,0.012)
;							
;							If (Not g_I\IronSight) Then
;								EntityOrder(g_I\LaserSprite, -1)
;							Else
;								EntityOrder(g_I\LaserSprite, 0)
;							EndIf
;							
;						EndIf
;					EndIf
;				Else
;					HideEntity g_I\LaserSprite
;				EndIf
;			EndIf
;		EndIf
;	Next
;	
;End Function

; ~ Main Update Function for attachments

Function UpdateAttachments()
	Local g.Guns
	Local KeyHits[9],i%
	
	For i = 0 To 8
		KeyHits[i] = KeyHit(i+2)
	Next
	
	If AttachmentOpen Then
		
		For g.Guns = Each Guns
			Select g_I\HoldingGun
				Case GUN_AK12, GUN_MP7, GUN_P90
					;[Block]
					If g_I\HoldingGun = g\ID Then
						
						If KeyHits[0] Then
							g\HasToggledSuppressor = (Not g\HasToggledSuppressor)
						EndIf
						If KeyHits[1] Then
							g\HasToggledRedDot = (Not g\HasToggledRedDot)
						EndIf
						If KeyHits[2] Then
							g\HasToggledAimPoint = (Not g\HasToggledAimPoint)
						EndIf
						If KeyHits[3] Then
							g\HasToggledEotech = (Not g\HasToggledEotech)
						EndIf
						If KeyHits[4] Then
							g\HasToggledAcog = (Not g\HasToggledAcog)
						EndIf
						If KeyHits[5] Then
							g\HasToggledLaserSight = (Not g\HasToggledLaserSight)
						EndIf
						If gc\CurrGamemode <> 3 Then
							If KeyHits[6] Then
								g\HasToggledExtMag = (Not g\HasToggledExtMag)
							EndIf
						EndIf
						
					EndIf
					;[End Block]
				Case GUN_USP, GUN_BERETTA, GUN_FIVESEVEN, GUN_GLOCK, GUN_P30L, GUN_P99, GUN_HK416
					;[Block]
					If g_I\HoldingGun = g\ID Then
						
						If KeyHits[0] Then
							g\HasToggledSuppressor = (Not g\HasToggledSuppressor)
						EndIf
						If KeyHits[1] Then
							g\HasToggledLaserSight = (Not g\HasToggledLaserSight)
						EndIf
						If gc\CurrGamemode <> 3 Then
							If KeyHits[2] Then
								g\HasToggledExtMag = (Not g\HasToggledExtMag)
							EndIf
						EndIf
						
					EndIf
					;[End Block]
				Case GUN_MP5K
					;[Block]
					If g_I\HoldingGun = g\ID Then
						
						If KeyHits[0] Then
							g\HasToggledSuppressor = (Not g\HasToggledSuppressor)
						EndIf
						If KeyHits[1] Then
							g\HasToggledRedDot = (Not g\HasToggledRedDot)
						EndIf
						If KeyHits[2] Then
							g\HasToggledAimPoint = (Not g\HasToggledAimPoint)
						EndIf
						If KeyHits[3] Then
							g\HasToggledEotech = (Not g\HasToggledEotech)
						EndIf
						If KeyHits[4] Then
							g\HasToggledAcog = (Not g\HasToggledAcog)
						EndIf
						If KeyHits[5] Then
							g\HasToggledVerticalGrip = (Not g\HasToggledVerticalGrip)
						EndIf
						If KeyHits[6] Then
							g\HasToggledStock = (Not g\HasToggledStock)
						EndIf
						If KeyHits[7] Then
							g\HasToggledFoldingStock = (Not g\HasToggledFoldingStock)
						EndIf
						If gc\CurrGamemode <> 3 Then
							If KeyHits[8] Then
								g\HasToggledExtMag = (Not g\HasToggledExtMag)
							EndIf
						EndIf
					EndIf
					;[End Block]
				Case GUN_SW500
					;[Block]
					If g_I\HoldingGun = g\ID Then
						
						If KeyHits[0] Then
							g\HasToggledRedDot = (Not g\HasToggledRedDot)
						EndIf
						If KeyHits[1] Then
							g\HasToggledAimPoint = (Not g\HasToggledAimPoint)
						EndIf
						If KeyHits[2] Then
							g\HasToggledEotech = (Not g\HasToggledEotech)
						EndIf
						If KeyHits[3] Then
							g\HasToggledAcog = (Not g\HasToggledAcog)
						EndIf
						If KeyHits[4] Then
							g\HasToggledLaserSight = (Not g\HasToggledLaserSight)
						EndIf
						
					EndIf
					;[End Block]
				Case GUN_EMRP
					;[Block]
					If g_I\HoldingGun = g\ID Then
						
						If KeyHits[0] Then
							g\HasToggledMUI = (Not g\HasToggledMUI)
						EndIf
						
					EndIf
					;[End Block]
				Default
					;[Block]
					; ~ Nothing
					;[End Block]
			End Select
			
			; ~ Attachment selecton
			
			If g\PickedUpSuppressor Then
				If g\CanHaveSuppressor Then
					If g\HasToggledSuppressor Then
						If (Not g\HasSuppressor) Then
							PlaySound_Strict g_I\AttachSFX
							AddSuppressor(g)
						EndIf
					Else
						If g\HasSuppressor Then
							PlaySound_Strict g_I\DetachSFX
							RemoveSuppressor(g)
						EndIf
					EndIf
				EndIf
			EndIf
			If g\PickedUpRedDot Then
				If g\CanHaveRedDot Then
					If g\HasToggledRedDot Then
						If (Not g\HasAimPoint) And (Not g\HasAcog) And (Not g\HasEoTech) Then
							If (Not g\HasRedDot) Then
								PlaySound_Strict g_I\AttachSFX
								AddRedDot(g)
								If g_I\HoldingGun = GUN_MP5K Then
									AddRail(g)
								EndIf
							EndIf
						EndIf
					Else
						If g\HasRedDot Then
							PlaySound_Strict g_I\DetachSFX
							RemoveRedDot(g)
							If g_I\HoldingGun = GUN_MP5K Then
								RemoveRail(g)
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			If g\PickedUpAimPoint Then
				If g\CanHaveAimPoint Then
					If g\HasToggledAimPoint Then
						If (Not g\HasRedDot) And (Not g\HasAcog) And (Not g\HasEoTech) Then
							If (Not g\HasAimPoint) Then
								PlaySound_Strict g_I\AttachSFX
								AddAimPoint(g)
								If g_I\HoldingGun = GUN_MP5K Then
									AddRail(g)
								EndIf
							EndIf
						EndIf
					Else
						If g\HasAimPoint Then
							PlaySound_Strict g_I\DetachSFX
							RemoveAimPoint(g)
							If g_I\HoldingGun = GUN_MP5K Then
								RemoveRail(g)
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			If g\PickedUpEotech Then
				If g\CanHaveEoTech Then
					If g\HasToggledEotech Then
						If (Not g\HasAimPoint) And (Not g\HasAcog) And (Not g\HasRedDot) Then
							If (Not g\HasEoTech) Then
								PlaySound_Strict g_I\AttachSFX
								AddEoTech(g)
								If g_I\HoldingGun = GUN_MP5K Then
									AddRail(g)
								EndIf
							EndIf
						EndIf
					Else
						If g\HasEoTech Then
							PlaySound_Strict g_I\DetachSFX
							RemoveEoTech(g)
							If g_I\HoldingGun = GUN_MP5K Then
								RemoveRail(g)
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			If g\PickedUpAcog Then
				If g\CanHaveAcog Then
					If g\HasToggledAcog Then
						If (Not g\HasAimPoint) And (Not g\HasRedDot) And (Not g\HasEoTech) Then
							If (Not g\HasAcog) Then
								PlaySound_Strict g_I\AttachSFX
								AddAcog(g)
								If g_I\HoldingGun = GUN_MP5K Then
									AddRail(g)
								EndIf
							EndIf
						EndIf
					Else
						If g\HasAcog Then
							PlaySound_Strict g_I\DetachSFX
							RemoveAcog(g)
							If g_I\HoldingGun = GUN_MP5K Then
								RemoveRail(g)
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			If g\PickedUpVerticalGrip Then
				If g\CanHaveVerticalGrip Then
					If g\HasToggledVerticalGrip Then
						If (Not g\HasVerticalGrip) Then
							PlaySound_Strict g_I\AttachSFX
							AddVerticalGrip(g)
						EndIf
					Else
						If g\HasVerticalGrip Then
							PlaySound_Strict g_I\DetachSFX
							RemoveVerticalGrip(g)
						EndIf
					EndIf
				EndIf
			EndIf
			If g\PickedUpLaserSight Then
				If g\CanHaveLaserSight Then
					If g\HasToggledLaserSight Then
						If (Not g\HasLaserSight) Then
							PlaySound_Strict g_I\AttachSFX
							AddLaserSight(g)
						EndIf
					Else
						If g\HasLaserSight Then
							PlaySound_Strict g_I\DetachSFX
							RemoveLaserSight(g)
						EndIf
					EndIf
				EndIf
			EndIf
			If g\PickedUpExtMag Then
				If g\CanHaveExtMag Then
					If g\HasToggledExtMag Then
						If (Not g\HasExtMag) Then
							PlaySound_Strict g_I\AttachSFX
							AddExtendedMag(g)
						EndIf
					Else
						If g\HasExtMag Then
							PlaySound_Strict g_I\DetachSFX
							RemoveExtendedMag(g)
						EndIf
					EndIf
				EndIf
			EndIf
			If g\PickedUpStock Then
				If g\CanHaveStock Then
					If g\HasToggledStock Then
						If (Not g\HasStock) And (Not g\HasFoldingStock) Then
							PlaySound_Strict g_I\AttachSFX
							AddStock(g)
						EndIf
					Else
						If g\HasStock Then
							PlaySound_Strict g_I\DetachSFX
							RemoveStock(g)
						EndIf
					EndIf
				EndIf
			EndIf
			If g\PickedUpFoldingStock Then
				If g\CanHaveFoldingStock Then
					If g\HasToggledFoldingStock Then
						If (Not g\HasStock) And (Not g\HasFoldingStock) Then
							PlaySound_Strict g_I\AttachSFX
							AddFoldingStock(g)
						EndIf
					Else
						If g\HasFoldingStock Then
							PlaySound_Strict g_I\DetachSFX
							RemoveFoldingStock(g)
						EndIf
					EndIf
				EndIf
			EndIf
			If g\PickedUpMUI Then
				If g\CanHaveMUI Then
					If g\HasToggledMUI Then
						If (Not g\HasMUI) Then
							PlaySound_Strict g_I\AttachSFX
							AddMUI(g)
						EndIf
					Else
						If g\HasMUI Then
							PlaySound_Strict g_I\DetachSFX
							RemoveMUI(g)
						EndIf
					EndIf
				EndIf
			EndIf
			
		Next
	EndIf
	
End Function

Function DrawAttachmentsInHUD()
	Local g.Guns
	Local x# = opt\GraphicWidth/2
	Local y# = opt\GraphicHeight/2
	Local width# = 64
	Local height# = 64
	Local spacing2# = 0
	Local width2# = width/3
	Local height2# = height/3
	Local i
	
	If AttachmentOpen Then
		
		For g.Guns = Each Guns
			Select g_I\HoldingGun
				Case GUN_AK12,GUN_P90,GUN_MP7
					;[Block]
					If g_I\HoldingGun = g\ID Then
						
						SetFont(fo\Font[Font_Default_Large])
						Text x-160,y-450,GetLocalString("Attachments", "atts")
						SetFont(fo\Font[Font_Default])
						Text x-220,y-400,GetLocalStringR("Attachments", "atts_exit",KeyName[KEY_SELECTATTACHMENT])
						
						SetFont(fo\Font[Font_Default])
						Text x-300,y-300,GetLocalString("Attachments", "att_on")
						SetFont(fo\Font[Font_Default])
						Text x-300,y-250,GetLocalString("Attachments", "att_off")
						
						If g_I\HoldingGun = GUN_AK12 Then
							DrawImage(GunAttachmentPreviewIMG[10],x-500,y-240)
						ElseIf g_I\HoldingGun = GUN_P90 Then
							DrawImage(GunAttachmentPreviewIMG[1],x-500,y-240)
						ElseIf g_I\HoldingGun = GUN_MP7 Then
							DrawImage(GunAttachmentPreviewIMG[7],x-500,y-240)
						EndIf
						
						; ~ Suppressor
						If g\CanHaveSuppressor = True And g\PickedUpSuppressor Then
							DrawFrame(x-520,y-15,width,height)
							DrawImage(AttachmentIMG[0],x-520,y-15)
						ElseIf (Not g\PickedUpSuppressor) Then
							DrawImage(AttachmentIMG[0],x-520,y-15)
							DrawImage(AttachmentIMG[15],x-520,y-15)
						EndIf
						If g\HasSuppressor Then
							Color 20,255,20
							Rect(x-522,y-17,width+4,height+4)
							DrawFrame(x-520,y-15,width,height)
							DrawImage(AttachmentIMG[0],x-520,y-15)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-520+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-520+(width2*i))+(width2*0.5), y-15+(height2*0.5), "1", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ Red Dot Sight
						If g\CanHaveRedDot = True And g\PickedUpRedDot Then
							DrawFrame(x-500,y-180,width,height)
							DrawImage(AttachmentIMG[1],x-500,y-180)
						ElseIf (Not g\PickedUpRedDot) Then
							DrawImage(AttachmentIMG[1],x-500,y-180)
							DrawImage(AttachmentIMG[15],x-500,y-180)
						EndIf
						If g\HasRedDot Then
							Color 20,255,20
							Rect(x-502,y-182,width+4,height+4)
							DrawFrame(x-500,y-180,width,height)
							DrawImage(AttachmentIMG[1],x-500,y-180)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-500+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-500+(width2*i))+(width2*0.5), y-180+(height2*0.5), "2", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ AimPoint Sight
						If g\CanHaveAimPoint = True And g\PickedUpAimPoint Then
							DrawFrame(x-425,y-180,width,height)
							DrawImage(AttachmentIMG[8],x-425,y-180)
						ElseIf (Not g\PickedUpAimPoint) Then
							DrawImage(AttachmentIMG[8],x-425,y-180)
							DrawImage(AttachmentIMG[15],x-425,y-180)
						EndIf
						If g\HasAimPoint Then
							Color 20,255,20
							Rect(x-427,y-182,width+4,height+4)
							DrawFrame(x-425,y-180,width,height)
							DrawImage(AttachmentIMG[8],x-425,y-180)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-425+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-425+(width2*i))+(width2*0.5), y-180+(height2*0.5), "3", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ EoTech Sight
						If g\CanHaveEoTech = True And g\PickedUpEotech Then
							DrawFrame(x-360,y-180,width,height)
							DrawImage(AttachmentIMG[3],x-360,y-180)
						ElseIf (Not g\PickedUpAimPoint) Then
							DrawImage(AttachmentIMG[3],x-360,y-180)
							DrawImage(AttachmentIMG[15],x-360,y-180)
						EndIf
						If g\HasEoTech Then
							Color 20,255,20
							Rect(x-362,y-182,width+4,height+4)
							DrawFrame(x-360,y-180,width,height)
							DrawImage(AttachmentIMG[3],x-360,y-180)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-360+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-360+(width2*i))+(width2*0.5), y-180+(height2*0.5), "4", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ Acog Scope
						If g\CanHaveAcog = True And g\PickedUpAcog Then
							DrawFrame(x-300,y-180,width,height)
							DrawImage(AttachmentIMG[2],x-300,y-180)
						ElseIf (Not g\PickedUpAcog) Then
							DrawImage(AttachmentIMG[2],x-300,y-180)
							DrawImage(AttachmentIMG[15],x-300,y-180)
						EndIf
						If g\HasAcog Then
							Color 20,255,20
							Rect(x-302,y-182,width+4,height+4)
							DrawFrame(x-300,y-180,width,height)
							DrawImage(AttachmentIMG[2],x-300,y-180)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-300+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-300+(width2*i))+(width2*0.5), y-180+(height2*0.5), "5", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ Laser Sight
						If g\CanHaveLaserSight = True And g\PickedUpLaserSight Then
							DrawFrame(x-580,y-15,width,height)
							DrawImage(AttachmentIMG[9],x-580,y-15)
						ElseIf (Not g\PickedUpLaserSight) Then
							DrawImage(AttachmentIMG[9],x-580,y-15)
							DrawImage(AttachmentIMG[15],x-580,y-15)
						EndIf
						If g\HasLaserSight Then
							Color 20,255,20
							Rect(x-582,y-17,width+4,height+4)
							DrawFrame(x-580,y-15,width,height)
							DrawImage(AttachmentIMG[9],x-580,y-15)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-580+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-580+(width2*i))+(width2*0.5), y-15+(height2*0.5), "6", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						If gc\CurrGamemode <> 3 Then
							
							; ~ Extended Mag
							If g_I\HoldingGun = GUN_P90 Then
								If g\CanHaveExtMag = True And g\PickedUpExtMag Then
									DrawFrame(x+30,y+50,width,height)
									DrawImage(AttachmentIMG[13],x+30,y+50)
								ElseIf (Not g\PickedUpExtMag) Then
									DrawImage(AttachmentIMG[13],x+30,y+50)
									DrawImage(AttachmentIMG[15],x+30,y+50)
								EndIf
								If g\HasExtMag Then
									Color 20,255,20
									Rect(x+30,y+50,width+4,height+4)
									DrawFrame(x+30,y+50,width,height)
									DrawImage(AttachmentIMG[13],x+30,y+50)
								EndIf
							ElseIf g_I\HoldingGun = GUN_AK12 Then
								If g\CanHaveExtMag = True And g\PickedUpExtMag Then
									DrawFrame(x+30,y+50,width,height)
									DrawImage(AttachmentIMG[10],x+30,y+50)
								ElseIf (Not g\PickedUpExtMag) Then
									DrawImage(AttachmentIMG[10],x+30,y+50)
									DrawImage(AttachmentIMG[15],x+30,y+50)
								EndIf
								If g\HasExtMag Then
									Color 20,255,20
									Rect(x+30,y+50,width+4,height+4)
									DrawFrame(x+30,y+50,width,height)
									DrawImage(AttachmentIMG[10],x+30,y+50)
								EndIf
							ElseIf g_I\HoldingGun = GUN_MP7 Then
								If g\CanHaveExtMag = True And g\PickedUpExtMag Then
									DrawFrame(x+30,y+50,width,height)
									DrawImage(AttachmentIMG[12],x+30,y+50)
								ElseIf (Not g\PickedUpExtMag) Then
									DrawImage(AttachmentIMG[12],x+30,y+50)
									DrawImage(AttachmentIMG[15],x+30,y+50)
								EndIf
								If g\HasExtMag Then
									Color 20,255,20
									Rect(x+30,y+50,width+4,height+4)
									DrawFrame(x+30,y+50,width,height)
									DrawImage(AttachmentIMG[12],x+30,y+50)
								EndIf
							Else
								If g\CanHaveExtMag = True And g\PickedUpExtMag Then
									DrawFrame(x+30,y+50,width,height)
									DrawImage(AttachmentIMG[11],x+30,y+50)
								ElseIf (Not g\PickedUpExtMag) Then
									DrawImage(AttachmentIMG[11],x+30,y+50)
									DrawImage(AttachmentIMG[15],x+30,y+50)
								EndIf
								If g\HasExtMag Then
									Color 20,255,20
									Rect(x+30,y+50,width+4,height+4)
									DrawFrame(x+30,y+50,width,height)
									DrawImage(AttachmentIMG[11],x+30,y+50)
								EndIf
							EndIf
							
							SetFont fo\Font[Font_Default]
							
							Color 255,255,255
							DrawFrame(x+30+(width2*i), y+50, width2, height2, (x Mod 64), (x Mod 64))
							Text (x+30+(width2*i))+(width2*0.5), y+50+(height2*0.5), "7", 1, 1
							spacing2# = 10
							
							SetFont fo\Font[Font_Default]
							Color 0,0,0
							
						EndIf
						
					EndIf
					;[End Block]
				Case GUN_USP,GUN_BERETTA,GUN_GLOCK,GUN_FIVESEVEN,GUN_P30L,GUN_P99,GUN_HK416
					;[Block]
					If g_I\HoldingGun = g\ID Then
						
						SetFont(fo\Font[Font_Default_Large])
						Text x-160,y-450,GetLocalString("Attachments", "atts")
						SetFont(fo\Font[Font_Default])
						Text x-220,y-400,GetLocalStringR("Attachments", "atts_exit",KeyName[KEY_SELECTATTACHMENT])
						
						SetFont(fo\Font[Font_Default])
						Text x-300,y-300,GetLocalString("Attachments", "att_on")
						SetFont(fo\Font[Font_Default])
						Text x-300,y-250,GetLocalString("Attachments", "att_off")
						
						If g_I\HoldingGun = GUN_USP Then
							DrawImage(GunAttachmentPreviewIMG[2],x-500,y-240)
						ElseIf g_I\HoldingGun = GUN_BERETTA Then
							DrawImage(GunAttachmentPreviewIMG[4],x-500,y-240)
						ElseIf g_I\HoldingGun = GUN_GLOCK Then
							DrawImage(GunAttachmentPreviewIMG[5],x-500,y-240)
						ElseIf g_I\HoldingGun = GUN_FIVESEVEN Then
							DrawImage(GunAttachmentPreviewIMG[6],x-500,y-240)
						ElseIf g_I\HoldingGun = GUN_P30L Then
							DrawImage(GunAttachmentPreviewIMG[11],x-500,y-240)
						ElseIf g_I\HoldingGun = GUN_P99 Then
							DrawImage(GunAttachmentPreviewIMG[12],x-500,y-240)
						ElseIf g_I\HoldingGun = GUN_HK416 Then
							DrawImage(GunAttachmentPreviewIMG[8],x-500,y-240)
						EndIf
						
						; ~ Suppressor
						If g\CanHaveSuppressor = True And g\PickedUpSuppressor Then
							DrawFrame(x-520,y-15,width,height)
							DrawImage(AttachmentIMG[0],x-520,y-15)
						ElseIf (Not g\PickedUpSuppressor) Then
							DrawImage(AttachmentIMG[0],x-520,y-15)
							DrawImage(AttachmentIMG[15],x-520,y-15)
						EndIf
						If g\HasSuppressor Then
							Color 20,255,20
							Rect(x-522,y-17,width+4,height+4)
							DrawFrame(x-520,y-15,width,height)
							DrawImage(AttachmentIMG[0],x-520,y-15)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-520+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-520+(width2*i))+(width2*0.5), y-15+(height2*0.5), "1", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ Laser Sight
						If g\CanHaveLaserSight = True And g\PickedUpLaserSight Then
							DrawFrame(x-580,y-15,width,height)
							DrawImage(AttachmentIMG[9],x-580,y-15)
						ElseIf (Not g\PickedUpLaserSight) Then
							DrawImage(AttachmentIMG[9],x-580,y-15)
							DrawImage(AttachmentIMG[15],x-580,y-15)
						EndIf
						If g\HasLaserSight Then
							Color 20,255,20
							Rect(x-582,y-17,width+4,height+4)
							DrawFrame(x-580,y-15,width,height)
							DrawImage(AttachmentIMG[9],x-580,y-15)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-580+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-580+(width2*i))+(width2*0.5), y-15+(height2*0.5), "2", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						If gc\CurrGamemode <> 3 Then
							
							; ~ Extended Mag
							If g_I\HoldingGun = GUN_HK416 Then
								If g\CanHaveExtMag = True And g\PickedUpExtMag Then
									DrawFrame(x+30,y+50,width,height)
									DrawImage(AttachmentIMG[11],x+30,y+50)
								ElseIf (Not g\PickedUpExtMag) Then
									DrawImage(AttachmentIMG[11],x+30,y+50)
									DrawImage(AttachmentIMG[15],x+30,y+50)
								EndIf
								If g\HasExtMag Then
									Color 20,255,20
									Rect(x+30,y+50,width+4,height+4)
									DrawFrame(x+30,y+50,width,height)
									DrawImage(AttachmentIMG[11],x+30,y+50)
								EndIf
							Else
								If g\CanHaveExtMag = True And g\PickedUpExtMag Then
									DrawFrame(x+30,y+50,width,height)
									DrawImage(AttachmentIMG[12],x+30,y+50)
								ElseIf (Not g\PickedUpExtMag) Then
									DrawImage(AttachmentIMG[12],x+30,y+50)
									DrawImage(AttachmentIMG[15],x+30,y+50)
								EndIf
								If g\HasExtMag Then
									Color 20,255,20
									Rect(x+30,y+50,width+4,height+4)
									DrawFrame(x+30,y+50,width,height)
									DrawImage(AttachmentIMG[12],x+30,y+50)
								EndIf
							EndIf
							
							SetFont fo\Font[Font_Default]
							
							Color 255,255,255
							DrawFrame(x+30+(width2*i), y+50, width2, height2, (x Mod 64), (x Mod 64))
							Text (x+30+(width2*i))+(width2*0.5), y+50+(height2*0.5), "3", 1, 1
							spacing2# = 10
							
							SetFont fo\Font[Font_Default]
							Color 0,0,0
							
						EndIf
						
					EndIf
					;[End Block]
				Case GUN_MP5K
					;[Block]
					If g_I\HoldingGun = g\ID Then
						
						SetFont(fo\Font[Font_Default_Large])
						Text x-160,y-450,GetLocalString("Attachments", "atts")
						SetFont(fo\Font[Font_Default])
						Text x-220,y-400,GetLocalStringR("Attachments", "atts_exit",KeyName[KEY_SELECTATTACHMENT])
						
						SetFont(fo\Font[Font_Default])
						Text x-300,y-300,GetLocalString("Attachments", "att_on")
						SetFont(fo\Font[Font_Default])
						Text x-300,y-250,GetLocalString("Attachments", "att_off")
						
						DrawImage(GunAttachmentPreviewIMG[3],x-500,y-240)
						
						; ~ Suppressor
						If g\CanHaveSuppressor = True And g\PickedUpSuppressor Then
							DrawFrame(x-520,y-15,width,height)
							DrawImage(AttachmentIMG[0],x-520,y-15)
						ElseIf (Not g\PickedUpSuppressor) Then
							DrawImage(AttachmentIMG[0],x-520,y-15)
							DrawImage(AttachmentIMG[15],x-520,y-15)
						EndIf
						If g\HasSuppressor Then
							Color 20,255,20
							Rect(x-522,y-17,width+4,height+4)
							DrawFrame(x-520,y-15,width,height)
							DrawImage(AttachmentIMG[0],x-520,y-15)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-520+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-520+(width2*i))+(width2*0.5), y-15+(height2*0.5), "1", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ Red Dot Sight
						If g\CanHaveRedDot = True And g\PickedUpRedDot Then
							DrawFrame(x-500,y-180,width,height)
							DrawImage(AttachmentIMG[1],x-500,y-180)
						ElseIf (Not g\PickedUpRedDot) Then
							DrawImage(AttachmentIMG[1],x-500,y-180)
							DrawImage(AttachmentIMG[15],x-500,y-180)
						EndIf
						If g\HasRedDot Then
							Color 20,255,20
							Rect(x-502,y-182,width+4,height+4)
							DrawFrame(x-500,y-180,width,height)
							DrawImage(AttachmentIMG[1],x-500,y-180)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-500+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-500+(width2*i))+(width2*0.5), y-180+(height2*0.5), "2", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ AimPoint Sight
						If g\CanHaveAimPoint = True And g\PickedUpAimPoint Then
							DrawFrame(x-425,y-180,width,height)
							DrawImage(AttachmentIMG[8],x-425,y-180)
						ElseIf (Not g\PickedUpAimPoint) Then
							DrawImage(AttachmentIMG[8],x-425,y-180)
							DrawImage(AttachmentIMG[15],x-425,y-180)
						EndIf
						If g\HasAimPoint Then
							Color 20,255,20
							Rect(x-427,y-182,width+4,height+4)
							DrawFrame(x-425,y-180,width,height)
							DrawImage(AttachmentIMG[8],x-425,y-180)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-425+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-425+(width2*i))+(width2*0.5), y-180+(height2*0.5), "3", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ EoTech Sight
						If g\CanHaveEoTech = True And g\PickedUpEotech Then
							DrawFrame(x-360,y-180,width,height)
							DrawImage(AttachmentIMG[3],x-360,y-180)
						ElseIf (Not g\PickedUpAimPoint) Then
							DrawImage(AttachmentIMG[3],x-360,y-180)
							DrawImage(AttachmentIMG[15],x-360,y-180)
						EndIf
						If g\HasEoTech Then
							Color 20,255,20
							Rect(x-362,y-182,width+4,height+4)
							DrawFrame(x-360,y-180,width,height)
							DrawImage(AttachmentIMG[3],x-360,y-180)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-360+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-360+(width2*i))+(width2*0.5), y-180+(height2*0.5), "4", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ Acog Scope
						If g\CanHaveAcog = True And g\PickedUpAcog Then
							DrawFrame(x-300,y-180,width,height)
							DrawImage(AttachmentIMG[2],x-300,y-180)
						ElseIf (Not g\PickedUpAcog) Then
							DrawImage(AttachmentIMG[2],x-300,y-180)
							DrawImage(AttachmentIMG[15],x-300,y-180)
						EndIf
						If g\HasAcog Then
							Color 20,255,20
							Rect(x-302,y-182,width+4,height+4)
							DrawFrame(x-300,y-180,width,height)
							DrawImage(AttachmentIMG[2],x-300,y-180)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-300+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-300+(width2*i))+(width2*0.5), y-180+(height2*0.5), "5", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						; ~ Vertical Grip
						If g\CanHaveVerticalGrip = True And g\PickedUpVerticalGrip Then
							DrawFrame(x-500,y+125,width,height)
							DrawImage(AttachmentIMG[6],x-500,y+125)
						ElseIf (Not g\PickedUpVerticalGrip) Then
							DrawImage(AttachmentIMG[6],x-500,y+125)
							DrawImage(AttachmentIMG[15],x-500,y+125)
						EndIf
						If g\HasVerticalGrip Then
							Color 20,255,20
							Rect(x-502,y+127,width+4,height+4)
							DrawFrame(x-500,y+125,width,height)
							DrawImage(AttachmentIMG[6],x-500,y+125)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-500+(width2*i), y+125, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-500+(width2*i))+(width2*0.5), y+125+(height2*0.5), "6", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ Stock
						If g\CanHaveStock = True And g\PickedUpStock Then
							DrawFrame(x+300,y-15,width,height)
							DrawImage(AttachmentIMG[4],x+300,y-15)
						ElseIf (Not g\PickedUpStock) Then
							DrawImage(AttachmentIMG[4],x+300,y-15)
							DrawImage(AttachmentIMG[15],x+300,y-15)
						EndIf
						If g\HasStock Then
							Color 20,255,20
							Rect(x+302,y-17,width+4,height+4)
							DrawFrame(x+300,y-15,width,height)
							DrawImage(AttachmentIMG[4],x+300,y-15)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x+300+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
						Text (x+300+(width2*i))+(width2*0.5), y-15+(height2*0.5), "7", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ Folding Stock
						If g\CanHaveFoldingStock = True And g\PickedUpFoldingStock Then
							DrawFrame(x+400,y-15,width,height)
							DrawImage(AttachmentIMG[5],x+400,y-15)
						ElseIf (Not g\PickedUpFoldingStock) Then
							DrawImage(AttachmentIMG[5],x+400,y-15)
							DrawImage(AttachmentIMG[15],x+400,y-15)
						EndIf
						If g\HasFoldingStock Then
							Color 20,255,20
							Rect(x+402,y-17,width+4,height+4)
							DrawFrame(x+400,y-15,width,height)
							DrawImage(AttachmentIMG[5],x+400,y-15)
						EndIf
						
						Color 255,255,255
						DrawFrame(x+400+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
						Text (x+400+(width2*i))+(width2*0.5), y-15+(height2*0.5), "8", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						If gc\CurrGamemode <> 3 Then
							
						; ~ Extended Mag
							If g\CanHaveExtMag = True And g\PickedUpExtMag Then
								DrawFrame(x+30,y+50,width,height)
								DrawImage(AttachmentIMG[12],x+30,y+50)
							ElseIf (Not g\PickedUpExtMag) Then
								DrawImage(AttachmentIMG[12],x+30,y+50)
								DrawImage(AttachmentIMG[15],x+30,y+50)
							EndIf
							If g\HasExtMag Then
								Color 20,255,20
								Rect(x+30,y+50,width+4,height+4)
								DrawFrame(x+30,y+50,width,height)
								DrawImage(AttachmentIMG[12],x+30,y+50)
							EndIf
							
							SetFont fo\Font[Font_Default]
							
							Color 255,255,255
							DrawFrame(x+30+(width2*i), y+50, width2, height2, (x Mod 64), (x Mod 64))
							Text (x+30+(width2*i))+(width2*0.5), y+50+(height2*0.5), "9", 1, 1
							spacing2# = 10
							
							SetFont fo\Font[Font_Default]
							Color 0,0,0
							
						EndIf
						
					EndIf
					;[End Block]
				Case GUN_SW500
					;[Block]
					If g_I\HoldingGun = g\ID Then
						
						SetFont(fo\Font[Font_Default_Large])
						Text x-160,y-450,GetLocalString("Attachments", "atts")
						SetFont(fo\Font[Font_Default])
						Text x-220,y-400,GetLocalStringR("Attachments", "atts_exit",KeyName[KEY_SELECTATTACHMENT])
						
						SetFont(fo\Font[Font_Default])
						Text x-300,y-300,GetLocalString("Attachments", "att_on")
						SetFont(fo\Font[Font_Default])
						Text x-300,y-250,GetLocalString("Attachments", "att_off")
						
						DrawImage(GunAttachmentPreviewIMG[9],x-500,y-240)
						
						; ~ Red Dot Sight
						If g\CanHaveRedDot = True And g\PickedUpRedDot Then
							DrawFrame(x-500,y-180,width,height)
							DrawImage(AttachmentIMG[1],x-500,y-180)
						ElseIf (Not g\PickedUpRedDot) Then
							DrawImage(AttachmentIMG[1],x-500,y-180)
							DrawImage(AttachmentIMG[15],x-500,y-180)
						EndIf
						If g\HasRedDot Then
							Color 20,255,20
							Rect(x-502,y-182,width+4,height+4)
							DrawFrame(x-500,y-180,width,height)
							DrawImage(AttachmentIMG[1],x-500,y-180)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-500+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-500+(width2*i))+(width2*0.5), y-180+(height2*0.5), "1", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ AimPoint Sight
						If g\CanHaveAimPoint = True And g\PickedUpAimPoint Then
							DrawFrame(x-425,y-180,width,height)
							DrawImage(AttachmentIMG[8],x-425,y-180)
						ElseIf (Not g\PickedUpAimPoint) Then
							DrawImage(AttachmentIMG[8],x-425,y-180)
							DrawImage(AttachmentIMG[15],x-425,y-180)
						EndIf
						If g\HasAimPoint Then
							Color 20,255,20
							Rect(x-427,y-182,width+4,height+4)
							DrawFrame(x-425,y-180,width,height)
							DrawImage(AttachmentIMG[8],x-425,y-180)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-425+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-425+(width2*i))+(width2*0.5), y-180+(height2*0.5), "2", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ EoTech Sight
						If g\CanHaveEoTech = True And g\PickedUpEotech Then
							DrawFrame(x-360,y-180,width,height)
							DrawImage(AttachmentIMG[3],x-360,y-180)
						ElseIf (Not g\PickedUpAimPoint) Then
							DrawImage(AttachmentIMG[3],x-360,y-180)
							DrawImage(AttachmentIMG[15],x-360,y-180)
						EndIf
						If g\HasEoTech Then
							Color 20,255,20
							Rect(x-362,y-182,width+4,height+4)
							DrawFrame(x-360,y-180,width,height)
							DrawImage(AttachmentIMG[3],x-360,y-180)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-360+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-360+(width2*i))+(width2*0.5), y-180+(height2*0.5), "3", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
						; ~ Acog Scope
						If g\CanHaveAcog = True And g\PickedUpAcog Then
							DrawFrame(x-300,y-180,width,height)
							DrawImage(AttachmentIMG[2],x-300,y-180)
						ElseIf (Not g\PickedUpAcog) Then
							DrawImage(AttachmentIMG[2],x-300,y-180)
							DrawImage(AttachmentIMG[15],x-300,y-180)
						EndIf
						If g\HasAcog Then
							Color 20,255,20
							Rect(x-302,y-182,width+4,height+4)
							DrawFrame(x-300,y-180,width,height)
							DrawImage(AttachmentIMG[2],x-300,y-180)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-300+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-300+(width2*i))+(width2*0.5), y-180+(height2*0.5), "4", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
					EndIf
					;[End Block]
				Case GUN_EMRP
					;[Block]
					If g_I\HoldingGun = g\ID Then
						
						SetFont(fo\Font[Font_Default_Large])
						Text x-160,y-450,GetLocalString("Attachments", "atts")
						SetFont(fo\Font[Font_Default])
						Text x-220,y-400,GetLocalStringR("Attachments", "atts_exit",KeyName[KEY_SELECTATTACHMENT])
						
						SetFont(fo\Font[Font_Default])
						Text x-300,y-300,GetLocalString("Attachments", "att_on")
						SetFont(fo\Font[Font_Default])
						Text x-300,y-250,GetLocalString("Attachments", "att_off")
						
						DrawImage(GunAttachmentPreviewIMG[13],x-500,y-240)
						
						; ~ MUI
						
						If g\CanHaveMUI = True And g\PickedUpMUI Then
							DrawFrame(x-400,y+50,width,height)
							DrawImage(AttachmentIMG[14],x-400,y+50)
						ElseIf (Not g\PickedUpMUI) Then
							DrawImage(AttachmentIMG[14],x-400,y+50)
							DrawImage(AttachmentIMG[15],x-400,y+50)
						EndIf
						If g\HasMUI Then
							Color 20,255,20
							Rect(x-400,y+50,width+4,height+4)
							DrawFrame(x-400,y+50,width,height)
							DrawImage(AttachmentIMG[14],x-400,y+50)
						EndIf
						
						SetFont fo\Font[Font_Default]
						
						Color 255,255,255
						DrawFrame(x-400+(width2*i), y+50, width2, height2, (x Mod 64), (x Mod 64))
						Text (x-400+(width2*i))+(width2*0.5), y+50+(height2*0.5), "1", 1, 1
						spacing2# = 10
						
						SetFont fo\Font[Font_Default]
						Color 0,0,0
						
					EndIf
					;[End Block]
				Default
					;[Block]
					If g_I\HoldingGun = g\ID Then
						SetFont(fo\Font[Font_Default_Large])
						Text x-160,y-450,GetLocalString("Attachments", "atts")
						SetFont(fo\Font[Font_Default])
						Text x-220,y-400,GetLocalStringR("Attachments", "atts_exit",KeyName[KEY_SELECTATTACHMENT])
						
						SetFont(fo\Font[Font_Default_Medium])
						Text x-200,y-300,GetLocalString("Attachments", "no_atts")
						
						DrawImage(GunAttachmentPreviewIMG[0],x-500,y-240)
					EndIf
					;[End Block]
			End Select
		Next
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D