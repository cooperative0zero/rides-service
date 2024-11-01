package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/api/v1/rides")
        method PUT()
        headers {
            contentType applicationJson()
        }
        body(
                [
                        id: 1,
                        passengerId: 1,
                        departureAddress: 'New Departure address 1',
                        destinationAddress: 'Destination address 1',
                        rideStatus: 'CREATED',
                        creationDate: '2024-04-09T10:15:30+07:00'
                ],
        )
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                [
                        id: 1,
                        passengerId: 1,
                        departureAddress: 'New Departure address 1',
                        destinationAddress: 'Destination address 1',
                        rideStatus: 'CREATED'
                ]
        )
    }
}
