
Const FACILITY_REACTOR_WATERTEXTURE_ID% = 0
Const FACILITY_REACTOR_MODEL% = 1
Const FACILITY_REACTOR_BUTTON% = 2
Const FACILITY_REACTOR_BUTTON_2% = 3

Function FillRoom_Facility_Reactor(r.Rooms)
	Local wa.Water
	
	;wa.Water = CreateWater("GFX\map\rooms\room2_reactor\room2_reactor_water.b3d", "coolant_water", 0, 0, 0, r\obj, (-8327.0*RoomScale))
	;EntityAlpha wa\obj,0.2
	;EntityColor wa\obj,100,100,100
	;r\Objects[FACILITY_REACTOR_WATERTEXTURE_ID] = LoadTexture_Strict("GFX\map\textures\SLH_water3.png",1,1)
	;EntityTexture wa\obj,r\Objects[FACILITY_REACTOR_WATERTEXTURE_ID]
	;ScaleTexture r\Objects[FACILITY_REACTOR_WATERTEXTURE_ID],0.1,0.1
	;TextureBlend r\Objects[FACILITY_REACTOR_WATERTEXTURE_ID],2
	
	r\Objects[FACILITY_REACTOR_MODEL] = LoadAnimMesh_Strict("GFX\Map\Props\Reactor.b3d")
	ScaleEntity r\Objects[FACILITY_REACTOR_MODEL],100*RoomScale,100*RoomScale,100*RoomScale
	PositionEntity r\Objects[FACILITY_REACTOR_MODEL],r\x-641*RoomScale,r\y-8296*RoomScale,r\z-59*RoomScale
	EntityParent(r\Objects[FACILITY_REACTOR_MODEL],r\obj)
	EntityPickMode(r\Objects[FACILITY_REACTOR_MODEL],2)
	
	r\Objects[FACILITY_REACTOR_BUTTON] = CreateButton(r\x-112*RoomScale,r\y-6878*RoomScale,r\z,90,-178,-90)
	EntityParent(r\Objects[FACILITY_REACTOR_BUTTON],r\obj)
	
	r\Objects[FACILITY_REACTOR_BUTTON_2] = CreateButton(r\x-172*RoomScale,r\y-7936*RoomScale,r\z,90,-178,-90)
	EntityParent(r\Objects[FACILITY_REACTOR_BUTTON_2],r\obj)
	
End Function

