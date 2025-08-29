import { useEffect, useRef } from "react";
import SockJS from 'socks-client';
import { Client } from '@stomp/stompjs';

export function webSocket() {
    const stompClient = useRef<Client | null>(null);


    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws');
        const client = new Client({
            webSocketFactory: () => socket,
        })
    })
}