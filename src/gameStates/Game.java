package gameStates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import settings.Settings;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class Game
{	
	public static float time = 0;
	public static boolean gameover = false;
	public static int wood = 0;
	public static int money = 0;
	
	public static void run()
	{
		Loader loader = new Loader();
		TextMaster.init(loader);
		
		//TERRAIN TEXTURES
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//ENTITIES
		
		//TREE
		ModelData treeData = OBJFileLoader.loadOBJ("tree");
		RawModel treeRawModel = loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(), treeData.getNormals(), treeData.getIndices());
        TexturedModel treeModel = new TexturedModel(treeRawModel, new ModelTexture(loader.loadTexture("tree")));
        
        //CHERRY TREE
        TexturedModel cherrytreeModel = new TexturedModel(OBJLoader.loadObjModel("cherry", loader), new ModelTexture(loader.loadTexture("cherry")));
        cherrytreeModel.getTexture().setHasTransparency(true);
        cherrytreeModel.getTexture().setShineDamper(10);
        cherrytreeModel.getTexture().setReflectivity(0.5f);
        cherrytreeModel.getTexture().setSpecularMap(loader.loadTexture("cherryS"));
        
        //LOW POLY TREE
        ModelData lpTreeData = OBJFileLoader.loadOBJ("lowPolyTree");
		RawModel lpTreeRawModel = loader.loadToVAO(lpTreeData.getVertices(), lpTreeData.getTextureCoords(), lpTreeData.getNormals(), lpTreeData.getIndices());
        TexturedModel lpTreeModel = new TexturedModel(lpTreeRawModel, new ModelTexture(loader.loadTexture("lowPolyTree")));
        
        //GRASS
//        ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
//		RawModel grassRawModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
//        TexturedModel grassModel = new TexturedModel(grassRawModel, new ModelTexture(loader.loadTexture("grassTexture")));
//        grassModel.getTexture().setHasTransparency(true);
//        grassModel.getTexture().setUseFakeLighting(true);
        
        //FERNS
        ModelData fernData = OBJFileLoader.loadOBJ("fern");
		RawModel fernRawModel = loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
        TexturedModel fernModel = new TexturedModel(fernRawModel, fernTextureAtlas);
        fernModel.getTexture().setHasTransparency(true);
        
        //FLOWERS
//        ModelData flowerData = OBJFileLoader.loadOBJ("grassModel");
//		RawModel flowerRawModel = loader.loadToVAO(flowerData.getVertices(), flowerData.getTextureCoords(), flowerData.getNormals(), flowerData.getIndices());
//        TexturedModel flowerModel = new TexturedModel(flowerRawModel, new ModelTexture(loader.loadTexture("flower")));
//        flowerModel.getTexture().setHasTransparency(true);
//        flowerModel.getTexture().setUseFakeLighting(true);
        
        //LAMP
        TexturedModel lampModel = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));
        lampModel.getTexture().setUseFakeLighting(true);
        lampModel.getTexture().setSpecularMap(loader.loadTexture("lampS"));
        
        //Barrel
        TexturedModel barrelModel= new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader), new ModelTexture(loader.loadTexture("barrel")));
        barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
        barrelModel.getTexture().setShineDamper(10);
        barrelModel.getTexture().setReflectivity(0.5f);
        
        //Box
        TexturedModel boxModel= new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader), new ModelTexture(loader.loadTexture("crate")));
        boxModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
        boxModel.getTexture().setShineDamper(10);
        boxModel.getTexture().setReflectivity(0.5f);
        
        //Boulder
        TexturedModel boulderModel= new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader), new ModelTexture(loader.loadTexture("boulder")));
        boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
        boulderModel.getTexture().setShineDamper(10);
        boulderModel.getTexture().setReflectivity(0.5f);
		
        //LIGHTS
        //DAY
		Light sun = new Light(new Vector3f(20000000, 20000000, 20000000),new Vector3f(0.8f, 0.8f, 0.8f));
		List<Light> lights = new ArrayList<Light>();
		lights.add(sun);
		
		//NIGHT
