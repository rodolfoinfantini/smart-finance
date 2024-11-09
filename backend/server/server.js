import net from 'net'

const connection = net.connect(
    { host: process.env.SERVER_HOST ?? 'localhost', port: process.env.SERVER_PORT ?? 3001 },
    () => {
        console.log('Connected to server')
        connection.setNoDelay(true)
        process.on('exit', () => {
            console.log('Closing connection')
            connection.write('exit\n')
            connection.end()
        })
        process.on('SIGINT', () => {
            connection.write('exit\n')
            connection.end()
            process.exit()
        })
        process.on('SIGTERM', () => {
            connection.write('exit\n')
            connection.end()
            process.exit()
        })

        connection.addListener('data', (data) => {
            data = data.toString()
            if (data.includes('Server is shutting down')) {
                console.log('Server is shutting down')
                process.exit()
            }
        })
    },
)

export default function Server() {
    return {
        newConnection: () => {
            return new Promise((resolve, _) => {
                const connection = net.connect(
                    {
                        host: process.env.SERVER_HOST ?? 'localhost',
                        port: process.env.SERVER_PORT ?? 3001,
                    },
                    () => {
                        connection.setNoDelay(true)
                        resolve({
                            sendEvent: async (event, data) => {
                                return new Promise((resolve, reject) => {
                                    connection.on('data', (data) => {
                                        data = data.toString()
                                        if (data.startsWith('error '))
                                            return reject(data.substring(6))
                                        if (!data.startsWith(`${event} `)) return
                                        data = data.substring(event.length + 1)

                                        connection.write('exit\n')
                                        connection.end()

                                        resolve(JSON.parse(data))
                                    })
                                    const message = `${event} ${JSON.stringify(data)}`
                                    connection.write(`${message}\n`)
                                })
                            },
                        })
                    },
                )
            })
        },
        sendEvent: async (event, data) => {
            return new Promise((resolve, reject) => {
                connection.on('data', (data) => {
                    data = data.toString()
                    if (data.startsWith('error ')) return reject(data.substring(6))
                    if (!data.startsWith(`${event} `)) return
                    data = data.substring(event.length + 1)
                    resolve(JSON.parse(data))
                })
                const message = `${event} ${JSON.stringify(data)}`
                connection.write(`${message}\n`)
            })
        },
    }
}
