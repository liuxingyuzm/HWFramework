package com.huawei.g11n.tmr.address.es;

import com.huawei.g11n.tmr.util.Regexs;

public class ReguExp extends Regexs {
    private static final String BIG_WORDS_Q = "(?:\\s{0,2}(?:de|del|el))?\\s*(?:[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*(?:\\s(?:(?i)del|de|da|i|d|la|en|el))?\\s){0,2}[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*\\s*";
    private static final String BUI = ",?\\s{0,2}\\b(?:(?i)Universidad|Universitaria|museo|biblioteca|restaurante|cafetería|hotel|ayuntamiento|librería|taberna|Universitaria|empresa|hospital|supermercado|Peluquería|Tintorería|Mercado|rascacielos|Catedral|Mezquita|estadio|Iglesia|Torre|Clínica|castillo|sanatorio|monte|Confitería|Pescadería|tienda\\s{1,6}de\\s{1,6}discos|tienda\\s{1,6}de\\s{1,6}licores|tienda\\s{1,6}de\\s{1,6}muebles|lago|tienda\\s{1,6}de\\s{1,6}antigüedades|palacio|metro|subte|Hostel|playa|Universidades|puerto|Parque|plaza|edif|escuelas\\s{1,6}técnicas\\s{1,6}productivas|Kursaal|aeropuerto|los\\s{1,6}Alpes|tienda\\s{1,6}oficial|cerro|Disneyland|albergue|CORTE\\s{1,6}INGLES|los\\s{1,6}Palacios|los\\s{1,6}Valles|clinica|Galería|Conciergerie|bosque|Museum|Park|Villa|Parc|Plaça|Museu|Restaurant|Templo|Hammam|Apartamento|Cathedral|Estació|Alcazar|Caixa|Naval|Cuesta|Church|University|Bodegas|Bar|Farmacia|Mausoleos|Universitat|Zoo|Jardines|Monumento|Basílica|Apartaments|Hostal|Teatre|Cathédrale|Farmàcia|Llibreria|Gelateria|Igresia|College|Muelle|Academia|aparcamiento|Montaña|Cafeteria|Zoobotanico|Universitdad|Supermercados|Apartamentos|Apartment|Apartamenos|Apartanebtos|Universitad|Cueva|Casino|Convento|Almacén|Cervecería|Embalse|Hospedería|Bazar|Pedralbes|Galeria|Librerias|Alameda|Market|Pabellón|Galerías)(?:\\p{Blank}{1,6}(?i)(?:entre|en|del|de|y))?(?:\\p{Blank}{1,6}(?i)(?:las|por|el|la))?(?:\\s{1,2}(?:de|del|el))?\\s*(?:[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*(?:\\s+(?:(?i)del|de|da|i|d|la|en|el))?\\s+){0,2}[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*(?:\\s*(?i)(?:las|por|el|la)\\s*)?(?:(?:\\p{Blank}*[0-9*]+\\s*))?(?:(?:(?:\\s{0,2}(?:(?:,\\s*nº)|º|nº|°|#|-|/|\\(.+\\))\\s*)|\\s+))?(?:,?\\s{0,2}[0-9*]+\\s{0,2})?(?:(?:\\s{0,2}(?:de|del|el))?\\s*(?:[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*(?:\\s+(?:(?i)del|de|da|i|d|la|en|el))?\\s+){0,2}[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*)?";
    private static final String BUI_INDEPEND = ",?\\s{0,2}\\b(?:(?i)Caf[èée]s?|paseo|Cine|teatro|dormitorio|ópera|banco|empresa|parroquia|Oficina|gimnasio|oficinas|piscina|taquilla|tienda|terrazas|enfermería|feria|fábrica|Congreso|exposición|Estudio|Plana|colegio|instituto|estación|planetario|capilla|planta|Puente|Zócalo|Corte|Bodeguita|Consejo|Discoteca|Cines|Fabrica|Cocina|zona|Comunidad|ermita)(?:\\p{Blank}{1,6}(?i)(?:entre|en|del|de|y))?(?:\\p{Blank}{1,6}(?i)(?:las|por|el|la))?(?:\\s{1,2}(?:de|del|el))?\\s*(?:[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*(?:\\s+(?:(?i)del|de|da|i|d|la|en|el))?\\s+){0,2}[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*(?:\\s*(?i)(?:las|por|el|la)\\s*)?(?:(?:\\p{Blank}*[0-9*]+\\s*))?(?:(?:(?:\\s{0,2}(?:(?:,\\s*nº)|º|nº|°|#|-|/|\\(.+\\))\\s*)|\\s+))?(?:,?\\s{0,2}[0-9*]+\\s{0,2})?(?:(?:\\s{0,2}(?:de|del|el))?\\s*(?:[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*(?:\\s+(?:(?i)del|de|da|i|d|la|en|el))?\\s+){0,2}[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*)?";
    private static final String CIT;
    private static final String KEY_BUI = "\\b(?:(?i)Universidad|Universitaria|museo|biblioteca|restaurante|cafetería|hotel|ayuntamiento|librería|taberna|Universitaria|empresa|hospital|supermercado|Peluquería|Tintorería|Mercado|rascacielos|Catedral|Mezquita|estadio|Iglesia|Torre|Clínica|castillo|sanatorio|monte|Confitería|Pescadería|tienda\\s{1,6}de\\s{1,6}discos|tienda\\s{1,6}de\\s{1,6}licores|tienda\\s{1,6}de\\s{1,6}muebles|lago|tienda\\s{1,6}de\\s{1,6}antigüedades|palacio|metro|subte|Hostel|playa|Universidades|puerto|Parque|plaza|edif|escuelas\\s{1,6}técnicas\\s{1,6}productivas|Kursaal|aeropuerto|los\\s{1,6}Alpes|tienda\\s{1,6}oficial|cerro|Disneyland|albergue|CORTE\\s{1,6}INGLES|los\\s{1,6}Palacios|los\\s{1,6}Valles|clinica|Galería|Conciergerie|bosque|Museum|Park|Villa|Parc|Plaça|Museu|Restaurant|Templo|Hammam|Apartamento|Cathedral|Estació|Alcazar|Caixa|Naval|Cuesta|Church|University|Bodegas|Bar|Farmacia|Mausoleos|Universitat|Zoo|Jardines|Monumento|Basílica|Apartaments|Hostal|Teatre|Cathédrale|Farmàcia|Llibreria|Gelateria|Igresia|College|Muelle|Academia|aparcamiento|Montaña|Cafeteria|Zoobotanico|Universitdad|Supermercados|Apartamentos|Apartment|Apartamenos|Apartanebtos|Universitad|Cueva|Casino|Convento|Almacén|Cervecería|Embalse|Hospedería|Bazar|Pedralbes|Galeria|Librerias|Alameda|Market|Pabellón|Galerías)";
    private static final String KEY_BUI_INDEPEND = "\\b(?:(?i)Caf[èée]s?|paseo|Cine|teatro|dormitorio|ópera|banco|empresa|parroquia|Oficina|gimnasio|oficinas|piscina|taquilla|tienda|terrazas|enfermería|feria|fábrica|Congreso|exposición|Estudio|Plana|colegio|instituto|estación|planetario|capilla|planta|Puente|Zócalo|Corte|Bodeguita|Consejo|Discoteca|Cines|Fabrica|Cocina|zona|Comunidad|ermita)";
    private static String KEY_CIT = "\\b(?:(?i)F\\.Miranda|Estany|Buitrago|Torreanaz|California|Aeroporto|París|los\\s{1,6}Herreros|Miranda|aragon|albacete|malaga|aparcamiento|embarcadero|túnel|Porlamar|taquilla|Caracas|Bierzo|Maracay|Cádiz|Cieza|Las\\s{1,6}Casas|Mallorca|sucursal|Zona\\s{1,6}peatonal|glorieta|Crucero|Ayuntamiento|Campus|\\s{0,6}-\\s{0,6}Madrid|Madrid|Municipio\\s{1,6}García|Meliila|Seville|Vista\\s{1,6}Alegre|Ronda\\s{0,6},\\s{0,6}de\\s{1,6}Francisco\\s{1,6}Narváez|Belgrano|Morón|C\\.\\s{0,6}Izcalli|Muñoz|PortAventura|Gines|Murcia|Bermeo|Fernando\\s{1,6}de\\s{1,6}Henares|Villa\\s{1,6}Allende|Elche\\s{0,6}-\\s{0,6}Atlético|Edif\\.\\s{0,6}Las\\s{1,6}Rosas|Edif\\.\\s{0,6}El\\s{1,6}tulipan|Albacete|frontera\\s{1,6}Colombo\\s{0,6}-\\s{0,6}Venezolana|Buenos\\s{1,6}aires|Barcelona|los\\s{1,6}Ríos|parera|Café\\s{1,6}Concierto|Barrio\\s{1,6}Don\\s{1,6}Nicanor\\s{1,6}Ochoa|Capuchinos|Torrejón|Tomas\\s{1,6}de\\s{1,6}Ibarra|LEZAMA|elegido|Bogotá|Jujuy|Málaga|Granada|ARGENTINA|Villanueva|Vecindario|Palma|Las\\s{1,6}Palmas\\s{1,6}de\\s{1,6}Gran\\s{1,6}Canaria|Cinequint|MARACAIBO|Carabobo|Venezuela|Santa\\s{1,6}Mónica|Elche|Sao\\s{1,6}Paulo|Almansa|Valencia|Murcia|sandalias|Naguanagua|Palermo|Moreno|Castellón|Córdoba\\s{1,6}Argentina|Córdoba|Argentina|Milán|Italia|Monterrey|Vega|Aranjuez|Parador|Rosario|Sevilla|La\\s{1,6}Candelaria|Hortaleza|Trípoli|Hortaleza|Valladolid|turmero|Reino\\s{1,6}Unido|Bilbao|Gandia|Sierra\\s{1,6}de\\s{1,6}las\\s{1,6}Nieves|Jaén|Sierra\\s{1,6}de\\s{1,6}Cazorla|Las\\s{1,6}Villas|Egipto|Cocentaina|Mexicano|Barrio\\s{1,6}de\\s{1,6}Boedo|Donosti|Cantabria|Pueblo\\s{1,6}Nuevo|San\\s{1,6}Cristobal|Alcázar|Gijón|Salamanca|Écija|A\\s{1,6}Coruña|Locales\\s{1,6}Michelangelo|Chile|Cancún|Prados|Cleveland\\s{0,6},\\s{0,6}Ohio|Altamira|Alcorcón|zaragoza|Burjassot|Ciudad\\s{0,6}Bolívar|Segovia|Barquisimeto|Logroño|Guadalajara|Antequera|Múnich|Quilmes|Quiró|lorca|Badalona|Monterrey|Metropolitano|Cumana|TorrejondeArdoz|SanIsidro|Gaulle|Bélgica|Alicante|colegiales|Lavalleja|ACoruña|Londres|Turín|Chacaíto|Cali|Tembleque|Virunga|Xuacón|Lepe|Espartinas|San\\s{1,6}Telmo|Llanes|Cartagena|Olivos|alburquerque|Medellín|los\\s{1,6}Alcornocales|moratadetajuña|Piedra|Luanco|Parla|AltaVista|Hogar\\s{1,6}San\\s{1,6}Pablo|Osma|Chacaito|Móstoles|Trujillo|Cebollera|Orihuela|Huelva|los\\s{1,6}Héroes\\s{1,6}Coacalco|Espelette|los\\s{1,6}Reyes|Metepec|castelldefels|Avellaneda|Mendoza|los\\s{1,6}Alcornocales|santa\\s{1,6}rosa|manglar|LaManga|CangasdeOnis|los\\s{1,6}Ángeles|parera|tudela|los\\s{1,6}Calares|lleida|los\\s{1,6}Rastrojos|ciudad\\s{1,6}de\\s{1,6}Cordoba|los\\s{1,6}Reyes|badajoz|San\\s{1,6}Casimiro\\s{0,6},\\s{0,6}Estado\\s{1,6}Aragua|Carirubana|los\\s{1,6}Andes|Matamoros\\s{1,6}Coahuila|Barrio\\s{1,6}d\\s{1,6}Santa\\s{1,6}Cruz|bormujos|madrugá|barquisimeto|los\\s{1,6}Arandinos|LasPalmas|Mostoles|Gijon|Colonia\\s{1,6}Santa\\s{1,6}Maria\\s{1,6}la\\s{1,6}Ribera|cartagena|los\\s{1,6}Templarios|Esparragal|Los\\s{1,6}leones|los\\s{1,6}Ríos|los\\s{1,6}Resorts|los\\s{1,6}Alcornocales|los\\s{1,6}ultimos\\s{1,6}dias|yecla|municipio|las\\s{1,6}Rozas|San\\s{1,6}josé|cabañeros|Anzoátegui|Caceres|los\\s{1,6}Cerritos|sanfernandodehenares|Manchester|PRINCIPE\\s{1,6}PIO|Tossa\\s{1,6}de\\s{1,6}Mar|Cabello\\.\\s{0,6}Lo|Urquiza\\s{1,6}hasta\\s{1,6}Alberti|León|altura\\s{1,6}Santos\\s{1,6}Ossa|con\\s{1,6}San\\s{1,6}Agustín\\s{0,6},\\s{0,6}Col\\.\\s{0,6}San\\s{1,6}Javier|Nerja\\s{0,6}\\(\\s{0,6}Málaga\\s{0,6}\\)|Farjado|D\\.\\s{0,6}Luis|H\\.\\s{0,6}Yrigoyen\\s{1,6}y\\s{1,6}M\\.\\s{0,6}Castro|colonia|Farmacia\\s{1,6}Martínez|Almagro|Los\\s{1,6}Yébenes|empalme|St\\.\\s{0,6}Joseph|Col\\.\\s{0,6}Talleres|Los\\s{1,6}támenes|los\\s{1,6}Vados|los\\s{1,6}Lagos|M\\.\\s{0,6}Freeman|los\\s{1,6}Remedios|J\\.\\s{0,6}Ramírez|Alicante\\s{0,6}-\\s{0,6}Elche|palmas|M\\.\\s{0,6}Auxiliadora|los\\s{1,6}Reales\\s{1,6}Alcázares|S\\.\\s{0,6}Sebastián|miguel|S\\.\\s{0,6}Francisco|S\\.\\s{0,6}Andres|los\\s{1,6}Arcos|J\\.\\s{0,6}Lennon|Maldivas|San\\s{1,6}Fernando|Hortaleza|S\\.\\s{0,6}Cristóbal|A\\.\\s{0,6}Páez|V\\.\\s{0,6}Hebron|S\\.\\s{0,6}Pellegrino|Elche\\s{0,6}-\\s{0,6}Atlético|S\\.\\s{0,6}J\\s{1,6}Bosco|L\\.\\s{0,6}Rodriguez|D\\.\\s{0,6}Bosco|los\\s{1,6}Morros|J\\.\\s{0,6}Pastore|los\\s{1,6}Rios|St\\.\\s{0,6}Judes|los\\s{1,6}Picos|St\\.\\s{0,6}Josep's|A\\.\\s{0,6}Sánchez|Fuente|Apartamentos|Apartments|Apartment|Piscinas|BNA)";
    private static final String KEY_STR = "\\b(?:(?i)Avenida|Travesía|Av\\.|calles|calle|carretera|Bulevar|Carrer|autovía|autopista|Rambla|Ronda|Blvd|c/|camino|Blvd\\.|Gran Vía|Passeig|Rúa)";
    private static final String NB = "(?:\\p{Blank}*[0-9*]+\\s*)";
    private static final String PSC = "\\s{0,2},?\\s{0,2}(?:(?i)CP|C\\.P\\.)\\s{0,2}(?:\\d{5})";
    private static final String STR;
    private static final String cit1 = (",?\\s{0,4}(((?:(?<!\\d)\\d{5}(?!\\d))\\s{0,4},?\\s{0,4})|(?:(?:(?i)P\\.C\\.|CP|C\\.P\\.)?\\s{0,4}(?:(?<!\\d)\\d{5}(?!\\d))\\s{0,4}))" + KEY_CIT + "(?:" + con3 + "(?:\\s{0,4},\\s{0,4}|\\s{0,4})" + ")" + "((" + tokenkey + provincekey + ")?" + tokenkey + countrykey + ")?");
    private static final String cit2 = (",?\\s{0,4}" + KEY_CIT + "(?:" + con3 + "(?:\\s{0,4},\\s{0,4}|\\s{1,4})" + "(?:\\s{0,4},?\\s{0,4}(?<!\\d)\\d{5}(?!\\d))?|" + "(?:\\s{0,4}(?:(?i)P\\.C\\.|CP|C\\.P\\.)?\\s{0,4}(?:(?<!\\d)\\d{5})(?!\\d))" + ")" + "((" + tokenkey + provincekey + ")?" + tokenkey + countrykey + ")?");
    private static final String cit3;
    private static final String con = "(?:(?:\\s{0,2}(?:(?:,\\s*nº)|º|nº|°|#|-|/|\\(.+\\))\\s*)|\\s+)";
    private static final String con2 = "\\s*(?i)(?:las|por|el|la)\\s*";
    private static final String con3 = "(?:\\p{Blank}{1,6}(?i)(?:entre|en|del|de|y))?(?:\\p{Blank}{1,6}(?i)(?:las|por|el|la))?";
    private static String countrykey = "((?i)Spanish|España)";
    private static final String prep = "(?:\\p{Blank}{1,6}(?i)(en\\p{Blank}{1,6}(la|el|al)?|ir\\p{Blank}{1,6}(a|al|el|la)?|al|a\\p{Blank}{1,6}(la|el)?)\\b)";
    private static final String prepAndSc = "(?:\\p{Blank}{1,6}(?i)(en\\p{Blank}{1,6}(la|el|al)?|ir\\p{Blank}{1,6}(a|al|el|la)?|al|a\\p{Blank}{1,6}(la|el)?)\\b)|\\p{Blank}{1,6}|\\p{Blank}{0,6},\\p{Blank}{0,6}";
    private static String provincekey = "[A-ZÁÉÍÓÚÑñ][a-záäãàèéìíöóùúüñç']{2,12}";
    private static String tokenkey = "(\\s|-|:|#|,|\\s*\\(.*\\)\\s*)+";

