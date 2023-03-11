Global Ark_Blur_Image%, Ark_Blur_Texture%, Ark_Sw%, Ark_Sh%
Global Ark_Blur_Cam%

Function CreateBlurImage()
	;Create blur Camera
	Local cam% = CreateCamera()
	CameraProjMode cam,2
	CameraZoom cam,0.1
	CameraClsMode cam, 0, 0
	CameraRange cam, 0.1, 1.5
	MoveEntity cam, 0, 0, 10000
	Ark_Blur_Cam = cam
	
	Ark_Sw = opt\GraphicWidth;GraphicsWidth()
	Ark_Sh = opt\GraphicHeight;GraphicsHeight()
	CameraViewport cam,0,0,Ark_Sw,Ark_Sh
	
	;Create sprite
	Local spr% = CreateMesh(cam)
	Local sf% = CreateSurface(spr)
	AddVertex sf, -1, 1, 0, 0, 0
	AddVertex sf, 1, 1, 0, 1, 0
	AddVertex sf, -1, -1, 0, 0, 1
	AddVertex sf, 1, -1, 0, 1, 1
	AddTriangle sf, 0, 1, 2
	AddTriangle sf, 3, 2, 1
	EntityFX spr, 17
	ScaleEntity spr, 4096.0 / Float(Ark_Sw), 4096.0 / Float(Ark_Sw), 1
	PositionEntity spr, 0, 0, 1.0001
	EntityOrder spr, -100000
	EntityBlend spr, 1
	Ark_Blur_Image = spr
	
	;Create blur texture
	;ark_blur_texture = CreateTextureUsingCacheSystem(2048, 2048, 256)
	Ark_Blur_Texture = CreateTextureUsingCacheSystem(4096, 4096, 0)
	EntityTexture spr, Ark_Blur_Texture
End Function

Function UpdateBlur(power#)
	
	EntityAlpha Ark_Blur_Image, power#
	
	;CopyRect ark_sw / 2 - 1024, ark_sh / 2 - 1024, 2048, 2048, 0, 0, BackBuffer(), TextureBuffer(ark_blur_texture)
	CopyRect 0, 0, opt\GraphicWidth, opt\GraphicHeight, 2048.0 - opt\GraphicWidth/2, 2048.0 - opt\GraphicHeight/2, BackBuffer(), TextureBuffer(Ark_Blur_Texture)
	
End Function







;~IDEal Editor Parameters:
;~C#Blitz3D