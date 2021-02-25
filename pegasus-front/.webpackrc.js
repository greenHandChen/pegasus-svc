export default {
  extraBabelPlugins: [
    ["import", {"libraryName": "antd", "libraryDirectory": "es", "style": "css"}]
  ],
  define: {
    'process.env': {
      API_HOST: process.env.API_HOST,
      CLIENT_ID: process.env.CLIENT_ID
    }
  }
}