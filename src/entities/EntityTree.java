package entities;

import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import textures.ModelTexture;

public class EntityTree extends Entity
{
	
	public EntityTree(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale)
	{
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
//	@Override
//	public void setModel(TexturedModel model)
//	{
//		ModelData treeData = OBJFileLoader.loadOBJ("tree");
//		RawModel treeRawModel = loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(), treeData.getNormals(), treeData.getIndices());
//        TexturedModel treeModel = new TexturedModel(treeRawModel, new ModelTexture(loader.loadTexture("tree")));
//		super.setModel(treeModel);
//	}
}
