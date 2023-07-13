package jeresources.forge;

import jeresources.platform.IModInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.resource.PathPackResources;

import java.util.List;

public class ModInfo implements IModInfo {

    private IModFileInfo modFile;

    public ModInfo(IModFileInfo modFile) {
        this.modFile = modFile;
    }

    @Override
    public String getName() {
        return modFile.moduleName();
    }

    @Override
    public List<? extends PackResources> getPackResources() {
        Path source = modFile.getFile().getFilePath();
        // for nested jars, the file path is actually a PathPath, it's fileSystem is a
        // PathFileSystem, but the source.fileSystem.target is a UnionPath pointing to
        // a UnionFileSystem which is what forge uses, which is what we want, so that
        // it matches internal registers, without this we'll get 'Wrong filesystem'
        // errors even though they're the same file pointing to the same thing
        //
        // tldr: if it's a PathFileSystem, make it a UnionFileSystem
        if (PathPath.class.isAssignableFrom(source.getClass())) {
            PathFileSystem pfs = ((PathPath)source).getFileSystem();
            source = pfs.getTarget();
        }
        return List.of(new PathPackResources(modFile.getFile().getFileName(), source));
    }
}
