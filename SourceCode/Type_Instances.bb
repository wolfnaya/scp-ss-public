;! ~ A file that stores the global accessors for the type instance variables

; ~ Global accessors that are memory-persistent

Global fps.FPSSettings = New FPSSettings
Global achv.Achievements = New Achievements
Global gc.GameConstants = New GameConstants
Global co.Controller = New Controller
Global fo.Fonts = New Fonts
Global ft.FixedTimesteps = New FixedTimesteps
Global gv.GlobalVariables = New GlobalVariables
Global kb.KeyBinds = New KeyBinds
Global opt.Options = New Options
Global aud.AudioControl = New AudioControl
Global cpt.Chapters = New Chapters
Global clm.ClassicMode = New ClassicMode
Global m_msg.Messages = New Messages
Global ecst.EventConstants = New EventConstants

; ~ Global accessors that will only be loaded into memory when necessary

; ~ [SCPs]
Global I_005.SCP005
Global I_008.SCP008
Global I_016.SCP016
Global I_035.SCP035
Global I_059.SCP059
Global I_109.SCP109
Global I_127.SCP127
Global I_198.SCP198
Global I_207.SCP207
Global I_268.SCP268
Global I_330.SCP330
Global I_357.SCP357
Global I_402.SCP402
Global I_409.SCP409
Global I_427.SCP427
Global I_500.SCP500
Global I_714.SCP714
Global I_1025.SCP1025
Global I_1033RU.SCP1033RU
Global I_1079.SCP1079
Global I_1102RU.SCP1102RU
; ~ [PLAYER]
Global g_I.GunInstance
Global wbi.WearibleItems
Global psp.PlayerSP
Global mpl.MainPlayer
Global mtfd.MTFDialogue
Global mi_I.MissionInstance
Global hds.HazardousDefenceSuit
; ~ [MULTIPLAYER]
Global mp_I.MultiplayerInstance
Global mp_O.MultiplayerOptions
Global pll.PlayerList
; ~ [MENU]
Global m_I.MenuInstance
Global I_MIG.MenuImages
Global m3d.Menu3DInstance
Global mns.MenuSecrets
Global mnsh.MenuShop
; ~ [MAP]
Global d_I.DoorInstance

;~IDEal Editor Parameters:
;~C#Blitz3D