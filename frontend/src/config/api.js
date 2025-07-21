export const apiUrl = import.meta.env.VITE_API_URL;


//Así, el día que necesites llamar a tu backend, solo haces:
//import { apiUrl } from '../config/api';

// y luego:
//fetch(`${apiUrl}/ruta`);