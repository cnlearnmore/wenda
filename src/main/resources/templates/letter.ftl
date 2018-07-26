<#include "header.ftl">
<link rel="stylesheet" media="all" href="${ctx}/styles/letter.css">
    <div id="main">
        <div class="zg-wrap zu-main clearfix ">
            <ul class="letter-list">
                <#list conversations as conversation>
                    <li id="conversation-item-10005_622873">
                        <a class="letter-link"
                           href="${ctx}/msg/detail?conversationId=${conversation.message.conversationId!''}"></a>
                        <div class="letter-info">
                            <span class="l-time">${conversation.message.createdDate?string('yyyy-MM-dd HH:mm:ss')}</span>
                            <div class="l-operate-bar">
                            <#--<a href="javascript:void(0);" class="sns-action-del" data-id="10005_622873">-->
                            <#--删除-->
                            <#--</a>-->
                                <a href="${ctx}/msg/detail?${conversation.message.conversationId!''}">
                                    共${conversation.message.id!''}条会话
                                </a>
                            </div>
                        </div>
                        <div class="chat-headbox">
                        <span class="msg-num">
                            ${conversation.unread!''}
                        </span>
                            <a class="list-head">
                                <img alt="头像" src="${conversation.user.headUrl!''}">
                            </a>
                        </div>
                        <div class="letter-detail">
                            <a title="通知" class="letter-name level-color-1">
                                ${conversation.user.name!''}
                            </a>
                            <p class="letter-brief">
                                ${conversation.message.content!''}
                            </p>
                        </div>
                    </li>
                </#list> </ul>

        </div>
    </div>
<#include "js.ftl">
<script type="text/javascript" src="${ctx}/scripts/main/site/detail.js"></script>
<#include "footer.ftl">