    static {
        StringBuilder sb = new StringBuilder(",?\\s{0,4}");
        sb.append(KEY_CIT);
        cit3 = sb.toString();
        StringBuilder sb2 = new StringBuilder(String.valueOf(cit1));
        sb2.append("|");
        sb2.append(cit2);
        sb2.append("|");
        sb2.append(cit3);
        CIT = sb2.toString();
        StringBuilder sb3 = new StringBuilder(",?\\s{0,2}\\b(?:(?i)Avenida|Travesía|Av\\.|calles|calle|carretera|Bulevar|Carrer|autovía|autopista|Rambla|Ronda|Blvd|c/|camino|Blvd\\.|Gran Vía|Passeig|Rúa)(?:(?:\\s{0,2}(?:de|del|el))?\\s*(?:[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*(?:\\s+(?:(?i)del|de|da|i|d|la|en|el))?\\s+){0,2}[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*\\s*)?(?:\\s*(?<!\\d)[0-9*]{1,5}(?!\\d)\\s*(,\\s*\\d{1,2}[A-Za-z])?)?(?:(?:(?:\\s{0,2}(?:(?:,\\s*nº)|º|nº|°|#|-|/|\\(.+\\))\\s*)|\\s+))?(?:\\p{Blank}{1,6}(?i)(?:entre|en|del|de|y))?(?:\\p{Blank}{1,6}(?i)(?:las|por|el|la))?(?:(?:(?:\\s{0,2}(?:(?:,\\s*nº)|º|nº|°|#|-|/|\\(.+\\))\\s*)|\\s+))?(?:(?:\\s{0,2}(?:de|del|el))?\\s*(?:[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*(?:\\s(?:(?i)del|de|da|i|d|la|en|el))?\\s){0,2}[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*\\s*)?(?:(?:\\p{Blank}*[0-9*]+\\s*))?(?:(?:(?:\\s{0,2}(?:(?:,\\s*nº)|º|nº|°|#|-|/|\\(.+\\))\\s*)|\\s+))?(?:\\s{0,2}(?:de|del|el))?\\s*(?:[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*\\s+){0,2}[-A-ZÁÉÍÓÚÑñ][A-Za-z0-9ÁÉÍÓÚÑáäãàèéìíöóùúüñç']*(?:\\s{0,2},?\\s{0,2}\\(?(?<!\\d)[0-9*]{1,5}(?!\\d)(,\\s*\\d{1,2}[A-Za-z])?\\)?)?(");
        sb3.append(tokenkey);
        sb3.append(countrykey);
        sb3.append(")?");
        STR = sb3.toString();
    }

    public void init() {
        put("p1", STR);
        put("p2", CIT);
        put("p3", BUI);
        put("p4", PSC);
        put("p5", BUI_INDEPEND);
        put("pgrep", prep);
        put("pPrepAndSc", "prepAndSc");
    }
}
