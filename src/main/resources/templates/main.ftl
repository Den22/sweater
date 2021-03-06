<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
<div> <@l.logout /></div>
    <span><a href="/user">User list</a></span>
<div>
    <form method="post" action="add" enctype="multipart/form-data">
        <input type="text" name="text" placeholder="Введите сообщение">
        <input type="text" name="tag" placeholder="Тег">
        <input type="file" name="file">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Добавить</button>
    </form>
</div>
<div>Список сообщений</div>
<form method="get" action="/main">
    <input type="text" name="filter" value="${filter!}">
    <button type="submit">Найти</button>
</form>
<#list messages as message>
<div>
    <b>${message.id}</b>
    <span>${message.text}</span>
    <i>${message.tag}</i>
    <strong>${message.authorName}</strong>
    <a href="/message?message=${message.id}"><img src="/img/icons/delete.png" alt="delete" width="16"></a>
    <div>
        <#if message.filename??>
            <img src="/img/${message.filename}">
        </#if>
    </div>
</div>
<#else>
No message
</#list>
</@c.page>
