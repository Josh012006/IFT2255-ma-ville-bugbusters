

export function formatDate(date: Date, withHour=true) {

    if(typeof date === "string") {
        date = new Date(date);
    }

    
    const pad = n => n.toString().padStart(2, '0');

    const day = pad(date.getDate());
    const month = pad(date.getMonth() + 1); // getMonth() est 0-based
    const year = date.getFullYear();
    const hour = pad(date.getHours());
    const minute = pad(date.getMinutes());

    const formatted = `${day}/${month}/${year}` + ((withHour)? `  ${hour}:${minute}` : "");
    return formatted;
}