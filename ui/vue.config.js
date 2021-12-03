module.exports = {
  devServer: {
    proxy: {
      "/": {
        target: `${process.env.SW_PROXY_TARGET || "http://127.0.0.1:7006"}`,
        changeOrigin: true,
      },
    },
  },
};
