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

import components.Button;
import engineTester.MainGameLoop;
import entities.Camera;
import entities.Entity;
import entities.Light;
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
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainMenu
{
	private static GUIText menuText;
	
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
		
		//TERRAIN
		List<Terrain> terrains = new ArrayList<Terrain>();
		Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap);	
		terrains.add(terrain);
		
        List<Entity> entities = new ArrayList<Entity>();
        List<Entity> normalMapEntities = new ArrayList<Entity>();
        
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
        
        Camera cameraMenu = new Camera();
        cameraMenu.setPosition(new Vector3f(500, 70, 500));
		MasterRenderer rendererMenu = new MasterRenderer(loader, cameraMenu);
		GuiRenderer guiRendererMenu = new GuiRenderer(loader);
		
		FontType menuFont = new FontType(loader.loadFontTexture("candara"), "candara");
		FontType arialMenuFont = new FontType(loader.loadFontTexture("segoeui"), "segoeui");
		
		List<GuiTexture> menuGUIs = new ArrayList<GuiTexture>();
		
		Button newGame = new Button(0, 0.05f, 0.41f, 0.25f, 0.075f, "New Game");
		
		Button settingsButton = new Button(0, 0.05f, 0.11f, 0.25f, 0.075f, "Settings");
		
		WaterFrameBuffers fbosMenu = new WaterFrameBuffers();
		WaterShader waterShaderMenu = new WaterShader();
		WaterRenderer waterRendererMenu = new WaterRenderer(loader, waterShaderMenu, rendererMenu.getProjectionMatrix(), fbosMenu);
		List<WaterTile> watersMenu = new ArrayList<WaterTile>();
		
		WaterTile waterMenu = new WaterTile(480, 480, 0);
		watersMenu.add(waterMenu);
		
		Fbo multisampleFboMenu = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo outputFboMenu = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		Fbo outputFbo2Menu = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		PostProcessing.init(loader);
		
		while(MainGameLoop.gameState == 0)
		{	
			rendererMenu.renderShadowMap(entities, normalMapEntities, sun);
			
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			menuText = new GUIText("Orbis Terrae", 4, menuFont, new Vector2f(0.37f, 0.05f), 1, false);
			menuText.setColour(0.1f, 1.0f, 0.0f);
								
			cameraMenu.setAngleAroundPlayer(cameraMenu.getAngleAroundPlayer() + 1);
			cameraMenu.moveNoPlayer();
			
			//REFLECTION
			fbosMenu.bindReflectionFrameBuffer();
			float distance = 2 * (cameraMenu.getPosition().y - waterMenu.getHeight() + 1f);
			cameraMenu.getPosition().y -= distance;
			cameraMenu.invertPitch();
			
			rendererMenu.renderAll(entities, normalMapEntities, terrains, lights, cameraMenu, new Vector4f(0, 1, 0, -waterMenu.getHeight()));
			
			cameraMenu.getPosition().y += distance;
			cameraMenu.invertPitch();
			
			//REFRACTION
			fbosMenu.bindRefractionFrameBuffer();		
			
			rendererMenu.renderAll(entities, normalMapEntities, terrains, lights, cameraMenu, new Vector4f(0, -1, 0, waterMenu.getHeight()));
			
			//SCREEN RENDER
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			fbosMenu.unbindCurrentFrameBuffer();
			
			multisampleFboMenu.bindFrameBuffer();		
			
			rendererMenu.renderAll(entities, normalMapEntities, terrains, lights, cameraMenu, new Vector4f(0, -1, 0, 10000));
			
			waterRendererMenu.render(watersMenu, cameraMenu, sun);
			multisampleFboMenu.unbindFrameBuffer();
			multisampleFboMenu.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, outputFboMenu);
			multisampleFboMenu.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, outputFbo2Menu);
			
			PostProcessing.doPostProcessing(outputFboMenu.getColourTexture(), outputFbo2Menu.getColourTexture());			
			
			newGame.drawButton((2.0f * Mouse.getX()) / Display.getWidth() - 1f, (2.0f * Mouse.getY()) / Display.getHeight() - 1f, loader, guiRendererMenu, 0.46f, 0.26f, 2);
			if(newGame.mousePressed((2.0f * Mouse.getX()) / Display.getWidth() - 1f, (2.0f * Mouse.getY()) / Display.getHeight() - 1f))
			{
				newGame.buttonText.delete();
				menuText.delete();
				settingsButton.buttonText.delete();
				Game.run();
			}
			
			settingsButton.drawButton((2.0f * Mouse.getX()) / Display.getWidth() - 1f, (2.0f * Mouse.getY()) / Display.getHeight() - 1f, loader, guiRendererMenu, 0.48f, 0.41f, 2);
			if(settingsButton.mousePressed((2.0f * Mouse.getX()) / Display.getWidth() - 1f, (2.0f * Mouse.getY()) / Display.getHeight() - 1f))
			{
				newGame.buttonText.delete();
				menuText.delete();
				settingsButton.buttonText.delete();
				SettingsMenu.run();
			}
			
			guiRendererMenu.render(menuGUIs);
			
			TextMaster.render();
			
			if(Settings.fullscreen)
			{
				try
				{
					Display.setFullscreen(true);
				} 
				catch (LWJGLException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					Display.setFullscreen(false);
				}
				catch (LWJGLException e)
				{
					e.printStackTrace();
				}
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_F11))
			{
				Settings.setFullscreen();
			}
						
			newGame.buttonText.delete();
			menuText.delete();
			settingsButton.buttonText.delete();
			DisplayManager.updateDisplay();
		}
		
		PostProcessing.cleanUp();
		multisampleFboMenu.cleanUp();
		TextMaster.cleanUp();
		fbosMenu.cleanUp();
		waterShaderMenu.cleanUp();
		guiRendererMenu.cleanUp();
		rendererMenu.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	
	public static void stop()
	{
		PostProcessing.cleanUp();
		TextMaster.cleanUp();
		//DisplayManager.closeDisplay();
	}
}