//		Light moon = new Light(new Vector3f(20000, 20000, 20000),new Vector3f(0.2f, 0.2f, 0.2f));
//		List<Light> nightLights = new ArrayList<Light>();
//		nightLights.add(moon);
//		Light lampLight = new Light(new Vector3f(2, 15, 2), new Vector3f(2, 2, 2), new Vector3f(1, 0.01f, 0.002f));
//		nightLights.add(lampLight);
		
		//DAWN/DUSK
//		Light dimSun = new Light(new Vector3f(20000, 20000, 20000),new Vector3f(0.5f, 0.5f, 0.5f));
//		List<Light> dawnDuskLights = new ArrayList<Light>();
//		dawnDuskLights.add(dimSun);
//		Light lampDimLight = new Light(new Vector3f(2, 15, 2), new Vector3f(1, 1, 1), new Vector3f(1, 0.01f, 0.002f));
//		dawnDuskLights.add(lampDimLight);
		
		//TERRAIN
		List<Terrain> terrains = new ArrayList<Terrain>();
		Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap);	
		terrains.add(terrain);
		
        List<Entity> entities = new ArrayList<Entity>();
        List<Entity> normalMapEntities = new ArrayList<Entity>();
        
        //LAMPS LIST
        float lampX = 2;
        float lampZ = 2;
        float lampY = terrain.getHeightOfTerrain(lampX, lampZ);
      	Entity lampEntity = new Entity(lampModel, new Vector3f(lampX, lampY, lampZ), 0, 0, 0, 1);
      	entities.add(lampEntity);
      	Light lampLight = new Light(new Vector3f(lampX, lampY + 15, lampZ), new Vector3f(2, 2, 2), new Vector3f(1, 0.01f, 0.002f));
      	//lights.add(lampLight);
        
		//FERN LIST
        Random random = new Random();
        for(int i = 0; i < 400; i++)
        {
        	if(i % 3 == 0)
        	{
        		float x = random.nextFloat() * 1240;
        		float z = random.nextFloat() * 1240;
        		float y = terrain.getHeightOfTerrain(x, z);
        		if(y >= 0)
        		entities.add(new Entity(fernModel, new Vector3f(x, y, z), random.nextInt(4), 0, random.nextFloat() * 360, 0, 0.5f));
        	}
        }
        //TREE LIST
        for(int i = 0; i < 400; i++)
        {
        	if(i % 3 == 0)
        	{
        		float x = random.nextFloat() * 1240;
        		float z = random.nextFloat() * 1240;
        		float y = terrain.getHeightOfTerrain(x, z);
        		if(y >= 0)
        		entities.add(new Entity(treeModel, new Vector3f(x, y, z), 0, random.nextFloat(), 0, 4));
        	}
        }
        //CHERRY TREE LIST
        for(int i = 0; i < 400; i++)
        {
        	if(i % 3 == 0)
        	{
        		float x = random.nextFloat() * 1240;
        		float z = random.nextFloat() * 1240;
        		float y = terrain.getHeightOfTerrain(x, z);
        		if(y >= 0)
        		entities.add(new Entity(cherrytreeModel, new Vector3f(x, y, z), 0, random.nextFloat(), 0, 4));
        	}
        }
        //LP TREE LIST
        for(int i = 0; i < 150; i++)
        {
        	if(i % 4 == 0)
        	{
        		float x = random.nextFloat() * 1240;
        		float z = random.nextFloat() * 1240;
        		float y = terrain.getHeightOfTerrain(x, z);
        		if(y >= 0)
        		entities.add(new Entity(lpTreeModel, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 1));
        	}
        }
        
        //Barrel
        float barrelX = 30;
        float barrelZ = 10;
        float barrelY = terrain.getHeightOfTerrain(barrelX, barrelZ) + 2.5f;
        normalMapEntities.add(new Entity(barrelModel, new Vector3f(barrelX, barrelY, barrelZ), 0, 0, 0, 0.5f));
        
        //Box
        float boxX = 10;
        float boxZ = 10;
        float boxY = terrain.getHeightOfTerrain(boxX, boxZ) + 2.5f;
        normalMapEntities.add(new Entity(boxModel, new Vector3f(boxX, boxY, boxZ), 0, 0, 0, 0.025f));
		
		//PLAYER
		ModelData playerData = OBJFileLoader.loadOBJ("person");
		RawModel playerRawModel = loader.loadToVAO(playerData.getVertices(), playerData.getTextureCoords(), playerData.getNormals(), playerData.getIndices());
		TexturedModel playerModel = new TexturedModel(playerRawModel, new ModelTexture(loader.loadTexture("playerTexture")));
		
		Player player = new Player(playerModel, new Vector3f(0, 0, 0), 0, 180, 0, 0.6f);
		Camera camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer(loader, camera);
		
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		//TEXT
		FontType font = new FontType(loader.loadFontTexture("candara"), "candara");
		GUIText text = new GUIText("Orbis Terrae - TESTING VERSION", 1.5f, font, new Vector2f(0, 0), 1, false);
		text.setColour(0.0f, 1.0f, 0.1f);
		text.setBorderEdge(0.9f);
		text.setBorderWidth(0.2f);
		text.setOutlineColour(new Vector3f(1f, 0.1f, 0.2f));
		
		//RESOURCES
		GUIText woodText = new GUIText("Wood: " + wood, 1.5f, font, new Vector2f(0.9f, 0), 1, false);
		woodText.setColour(0.3f, 0.4f, 0.7f);
		
		//PLAYER INFO
		GUIText healthText = new GUIText("Health: " + player.health, 1.5f, font, new Vector2f(0.1f, 0.9f), 1, false);
		healthText.setColour(0.9f, 0.1f, 0.1f);
		
		//PLAYER INFO
		GUIText breathText = new GUIText("Breath: " + player.breath, 1.5f, font, new Vector2f(0.4f, 0.9f), 1, false);
		breathText.setColour(0.2f, 0.3f, 0.8f);
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		//GuiTexture guiLogo = new GuiTexture(loader.loadTexture("guiLogo"), new Vector2f(-0.8f, 0.9f), new Vector2f(0.2f, 0.2f));
		//guis.add(guiLogo);
		
		List<GuiTexture> deadGuis = new ArrayList<GuiTexture>();
		GuiTexture dead = new GuiTexture(loader.loadTexture("dead"), new Vector2f(0.7f, -0.5f), new Vector2f(2, 1.5f));
		deadGuis.add(dead);
				
		List<GuiTexture> uwGuis = new ArrayList<GuiTexture>();
		GuiTexture cameraDistort = new GuiTexture(loader.loadTexture("underwater"), new Vector2f(-0.8f, 0.8f), new Vector2f(2f, 2f));
		uwGuis.add(cameraDistort);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		//SkyboxRenderer skyRenderer = new SkyboxRenderer(loader, renderer.getProjectionMatrix());
		
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
		
		entities.add(player);
		
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		
		WaterTile water = new WaterTile(480, 480, 0);
		waters.add(water);
		
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleAtlas"), 4, true);
		ParticleTexture particleCosmic = new ParticleTexture(loader.loadTexture("cosmic"), 4, true);
		ParticleTexture particleFire = new ParticleTexture(loader.loadTexture("fire"), 8, false);
		
		ParticleSystem system = new ParticleSystem(particleTexture, 40, 10, 0.1f, 1, 1.6f);
		system.setLifeError(0.1f);
		system.setSpeedError(0.25f);
		system.setScaleError(0.5f);
		system.randomizeRotation();
		
		ParticleSystem cosmicSystem = new ParticleSystem(particleCosmic, 40, 10, 0.1f, 1, 1.6f);
		cosmicSystem.setLifeError(0.1f);
		cosmicSystem.setSpeedError(0.25f);
		cosmicSystem.setScaleError(0.5f);
		cosmicSystem.randomizeRotation();
		
		ParticleSystem fireSystem = new ParticleSystem(particleFire, 100, 10, 0.01f, 1, 1.6f);
		fireSystem.setLifeError(0.1f);
		fireSystem.setSpeedError(0.25f);
		fireSystem.setScaleError(0.5f);
		cosmicSystem.randomizeRotation();
		
		ParticleSystem flameSystem = new ParticleSystem(particleTexture, 100, 10, 0.01f, 1, 1.6f);
		flameSystem.setDirection(new Vector3f(0, 1, 0), 0.1f);
		flameSystem.setLifeError(0.1f);
		flameSystem.setSpeedError(0.25f);
		flameSystem.setScaleError(0.5f);
		flameSystem.randomizeRotation();
		
		Fbo multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		Fbo outputFbo2 = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		PostProcessing.init(loader);
		
		while(!Display.isCloseRequested())
		{
			if(time < 24000)
			{
				time += DisplayManager.getFrameTimeSeconds() * 10;
			}
			else
			{
				time = 0;
			}
			if(!gameover)
			{
				player.move(terrain);
				camera.move();
				picker.update();
				Mouse.setGrabbed(true);

				if(player.onFire)
				{
					flameSystem.generateParticles(player.getPosition());
					if(player.health > 0)
					{
						player.health = player.health - 0.05f;
					}
				}
				
				if(player.getPosition().getY() > -0.5f)
				{
					player.onFire = false;
				}
				
				Vector3f terrainPoint = picker.getCurrentTerrainPoint();
//				for(int i = 0; i < entities.size(); i++)
//				{
//					for(Entity entity : entities)
//					{
//						entity = entities.get(i);
//						//if(entity = new Entity)
//						if(entity.getPosition() == new Vector3f(terrainPoint.getX(), terrainPoint.getY(), terrainPoint.getZ()))
//						{
							if(Mouse.isButtonDown(1))
							{
								wood = wood + 1;
								woodText.setTextString("Wood: " + wood);
								woodText.update();
							}
//						}
//					}	
//				}
							
				breathText.setTextString("Breath: " + (int) player.breath);
				healthText.setTextString("Health: " + (int) player.health);
				breathText.update();
				healthText.update();
					
				ParticleMaster.update(camera);
				
				renderer.renderShadowMap(entities, normalMapEntities, sun);
				
				GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
				
				//REFLECTION
				fbos.bindReflectionFrameBuffer();
				float distance = 2 * (camera.getPosition().y - water.getHeight() + 1f);
				camera.getPosition().y -= distance;
				camera.invertPitch();
				
				renderer.renderAll(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight()));
				
				camera.getPosition().y += distance;
				camera.invertPitch();
				
				//REFRACTION
				fbos.bindRefractionFrameBuffer();		
				
				renderer.renderAll(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));
				
				//SCREEN RENDER
				GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
				fbos.unbindCurrentFrameBuffer();
				
				multisampleFbo.bindFrameBuffer();		
				
				renderer.renderAll(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 10000));
				
				waterRenderer.render(waters, camera, sun);
				ParticleMaster.renderParticles(camera);
				multisampleFbo.unbindFrameBuffer();
				multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, outputFbo);
				multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, outputFbo2);
				
				PostProcessing.doPostProcessing(outputFbo.getColourTexture(), outputFbo2.getColourTexture());
					
				if(camera.getPosition().y <= water.getHeight())
				{
					guiRenderer.render(uwGuis);
					renderer.RED = 0.0f;
					renderer.GREEN = 0.5f;
					renderer.BLUE = 0.6f;
					renderer.density = 0.01f;
				}
				else
				{
					renderer.RED = 0.5444f;
					renderer.GREEN = 0.62f;
					renderer.BLUE = 0.69f;
					renderer.density = 0.0039f;
				}
				
				guiRenderer.render(guis);
			}
			
			if(gameover)
			{
				guiRenderer.render(deadGuis);
			}
			TextMaster.render();
			
			DisplayManager.updateDisplay();		
		}
		
	//CLEANUP
	outputFbo2.cleanUp();
	outputFbo.cleanUp();
	PostProcessing.cleanUp();
	multisampleFbo.cleanUp();
	ParticleMaster.cleanUp();
	TextMaster.cleanUp();
	fbos.cleanUp();
	waterShader.cleanUp();
	guiRenderer.cleanUp();
	renderer.cleanUp();
	loader.cleanUp();
	DisplayManager.closeDisplay();
	}

}
