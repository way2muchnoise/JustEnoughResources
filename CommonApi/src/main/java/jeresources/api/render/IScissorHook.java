package jeresources.api.render;

public interface IScissorHook {
    class ScissorInfo {
        public int x, y, width, height;

        public ScissorInfo(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    /**
     * Change the current scissor info
     *
     * @param scissorInfo the current context
     * @return changed version of the scissor for your needs
     */
    ScissorInfo transformScissor(ScissorInfo scissorInfo);
}
