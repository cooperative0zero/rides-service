package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/api/v1/rides")
        method POST()
        headers {
            contentType applicationJson()
        }
        body(
                [
                        passengerId: 1,
                        departureAddress: 'Departure address 1',
                        destinationAddress: 'Destination address 1',
                        rideStatus: 'CREATED',
                ]
        )
    }

    response {
        status CREATED()
        headers {
            contentType applicationJson()
        }
        body(
                [
                        id: 3,
                        passengerId: 1,
                        departureAddress: 'Departure address 1',
                        destinationAddress: 'Destination address 1',
                        rideStatus: 'CREATED',
                ]
        )
    }
}
