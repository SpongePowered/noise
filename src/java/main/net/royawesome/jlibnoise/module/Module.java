package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;

public abstract class Module {
	protected Module[] SourceModule;

	public Module(int sourceModuleCount){
		SourceModule = null;

		// Create an array of pointers to all source modules required by this
		// noise module.  Set these pointers to NULL.
		if (sourceModuleCount > 0) {
			SourceModule = new Module[sourceModuleCount];
			for (int i = 0; i < sourceModuleCount; i++) {
				SourceModule[i] = null;
			}
		} else {
			SourceModule = null;
		}

	}

	public Module getSourceModule(int index){
		if (index >= GetSourceModuleCount () || index < 0 || SourceModule[index] == null) {
			throw new NoModuleException();
		}
		return (SourceModule[index]);

	}

	public void SetSourceModule (int index,  Module sourceModule)
	{
		if(SourceModule == null) return;
		if (index >= GetSourceModuleCount() || index < 0) {
			throw new IllegalArgumentException("Index must be between 0 and GetSourceMoudleCount()");
		}
		SourceModule[index] = sourceModule;
	}


	public abstract int GetSourceModuleCount();
	public abstract double GetValue (double x, double y, double z);
}
