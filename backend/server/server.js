import net from 'net'

export default async function Server(multipleOperations = false) {
    return new Promise((resolve, reject) => {
        const connection = net.connect(
            { host: process.env.SERVER_HOST ?? 'localhost', port: process.env.SERVER_PORT ?? 3001 },
            () => {
                connection.setNoDelay(true)
                resolve({
                    sendEvent: async (event, data) => {
                        return new Promise((resolve, reject) => {
                            connection.on('data', (data) => {
                                data = data.toString()
                                if (!data.startsWith(event)) return
                                resolve(JSON.parse(data.replace(`${event} `, '')))

                                if (!multipleOperations) {
                                    connection.write('exit\n')
                                    connection.end()
                                }
                            })
                            connection.write(`${event} ${JSON.stringify(data)}\n`)
                        })
                    },
                    close: () => {
                        connection.write('exit\n')
                        connection.end()
                    },
                })
            },
        )
    })
}
