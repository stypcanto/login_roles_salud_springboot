import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')

  return {
    plugins: [react()],
    base: '/',
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src'),
      },
    },
    define: {
      'process.env': {}, // 👈 evita "process is not defined"
    },
    server: {
      proxy: {
        '/auth': {
          target: env.VITE_API_URL || 'http://localhost:8080',
          changeOrigin: true,
        },
        // 👉 aquí puedes agregar más proxys si necesitas
        // '/api': { target: env.VITE_API_URL, changeOrigin: true }
      },
    },
  }
})
