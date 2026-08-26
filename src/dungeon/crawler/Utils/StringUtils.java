package dungeon.crawler.Utils;

public class StringUtils {
    public static String format(final String format, final Object... args) {

        String[] split = format.split("%s", -1);
        
        final StringBuilder buffer = new StringBuilder();
        
        int loops = Math.min(split.length - 1, args.length);
        
        for (int i = 0; i < loops; i++) {
            buffer.append(split[i]);
            buffer.append(String.valueOf(args[i]));
        }
        
        buffer.append(split[split.length - 1]);
        
        return buffer.toString();
    }
}