Function UpdateEvent_Facility_Reactor(e.Events)
	Local p.Particles,i
	
	If (Not clm\GlobalMode) Then
	
		If PlayerRoom = e\room
			
			ShowEntity e\room\Objects[FACILITY_REACTOR_MODEL]
		
			If TaskExists(TASK_FIND_REACTOR) And (Not TaskExists(TASK_TURN_ON_REACTOR)) Then
				EndTask(TASK_FIND_REACTOR)
				BeginTask(TASK_TURN_ON_REACTOR)
			EndIf
			
			;e\EventState4 = e\EventState4 + FPSfactor
			
			;PositionTexture e\room\Objects[FACILITY_REACTOR_WATERTEXTURE_ID], 0, e\EventState4 * 0.005
			;ShouldUpdateWater = "coolant_water"
			
			If e\EventState = 0 Then
				Animate2(e\room\Objects[FACILITY_REACTOR_MODEL], AnimTime(e\room\Objects[FACILITY_REACTOR_MODEL]), 1.0, 500.0, 0.3,True)
			Else
				Animate2(e\room\Objects[FACILITY_REACTOR_MODEL], AnimTime(e\room\Objects[FACILITY_REACTOR_MODEL]), 501.0, 1000.0, 0.3,False)
			EndIf
			
			If EntityY(Collider) < - 5000*RoomScale Then
				If e\EventState3 = 0 Then
					If e\EventState = 0 Then
						If ChannelPlaying(e\SoundCHN2)=False Then
							e\SoundCHN2 = PlaySound_Strict(LoadTempSound("SFX\General\Reactor_Idle.ogg"))
						EndIf
					EndIf
					If e\EventState <> 0 And e\EventState <> 3 Then
						If ChannelPlaying(e\SoundCHN2)=False Then
							e\SoundCHN2 = PlaySound_Strict(LoadTempSound("SFX\General\Reactor_Power_Up.ogg"))
						EndIf
					EndIf
				EndIf
			EndIf
			
			e\Sound = LoadSound_Strict("SFX\General\Spark_Short.ogg")
			
			If TaskExists(TASK_TURN_ON_REACTOR) Then
			
				If e\EventState = 0 Then
					UpdateButton(e\room\Objects[FACILITY_REACTOR_BUTTON])
					If d_I\ClosestButton = e\room\Objects[FACILITY_REACTOR_BUTTON] And KeyHitUse Then
						PlaySound_Strict ButtonSFX5%
						e\EventState = 1
						e\EventState2 = e\EventState2 + FPSfactor
						ShouldPlay = MUS_NULL
						If e\EventState3 = 0 Then
							If ChannelPlaying(e\SoundCHN)=False Then
								e\SoundCHN = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Reactor_Explosion.ogg"))
							EndIf
						EndIf
						If e\EventState3 <> 0 Then
							If ChannelPlaying(e\SoundCHN)=False Then
								e\SoundCHN = PlaySound_Strict(LoadTempSound("SFX\General\Reactor_Stop.ogg"))
							EndIf
						EndIf
						If TaskExists(TASK_TURN_ON_REACTOR) And (Not TaskExists(TASK_STOP_REACTOR)) Then
							FailTask(TASK_TURN_ON_REACTOR)
							BeginTask(TASK_STOP_REACTOR)
						EndIf
					EndIf
				EndIf
				
			EndIf
			If TaskExists(TASK_STOP_REACTOR) Then	
				If e\EventState = 1 Then
					UpdateButton(e\room\Objects[FACILITY_REACTOR_BUTTON_2])
					If d_I\ClosestButton = e\room\Objects[FACILITY_REACTOR_BUTTON_2] And KeyHitUse Then
						PlaySound_Strict ButtonSFX5%
						e\EventState = 2
					EndIf
				EndIf
				
				If e\EventState <> 0 Then
					e\EventState2 = e\EventState2 + FPSfactor
					ShouldPlay = MUS_NULL
				EndIf
				
				If e\EventState2 > 0 And e\EventState2 < 70*50 Then
					If Rand(50) = 1 Then
						PlaySound2(e\Sound, Camera, e\room\Objects[FACILITY_REACTOR_BUTTON], 3.0, 0.4)
						If ParticleAmount > 0 Then
							For i = 0 To (2 + (1 * (ParticleAmount - 1)))
								p.Particles = CreateParticle(EntityX(e\room\Objects[FACILITY_REACTOR_BUTTON],True), EntityY(e\room\Objects[FACILITY_REACTOR_BUTTON],True)+Rnd(0.0,0.05), EntityZ(e\room\Objects[FACILITY_REACTOR_BUTTON],True),7, 0.002, 0.0, 25.0)
								p\speed = Rnd(0.005, 0.03) : p\size = Rnd(0.005, 0.0075) : p\Achange = -0.05
								RotateEntity(p\pvt, Rnd(-20.0, 0.0), e\room\angle, 0.0)
								ScaleSprite(p\obj, p\size, p\size)
							Next
						EndIf	
					EndIf
				EndIf
				
				If e\EventState <> 2 Then
					If e\EventState2 >= 70*46.7 And e\EventState2 < 70*46.8 Then
						If TaskExists(TASK_STOP_REACTOR) Then
							FailTask(TASK_STOP_REACTOR)
						EndIf
						If ChannelPlaying(e\SoundCHN) Then
							StopChannel(e\SoundCHN)
						EndIf
						If ChannelPlaying(e\SoundCHN2) Then
							StopChannel(e\SoundCHN2)
						EndIf
						ExplosionTimer = 1
						DeathMSG="Facility at [REDACTED] was destroyed by a huge explosion, caused by reactor overheat.Everyone, who were inside the facility died."
					EndIf
				Else
					e\EventState3 = e\EventState3 + FPSfactor
					If e\EventState3 > 0 And e\EventState3 < 70*5 Then
						e\EventState = 3
						If e\EventState = 3 Then
							If ChannelPlaying(e\SoundCHN2) Then
								StopChannel(e\SoundCHN2)
							EndIf
						EndIf
						If ChannelPlaying(e\SoundCHN) Then
							StopChannel(e\SoundCHN)
							e\SoundCHN2 = StreamSound_Strict("SFX\Music\Misc\Reactor_Explosion_End.ogg", opt\VoiceVol, 0)
							e\SoundCHN2_isStream = True
							e\EventState = 4
						EndIf
						CameraShake = 10
						If TaskExists(TASK_STOP_REACTOR) And (Not TaskExists(TASK_COME_BACK_TO_GUARD_2)) Then
							EndTask(TASK_STOP_REACTOR)
							If (Not ecst\KilledGuard) Then
								BeginTask(TASK_COME_BACK_TO_GUARD_2)
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
		
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D