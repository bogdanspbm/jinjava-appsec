import java

// Запрос для поиска всех публичных методов, принимающих параметр типа String
from Method m, Parameter p
where 
    m.isPublic() and // метод должен быть публичным
    p.getCallable() = m and // параметр должен принадлежать методу
    p.getType() instanceof TypeString // тип параметра должен быть String
select m, m.getDeclaringType(), p, p.getName()
