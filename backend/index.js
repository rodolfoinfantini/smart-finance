import express from 'express'
import cors from 'cors'
import initRoutes from './routers/routers.js'
import dotenv from 'dotenv'
import { sync } from './data/entities/models.js'

dotenv.config()

await sync()

const app = express()
app.use(cors())
app.use(express.json())

initRoutes(app)

app.listen(process.env.PORT ?? 3000, () => {
    console.log('Server is running on port ' + process.env.PORT ?? 3000)
})
