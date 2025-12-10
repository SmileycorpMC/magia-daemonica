package net.smileycorp.magiadaemonica.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class ModLocalization implements ISelectiveResourceReloadListener {

    public static final ModLocalization INSTANCE = new ModLocalization();

    private final Map<ResourceLocation, Map<Locale, String>> dictionary = Maps.newHashMap();
    private final List<ResourceLocation> resources = Lists.newArrayList();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (!resourcePredicate.test(VanillaResourceType.LANGUAGES)) return;
        dictionary.clear();
        Set<Locale> languages = Sets.newHashSet(Locale.US);
        Locale locale = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getJavaLocale();
        if (locale != Locale.US) languages.add(locale);
        for (ResourceLocation loc : resources) {
            for (Locale lang : languages) {
                try (IResource resource = resourceManager.getResource(new ResourceLocation(loc.getResourceDomain(),
                        "localization/" + locale.toString().toLowerCase(Locale.US) + "/" + loc.getResourcePath() + ".txt"))) {
                    Map<Locale, String> localeMap = dictionary.computeIfAbsent(loc, rl -> Maps.newHashMap());
                    byte[] data = new byte[resource.getInputStream().available()];
                    resource.getInputStream().read(data);
                    localeMap.put(lang, new String(data));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> getText(ResourceLocation loc, int width, Object... args) {
        Minecraft mc = Minecraft.getMinecraft();
        Map<Locale, String> localeMap = dictionary.get(loc);
        Locale locale = mc.getLanguageManager().getCurrentLanguage().getJavaLocale();
        String str = String.format(locale, localeMap.getOrDefault(locale, localeMap.get(Locale.US)), args);
        str = str.replace("\r", "");
        List<String> text = Lists.newArrayList();
        int position = 0;
        while (position < str.length()) {
            //if (description.size() >= 7) break;
            int size = Math.min(width / 3, str.length() - position);
            while (mc.fontRenderer.getStringWidth(str.substring(position, position + size)) > width) size--;
            int newPos = position + size;
            if (str.substring(position, newPos).contains("\n")) {
                int i = str.substring(position, newPos).indexOf("\n");
                text.add(str.substring(position, position + i));
                position = position + i + 1;
                continue;
            }
            if (newPos >= str.length()) {
                text.add(str.substring(position));
                break;
            }
            for (int i = 0; i <= size; i++) {
                if (i == size) {
                    text.add(str.substring(position, newPos + 1));
                    position = newPos;
                    break;
                } else if (str.charAt(newPos - i) == ' ') {
                    text.add(str.substring(position, newPos - i + 1));
                    position = newPos - i + 1;
                    break;
                }
            }
        }
        return text;
    }

    public void register(ResourceLocation resource) {
        resources.add(resource);
    }
    
}
